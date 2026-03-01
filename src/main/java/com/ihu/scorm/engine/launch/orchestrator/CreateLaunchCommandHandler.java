package com.ihu.scorm.engine.launch.orchestrator;

import com.ihu.scorm.engine.attempt.crud.AttemptEntity;
import com.ihu.scorm.engine.attempt.crud.AttemptStatus;
import com.ihu.scorm.engine.attempt.persist.AttemptPersistService;
import com.ihu.scorm.engine.attempt.persist.AttemptReadService;
import com.ihu.scorm.engine.common.command.CommandHandler;
import com.ihu.scorm.engine.common.config.JwtProperties;
import com.ihu.scorm.engine.common.config.PlayerProperties;
import com.ihu.scorm.engine.common.config.RuntimeProperties;
import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.constant.SystemMessages;
import com.ihu.scorm.engine.common.error.ConflictException;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.infra.ClockProvider;
import com.ihu.scorm.engine.common.token.JwtTokenService;
import com.ihu.scorm.engine.course.crud.CourseEntity;
import com.ihu.scorm.engine.course.persist.CourseReadService;
import com.ihu.scorm.engine.enrollment.crud.EnrollmentEntity;
import com.ihu.scorm.engine.enrollment.persist.EnrollmentReadService;
import com.ihu.scorm.engine.launch.LaunchConstants;
import com.ihu.scorm.engine.launch.api.LaunchCreatedDto;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import com.ihu.scorm.engine.launch.persist.LaunchPersistService;
import com.ihu.scorm.engine.runtime.state.LaunchRuntimeState;
import com.ihu.scorm.engine.runtime.state.LaunchRuntimeStateFactory;
import com.ihu.scorm.engine.runtime.state.RuntimeStateStore;
import com.ihu.scorm.engine.user.crud.UserEntity;
import com.ihu.scorm.engine.user.persist.UserReadService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateLaunchCommandHandler implements CommandHandler<CreateLaunchCommand, LaunchCreatedDto> {

  private final UserReadService userReadService;
  private final CourseReadService courseReadService;
  private final EnrollmentReadService enrollmentReadService;
  private final AttemptReadService attemptReadService;
  private final AttemptPersistService attemptPersistService;
  private final LaunchPersistService launchPersistService;
  private final JwtTokenService jwtTokenService;
  private final ClockProvider clockProvider;
  private final JwtProperties jwtProperties;
  private final PlayerProperties playerProperties;
  private final RuntimeStateStore runtimeStateStore;
  private final LaunchRuntimeStateFactory launchRuntimeStateFactory;
  private final RuntimeProperties runtimeProperties;

  public CreateLaunchCommandHandler(
      UserReadService userReadService,
      CourseReadService courseReadService,
      EnrollmentReadService enrollmentReadService,
      AttemptReadService attemptReadService,
      AttemptPersistService attemptPersistService,
      LaunchPersistService launchPersistService,
      JwtTokenService jwtTokenService,
      ClockProvider clockProvider,
      JwtProperties jwtProperties,
      PlayerProperties playerProperties,
      RuntimeStateStore runtimeStateStore,
      LaunchRuntimeStateFactory launchRuntimeStateFactory,
      RuntimeProperties runtimeProperties) {
    this.userReadService = userReadService;
    this.courseReadService = courseReadService;
    this.enrollmentReadService = enrollmentReadService;
    this.attemptReadService = attemptReadService;
    this.attemptPersistService = attemptPersistService;
    this.launchPersistService = launchPersistService;
    this.jwtTokenService = jwtTokenService;
    this.clockProvider = clockProvider;
    this.jwtProperties = jwtProperties;
    this.playerProperties = playerProperties;
    this.runtimeStateStore = runtimeStateStore;
    this.launchRuntimeStateFactory = launchRuntimeStateFactory;
    this.runtimeProperties = runtimeProperties;
  }

  @Override
  @Transactional
  public LaunchCreatedDto handle(CreateLaunchCommand command) {
    UserEntity user = userReadService.getRequired(command.userId());
    CourseEntity course = courseReadService.getRequired(command.courseId());
    EnrollmentEntity enrollment = enrollmentReadService.getActiveRequired(command.userId(), command.courseId());

    Instant now = clockProvider.now();
    AttemptEntity activeAttempt = attemptReadService.findActive(enrollment.getId());
    if (activeAttempt != null && !command.forceNewAttempt()) {
      throw new ConflictException(
          ErrorCode.ACTIVE_ATTEMPT_EXISTS,
          ErrorMessages.ACTIVE_ATTEMPT_ALREADY_EXISTS,
          Map.of(
              DetailKey.ENROLLMENT_ID,
              enrollment.getId(),
              DetailKey.ATTEMPT_ID,
              activeAttempt.getId()));
    }
    if (activeAttempt != null) {
      attemptPersistService.closeAttempt(activeAttempt, AttemptStatus.TERMINATED, now);
    }

    AttemptEntity attempt = attemptPersistService.create(
        user.getId(),
        course.getId(),
        enrollment.getId(),
        attemptReadService.nextAttemptNo(enrollment.getId()),
        now);

    LaunchEntity launch = new LaunchEntity();
    launch.setAttemptId(attempt.getId());
    launch.setStandard(course.getStandard());
    launch.setLaunchTokenHash(LaunchConstants.PENDING_TOKEN_HASH);
    launch.setLastSeenAt(now);
    launch.setExpiresAt(now.plus(jwtProperties.launchTtlMinutesOrDefault(), ChronoUnit.MINUTES));
    LaunchEntity savedLaunch = launchPersistService.create(launch);

    String token = jwtTokenService.createLaunchToken(
        savedLaunch.getId(),
        attempt.getId(),
        user.getId(),
        course.getId(),
        course.getStandard());

    savedLaunch.setLaunchTokenHash(sha256(token));
    launchPersistService.save(savedLaunch);

    LaunchRuntimeState runtimeState = launchRuntimeStateFactory.seedForLaunch(savedLaunch, attempt, now);
    runtimeStateStore.save(runtimeState, runtimeProperties.launchStateTtlSecondsOrDefault());

    String launchUrl = playerProperties.baseUrl()
        + LaunchConstants.LAUNCH_PATH_PREFIX
        + savedLaunch.getId()
        + LaunchConstants.TOKEN_QUERY_PREFIX
        + token;

    return new LaunchCreatedDto(
        savedLaunch.getId(),
        attempt.getId(),
        course.getStandard(),
        launchUrl,
        token,
        savedLaunch.getExpiresAt());
  }

  private String sha256(String text) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(text.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception exception) {
      throw new IllegalStateException(SystemMessages.FAILED_TO_HASH_LAUNCH_TOKEN, exception);
    }
  }
}

package com.ihu.scorm.engine.launch.orchestrator;

import com.ihu.scorm.engine.attempt.crud.AttemptEntity;
import com.ihu.scorm.engine.attempt.persist.AttemptReadService;
import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.config.StorageProperties;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.ValidationException;
import com.ihu.scorm.engine.common.infra.ClockProvider;
import com.ihu.scorm.engine.common.storage.ObjectStorage;
import com.ihu.scorm.engine.common.token.LaunchTokenPayload;
import com.ihu.scorm.engine.course.crud.CourseEntity;
import com.ihu.scorm.engine.course.persist.CourseReadService;
import com.ihu.scorm.engine.launch.api.LaunchContextDto;
import com.ihu.scorm.engine.launch.api.LaunchCreatedDto;
import com.ihu.scorm.engine.launch.LaunchConstants;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import com.ihu.scorm.engine.launch.crud.LaunchStatus;
import com.ihu.scorm.engine.launch.persist.LaunchReadService;
import com.ihu.scorm.engine.runtime.state.LaunchRuntimeState;
import com.ihu.scorm.engine.runtime.state.RuntimeStateStore;
import com.ihu.scorm.engine.user.crud.UserEntity;
import com.ihu.scorm.engine.user.persist.UserReadService;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class LaunchOrchestrator {

  private final UserReadService userReadService;
  private final CourseReadService courseReadService;
  private final AttemptReadService attemptReadService;
  private final LaunchReadService launchReadService;
  private final ClockProvider clockProvider;
  private final StorageProperties storageProperties;
  private final ObjectStorage objectStorage;
  private final RuntimeStateStore runtimeStateStore;
  private final CreateLaunchCommandHandler createLaunchCommandHandler;
  private final TerminateLaunchCommandHandler terminateLaunchCommandHandler;

  public LaunchOrchestrator(
      UserReadService userReadService,
      CourseReadService courseReadService,
      AttemptReadService attemptReadService,
      LaunchReadService launchReadService,
      ClockProvider clockProvider,
      StorageProperties storageProperties,
      ObjectStorage objectStorage,
      RuntimeStateStore runtimeStateStore,
      CreateLaunchCommandHandler createLaunchCommandHandler,
      TerminateLaunchCommandHandler terminateLaunchCommandHandler) {
    this.userReadService = userReadService;
    this.courseReadService = courseReadService;
    this.attemptReadService = attemptReadService;
    this.launchReadService = launchReadService;
    this.clockProvider = clockProvider;
    this.storageProperties = storageProperties;
    this.objectStorage = objectStorage;
    this.runtimeStateStore = runtimeStateStore;
    this.createLaunchCommandHandler = createLaunchCommandHandler;
    this.terminateLaunchCommandHandler = terminateLaunchCommandHandler;
  }

  public LaunchCreatedDto createLaunch(CreateLaunchCommand command) {
    return createLaunchCommandHandler.handle(command);
  }

  public LaunchContextDto getLaunchContext(UUID launchId, LaunchTokenPayload launchTokenPayload) {
    LaunchEntity launch = launchReadService.getRequired(launchId);
    if (launch.getStatus() != LaunchStatus.ACTIVE) {
      throw new ValidationException(
          ErrorCode.LAUNCH_CLOSED,
          ErrorMessages.LAUNCH_IS_ALREADY_CLOSED,
          Map.of(DetailKey.LAUNCH_ID, launchId));
    }

    if (!launch.getId().equals(launchTokenPayload.launchId())) {
      throw new ValidationException(
          ErrorCode.TOKEN_LAUNCH_MISMATCH,
          ErrorMessages.LAUNCH_TOKEN_DOES_NOT_MATCH_LAUNCH_ID,
          Map.of(DetailKey.LAUNCH_ID, launchId));
    }

    if (launch.getExpiresAt().isBefore(clockProvider.now())) {
      throw new ValidationException(
          ErrorCode.LAUNCH_EXPIRED,
          ErrorMessages.LAUNCH_TOKEN_EXPIRED,
          Map.of(DetailKey.LAUNCH_ID, launchId));
    }

    AttemptEntity attempt = attemptReadService.getRequired(launch.getAttemptId());
    UserEntity user = userReadService.getRequired(attempt.getUserId());
    CourseEntity course = courseReadService.getRequired(attempt.getCourseId());

    URL presignedUrl = objectStorage.getPresignedGetUrl(
        course.getStorageBucket(),
        course.getStorageObjectKeyZip(),
        storageProperties.presignedTtlMinutesOrDefault());

    LaunchRuntimeState state = runtimeStateStore.get(launchId);
    if (state == null) {
      state = new LaunchRuntimeState();
    }

    String fullName = (user.getFirstName() == null ? "" : user.getFirstName() + " ")
        + (user.getLastName() == null ? "" : user.getLastName());

    return new LaunchContextDto(
        launch.getId(),
        attempt.getId(),
        new LaunchContextDto.UserView(user.getId(), fullName.trim().isBlank() ? user.getUsername() : fullName.trim(), user.getEmail()),
        new LaunchContextDto.CourseView(course.getId(), course.getTitle(), course.getStandard()),
        new LaunchContextDto.PlayerView(
            course.getStandard().name(),
            new LaunchContextDto.ContentSourceView(
                LaunchConstants.CONTENT_SOURCE_MINIO_PRESIGNED_ZIP,
                presignedUrl.toString()),
            course.getEntrypointPath()),
        new LaunchContextDto.RuntimeView(state.getNormalizedProgress()));
  }

  public void terminate(UUID launchId, LaunchTokenPayload launchTokenPayload) {
    terminateLaunchCommandHandler.handle(new TerminateLaunchCommand(launchId, launchTokenPayload));
  }
}

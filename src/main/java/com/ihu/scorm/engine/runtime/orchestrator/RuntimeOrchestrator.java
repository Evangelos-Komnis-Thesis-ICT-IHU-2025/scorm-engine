package com.ihu.scorm.engine.runtime.orchestrator;

import com.ihu.scorm.engine.attempt.crud.AttemptEntity;
import com.ihu.scorm.engine.attempt.persist.AttemptReadService;
import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.config.RuntimeProperties;
import com.ihu.scorm.engine.common.error.ConflictException;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.ValidationException;
import com.ihu.scorm.engine.common.infra.ClockProvider;
import com.ihu.scorm.engine.common.token.LaunchTokenPayload;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import com.ihu.scorm.engine.launch.crud.LaunchStatus;
import com.ihu.scorm.engine.launch.persist.LaunchPersistService;
import com.ihu.scorm.engine.launch.persist.LaunchReadService;
import com.ihu.scorm.engine.runtime.adapter.NormalizedUpdateResult;
import com.ihu.scorm.engine.runtime.adapter.StandardAdapter;
import com.ihu.scorm.engine.runtime.adapter.StandardAdapterRegistry;
import com.ihu.scorm.engine.runtime.adapter.StandardCommitPayload;
import com.ihu.scorm.engine.runtime.api.RuntimeCommitForm;
import com.ihu.scorm.engine.runtime.api.RuntimeCommitResponse;
import com.ihu.scorm.engine.runtime.state.LaunchRuntimeState;
import com.ihu.scorm.engine.runtime.state.LaunchRuntimeStateFactory;
import com.ihu.scorm.engine.runtime.state.RuntimeStateStore;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RuntimeOrchestrator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeOrchestrator.class);

  private final LaunchReadService launchReadService;
  private final LaunchPersistService launchPersistService;
  private final AttemptReadService attemptReadService;
  private final RuntimeStateStore runtimeStateStore;
  private final LaunchRuntimeStateFactory launchRuntimeStateFactory;
  private final StandardAdapterRegistry standardAdapterRegistry;
  private final RuntimeProperties runtimeProperties;
  private final ClockProvider clockProvider;
  private final RuntimeRateLimiter runtimeRateLimiter;

  public RuntimeOrchestrator(
      LaunchReadService launchReadService,
      LaunchPersistService launchPersistService,
      AttemptReadService attemptReadService,
      RuntimeStateStore runtimeStateStore,
      LaunchRuntimeStateFactory launchRuntimeStateFactory,
      StandardAdapterRegistry standardAdapterRegistry,
      RuntimeProperties runtimeProperties,
      ClockProvider clockProvider,
      RuntimeRateLimiter runtimeRateLimiter) {
    this.launchReadService = launchReadService;
    this.launchPersistService = launchPersistService;
    this.attemptReadService = attemptReadService;
    this.runtimeStateStore = runtimeStateStore;
    this.launchRuntimeStateFactory = launchRuntimeStateFactory;
    this.standardAdapterRegistry = standardAdapterRegistry;
    this.runtimeProperties = runtimeProperties;
    this.clockProvider = clockProvider;
    this.runtimeRateLimiter = runtimeRateLimiter;
  }

  @Transactional
  public RuntimeCommitResponse commit(UUID launchId, LaunchTokenPayload tokenPayload, RuntimeCommitForm form) {
    LaunchEntity launch = launchReadService.getRequired(launchId);
    if (launch.getStatus() != LaunchStatus.ACTIVE) {
      throw new ValidationException(
          ErrorCode.LAUNCH_CLOSED,
          ErrorMessages.LAUNCH_IS_CLOSED,
          Map.of(DetailKey.LAUNCH_ID, launchId));
    }
    validateLaunchToken(launch, tokenPayload);
    runtimeRateLimiter.checkOrThrow(launchId, clockProvider.now());

    AttemptEntity attempt = attemptReadService.getRequired(launch.getAttemptId());

    LaunchRuntimeState state = runtimeStateStore.get(launchId);
    if (state == null) {
      state = launchRuntimeStateFactory.initializeForCommit(launch, attempt, clockProvider.now());
    }

    if (state.getLastSequence() != null && form.sequence() <= state.getLastSequence()) {
      if (runtimeProperties.strictCommitSequenceOrDefault()) {
        throw new ConflictException(
            ErrorCode.SEQUENCE_OUTDATED,
            ErrorMessages.COMMIT_SEQUENCE_IS_OLDER_THAN_LAST_ACCEPTED,
            Map.of(
                DetailKey.SEQUENCE, form.sequence(),
                DetailKey.LAST_SEQUENCE, state.getLastSequence()));
      }
      return new RuntimeCommitResponse(launchId, launch.getAttemptId(), state.getLastSequence(), state.getNormalizedProgress());
    }

    StandardAdapter adapter = standardAdapterRegistry.getRequired(launch.getStandard());
    NormalizedUpdateResult result = adapter.applyCommit(new StandardCommitPayload(
        launchId,
        launch.getAttemptId(),
        attempt.getUserId(),
        attempt.getCourseId(),
        launch.getStandard(),
        form.sequence(),
        form.clientTime(),
        form.payload()), state);

    Instant now = clockProvider.now();
    state.setRawRuntimeState(result.mergedRawState());
    state.setNormalizedProgress(result.normalizedProgress());
    state.setLastSequence(form.sequence());
    state.setLastCommitAt(form.clientTime());
    state.setLastSeenAt(now);
    state.setDirty(true);

    runtimeStateStore.save(state, runtimeProperties.launchStateTtlSecondsOrDefault());

    launch.setLastSeenAt(now);
    launchPersistService.save(launch);

    LOGGER.info("runtime_commit launchId={} attemptId={} sequence={}", launch.getId(), launch.getAttemptId(), form.sequence());

    return new RuntimeCommitResponse(launchId, launch.getAttemptId(), form.sequence(), result.normalizedProgress());
  }

  private void validateLaunchToken(LaunchEntity launch, LaunchTokenPayload tokenPayload) {
    if (!launch.getId().equals(tokenPayload.launchId()) || !launch.getAttemptId().equals(tokenPayload.attemptId())) {
      throw new ValidationException(
          ErrorCode.TOKEN_MISMATCH,
          ErrorMessages.LAUNCH_TOKEN_DOES_NOT_MATCH_LAUNCH,
          Map.of(DetailKey.LAUNCH_ID, launch.getId()));
    }
  }
}

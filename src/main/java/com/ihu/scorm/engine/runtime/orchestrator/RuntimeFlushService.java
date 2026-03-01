package com.ihu.scorm.engine.runtime.orchestrator;

import com.ihu.scorm.engine.attempt.crud.AttemptEntity;
import com.ihu.scorm.engine.attempt.crud.AttemptStatus;
import com.ihu.scorm.engine.attempt.crud.SnapshotReason;
import com.ihu.scorm.engine.attempt.persist.AttemptPersistService;
import com.ihu.scorm.engine.attempt.persist.AttemptReadService;
import com.ihu.scorm.engine.common.infra.ClockProvider;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import com.ihu.scorm.engine.launch.crud.LaunchStatus;
import com.ihu.scorm.engine.launch.persist.LaunchPersistService;
import com.ihu.scorm.engine.runtime.state.LaunchRuntimeState;
import com.ihu.scorm.engine.runtime.state.RuntimeStateStore;
import java.time.Instant;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RuntimeFlushService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeFlushService.class);

  private final RuntimeStateStore runtimeStateStore;
  private final AttemptReadService attemptReadService;
  private final AttemptPersistService attemptPersistService;
  private final LaunchPersistService launchPersistService;
  private final ClockProvider clockProvider;

  public RuntimeFlushService(
      RuntimeStateStore runtimeStateStore,
      AttemptReadService attemptReadService,
      AttemptPersistService attemptPersistService,
      LaunchPersistService launchPersistService,
      ClockProvider clockProvider) {
    this.runtimeStateStore = runtimeStateStore;
    this.attemptReadService = attemptReadService;
    this.attemptPersistService = attemptPersistService;
    this.launchPersistService = launchPersistService;
    this.clockProvider = clockProvider;
  }

  @Transactional
  public void flushLaunch(LaunchEntity launch, SnapshotReason reason, AttemptStatus finalStatus) {
    if (launch.getStatus() == LaunchStatus.CLOSED) {
      return;
    }

    AttemptEntity attempt = attemptReadService.getRequired(launch.getAttemptId());
    LaunchRuntimeState state = runtimeStateStore.get(launch.getId());
    Instant now = clockProvider.now();

    if (state != null) {
      attemptPersistService.applyNormalizedProgress(attempt, state.getNormalizedProgress());
      attemptPersistService.saveSnapshot(
          attempt.getId(),
          launch.getId(),
          state.getRawRuntimeState(),
          state.getNormalizedProgress(),
          reason,
          now);
      runtimeStateStore.delete(launch.getId());
    } else {
      attemptPersistService.saveSnapshot(
          attempt.getId(),
          launch.getId(),
          Map.of(),
          new com.ihu.scorm.engine.runtime.state.NormalizedProgress(),
          reason,
          now);
    }

    attemptPersistService.closeAttempt(attempt, finalStatus, now);
    launchPersistService.close(launch, now);

    LOGGER.info("runtime_flush launchId={} attemptId={} reason={} finalStatus={}",
        launch.getId(), attempt.getId(), reason, finalStatus);
  }
}

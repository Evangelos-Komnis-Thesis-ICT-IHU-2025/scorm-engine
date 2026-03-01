package com.ihu.scorm.engine.runtime.orchestrator;

import com.ihu.scorm.engine.attempt.crud.AttemptStatus;
import com.ihu.scorm.engine.attempt.crud.SnapshotReason;
import com.ihu.scorm.engine.common.config.RuntimeProperties;
import com.ihu.scorm.engine.common.infra.ClockProvider;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import com.ihu.scorm.engine.launch.persist.LaunchReadService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RuntimeFlushScheduler {

  private final LaunchReadService launchReadService;
  private final RuntimeFlushService runtimeFlushService;
  private final RuntimeProperties runtimeProperties;
  private final ClockProvider clockProvider;

  public RuntimeFlushScheduler(
      LaunchReadService launchReadService,
      RuntimeFlushService runtimeFlushService,
      RuntimeProperties runtimeProperties,
      ClockProvider clockProvider) {
    this.launchReadService = launchReadService;
    this.runtimeFlushService = runtimeFlushService;
    this.runtimeProperties = runtimeProperties;
    this.clockProvider = clockProvider;
  }

  @Scheduled(fixedDelayString = "${app.runtime.flush-job-interval-ms:60000}")
  public void flushStaleLaunches() {
    Instant threshold = clockProvider.now().minus(runtimeProperties.staleFlushMinutesOrDefault(), ChronoUnit.MINUTES);
    List<LaunchEntity> stale = launchReadService.staleActive(threshold);
    for (LaunchEntity launch : stale) {
      runtimeFlushService.flushLaunch(launch, SnapshotReason.TIMEOUT_FLUSH, AttemptStatus.ABANDONED);
    }
  }
}

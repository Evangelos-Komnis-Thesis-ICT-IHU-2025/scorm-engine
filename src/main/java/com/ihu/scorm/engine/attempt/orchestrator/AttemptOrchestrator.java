package com.ihu.scorm.engine.attempt.orchestrator;

import com.ihu.scorm.engine.attempt.api.AttemptProgressDto;
import com.ihu.scorm.engine.attempt.crud.AttemptEntity;
import com.ihu.scorm.engine.attempt.persist.AttemptReadService;
import com.ihu.scorm.engine.attempt.persist.GetAttemptsQuery;
import com.ihu.scorm.engine.common.query.PageResult;
import com.ihu.scorm.engine.launch.crud.LaunchEntity;
import com.ihu.scorm.engine.launch.crud.LaunchStatus;
import com.ihu.scorm.engine.launch.persist.LaunchReadService;
import com.ihu.scorm.engine.runtime.state.LaunchRuntimeState;
import com.ihu.scorm.engine.runtime.state.NormalizedProgress;
import com.ihu.scorm.engine.runtime.state.RuntimeStateStore;
import com.ihu.scorm.engine.runtime.state.ScoreInfo;
import com.ihu.scorm.engine.runtime.state.TimeInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class AttemptOrchestrator {

  private final AttemptReadService attemptReadService;
  private final LaunchReadService launchReadService;
  private final RuntimeStateStore runtimeStateStore;

  public AttemptOrchestrator(
      AttemptReadService attemptReadService,
      LaunchReadService launchReadService,
      RuntimeStateStore runtimeStateStore) {
    this.attemptReadService = attemptReadService;
    this.launchReadService = launchReadService;
    this.runtimeStateStore = runtimeStateStore;
  }

  public AttemptProgressDto getProgress(java.util.UUID attemptId) {
    AttemptEntity attempt = attemptReadService.getRequired(attemptId);
    LaunchEntity latestLaunch = launchReadService.findLatestByAttemptId(attemptId);

    if (latestLaunch != null && latestLaunch.getStatus() == LaunchStatus.ACTIVE) {
      LaunchRuntimeState state = runtimeStateStore.get(latestLaunch.getId());
      if (state != null) {
        return new AttemptProgressDto(attemptId, state.getNormalizedProgress());
      }
    }

    return new AttemptProgressDto(attemptId, fromAttempt(attempt));
  }

  public PageResult<AttemptEntity> list(GetAttemptsQuery query) {
    Page<AttemptEntity> page = attemptReadService.list(query);
    return new PageResult<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages());
  }

  private NormalizedProgress fromAttempt(AttemptEntity attempt) {
    NormalizedProgress progress = new NormalizedProgress();
    progress.setCompletionStatus(attempt.getCompletionStatus());
    progress.setSuccessStatus(attempt.getSuccessStatus());

    ScoreInfo scoreInfo = new ScoreInfo();
    scoreInfo.setRaw(attempt.getScoreRaw());
    scoreInfo.setScaled(attempt.getScoreScaled());
    progress.setScore(scoreInfo);

    TimeInfo timeInfo = new TimeInfo();
    timeInfo.setTotalSeconds(attempt.getTotalTimeSeconds());
    progress.setTime(timeInfo);

    progress.setLocation(attempt.getLastLocation());
    progress.setSuspendData(attempt.getLastSuspendData());
    progress.setLastCommitAt(attempt.getLastCommittedAt());
    return progress;
  }
}

package com.ihu.scorm.engine.runtime.adapter;

import com.ihu.scorm.engine.attempt.crud.CompletionStatus;
import com.ihu.scorm.engine.attempt.crud.SuccessStatus;
import com.ihu.scorm.engine.runtime.state.LaunchRuntimeState;
import com.ihu.scorm.engine.runtime.state.NormalizedProgress;
import com.ihu.scorm.engine.standard.LearningStandard;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class Scorm12Adapter extends AbstractStandardAdapter {

  @Override
  public LearningStandard standard() {
    return LearningStandard.SCORM_12;
  }

  @Override
  public NormalizedUpdateResult applyCommit(StandardCommitPayload payload, LaunchRuntimeState currentState) {
    Map<String, Object> merged = mergeRaw(payload.rawKeyValues(), currentState);
    NormalizedProgress progress = copyProgress(currentState);

    Object lessonStatus = payload.rawKeyValues().get(ScormRuntimeConstants.Scorm12.KEY_LESSON_STATUS);
    if (lessonStatus != null) {
      String status = String.valueOf(lessonStatus).toLowerCase();
      switch (status) {
        case ScormRuntimeConstants.Scorm12.STATUS_COMPLETED -> progress.setCompletionStatus(CompletionStatus.COMPLETE);
        case ScormRuntimeConstants.Scorm12.STATUS_INCOMPLETE,
            ScormRuntimeConstants.Scorm12.STATUS_BROWSED -> progress.setCompletionStatus(CompletionStatus.INCOMPLETE);
        case ScormRuntimeConstants.Scorm12.STATUS_NOT_ATTEMPTED -> progress.setCompletionStatus(CompletionStatus.NOT_ATTEMPTED);
        case ScormRuntimeConstants.Scorm12.STATUS_PASSED -> {
          progress.setCompletionStatus(CompletionStatus.COMPLETE);
          progress.setSuccessStatus(SuccessStatus.PASSED);
        }
        case ScormRuntimeConstants.Scorm12.STATUS_FAILED -> {
          progress.setCompletionStatus(CompletionStatus.COMPLETE);
          progress.setSuccessStatus(SuccessStatus.FAILED);
        }
        default -> {
        }
      }
    }

    if (payload.rawKeyValues().containsKey(ScormRuntimeConstants.Scorm12.KEY_SCORE_RAW)) {
      progress.getScore().setRaw(parseBigDecimal(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm12.KEY_SCORE_RAW)));
    }

    Long sessionSeconds = parseScorm12Time(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm12.KEY_SESSION_TIME));
    if (sessionSeconds != null) {
      progress.getTime().setSessionSeconds(sessionSeconds);
    }

    Long totalProvided = parseScorm12Time(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm12.KEY_TOTAL_TIME));
    Long existingTotal = progress.getTime().getTotalSeconds() == null ? 0L : progress.getTime().getTotalSeconds();
    long computedTotal = sessionSeconds == null ? existingTotal : existingTotal + sessionSeconds;
    if (totalProvided != null) {
      computedTotal = Math.max(computedTotal, totalProvided);
    }
    progress.getTime().setTotalSeconds(computedTotal);

    if (payload.rawKeyValues().containsKey(ScormRuntimeConstants.Scorm12.KEY_LESSON_LOCATION)) {
      progress.setLocation(String.valueOf(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm12.KEY_LESSON_LOCATION)));
    }

    if (payload.rawKeyValues().containsKey(ScormRuntimeConstants.Scorm12.KEY_SUSPEND_DATA)) {
      progress.setSuspendData(String.valueOf(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm12.KEY_SUSPEND_DATA)));
    }

    progress.setLastCommitAt(payload.clientTime());

    return new NormalizedUpdateResult(progress, merged);
  }
}

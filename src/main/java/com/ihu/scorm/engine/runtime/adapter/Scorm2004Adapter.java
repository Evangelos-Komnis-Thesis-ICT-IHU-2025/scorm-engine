package com.ihu.scorm.engine.runtime.adapter;

import com.ihu.scorm.engine.attempt.crud.CompletionStatus;
import com.ihu.scorm.engine.attempt.crud.SuccessStatus;
import com.ihu.scorm.engine.runtime.state.LaunchRuntimeState;
import com.ihu.scorm.engine.runtime.state.NormalizedProgress;
import com.ihu.scorm.engine.standard.LearningStandard;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class Scorm2004Adapter extends AbstractStandardAdapter {

  @Override
  public LearningStandard standard() {
    return LearningStandard.SCORM_2004;
  }

  @Override
  public NormalizedUpdateResult applyCommit(StandardCommitPayload payload, LaunchRuntimeState currentState) {
    Map<String, Object> merged = mergeRaw(payload.rawKeyValues(), currentState);
    NormalizedProgress progress = copyProgress(currentState);

    Object completion = payload.rawKeyValues().get(ScormRuntimeConstants.Scorm2004.KEY_COMPLETION_STATUS);
    if (completion != null) {
      String completionText = String.valueOf(completion).toLowerCase();
      switch (completionText) {
        case ScormRuntimeConstants.Scorm2004.STATUS_COMPLETED -> progress.setCompletionStatus(CompletionStatus.COMPLETE);
        case ScormRuntimeConstants.Scorm2004.STATUS_INCOMPLETE -> progress.setCompletionStatus(CompletionStatus.INCOMPLETE);
        case ScormRuntimeConstants.Scorm2004.STATUS_NOT_ATTEMPTED,
            ScormRuntimeConstants.Scorm2004.STATUS_UNKNOWN -> progress.setCompletionStatus(CompletionStatus.NOT_ATTEMPTED);
        default -> {
        }
      }
    }

    Object success = payload.rawKeyValues().get(ScormRuntimeConstants.Scorm2004.KEY_SUCCESS_STATUS);
    if (success != null) {
      String successText = String.valueOf(success).toLowerCase();
      switch (successText) {
        case ScormRuntimeConstants.Scorm2004.STATUS_PASSED -> progress.setSuccessStatus(SuccessStatus.PASSED);
        case ScormRuntimeConstants.Scorm2004.STATUS_FAILED -> progress.setSuccessStatus(SuccessStatus.FAILED);
        default -> progress.setSuccessStatus(SuccessStatus.UNKNOWN);
      }
    }

    if (payload.rawKeyValues().containsKey(ScormRuntimeConstants.Scorm2004.KEY_SCORE_RAW)) {
      progress.getScore().setRaw(parseBigDecimal(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm2004.KEY_SCORE_RAW)));
    }
    if (payload.rawKeyValues().containsKey(ScormRuntimeConstants.Scorm2004.KEY_SCORE_SCALED)) {
      progress.getScore().setScaled(parseBigDecimal(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm2004.KEY_SCORE_SCALED)));
    }
    if (payload.rawKeyValues().containsKey(ScormRuntimeConstants.Scorm2004.KEY_SCORE_MIN)) {
      progress.getScore().setMin(parseBigDecimal(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm2004.KEY_SCORE_MIN)));
    }
    if (payload.rawKeyValues().containsKey(ScormRuntimeConstants.Scorm2004.KEY_SCORE_MAX)) {
      progress.getScore().setMax(parseBigDecimal(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm2004.KEY_SCORE_MAX)));
    }

    Long sessionSeconds = parseScorm2004Duration(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm2004.KEY_SESSION_TIME));
    if (sessionSeconds != null) {
      progress.getTime().setSessionSeconds(sessionSeconds);
    }

    Long totalProvided = parseScorm2004Duration(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm2004.KEY_TOTAL_TIME));
    Long existingTotal = progress.getTime().getTotalSeconds() == null ? 0L : progress.getTime().getTotalSeconds();
    long computedTotal = sessionSeconds == null ? existingTotal : existingTotal + sessionSeconds;
    if (totalProvided != null) {
      computedTotal = Math.max(computedTotal, totalProvided);
    }
    progress.getTime().setTotalSeconds(computedTotal);

    if (payload.rawKeyValues().containsKey(ScormRuntimeConstants.Scorm2004.KEY_LOCATION)) {
      progress.setLocation(String.valueOf(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm2004.KEY_LOCATION)));
    }
    if (payload.rawKeyValues().containsKey(ScormRuntimeConstants.Scorm2004.KEY_SUSPEND_DATA)) {
      progress.setSuspendData(String.valueOf(payload.rawKeyValues().get(ScormRuntimeConstants.Scorm2004.KEY_SUSPEND_DATA)));
    }

    progress.setLastCommitAt(payload.clientTime());

    return new NormalizedUpdateResult(progress, merged);
  }
}

package com.ihu.scorm.engine.runtime.adapter;

import com.ihu.scorm.engine.runtime.state.LaunchRuntimeState;
import com.ihu.scorm.engine.standard.LearningStandard;

public interface StandardAdapter {

  LearningStandard standard();

  NormalizedUpdateResult applyCommit(StandardCommitPayload payload, LaunchRuntimeState currentState);
}

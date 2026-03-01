package com.ihu.scorm.engine.runtime.api;

import com.ihu.scorm.engine.runtime.state.NormalizedProgress;
import java.util.UUID;

public record RuntimeCommitResponse(
    UUID launchId,
    UUID attemptId,
    Integer acceptedSequence,
    NormalizedProgress normalizedProgress) {
}

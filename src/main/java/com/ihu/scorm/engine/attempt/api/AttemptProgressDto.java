package com.ihu.scorm.engine.attempt.api;

import com.ihu.scorm.engine.runtime.state.NormalizedProgress;
import java.util.UUID;

public record AttemptProgressDto(UUID attemptId, NormalizedProgress normalizedProgress) {
}

package com.ihu.scorm.engine.runtime.adapter;

import com.ihu.scorm.engine.standard.LearningStandard;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record StandardCommitPayload(
    UUID launchId,
    UUID attemptId,
    UUID userId,
    UUID courseId,
    LearningStandard standard,
    Integer sequence,
    Instant clientTime,
    Map<String, Object> rawKeyValues) {
}

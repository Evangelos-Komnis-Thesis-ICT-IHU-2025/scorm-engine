package com.ihu.scorm.engine.common.token;

import com.ihu.scorm.engine.standard.LearningStandard;
import java.time.Instant;
import java.util.UUID;

public record LaunchTokenPayload(
    UUID launchId,
    UUID attemptId,
    UUID userId,
    UUID courseId,
    LearningStandard standard,
    Instant expiresAt) {
}

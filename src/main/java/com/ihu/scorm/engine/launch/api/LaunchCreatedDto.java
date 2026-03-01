package com.ihu.scorm.engine.launch.api;

import com.ihu.scorm.engine.standard.LearningStandard;
import java.time.Instant;
import java.util.UUID;

public record LaunchCreatedDto(
    UUID launchId,
    UUID attemptId,
    LearningStandard standard,
    String launchUrl,
    String launchToken,
    Instant expiresAt) {
}

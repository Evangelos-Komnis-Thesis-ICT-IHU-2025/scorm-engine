package com.ihu.scorm.engine.reporting.api;

import java.util.UUID;

public record CourseStatsDto(
    UUID courseId,
    long enrolledCount,
    long startedCount,
    long completedCount,
    long activeAttempts,
    double averageTimeSeconds,
    double averageScoreRaw) {
}

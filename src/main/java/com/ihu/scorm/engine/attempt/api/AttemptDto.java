package com.ihu.scorm.engine.attempt.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AttemptDto(
    UUID id,
    UUID userId,
    UUID courseId,
    UUID enrollmentId,
    Integer attemptNo,
    String status,
    Instant startedAt,
    Instant endedAt,
    String completionStatus,
    String successStatus,
    BigDecimal scoreRaw,
    BigDecimal scoreScaled,
    Long totalTimeSeconds,
    String lastLocation,
    Instant lastCommittedAt,
    Instant createdAt,
    Instant updatedAt) {
}

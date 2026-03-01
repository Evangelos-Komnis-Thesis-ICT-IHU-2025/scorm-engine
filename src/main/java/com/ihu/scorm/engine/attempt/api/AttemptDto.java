package com.ihu.scorm.engine.attempt.api;

import com.ihu.scorm.engine.attempt.crud.AttemptStatus;
import com.ihu.scorm.engine.attempt.crud.CompletionStatus;
import com.ihu.scorm.engine.attempt.crud.SuccessStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AttemptDto(
    UUID id,
    UUID userId,
    UUID courseId,
    UUID enrollmentId,
    Integer attemptNo,
    AttemptStatus status,
    Instant startedAt,
    Instant endedAt,
    CompletionStatus completionStatus,
    SuccessStatus successStatus,
    BigDecimal scoreRaw,
    BigDecimal scoreScaled,
    Long totalTimeSeconds,
    String lastLocation,
    Instant lastCommittedAt,
    Instant createdAt,
    Instant updatedAt) {
}

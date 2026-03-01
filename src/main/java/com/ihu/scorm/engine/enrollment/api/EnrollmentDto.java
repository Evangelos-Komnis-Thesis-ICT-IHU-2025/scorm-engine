package com.ihu.scorm.engine.enrollment.api;

import java.time.Instant;
import java.util.UUID;

public record EnrollmentDto(
    UUID id,
    UUID userId,
    UUID courseId,
    String status,
    Instant createdAt,
    Instant updatedAt) {
}

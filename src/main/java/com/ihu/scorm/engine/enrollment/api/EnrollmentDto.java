package com.ihu.scorm.engine.enrollment.api;

import com.ihu.scorm.engine.enrollment.crud.EnrollmentStatus;
import java.time.Instant;
import java.util.UUID;

public record EnrollmentDto(
    UUID id,
    UUID userId,
    UUID courseId,
    EnrollmentStatus status,
    Instant createdAt,
    Instant updatedAt) {
}

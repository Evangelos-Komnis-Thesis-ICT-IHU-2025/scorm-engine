package com.ihu.scorm.engine.enrollment.api;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateEnrollmentForm(@NotNull UUID userId, @NotNull UUID courseId) {
}

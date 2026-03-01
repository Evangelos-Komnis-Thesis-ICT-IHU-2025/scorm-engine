package com.ihu.scorm.engine.launch.api;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateLaunchForm(@NotNull UUID userId, @NotNull UUID courseId, Boolean forceNewAttempt) {
}

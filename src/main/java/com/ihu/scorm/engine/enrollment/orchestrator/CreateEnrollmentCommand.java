package com.ihu.scorm.engine.enrollment.orchestrator;

import java.util.UUID;

public record CreateEnrollmentCommand(UUID userId, UUID courseId) {
}

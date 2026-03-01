package com.ihu.scorm.engine.launch.orchestrator;

import java.util.UUID;

public record CreateLaunchCommand(UUID userId, UUID courseId, boolean forceNewAttempt) {
}

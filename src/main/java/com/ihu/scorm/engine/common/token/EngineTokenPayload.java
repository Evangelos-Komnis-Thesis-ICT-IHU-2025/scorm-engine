package com.ihu.scorm.engine.common.token;

import java.util.List;
import java.util.UUID;

public record EngineTokenPayload(UUID userId, List<String> roles) {
}

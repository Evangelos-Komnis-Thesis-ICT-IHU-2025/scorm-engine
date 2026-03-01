package com.ihu.scorm.engine.common.api;

import java.util.Map;

public record ApiError(String code, String message, Map<String, Object> details) {
}

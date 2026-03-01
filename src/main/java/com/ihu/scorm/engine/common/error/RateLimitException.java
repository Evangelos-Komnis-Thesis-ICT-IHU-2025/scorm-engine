package com.ihu.scorm.engine.common.error;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class RateLimitException extends ApiException {

  public RateLimitException(String code, String message, Map<String, Object> details) {
    super(HttpStatus.TOO_MANY_REQUESTS, code, message, details);
  }

  public RateLimitException(ErrorCode code, String message, Map<String, Object> details) {
    super(HttpStatus.TOO_MANY_REQUESTS, code, message, details);
  }
}

package com.ihu.scorm.engine.common.error;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class ValidationException extends ApiException {

  public ValidationException(String code, String message, Map<String, Object> details) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, code, message, details);
  }

  public ValidationException(ErrorCode code, String message, Map<String, Object> details) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, code, message, details);
  }
}

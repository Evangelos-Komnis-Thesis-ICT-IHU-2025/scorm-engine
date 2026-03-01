package com.ihu.scorm.engine.common.error;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {

  public ConflictException(String code, String message, Map<String, Object> details) {
    super(HttpStatus.CONFLICT, code, message, details);
  }

  public ConflictException(ErrorCode code, String message, Map<String, Object> details) {
    super(HttpStatus.CONFLICT, code, message, details);
  }
}

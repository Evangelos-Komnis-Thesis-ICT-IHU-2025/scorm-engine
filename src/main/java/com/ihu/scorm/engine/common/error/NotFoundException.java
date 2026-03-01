package com.ihu.scorm.engine.common.error;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

  public NotFoundException(String code, String message, Map<String, Object> details) {
    super(HttpStatus.NOT_FOUND, code, message, details);
  }

  public NotFoundException(ErrorCode code, String message, Map<String, Object> details) {
    super(HttpStatus.NOT_FOUND, code, message, details);
  }
}

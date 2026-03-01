package com.ihu.scorm.engine.common.error;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {

  public UnauthorizedException(String code, String message, Map<String, Object> details) {
    super(HttpStatus.UNAUTHORIZED, code, message, details);
  }

  public UnauthorizedException(ErrorCode code, String message, Map<String, Object> details) {
    super(HttpStatus.UNAUTHORIZED, code, message, details);
  }
}

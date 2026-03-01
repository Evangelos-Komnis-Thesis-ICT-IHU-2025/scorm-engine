package com.ihu.scorm.engine.common.api;

import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiErrorResponse> handleApi(ApiException exception, HttpServletRequest request) {
    return ResponseEntity.status(exception.getStatus())
        .body(new ApiErrorResponse(new ApiError(exception.getCode(), exception.getMessage(), exception.getDetails())));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
    Map<String, Object> details = new LinkedHashMap<>();
    exception.getBindingResult().getFieldErrors().forEach(err -> details.put(err.getField(), err.getDefaultMessage()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ApiErrorResponse(new ApiError(
            ErrorCode.VALIDATION_ERROR.value(),
            ErrorMessages.VALIDATION_FAILED,
            details)));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ApiErrorResponse(new ApiError(
            ErrorCode.BAD_REQUEST.value(),
            exception.getMessage(),
            Map.of())));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiErrorResponse(new ApiError(
            ErrorCode.INTERNAL_ERROR.value(),
            ErrorMessages.UNEXPECTED_SERVER_ERROR,
            Map.of())));
  }
}

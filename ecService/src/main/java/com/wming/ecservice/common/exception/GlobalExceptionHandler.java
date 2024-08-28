package com.wming.ecservice.common.exception;

import com.wming.ecservice.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
      ResourceNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(e.getMessage()));
  }
}

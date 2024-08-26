package com.wming.ecservice.common.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

  private boolean success;
  private String message;
  private T data;

  public ApiResponse(boolean success, String message, T data) {
    this.success = success;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(true, "요청에 성공했습니다", data);
  }

  public static <T> ApiResponse<T> successString(String message) {
    return new ApiResponse<>(true, message, null);
  }

  public static <T> ApiResponse<T> fail(String message) {
    return new ApiResponse<>(false, message, null);
  }
}

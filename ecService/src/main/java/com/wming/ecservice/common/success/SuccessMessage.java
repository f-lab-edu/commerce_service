package com.wming.ecservice.common.success;

import lombok.Getter;

@Getter
public enum SuccessMessage {

  SUCCESS_ORDER("주문이 성공했습니다.");

  private final String message;

  SuccessMessage(String message) {
    this.message = message;
  }

  public String getMessage(Object... args) {
    return String.format(this.message, args);
  }
}

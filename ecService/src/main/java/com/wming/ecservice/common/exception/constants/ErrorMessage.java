package com.wming.ecservice.common.exception.constants;

import lombok.Getter;

@Getter
public enum ErrorMessage {
  PRODUCT_NOT_FOUND("상품이 존재하지 않습니다. 상품 이름 = %s"),
  INSUFFICIENT_STOCK("재고가 부족합니다. 상품 이름 = %s"),
  FAILED_PAYMENT("결제에 실패했습니다.");

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage(Object... args) {
    return String.format(this.message, args);
  }
}

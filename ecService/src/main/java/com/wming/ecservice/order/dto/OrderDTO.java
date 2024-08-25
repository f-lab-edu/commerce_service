package com.wming.ecservice.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderDTO {

  /*주문 번호*/
  private long orderId;

  /*상품 번호*/
  private long productId;

  /*상품 주문 수*/
  private int quantity;

}

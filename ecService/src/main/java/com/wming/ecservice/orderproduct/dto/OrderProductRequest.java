package com.wming.ecservice.orderproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductRequest {

  private Long productId;
  private String productName;
  private int quantity;
}

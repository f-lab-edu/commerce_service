package com.wming.ecservice.order.dto;

import com.wming.ecservice.orderproduct.dto.OrderProductRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderRequest {

  /* 상품 리스트들 */
  private List<OrderProductRequest> orderProducts;


}

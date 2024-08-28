package com.wming.ecservice.order.controller;

import com.wming.ecservice.common.response.ApiResponse;
import com.wming.ecservice.common.success.SuccessMessage;
import com.wming.ecservice.order.dto.OrderRequest;
import com.wming.ecservice.order.service.OrderSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

  private OrderSerivce orderSerivce;

  public OrderController(OrderSerivce orderSerivce) {
    this.orderSerivce = orderSerivce;
  }

  @PostMapping(value = "/createOrder", produces = "application/json")
  public ResponseEntity<ApiResponse<String>> createOrder(@RequestBody OrderRequest orderRequest) {
    // 주문 정보 받는 거는  (상품 정보 product ,
    orderSerivce.createOrder(orderRequest);
    return ResponseEntity.ok(ApiResponse.successString(SuccessMessage.SUCCESS_ORDER.getMessage()));
  }
}


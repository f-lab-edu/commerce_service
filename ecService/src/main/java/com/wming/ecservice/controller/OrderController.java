package com.wming.ecservice.controller;

import com.wming.ecservice.dto.OrderDTO;
import com.wming.ecservice.service.OrderSerivce;
import lombok.extern.slf4j.Slf4j;
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

  @PostMapping("/createOrder")
  public String createOrder(@RequestBody OrderDTO orderDto) {
    orderSerivce.createOrder(orderDto);
    return "주문이 완료되었습니다.";
  }
}


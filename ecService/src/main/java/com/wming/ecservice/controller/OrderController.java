package com.wming.ecservice.controller;

import com.wming.ecservice.service.OrderSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderSerivce orderSerivce;

    public OrderController(OrderSerivce orderSerivce) {
        this.orderSerivce = orderSerivce;
    }

    @PostMapping("/createOrder")
    public String createOrder(@RequestParam("productId") Long productId, @RequestParam("quantity") int quantity) {
        orderSerivce.createOrder(productId, quantity);
        return "주문이 완료되었습니다.";
    }
}


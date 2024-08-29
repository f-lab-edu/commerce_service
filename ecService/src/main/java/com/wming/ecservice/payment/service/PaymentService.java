package com.wming.ecservice.payment.service;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {

  public boolean processPayment(BigDecimal totalPrice) {
    log.info("결제 성공");
    return true;
  }
}

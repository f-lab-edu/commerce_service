package com.wming.ecservice.payment.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  public boolean processPayment(BigDecimal totalPrice) {
    return true;
  }
}

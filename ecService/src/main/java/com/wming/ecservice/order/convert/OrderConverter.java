package com.wming.ecservice.order.convert;

import com.wming.ecservice.order.entity.OrderEntity;
import com.wming.ecservice.order.entity.OrderStatus;
import com.wming.ecservice.orderproduct.entity.OrderProductEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {

  public OrderEntity convertToOrderEntity(List<OrderProductEntity> orderProductEntityList,
      BigDecimal totalPrice) {
    return new OrderEntity(
        orderProductEntityList,
        LocalDateTime.now(),
        totalPrice,
        OrderStatus.PENDING
    );
  }
}




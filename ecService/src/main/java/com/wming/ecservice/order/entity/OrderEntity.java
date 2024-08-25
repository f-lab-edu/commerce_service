package com.wming.ecservice.order.entity;

import com.wming.ecservice.orderproduct.entity.OrderProductEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Table(name = "ORDERS")
@Entity
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<OrderProductEntity> orderProductEntities = new ArrayList<>();

  private LocalDateTime createTime; //주문일

  private BigDecimal totalPrice; //총 결제 금액

  private OrderStatus orderStatus; //주문 상태

  public OrderEntity() {
  }

  public OrderEntity(List<OrderProductEntity> orderProductEntityList, LocalDateTime now,
      BigDecimal totalPrice, OrderStatus orderStatus) {
    this.orderProductEntities = orderProductEntityList;
    this.createTime = now;
    this.totalPrice = totalPrice;
    this.orderStatus = orderStatus;
  }
}

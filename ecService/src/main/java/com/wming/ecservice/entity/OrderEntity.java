package com.wming.ecservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Entity
@Slf4j
@Builder
@AllArgsConstructor
@Getter
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  @ManyToOne // 주문은 하나의 상품과 매핑
  private ProductEntity productEntity;

  private int quantity;
  private boolean isPaid;

  public OrderEntity(ProductEntity productEntity, int quantity, boolean isPaid) {
    this.productEntity = productEntity;
    this.quantity = quantity;
    this.isPaid = isPaid;
  }

  public void checkAndDecrementStock() {
    if (productEntity.getProductStock() >= quantity) {
      productEntity.reduceStock(quantity);
    } else {
      log.debug("재고 부족");
    }
  }

  public void pay() {
    this.isPaid = true;
    log.debug("결제 완료");
  }
}

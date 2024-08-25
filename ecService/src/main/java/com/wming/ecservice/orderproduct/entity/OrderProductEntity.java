package com.wming.ecservice.orderproduct.entity;

import com.wming.ecservice.order.entity.OrderEntity;
import com.wming.ecservice.product.entity.ProductEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
@Entity
@Table(name = "ORDER_PRODUCT")
public class OrderProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderProductId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PRODUCT_ID")
  private ProductEntity productEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ORDER_ID")
  private OrderEntity orderEntity;

  private BigDecimal price;

  private int quantity;

  public OrderProductEntity() {
  }

  public OrderProductEntity(ProductEntity productEntity, BigDecimal price,
      int quantity) {
    this.productEntity = productEntity;
    this.price = price;
    this.quantity = quantity;
  }

  /*전체 값 계산 결과*/
  public BigDecimal getTotalPrice() {
    return price.multiply(BigDecimal.valueOf(quantity));
  }
}

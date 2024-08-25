package com.wming.ecservice.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Table(name = "PRODUCT")
@Entity
@Getter
@NoArgsConstructor
@Slf4j
public class ProductEntity {

  //자동으로 아이디 값 증가
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId; // 상품 ID (PK)

  @Column(nullable = false)
  private String productName; // 상품명

  @Column(nullable = false)
  private String productContent; // 상품 설명

  @Column(nullable = false)
  private BigDecimal productPrice; // 상품 가격

  @Column(nullable = false)
  private int productStock; // 상품 재고

  public void reduceStock(int quantity) {
    this.productStock -= quantity;
    log.debug("재고 감소 완료 : productStock={}", productStock);
  }

  public boolean isStockAvaliable(int quantity) {
    return this.productStock >= quantity;
  }

}

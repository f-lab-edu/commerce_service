package com.wming.ecservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

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
        log.debug("재고 감소 완료 : productStock={}" ,productStock);
    }
}

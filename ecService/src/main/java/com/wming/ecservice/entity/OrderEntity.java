package com.wming.ecservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Slf4j
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordId;

    @ManyToOne // 주문은 하나의 상품과 매핑
    private ProductEntity productEntity;

    private int     quantity;
    private boolean isPaid;

    public OrderEntity(ProductEntity productEntity, int quantity) {
        this.productEntity = productEntity;
        this.quantity = quantity;
    }

    public void processOrder() {
        if (productEntity.getProductStock() >= quantity) {
            productEntity.reduceStock(quantity);
            log.info("주문 완료 : ordId={}" ,ordId);

        } else {
            log.info("재고 부족");
        }
    }

    public void pay() {
        this.isPaid = true;
        log.info("결제 완료");
    }
}

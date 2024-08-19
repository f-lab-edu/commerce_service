package com.wming.ecservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
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
            System.out.println("주문이 완료되었습니다.");
        } else {
            System.out.println("재고가 부족합니다.");
        }
    }

    public void pay() {
        this.isPaid = true;
        System.out.println("결제가 완료되었습니다.");
    }
}

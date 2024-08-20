package com.wming.ecservice.service;

import com.wming.ecservice.entity.OrderEntity;
import com.wming.ecservice.entity.ProductEntity;
import com.wming.ecservice.repository.OrderRepository;
import com.wming.ecservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderSerivce {
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    @Autowired
    public OrderSerivce(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void createOrder(Long prodId, int quantity) {
        ProductEntity productEntity = productRepository.findById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        OrderEntity orderEntity = new OrderEntity(productEntity , quantity);
        orderEntity.processOrder();
        orderRepository.save(orderEntity);

        this.payOrder(orderEntity.getOrdId());
    }

    @Transactional
    public void payOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        orderEntity.pay();
        orderRepository.save(orderEntity);
    }
}
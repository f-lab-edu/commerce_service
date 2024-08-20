package com.wming.ecservice.service;

import com.wming.ecservice.dto.OrderDTO;
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
  public void createOrder(OrderDTO orderDTO) {
    //1. 상품 조회
    ProductEntity productEntity = productRepository.findById(orderDTO.getProductId())
        .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

    //2. 주문 엔티티 생성 후 저장
    OrderEntity orderEntity = convertToEntity(productEntity, orderDTO);
    orderEntity = orderRepository.save(orderEntity);

    //3. 저장된 주문의 ID를 이용해 결제 처리
    OrderDTO savedOrderDTO = convertToDTO(orderEntity);
    this.payOrder(savedOrderDTO);
  }

  @Transactional
  public void payOrder(OrderDTO orderDto) {

    //1. 주문 조회
    OrderEntity orderEntity = orderRepository.findById(orderDto.getOrdId())
        .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

    //2. 결제 처리 로직
    orderEntity.pay();

    //3. 결제 상태 변경된 후 주문 다시 저장
    orderRepository.save(orderEntity);
  }

  /* DTO -> Entity로 변환 */
  public OrderEntity convertToEntity(ProductEntity productEntity, OrderDTO orderDTO) {
    return OrderEntity.builder()
        .productEntity(productEntity)
        .quantity(orderDTO.getQuantity())
        .build();
  }

  /* Entity로 -> DTO로 변환 */
  public OrderDTO convertToDTO(OrderEntity orderEntity) {
    return OrderDTO.builder()
        .ordId(orderEntity.getOrdId())
        .productId(orderEntity.getProductEntity().getProductId())
        .quantity(orderEntity.getQuantity())
        .build();
  }
}
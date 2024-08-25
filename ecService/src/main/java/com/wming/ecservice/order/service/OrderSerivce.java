package com.wming.ecservice.order.service;

import com.wming.ecservice.order.convert.OrderConverter;
import com.wming.ecservice.order.dto.OrderRequest;
import com.wming.ecservice.order.entity.OrderEntity;
import com.wming.ecservice.order.repository.OrderRepository;
import com.wming.ecservice.orderproduct.dto.OrderProductRequest;
import com.wming.ecservice.orderproduct.entity.OrderProductEntity;
import com.wming.ecservice.payment.service.PaymentService;
import com.wming.ecservice.product.entity.ProductEntity;
import com.wming.ecservice.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderSerivce {

  private final PaymentService paymentService;
  private final OrderConverter orderConverter;
  private ProductRepository productRepository;
  private OrderRepository orderRepository;
  private List<OrderProductEntity> orderProductEntityList;

  @Autowired
  public OrderSerivce(ProductRepository productRepository, OrderRepository orderRepository,
      PaymentService paymentService, OrderConverter orderConverter,
      List<OrderProductEntity> orderProductEntityList) {
    this.productRepository = productRepository;
    this.orderRepository = orderRepository;
    this.paymentService = paymentService;
    this.orderConverter = orderConverter;
    this.orderProductEntityList = orderProductEntityList;
  }

  /*주문 생성*/
  @Transactional
  public void createOrder(OrderRequest orderRequest) {

    BigDecimal totalPrice = BigDecimal.ZERO;

    for (OrderProductRequest orderProduct : orderRequest.getOrderProducts()) {
      //1. 상품이 존재하는지 확인
      ProductEntity productEntity = productRepository.findById(orderProduct.getProductId())
          .orElseThrow(
              () -> new RuntimeException("상품 없음, 상품 이름 =" + orderProduct.getProductName()));

      //2. 재고 확인
      this.checkStockAvailability(productEntity, orderProduct.getQuantity());

      //3. 총결제 금액 계산
      BigDecimal productPrice = productEntity.getProductPrice();
      BigDecimal quantity = BigDecimal.valueOf(orderProduct.getQuantity());
      totalPrice = totalPrice.add(productPrice.multiply(quantity));

      //4. OrderProductEntity 생성해주기
      orderProductEntityList.add(
          new OrderProductEntity(productEntity, productEntity.getProductPrice(),
              orderProduct.getQuantity())
      );
    }

    //4. 결제 처리
    boolean paymentResult = paymentService.processPayment(totalPrice);

    if (!paymentResult) {
      throw new RuntimeException("결제에 실패했습니다.");
    }

    //5. 조회된 상품들과 DTO를 바탕으로 OrderEntity를 생성
    OrderEntity orderEntity = orderConverter.convertToOrderEntity(orderProductEntityList,
        totalPrice);

    //6. 주문 저장
    orderRepository.save(orderEntity);
  }

  /*재고 확인을 위한 메서드*/
  private void checkStockAvailability(ProductEntity productEntity,
      int quantity) {

    // 2_1.재고 확인
    if (!productEntity.isStockAvaliable(productEntity.getProductStock())) {
      throw new RuntimeException("재고가 충분하지 않습니다. 상품: " + productEntity.getProductName());
    }

    // 2_2. 실제 재고 감소
    productEntity.reduceStock(quantity);
  }
}
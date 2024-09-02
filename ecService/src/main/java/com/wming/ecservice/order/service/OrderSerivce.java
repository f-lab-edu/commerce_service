package com.wming.ecservice.order.service;

import com.wming.ecservice.common.exception.FailedPaymentException;
import com.wming.ecservice.common.exception.ResourceNotFoundException;
import com.wming.ecservice.common.exception.constants.ErrorMessage;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderSerivce {

  private final PaymentService paymentService;
  private final OrderConverter orderConverter;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final StockService stockService;

  @Autowired
  public OrderSerivce(ProductRepository productRepository, OrderRepository orderRepository,
      PaymentService paymentService, OrderConverter orderConverter,
      List<OrderProductEntity> orderProductEntityList, StockService stockService) {
    this.productRepository = productRepository;
    this.orderRepository = orderRepository;
    this.paymentService = paymentService;
    this.orderConverter = orderConverter;
    this.stockService = stockService;
  }

  /*주문 생성*/
  @Transactional
  public void createOrder(OrderRequest orderRequest) {

    log.info("주문 생성 시도: {}", orderRequest);

    //1. 모든 상품을 조회
    Map<Long, ProductEntity> productEntityMap = getProductEntities(orderRequest);

    //2. 주문 상품 정보 생성 및 재고 감소 처리
    List<OrderProductEntity> orderProductEntities = createOrderProductEntities(orderRequest,
        productEntityMap);

    // 3. 총결제 금액 계산
    BigDecimal totalPrice = calculateTotalPrice(orderProductEntities);

    //4. 결제
    boolean paymentResult = paymentService.processPayment(totalPrice);

    if (!paymentResult) {
      throw new FailedPaymentException(ErrorMessage.FAILED_PAYMENT.getMessage());
    }

    //5. 주문 저장
    saveOrder(orderProductEntities, totalPrice);
  }
  
  /**
   * 주문 요청에 포함된 상품 ID들을 사용하여 상품 정보를 조회.
   *
   * @param orderRequest 주문 요청 객체, 주문에 포함된 상품 리스트를 포함.
   * @return Map<Long, ProductEntity> 주문에 포함된 상품 ID와 해당 상품 엔티티 간의 매핑을 반환.
   */
  private Map<Long, ProductEntity> getProductEntities(OrderRequest orderRequest) {

    List<Long> productIds = orderRequest.getOrderProducts().stream()
        .map(OrderProductRequest::getProductId)
        .collect(Collectors.toList());

    return productRepository.findAllById(productIds)
        .stream()
        .collect(Collectors.toMap(ProductEntity::getProductId, productEntity -> productEntity));
  }

  /**
   * 회원 주문 데이터에 기반하여 주문 정보 생성.
   *
   * @param orderRequest     주문 요청 객체, 주문에 포함된 상품 리스트를 포함.
   * @param productEntityMap 주문에 포함된 상품 엔티티 Map.
   * @return List<OrderProductEntity> 주문 상품 정보 엔티티 반환.
   */
  private List<OrderProductEntity> createOrderProductEntities(OrderRequest orderRequest,
      Map<Long, ProductEntity> productEntityMap) {

    List<OrderProductEntity> orderProductEntities = new ArrayList<>();

    for (OrderProductRequest orderProduct : orderRequest.getOrderProducts()) {

      //1. 상품 존재 확인
      ProductEntity productEntity = productEntityMap.get(orderProduct.getProductId());

      if (productEntity == null) {
        throw new ResourceNotFoundException(
            ErrorMessage.PRODUCT_NOT_FOUND.getMessage(orderProduct.getProductName()));
      }

      //2. 재고 확인 및 감소
      stockService.checkAndReduceStock(productEntity, orderProduct.getQuantity());

      //4. OrderProductEntity 생성
      orderProductEntities.add(
          new OrderProductEntity(productEntity, productEntity.getProductPrice(),
              orderProduct.getQuantity())
      );
    }
    return orderProductEntities;
  }

  /**
   * 주문 상품 엔티티 객체를 통해 전체 계산 금액 환산.
   *
   * @param orderProductEntities 회원 주문 상품 매핑 엔티티 객체.
   * @return BigDecimal 최종 주문 금액 계산값 반환.
   */
  private BigDecimal calculateTotalPrice(List<OrderProductEntity> orderProductEntities) {
    BigDecimal totalPrice = BigDecimal.ZERO;

    for (OrderProductEntity orderProduct : orderProductEntities) {
      BigDecimal productPrice = orderProduct.getPrice();
      BigDecimal quantity = BigDecimal.valueOf(orderProduct.getQuantity());
      totalPrice = totalPrice.add(productPrice.multiply(quantity));
    }
    return totalPrice;
  }

  /**
   * 생성된 주문 저장
   *
   * @param orderProductEntities 하나의 주문번호와 매핑된 주문 상품 정보
   * @param totalPrice           최종 결제 금액 정보
   */
  private void saveOrder(List<OrderProductEntity> orderProductEntities, BigDecimal totalPrice) {
    OrderEntity orderEntity = orderConverter.convertToOrderEntity(orderProductEntities,
        totalPrice);

    log.info("주문 저장 : {}", orderEntity.getOrderId());
    orderRepository.save(orderEntity);
  }
}
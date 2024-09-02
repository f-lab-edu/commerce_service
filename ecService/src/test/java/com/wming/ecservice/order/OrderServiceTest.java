package com.wming.ecservice.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.wming.ecservice.common.exception.ResourceNotFoundException;
import com.wming.ecservice.order.controller.OrderController;
import com.wming.ecservice.order.dto.OrderRequest;
import com.wming.ecservice.order.service.OrderSerivce;
import com.wming.ecservice.order.service.StockService;
import com.wming.ecservice.orderproduct.dto.OrderProductRequest;
import com.wming.ecservice.product.entity.ProductEntity;
import com.wming.ecservice.product.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


@SpringBootTest
public class OrderServiceTest {

  @Autowired
  private StockService stockService;

  @Autowired
  private OrderController orderController;

  @Autowired
  private ProductRepository productRepository;

  @MockBean
  private OrderSerivce orderSerivce;

  @Test
  @DisplayName("주문 실패 시 재고 원복 테스트")
  public void testRollbackStock() {

    //given
    ArrayList<OrderProductRequest> orderProductRequests = new ArrayList<>();
    orderProductRequests.add(new OrderProductRequest(1L, "상품", 3));
    orderProductRequests.add(new OrderProductRequest(2L, "상품", 5));
    OrderRequest orderRequest = new OrderRequest(orderProductRequests);

    // 초기 재고 상태 확인
    ProductEntity product1 = productRepository.findById(1L)
        .orElseThrow(() -> new RuntimeException("상품1 없음"));
    ProductEntity product2 = productRepository.findById(2L)
        .orElseThrow(() -> new RuntimeException("상품2 없음"));

    int initialStockProduct1 = product1.getProductStock();
    int initialStockProduct2 = product2.getProductStock();

    //when
    doThrow(new RuntimeException("인위적인 결제 실패")).when(orderSerivce)
        .createOrder(any(OrderRequest.class));

    try {
      orderController.createOrder(orderRequest);
    } catch (RuntimeException e) {
      // 예외 처리 로직
      System.out.println("예외 발생: " + e.getMessage());
    }

    // then: 재고가 원복되었는지 확인
    product1 = productRepository.findById(1L)
        .orElseThrow(() -> new RuntimeException("상품1 없음"));
    product2 = productRepository.findById(2L)
        .orElseThrow(() -> new RuntimeException("상품2 없음"));

    assertEquals(initialStockProduct1, product1.getProductStock(), "Product 1의 재고가 원복되지 않았습니다!");
    assertEquals(initialStockProduct2, product2.getProductStock(), "Product 2의 재고가 원복되지 않았습니다!");

  }


  @RepeatedTest(20)
  @Test
  @DisplayName("동시에 같은 상품의 재고를 확인하고 감소시키려 할 때")
  public void testConcurrentReduceStock() throws InterruptedException {

    //Given
    int numberOfThreads = 200;
    int reduceStock = 5;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(1); // 시작 신호를 위한 latch
    AtomicInteger successfulReductions = new AtomicInteger(0);
    AtomicInteger failedReductions = new AtomicInteger(0);
    ProductEntity productEntity = new ProductEntity(1L, "상품1", "테스트상품입니답", new BigDecimal("25000"),
        200);

    //When : 여러 스레드가 동시에 주문을 생성
    for (int i = 0; i < numberOfThreads; i++) {
      executorService.execute(() -> {
        try {
          latch.await(); //모든 스레드가 준비될 때까지 대기
          Thread.sleep((int) (Math.random() * 10));// 인위적으로 딜레이를 추가하여 경합 상태를 유발

          stockService.checkAndReduceStock(productEntity, reduceStock);
          successfulReductions.incrementAndGet();

        } catch (ResourceNotFoundException e) {
          failedReductions.incrementAndGet();
        } catch (Exception e) {
          Thread.currentThread().interrupt();
        }
      });
    }

    latch.countDown(); // 모든 스레드에 시작 신호
    executorService.shutdown();
    Thread.sleep(1000); // 스레드들이 모두 끝날 때까지 대기

    // Then: 40개의 스레드는 성공, 160개의 스레드는 실패해야 함
    assertEquals(40, successfulReductions.get(), "성공한 스레드 수가 예상과 다름");
    assertEquals(160, failedReductions.get(), "실패한 스레드 수가 예상과 다름");
    assertEquals(0, productEntity.getProductStock(), "재고가 0이 되어야 함");

  }
}

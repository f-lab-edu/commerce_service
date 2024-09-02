package com.wming.ecservice.order;

import com.wming.ecservice.order.dto.OrderRequest;
import com.wming.ecservice.order.service.OrderSerivce;
import com.wming.ecservice.orderproduct.dto.OrderProductRequest;
import com.wming.ecservice.product.entity.ProductEntity;
import com.wming.ecservice.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class OrderServiceConcurrencyTest {

    @Autowired
    private OrderSerivce orderSerivce;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("동시에 같은 상품의 재고를 확인하고 감소시키려 할 때")
    public void testConcurrentOrders() throws InterruptedException {

        //Given
        int numberOfThreads = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        List<Future<?>> futures = new ArrayList<>(); //스레드의 결과 저장할 Future객체

        //When : 여러 스레드가 동시에 주문을 생성
        for(int i = 0 ; i < numberOfThreads; i++) {

            futures.add(executorService.submit(() -> {

                try {
                    List<OrderProductRequest> OrderProductRequests = new ArrayList<>();
                    OrderProductRequests.add(new OrderProductRequest(1L, "product_1" , 30));
                    OrderRequest orderRequest = new OrderRequest(OrderProductRequests);
                    orderSerivce.createOrder(orderRequest);
                } catch (Exception e) {
                    System.out.println("스레드 예외 발생 " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }));
        }

        latch.await();
        executorService.shutdown();

        ProductEntity productEntity = productRepository.findById(1L).get();
        System.out.println("최종 재고: " + productEntity.getProductStock());

        assertTrue(productEntity.getProductStock() >= 0, "재고는 음수가 될 수 없음");
    }
}

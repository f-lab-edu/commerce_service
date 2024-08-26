package com.wming.ecservice.order.service;

import com.wming.ecservice.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  /*재고 확인을 위한 메서드*/
  public void checkStockAvailability(ProductEntity productEntity,
      int quantity) {

    // 2_1.재고 확인
    if (!productEntity.isStockAvaliable(productEntity.getProductStock())) {
      throw new RuntimeException("재고가 충분하지 않습니다. 상품: " + productEntity.getProductName());
    }

    // 2_2. 실제 재고 감소
    productEntity.reduceStock(quantity);
  }
}

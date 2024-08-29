package com.wming.ecservice.order.service;

import com.wming.ecservice.common.exception.ErrorMessage;
import com.wming.ecservice.common.exception.ResourceNotFoundException;
import com.wming.ecservice.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  /*재고 확인을 위한 메서드*/
  public void checkAndReduceStock(ProductEntity productEntity,
      int quantity) {

    // 2_1.재고 확인
    if (!productEntity.isStockAvaliable(quantity)) {
      throw new ResourceNotFoundException(
          ErrorMessage.INSUFFICIENT_STOCK.getMessage(productEntity.getProductName()));
    }

    // 2_2. 실제 재고 감소
    productEntity.reduceStock(quantity);
  }
}

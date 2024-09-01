package com.wming.ecservice.order.service;

import com.wming.ecservice.common.exception.ResourceNotFoundException;
import com.wming.ecservice.common.exception.constants.ErrorMessage;
import com.wming.ecservice.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  /* 재고 확인 및 감소 */
  public void checkAndReduceStock(ProductEntity productEntity,
      int quantity) {

    if (!productEntity.isStockAvaliable(quantity)) {
      throw new ResourceNotFoundException(
          ErrorMessage.INSUFFICIENT_STOCK.getMessage(productEntity.getProductName()));
    }

    productEntity.reduceStock(quantity);
  }
}

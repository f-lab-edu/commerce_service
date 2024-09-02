package com.wming.ecservice.order.service;

import com.wming.ecservice.common.exception.ResourceNotFoundException;
import com.wming.ecservice.common.exception.constants.ErrorMessage;
import com.wming.ecservice.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  public void checkAndReduceStock(ProductEntity productEntity,
      int quantity) {
    synchronized (productEntity) {
      if (!productEntity.isStockAvaliable(quantity)) {
        throw new ResourceNotFoundException(
            ErrorMessage.INSUFFICIENT_STOCK.getMessage(productEntity.getProductName()));
      }
      productEntity.reduceStock(quantity);
    }
  }
}

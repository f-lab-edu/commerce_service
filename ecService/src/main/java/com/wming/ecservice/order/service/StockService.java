package com.wming.ecservice.order.service;

import com.wming.ecservice.common.exception.constants.ErrorMessage;
import com.wming.ecservice.common.exception.ResourceNotFoundException;
import com.wming.ecservice.orderproduct.dto.OrderProductRequest;
import com.wming.ecservice.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

  /* 결제 실패 시, 재고 복구 */
  public void restoreStock(Map<Long, ProductEntity> productEntityMap, List<OrderProductRequest> orderProductRequests) {
      for(OrderProductRequest orderProductRequest : orderProductRequests) {
        ProductEntity productEntity = productEntityMap.get(orderProductRequest.getProductId());
        if(productEntity != null) {
          productEntity.increaseStock(orderProductRequest.getQuantity());
        }
      }
  }
}

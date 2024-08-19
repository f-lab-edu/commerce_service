package com.wming.ecservice.repository;

import com.wming.ecservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity , Long> {
}

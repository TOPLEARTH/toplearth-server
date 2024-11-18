package com.gdsc.toplearth_server.infrastructure.repository.product;

import com.gdsc.toplearth_server.domain.entity.product.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositoryImpl extends JpaRepository<Order, Long> {
}

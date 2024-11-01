package com.gdsc.toplearth_server.infrastructure.repository.product;

import com.gdsc.toplearth_server.domain.entity.product.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepositoryImpl extends JpaRepository<Orders, Long> {
}

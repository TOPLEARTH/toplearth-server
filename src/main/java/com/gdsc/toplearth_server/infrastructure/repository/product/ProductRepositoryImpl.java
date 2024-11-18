package com.gdsc.toplearth_server.infrastructure.repository.product;

import com.gdsc.toplearth_server.domain.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositoryImpl extends JpaRepository<Product, Long> {
}

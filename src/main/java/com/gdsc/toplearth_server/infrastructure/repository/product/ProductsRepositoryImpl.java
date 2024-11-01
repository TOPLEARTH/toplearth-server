package com.gdsc.toplearth_server.infrastructure.repository.product;

import com.gdsc.toplearth_server.domain.entity.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepositoryImpl extends JpaRepository<Products, Long> {
}

package com.product.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.Product;
import com.product.entity.enums.ProductType;

/**
 * Spring Data JPA repository for {@link Product product entity}
 * @author Mokrushin Vladimir
 *
 */

@Repository
public interface ProductRepository extends JpaRepository <Product, Long> {
	
	Optional<Product> findBySku (String sku);
	
	Optional<Product> findById (Long id);
	
	List<Product> findByNameAndProductTypeAndPrice(String name, ProductType productType, BigDecimal price);
	

}

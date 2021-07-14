package com.product.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.product.entities.Product;

/**
 * Spring Data JPA repository for {@link Product product entity}
 * @author Mokrushin Vladimir
 *
 */
public interface ProductRepository extends CrudRepository <Product, Long> {
	
	List<Product> findBySku (String sku);
	
	Optional<Product> findById (Long id);

}

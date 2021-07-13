package com.product.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.product.entities.enums.ProductType;

@Entity
@Table (name = "products")
public class Product {

	@Id
	@GeneratedValue
	private Long id;
	
	private String sku;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private ProductType productType;
	
	private BigDecimal price;
}

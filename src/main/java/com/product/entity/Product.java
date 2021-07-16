package com.product.entity;


import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.product.entity.enums.ProductType;

/**
 * Product is an entity that represents product of different
 * types that could be sold
 * @author Mokrushin Vladimir
 *
 */
@Entity
public class Product {

	@Id
	@GeneratedValue
	private Long id;
	
	private String sku;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private ProductType productType;
	
	private BigDecimal price;
	
	/**
	 * The default constructor exists only for the sake of JPA
	 */
	protected Product() {}
	
	public Product (String sku, String name, ProductType productType, BigDecimal price) {
		this.sku = sku;
		this.name = name;
		this.productType = productType;
		this.price = price;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Product[id = %d, sku = %s, name = %s, productType = %s, price = " + this.getPrice().toString() + "]",
				id, sku, name, productType, price);
	}
	
	public Long getId() {
		return id;
	}
	
	public String getSku() {
		return sku;
	}
	
	public String getName() {
		return name;
	}
	
	public ProductType getProductType() {
		return productType;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}

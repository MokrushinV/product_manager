package com.product.entity;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicLong;

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

	private static AtomicLong skuCounter = new AtomicLong();
	
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
	
	public Product (String name, ProductType productType, BigDecimal price) {
		this.name = name;
		this.productType = productType;
		this.price = price;
		setSku(this, name, productType, price);
	}
	
	@Override
	public String toString() {
		return String.format(
				"Product[id = %d, sku = %s, name = %s, productType = %s, price = " +
						this.getPrice().toString() + "]",
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
	
	public static void setSku(Product product, String name, ProductType productType, BigDecimal price) {
		product.sku = generateSku(product, name, productType, price);
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
	
	private static String generateSku(Product product, String name, ProductType productType, BigDecimal price) {
		String result = "";
		String afterSpace = name;
		int end = name.indexOf(' ');
		
		if (end == -1) {
			end = name.length() - 1;
		}
		
		do {	
			String beforeSpace = afterSpace.substring(0,end);
			
			if (beforeSpace.length() > 3) {
				result += beforeSpace.substring(0, 3).toUpperCase();
			} else {
				result += beforeSpace.substring(0, 1).toUpperCase();
			}
			
			if (end + 1 < name.length()) {
				afterSpace = afterSpace.substring(end+1);
			} else {
				break;
			}
				
			end = afterSpace.indexOf(' ');
			
			if (end == -1) {
				if (afterSpace.length() > 3) {
					result += afterSpace.substring(0, 3).toUpperCase();
				} else {
					result += afterSpace.substring(0, 1).toUpperCase();
				}
				break;
			}
		} while (end != -1);
		
		String sku = skuCounter.getAndIncrement() + 
					 result +
					 "T" + productType.ordinal() +
					 "P" + price.setScale(0,RoundingMode.HALF_DOWN).toString();
		return sku;
	}
	
}

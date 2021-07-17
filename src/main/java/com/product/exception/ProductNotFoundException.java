package com.product.exception;

@SuppressWarnings("serial")
public class ProductNotFoundException extends RuntimeException{
	
	public ProductNotFoundException(Long id) {
		
		super("Could not find product with id: " + id);
		
	}

	public ProductNotFoundException(String sku) {


		super("Could not find product with sku: " + sku);
		
	}

}

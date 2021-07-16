package com.product.controller;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.product.assembler.ProductModelAssembler;
import com.product.entity.Product;
import com.product.exception.ProductNotFoundException;
import com.product.repository.ProductRepository;

/**
 * API end-point for product management.
 * @author Mokrushin Vladimir
 *
 */
@RestController
public class ProductController {
	
	private final ProductRepository repository;
	private final ProductModelAssembler assembler;
	
	ProductController (ProductRepository repository, ProductModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}
	
	@GetMapping ("/products")
	public CollectionModel <EntityModel<Product>> getAllProducts() {
		
		List <EntityModel<Product>> products = repository.findAll().stream()
				.map (assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(products,
				linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel(),
				linkTo(methodOn(ProductController.class).getAllProductsSortedByName()).withRel("Sorted by name"),
				linkTo(methodOn(ProductController.class).getAllProductsSortedByType()).withRel("Sorted by product type"));
	}
	
	@GetMapping ("/products/sortedByName")
	public CollectionModel <EntityModel<Product>> getAllProductsSortedByName() {
		
		List <EntityModel <Product>> products = repository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
				.map (assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(products,
				linkTo(methodOn(ProductController.class).getAllProductsSortedByName()).withSelfRel(),
				linkTo(methodOn(ProductController.class).getAllProducts()).withRel("All products"),
				linkTo(methodOn(ProductController.class).getAllProductsSortedByType()).withRel("Sorted by product type"));
	}
	
	@GetMapping ("/products/sortedByType")
	public CollectionModel <EntityModel<Product>> getAllProductsSortedByType() {
		
		List <EntityModel <Product>> products = repository.findAll(Sort.by(Sort.Direction.ASC, "productType")).stream()
				.map (assembler::toModel)
				.collect(Collectors.toList());
		return CollectionModel.of(products,
				
				linkTo(methodOn(ProductController.class).getAllProductsSortedByType()).withSelfRel(),
				linkTo(methodOn(ProductController.class).getAllProducts()).withRel("All products"),
				linkTo(methodOn(ProductController.class).getAllProductsSortedByName()).withRel("Sorted by name"));
	}	
	
	@GetMapping ("/products/{id}")
	public EntityModel<Product> getProductById(@PathVariable Long id) {
		Product productToGet = repository.findById(id).orElseThrow( () -> new ProductNotFoundException(id));
		return assembler.toModel(productToGet);
	}
	
	@DeleteMapping ("/products/{id}") //works for console
	public ResponseEntity<?> deleteProductById(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping ("/products")
	public ResponseEntity<?> newProduct(@RequestBody Product newProduct) {
		
		//Checking if such a product already exists (that's probably should be done by another function)
		List<Product> sameProducts = repository.findByNameAndProductTypeAndPrice (newProduct.getName(),
			    newProduct.getProductType(),
			    newProduct.getPrice());
		if ( !sameProducts.isEmpty()) {
			EntityModel<Product> productModel = assembler.toModel(sameProducts.get(0));
			return ResponseEntity
					.created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
					.header("Products already contains such product", "Consider changing Name, Type or Price")
					.body(productModel);
		}
		
		//adding new entity
		EntityModel<Product> productModel = assembler.toModel(repository.saveAndFlush(newProduct));
		return ResponseEntity
				.created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(productModel);
	}
	
	@PutMapping ("/products/{id}")
	public ResponseEntity<?> editProduct(@RequestBody Product newProduct, @PathVariable Long id) {
		
		//Checking if such a product already exists (that's probably should be done by another function)
		List<Product> sameProducts = repository.findByNameAndProductTypeAndPrice (newProduct.getName(),
			    newProduct.getProductType(),
			    newProduct.getPrice());
		if ( !sameProducts.isEmpty()) {
			EntityModel<Product> productModel = assembler.toModel(sameProducts.get(0));
			return ResponseEntity
					.created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
					.header("Products already contains such product", "Consider changing Name, Type or Price")
					.body(productModel);
		}
		
		//editing existing entity
		Product editedProduct = repository.findById(id)
				.map (product -> {
					product.setName(newProduct.getName());
					product.setProductType(newProduct.getProductType());
					product.setPrice(newProduct.getPrice());
					return repository.saveAndFlush(product);
				})
				.orElseThrow ( () -> new ProductNotFoundException(id));
		
		EntityModel<Product> productModel = assembler.toModel(editedProduct);
		return ResponseEntity
				.created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(productModel);
	}

}

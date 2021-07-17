package com.product.controller;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.product.assembler.ProductModelAssembler;
import com.product.entity.Product;
import com.product.exception.ProductNotFoundException;
import com.product.repository.ProductRepository;
import com.product.service.ProductService;

/**
 * API end-point for product management.
 * @author Mokrushin Vladimir
 *
 */
@RestController
public class ProductController {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private ProductModelAssembler assembler;
	
	@Autowired
	private ProductService service;
	
	//--------GET operations
	
	@GetMapping ("/products")
	public CollectionModel<EntityModel<Product>> getAllProducts(
								@RequestParam(defaultValue = "0") Integer pageNo,
								@RequestParam(defaultValue = "7") Integer pageSize,
								@RequestParam(defaultValue = "id") String sortBy) {										
		List <EntityModel<Product>> products = service.getAllProducts(pageNo, pageSize, sortBy).stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(products,
				linkTo(methodOn(ProductController.class).getAllProducts(pageNo, pageSize, sortBy)).withSelfRel());
	}	
	
	@GetMapping ("/products/{id}")
	public EntityModel<Product> getProductById(@PathVariable Long id) {
		
		Product productToGet = repository.findById(id).orElseThrow( () -> new ProductNotFoundException(id));
		return assembler.toModel(productToGet);
	}
	
	@GetMapping ("/products/sku/{sku}")
	public EntityModel<Product> getProductBySku(@PathVariable String sku) {
		
		Product productToGet = repository.findBySku(sku).orElseThrow( () -> new ProductNotFoundException(sku));
		return assembler.toModel(productToGet);
	}
	
	//--------DELETE operations
	
	@GetMapping ("/products/delete/{id}") //works for web-app
	public ResponseEntity<?> deleteProductByIdWeb(@PathVariable Long id) {
		
		Product product = repository.findById(id).orElseThrow( () -> new ProductNotFoundException(id));
		Link mainMenuLink = assembler.toModel(product).getRequiredLink("Products");
		
		repository.deleteById(id);
		return ResponseEntity
				.created(mainMenuLink.toUri())
				.header("Product has been deleted", "OK")
				.body(mainMenuLink);
	}
	
	@DeleteMapping ("/products/{id}") //works for console
	public ResponseEntity<?> deleteProductByIdConsole(@PathVariable Long id) {
		
		Product product = repository.findById(id).orElseThrow( () -> new ProductNotFoundException(id));
		Link mainMenuLink = assembler.toModel(product).getRequiredLink("Products");
		
		repository.deleteById(id);
		return ResponseEntity
				.created(mainMenuLink.toUri())
				.header("Product has been deleted", "OK")
				.body(mainMenuLink);
	}
	
	@DeleteMapping ("/products/sku/{sku}")
	public ResponseEntity<?> deleteProductBySkuConsole(@PathVariable String sku) {
		
		Product product = repository.findBySku(sku).orElseThrow( () -> new ProductNotFoundException(sku));
		Link mainMenuLink = assembler.toModel(product).getRequiredLink("Products");
		
		repository.deleteById(product.getId());
		return ResponseEntity
				.created(mainMenuLink.toUri())
				.header("Product has been deleted", "OK")
				.body(mainMenuLink);
	}
	
	//--------POST operations
	
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
		Product.setSku(newProduct, newProduct.getName(), newProduct.getProductType(), newProduct.getPrice());//crutch
		EntityModel<Product> productModel = assembler.toModel(repository.saveAndFlush(newProduct));
		return ResponseEntity
				.created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(productModel);
	}
	
	//--------PUT operations
	
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
					Product.setSku(product, product.getName(), product.getProductType(), product.getPrice());//crutch
					return repository.saveAndFlush(product);
				})
				.orElseThrow ( () -> new ProductNotFoundException(id));
		
		EntityModel<Product> productModel = assembler.toModel(editedProduct);
		return ResponseEntity
				.created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(productModel);
	}
	
	@PutMapping ("/products/sku/{sku}")
	public ResponseEntity<?> editProductBySku(@RequestBody Product newProduct, @PathVariable String sku) {
		
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
		Product editedProduct = repository.findBySku(sku)
				.map (product -> {
					product.setName(newProduct.getName());
					product.setProductType(newProduct.getProductType());
					product.setPrice(newProduct.getPrice());
					Product.setSku(product, product.getName(), product.getProductType(), product.getPrice());//crutch
					return repository.saveAndFlush(product);
				})
				.orElseThrow ( () -> new ProductNotFoundException(sku));
		
		EntityModel<Product> productModel = assembler.toModel(editedProduct);
		return ResponseEntity
				.created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(productModel);
	}

}

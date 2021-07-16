package com.product.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.product.controller.ProductController;
import com.product.entity.Product;

/**
 * Modifies {@link Product}, creating DTO of a {@link Product}
 * to send to the client.
 * @author Mokrushin Vladimir
 *
 */
@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>>{

	@Override
	public EntityModel<Product> toModel(Product product) {
		
		return EntityModel.of(product,
				linkTo (methodOn (ProductController.class).getProductById(product.getId())).withSelfRel(),
				linkTo (methodOn (ProductController.class).getAllProducts()).withRel("Products"),
				linkTo (methodOn (ProductController.class).deleteProductById(product.getId())).withRel("Delete"));
	}

}

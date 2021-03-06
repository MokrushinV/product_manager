package com.product;

import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.product.entity.Product;
import com.product.entity.enums.ProductType;
import com.product.repository.ProductRepository;

@SpringBootApplication
public class ProductManagerApplication {
	
	private static final Logger log = 
			LoggerFactory.getLogger(ProductManagerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProductManagerApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner generalTest(ProductRepository repository) {
		return (args) -> {
			//loading sample data
			BigDecimal d1 = new BigDecimal(1000.00).setScale(2, RoundingMode.HALF_DOWN);
			BigDecimal d2 = new BigDecimal(1500.00).setScale(2, RoundingMode.HALF_DOWN);
			BigDecimal d3 = new BigDecimal(200.00).setScale(2, RoundingMode.HALF_DOWN);
			BigDecimal d4 = new BigDecimal(700.00).setScale(2, RoundingMode.HALF_DOWN);
			
			repository.save(new Product("Awesome Game", ProductType.GAME, d1));
			repository.save(new Product("Awesome Game 2", ProductType.GAME, d2));
			repository.save(new Product("Crystall", ProductType.GAME_CURRENCY, d3));
			repository.save(new Product("Awesome T-shirt", ProductType.SHIRT, d4));
			
			//get preloaded products
			log.info("Products found with findAll():");
			log.info("------------------------------");
			for (Product product : repository.findAll()) {
				log.info(product.toString());
			}
			log.info("------------------------------");
			
			//get an individual product by id
			Optional<Product> product = repository.findById(1L);
			log.info("Product found with findById(1L):");
			log.info("------------------------------");
			if (product.isPresent())
				log.info(product.toString());
			else log.info("No such product!");
			log.info("------------------------------");
			
			//get products by sku
			Optional<Product> productBySku = repository.findBySku("1AWEGAM2T1P1500");
			log.info("Product found with findBySku(\"1AWEGAM2T1P1500\"):");
			log.info("---------------------------------------");
			if (productBySku.isPresent())
				log.info(product.toString());
			else log.info("No such product!");
			log.info("---------------------------------------");
			
			// checking if product with the given name, productType and price already exists
			log.info("Product found with same parameters as the first product");
			log.info("---------------------------------------");
			repository.findByNameAndProductTypeAndPrice("Awesome Game",
														ProductType.GAME,
														d1).forEach(productExisted -> {
				log.info(productExisted.toString());
			});
			log.info("---------------------------------------");
		};
	}

}

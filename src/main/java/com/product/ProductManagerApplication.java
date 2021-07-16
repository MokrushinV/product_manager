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
			
			repository.save(new Product("g001", "Awesome Game", ProductType.GAME, d1));
			repository.save(new Product("g002", "Awesome Game 2", ProductType.GAME, d2));
			repository.save(new Product("gc001", "Crystall", ProductType.GAME_CURRENCY, d3));
			repository.save(new Product("S001", "Awesome T-shirt", ProductType.SHIRT, d4));
			
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
			log.info("Product found with findBySku(\"g002\"):");
			log.info("---------------------------------------");
			repository.findBySku("g002").forEach(product_g002 -> {
				log.info(product_g002.toString());
			});
			log.info("---------------------------------------");
		};
	}

}

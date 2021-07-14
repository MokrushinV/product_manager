package com.product;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.product.entities.Product;
import com.product.entities.enums.ProductType;
import com.product.repositories.ProductRepository;

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
			repository.save(new Product("g001", "Awesome Game", ProductType.GAME, 100000L));
			repository.save(new Product("g002", "Awesome Game 2", ProductType.GAME, 150000L));
			repository.save(new Product("gc001", "Crystall", ProductType.GAME_CURRENCY, 20000L));
			repository.save(new Product("S001", "Awesome T-shirt", ProductType.SHIRT, 70000L));
			
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

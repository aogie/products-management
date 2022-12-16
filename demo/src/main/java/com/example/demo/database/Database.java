package com.example.demo.database;

import com.example.demo.models.Product;
import com.example.demo.repositories.ProductRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    //Logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Product productA = new Product("Iphone", 2022, 1000.0, "");
//                Product productB = new Product("Ipad", 2022, 2000.0, "");
//                logger.info("Insert data: " + productRepository.save(productA));
//                logger.info("Insert data: " + productRepository.save(productB));
            }
        };
    }
}

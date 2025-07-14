package com.demo.ecommerce.products;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Tag("Integration")
@Transactional
@SpringBootTest
public class ProductServiceIntegrationTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @DisplayName("Create a product")
    @Test
    void create() {

    }

    @DisplayName("Find a product by id")
    @Test
    void findById() {

    }

    @DisplayName("Find a non-existent product by id returns empty")
    @Test
    void findByIdEmpty(){

    }

    @DisplayName("Update a product by id")
    @Test
    void updateById(){

    }

    @DisplayName("Update a non-existing product by id throws a ResourceNotFoundException")
    @Test
    void updateByIdNotFound(){

    }

    @DisplayName("Delete a product by id")
    @Test
    void deleteById(){

    }

    @DisplayName("Delete a non-existing(or already soft-deleted) product by id throws a ResourceNotFoundException")
    @Test
    void deleteByIdNotFound(){

    }

}

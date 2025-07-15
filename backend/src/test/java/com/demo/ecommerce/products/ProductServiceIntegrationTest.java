package com.demo.ecommerce.products;

import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.products.dto.ProductCreateRequest;
import com.demo.ecommerce.products.dto.ProductUpdateRequest;
import com.demo.ecommerce.users.Role;
import com.demo.ecommerce.users.User;
import com.demo.ecommerce.users.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Integration")
@SpringBootTest
public class ProductServiceIntegrationTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @DisplayName("Create a product")
    @Test
    void create() {
        assertFalse(productRepository.existsById(1L));
        String name = "Test Product 2";
        String description = "Test description";

        ProductCreateRequest product = ProductCreateRequest.builder().name(name).description(description).ownerId(1L).build();
        Product newProduct = productService.create(product);

        assertEquals(1L, newProduct.getId());
        assertEquals(product.getName(), newProduct.getName());
        assertEquals(product.getDescription(), newProduct.getDescription());
    }

    @DisplayName("Find a product by id")
    @Test
    void findById() {
        User user = userRepository.save(User.builder().name("Test User").email("test@outlook.com").role(Role.CUSTOMER).password("1234").build());
        Product product = productRepository.save(Product.builder().name("Test Product").description("This is a test product for testing purposes").user(user).build());

        assertTrue(productService.findById(product.getId()).isPresent());
        assertEquals(product, productService.findById(product.getId()).get());
    }

    @DisplayName("Find a non-existent product by id returns empty")
    @Test
    void findByIdEmpty(){
        assertFalse(productRepository.existsById(Long.MAX_VALUE));
        assertTrue(productService.findById(Long.MAX_VALUE).isEmpty());
    }

    @DisplayName("Find an inactive product returns empty")
    @Test
    void findByIdInactive(){
        User user = userRepository.save(User.builder().name("Test User").email("test@outlook.com").role(Role.CUSTOMER).password("1234").build());
        Product inactiveProduct = productRepository.save(Product.builder().name("Test Product").description("This is a test product for testing purposes").user(user).build());

        assertTrue(productRepository.existsById(inactiveProduct.getId()));
        assertTrue(productService.findById(inactiveProduct.getId(), EnumSet.of(ProductFilterFlag.SHOW_ACTIVE)).isEmpty());
        assertEquals(inactiveProduct, productService.findById(inactiveProduct.getId(), EnumSet.noneOf(ProductFilterFlag.class)).get());
    }

    @DisplayName("Update a product by id")
    @Test
    void updateById(){
        User ogUser = userRepository.save(User.builder().name("Test User").email("test@outlook.com").role(Role.CUSTOMER).password("1234").build());
        Product ogProduct = productRepository.save(Product.builder().name("Test Product").description("This is a test product for testing purposes").user(ogUser).build());

        User newOwner = userRepository.save(User.builder().name("Test Admin").email("admin@outlook.com").role(Role.ADMINISTRATOR).password("12345").build());
        ProductUpdateRequest productChanges = ProductUpdateRequest.builder().name("Changed Product").description("Changed description").ownerId(newOwner.getId()).active(false).build();

        assertEquals(newOwner, ogProduct.getUser());
        assertEquals(ogProduct.getDescription(), productChanges.getDescription());
        assertEquals(ogProduct.getName(), productChanges.getName());
        assertEquals(ogProduct.isActive(), productChanges.isActive());

        Product updatedProduct = productService.updateById(ogProduct.getId(), productChanges);

        assertEquals(newOwner, updatedProduct.getUser());
        assertEquals(updatedProduct.getName(), productChanges.getName());
        assertEquals(updatedProduct.getDescription(), productChanges.getDescription());
        assertEquals(updatedProduct.isActive(), productChanges.isActive());

    }

    @DisplayName("Update a non-existing product by id throws a ResourceNotFoundException")
    @Test
    void updateByIdNotFound(){
        final long ID = Long.MAX_VALUE;
        User user = userRepository.save(User.builder().name("Test User").email("test@outlook.com").role(Role.CUSTOMER).password("1234").build());
        ProductUpdateRequest productChanges = ProductUpdateRequest.builder().name("Changed Product").description("Changed description").ownerId(user.getId()).build();

        assertTrue(productRepository.existsById(ID));
        assertThrows(ResourceNotFoundException.class, () -> productService.updateById(ID, productChanges));

    }

    @DisplayName("Delete a product by id")
    @Test
    void deleteById(){
        User user = userRepository.save(User.builder().name("Test User").email("test@outlook.com").role(Role.CUSTOMER).password("1234").build());
        Product product = productRepository.save(Product.builder().name("Test Product").description("This is a test product for testing purposes").user(user).build());

        assertNull(productRepository.findById(product.getId()).get().getDeletedAt());
        productService.deleteById(product.getId());
        assertTrue(productRepository.existsById(product.getId()));
        assertNotNull(productRepository.findById(product.getId()).get().getDeletedAt());
    }

    @DisplayName("Delete a non-existing product by id throws a ResourceNotFoundException")
    @Test
    void deleteByIdNotFound(){
        final long ID = Long.MAX_VALUE;

        assertFalse(productRepository.existsById(ID));
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(ID));
    }

    @DisplayName("Delete an already deleted product by id throws a ResourceNotFoundException")
    @Test
    void deleteByIdAlreadyDeleted(){
        final long ID = Long.MAX_VALUE;
        User user = userRepository.save(User.builder().name("Test User").email("test@outlook.com").role(Role.CUSTOMER).password("1234").build());
        Product product = productRepository.save(Product.builder().name("Test Product").description("This is a test product for testing purposes").user(user).build());

        // Delete it once should be correct
        assertNull(productRepository.findById(product.getId()).get().getDeletedAt());
        productService.deleteById(product.getId());
        // Second run should return not found
        assertFalse(productRepository.existsById(ID));
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(ID));
    }
}

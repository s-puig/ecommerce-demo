package com.demo.ecommerce.products;

import com.demo.ecommerce.common.annotations.ImportTestContext;
import com.demo.ecommerce.common.exceptions.BadRequestException;
import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.products.dto.ProductCreateRequest;
import com.demo.ecommerce.products.dto.ProductUpdateRequest;
import com.demo.ecommerce.users.Role;
import com.demo.ecommerce.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@Tag("Unit")
@ImportTestContext(ProductMapperImpl.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductMapper productMapper;

    @MockitoBean
    ProductService productService;

    private User.UserBuilder testUser() {
        return User.builder().id(1L).name("Test User").password("Pass123").email("test@builder.com").role(Role.CUSTOMER);
    }
    private Product.ProductBuilder testProduct(User user) {
        return Product.builder().id(1L).user(user).name("Test Product").description("Test description").active(true);
    }

    @DisplayName("Get an non-existing product returns 404")
    @Test
    void getNonExistingProduct() throws Exception {
        long ID = Long.MAX_VALUE;
        when(productService.findById(ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get an existing product")
    @Test
    void getExistingProduct() throws Exception {
        User testUser = testUser().build();
        Product testProduct = testProduct(testUser).build();

        when(productService.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        mockMvc.perform(get("/api/products/{id}", testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productMapper.entityToResponse(testProduct))));
    }

    @DisplayName("Create a product")
    @Test
    @Disabled
    void createProduct() throws Exception {
        User testUser = testUser().build();

        ProductCreateRequest request = ProductCreateRequest.builder()
                .active(true)
                .description("Test description")
                .ownerId(testUser.getId())
                .name("Test product")
                .build();
        Product product = productMapper.createToEntity(request);

        when(productService.create(any(ProductCreateRequest.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(productMapper.entityToResponse(product))));
    }

    @DisplayName("Create a product with a bad user returns a 400 status")
    @Test
    void createProductBadUserThrowsBadRequestException() throws Exception {
        User testUser = testUser().build();
        Product testProduct = testProduct(testUser).build();

        ProductCreateRequest request = ProductCreateRequest.builder()
                .active(testProduct.isActive())
                .description(testProduct.getDescription())
                .ownerId(testUser.getId())
                .name(testProduct.getName())
                .build();

        when(productService.create(any(ProductCreateRequest.class))).thenThrow(BadRequestException.class);

        mockMvc.perform(post("/api/products")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Delete a product by id")
    @Test
    void deleteProduct() throws Exception {
        long ID = Long.MAX_VALUE;

        doNothing().when(productService).deleteById(eq(ID), any());

        mockMvc.perform(delete("/api/products/{id}", ID))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete a non-existing product by id returns a 404")
    @Test
    void deleteNonExistingProductThrowsResourceNotFoundException() throws Exception {
        long ID = Long.MAX_VALUE;

        doThrow(new ResourceNotFoundException()).when(productService).deleteById(eq(ID));

        mockMvc.perform(delete("/api/products/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Update a product")
    @Test
    @Disabled
    void updateProduct() throws Exception {
        User testUser = testUser().build();
        Product testProduct = testProduct(testUser).build();

        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .active(testProduct.isActive())
                .name(testProduct.getName())
                .description(testProduct.getDescription())
                .build();

        when(productService.updateById(eq(testProduct.getId()), any(ProductUpdateRequest.class))).thenReturn(testProduct);

        mockMvc.perform(put("/api/products/{id}", testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productMapper.entityToResponse(testProduct))));
    }

    @DisplayName("Update a non-existing product returns 404")
    @Test
    @Disabled
    void updateNonExistingProductThrowsResourceNotFoundException() throws Exception {
        long ID = Long.MAX_VALUE;

        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .active(true)
                .description(" Test ")
                .name("Test")
                .build();

        doThrow(ResourceNotFoundException.class).when(productService).updateById(eq(ID), any(ProductUpdateRequest.class));

        mockMvc.perform(put("/api/products/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}

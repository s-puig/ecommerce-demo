package com.demo.ecommerce.products;

import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.products.dto.ProductCreateRequest;
import com.demo.ecommerce.products.dto.ProductResponse;
import com.demo.ecommerce.products.dto.ProductUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "api/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ProductMapper productMapper;

    @Operation(summary = "Get a product by id")
    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable long id) {
        return ResponseEntity.ok(productMapper.entityToResponse(productService.findById(id).orElseThrow(ResourceNotFoundException::new)));
    }

    @Operation(summary = "Update a product by id")
    @PutMapping("{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable long id, @Valid ProductUpdateRequest request) {
        return ResponseEntity.ok().body(productMapper.entityToResponse(productService.updateById(id, request)));
    }

    @Operation(summary = "Create a product")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid ProductCreateRequest request) {
        Product product = productService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(product.getId());
        return ResponseEntity.created(location).body(productMapper.entityToResponse(product));
    }

    @Operation(summary = "Soft-delete a product by id")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //TODO: Patch ops
}

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

    /**
     * Retrieves a product by its ID.
     *
     * @param id the unique identifier of the product to be retrieved
     * @return a ResponseEntity containing the ProductResponse object with HTTP status code 200 (OK)
     * @throws ResourceNotFoundException if the product with the specified ID does not exist
     */
    @Operation(summary = "Get a product by id")
    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable long id) {
        return ResponseEntity.ok(productMapper.entityToResponse(productService.findById(id).orElseThrow(ResourceNotFoundException::new)));
    }

    /**
     * Updates a product by its ID.
     *
     * @param id      the ID of the product to update
     * @param request the ProductUpdateRequest containing the updated details of the product
     * @return a ResponseEntity containing the updated ProductResponse object
     * @throws ResourceNotFoundException if the product with the given ID is not found
     */
    @Operation(summary = "Update a product by id")
    @PutMapping("{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable long id, @Valid ProductUpdateRequest request) {
        return ResponseEntity.ok().body(productMapper.entityToResponse(productService.updateById(id, request)));
    }

    /**
     * Creates a new product.
     *
     * @param request the ProductCreateRequest containing details of the product to be created
     * @return ResponseEntity containing the created ProductResponse object and HTTP 201 Created status with Location header set to the newly created product's URI
     */
    @Operation(summary = "Create a product")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid ProductCreateRequest request) {
        Product product = productService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(product.getId());
        return ResponseEntity.created(location).body(productMapper.entityToResponse(product));
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @return a ResponseEntity with no content if successful
     */
    @Operation(summary = "Soft-delete a product by id")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //TODO: Patch ops
}

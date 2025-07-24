package com.demo.ecommerce.products;

import com.demo.ecommerce.common.exceptions.BadRequestException;
import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.products.dto.ProductCreateRequest;
import com.demo.ecommerce.products.dto.ProductUpdateRequest;
import com.demo.ecommerce.users.UserService;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.*;

class ProductSpecification {
    /**
     * Creates a specification to filter products based on the provided flags.
     *
     * @param flags an EnumSet of {@link ProductExcludeFlag} specifying the conditions for filtering products
     * @return a Specification that filters out products that do not match the conditions
     */
    public static Specification<Product> filter(EnumSet<ProductExcludeFlag> flags) {
        return (root, query, builder) -> {
            Predicate[] filterPredicates = flags.stream()
                    .map(flag -> flag.toPredicate(root, query, builder))
                    .toArray(Predicate[]::new);

            return builder.and(filterPredicates);
        };
    }

    /**
     * Creates a specification to filter products by their ID.
     *
     * @param id the product ID to match
     * @return a Specification that matches products with the specified ID
     */
    public static Specification<Product> hasId(long id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }
}

@Service
@Validated
public class ProductService {
    /**
     * Default filter flags used to exclude products in multiple ProductService methods.
     */
    public static EnumSet<ProductExcludeFlag> DEFAULT_FILTER_FLAGS = EnumSet.of(ProductExcludeFlag.INACTIVE, ProductExcludeFlag.DELETED);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserService userService;

    /**
     * Retrieves a product by its ID using default filter flags.
     *
     * @param id the unique identifier of the product to retrieve
     * @return an Optional containing the Product if found, or an empty Optional otherwise
     */
    @Transactional
    public Optional<Product> findById(@Min(1) long id) {
        return findById(id, DEFAULT_FILTER_FLAGS);
    }

    /**
     * Retrieves a product by its ID with optional filtering.
     *
     * @param id            The unique identifier of the product to retrieve
     * @param filterFlags   The flags used to filter the product during retrieval
     * @return An {@link Optional} containing the product if found, or an empty {@link Optional} otherwise
     */
    @Transactional
    public Optional<Product> findById(@Min(1) long id, EnumSet<ProductExcludeFlag> filterFlags) {
        return productRepository.findOne(ProductSpecification
                .hasId(id)
                .and(ProductSpecification.filter(filterFlags))
        );
    }

    /**
     * Updates a product by its ID with the provided update request.
     *
     * @param id            The unique identifier of the product to be updated
     * @param updateRequest The request object containing the updated data for the product
     * @return The updated {@link Product} entity
     * @throws ResourceNotFoundException if no product is found with the given ID or if any other resource required for update cannot be found
     */
    @NotNull
    @Transactional
    public Product updateById(@Min(1) long id, @NotNull @Valid ProductUpdateRequest updateRequest) {
        return updateById(id, updateRequest, DEFAULT_FILTER_FLAGS);
    }

    /**
     * Updates a product by its ID with the specified update request and filter flags.
     *
     * @param id            The unique identifier of the product to be updated
     * @param updateRequest The request object containing the new data for the product
     * @param filterFlags   An enum set of flags used to filter products during retrieval
     * @return The updated {@link Product} entity
     * @throws ResourceNotFoundException if no product is found with the given ID
     */
    @NotNull
    @Transactional
    public Product updateById(@Min(1) long id, @NotNull @Valid ProductUpdateRequest updateRequest, EnumSet<ProductExcludeFlag> filterFlags) {
        Product product = findById(id, filterFlags).orElseThrow(ResourceNotFoundException::new);
        productMapper.updateProduct(updateRequest, product);
        return save(product);
    }

    /**
     * Creates a new product based on the provided request.
     *
     * @param request the request object containing product creation data
     * @return the created {@link Product} entity
     * @throws BadRequestException if the owner specified in the request cannot be found
     */
    @NotNull
    @Transactional
    public Product create(@NotNull @Valid ProductCreateRequest request) {
        Product product = productMapper.createToEntity(request);
        product.setUser(userService.findById(request.getOwnerId()).orElseThrow(() -> new BadRequestException("Owner cannot be found")));

        return save(product);
    }

    /**
     * Soft-deletes a product by its ID with default filter flags.
     *
     * @param id The ID of the product to be deleted
     * @throws ResourceNotFoundException if no product is found with the given ID and default filters.
     */
    @Transactional
    public void deleteById(@Min(1) long id) {
        deleteById(id, EnumSet.of(ProductExcludeFlag.DELETED));
    }

    /**
     * Soft-deletes a product by its ID using specified filter flags.
     *
     * @param id          the unique identifier of the product to delete
     * @param filterFlags the set of filter flags to apply when finding the product
     */
    @Transactional
    public void deleteById(@Min(1) long id, EnumSet<ProductExcludeFlag> filterFlags) {
        Product product = productRepository.findOne(ProductSpecification.filter(filterFlags)
                .and(ProductSpecification.hasId(id))
        ).orElseThrow(ResourceNotFoundException::new);
        product.setDeletedAt(Instant.now());
    }

    /**
     * Saves a product entity to the database.
     *
     * @param product the {@link Product} object containing details of the product to be saved
     * @return the saved {@link Product} object with its ID assigned by the repository
     */
    @NotNull
    @Transactional
    public Product save(@NotNull @Valid Product product) {
        return productRepository.save(product);
    }
}

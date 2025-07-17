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
    public static Specification<Product> filter(EnumSet<ProductExcludeFlag> flags) {
        return (root, query, builder) -> {
            Predicate[] filterPredicates = flags.stream()
                    .map(flag -> flag.toPredicate(root, query, builder))
                    .toArray(Predicate[]::new);

            return builder.and(filterPredicates);
        };
    }

    public static Specification<Product> hasId(long id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }
}

@Service
@Validated
public class ProductService {
    public static EnumSet<ProductExcludeFlag> DEFAULT_FILTER_FLAGS = EnumSet.of(ProductExcludeFlag.INACTIVE, ProductExcludeFlag.DELETED);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserService userService;

    @Transactional
    public Optional<Product> findById(@Min(1) long id) {
        return findById(id, DEFAULT_FILTER_FLAGS);
    }

    @Transactional
    public Optional<Product> findById(@Min(1) long id, EnumSet<ProductExcludeFlag> filterFlags) {
        return productRepository.findOne(ProductSpecification
                .hasId(id)
                .and(ProductSpecification.filter(filterFlags))
        );
    }

    @NotNull
    @Transactional
    public Product updateById(@Min(1) long id, @NotNull @Valid ProductUpdateRequest updateRequest) {
        return updateById(id, updateRequest, DEFAULT_FILTER_FLAGS);
    }

    @NotNull
    @Transactional
    public Product updateById(@Min(1) long id, @NotNull @Valid ProductUpdateRequest updateRequest, EnumSet<ProductExcludeFlag> filterFlags) {
        Product product = findById(id, filterFlags).orElseThrow(ResourceNotFoundException::new);
        productMapper.updateProduct(updateRequest, product);
        return save(product);
    }

    @NotNull
    @Transactional
    public Product create(@NotNull @Valid ProductCreateRequest request) {
        Product product = productMapper.createToEntity(request);
        product.setUser(userService.findById(request.getOwnerId()).orElseThrow(() -> new BadRequestException("Owner cannot be found")));

        return save(product);
    }

    @Transactional
    public void deleteById(@Min(1) long id) {
        deleteById(id, EnumSet.of(ProductExcludeFlag.DELETED));
    }

    @Transactional
    public void deleteById(@Min(1) long id, EnumSet<ProductExcludeFlag> filterFlags) {
        Product product = productRepository.findOne(ProductSpecification.filter(filterFlags)
                .and(ProductSpecification.hasId(id))
        ).orElseThrow(ResourceNotFoundException::new);
        product.setDeletedAt(Instant.now());
    }

    @NotNull
    @Transactional
    public Product save(@NotNull @Valid Product product) {
        return productRepository.save(product);
    }
}

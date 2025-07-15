package com.demo.ecommerce.products;

import com.demo.ecommerce.products.dto.ProductCreateRequest;
import com.demo.ecommerce.products.dto.ProductUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.EnumSet;
import java.util.Optional;

@Service
@Validated
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Optional<Product> findById(@Min(1) long id) {
        return findById(id, EnumSet.of(ProductFilterFlag.SHOW_ACTIVE));
    }

    public Optional<Product> findById(@Min(1) long id, EnumSet<ProductFilterFlag> filterFlags) {
        throw new UnsupportedOperationException("Function not implemented");
    }

    @NotNull
    @Transactional
    public Product updateById(@Min(1) long id, @NotNull @Valid ProductUpdateRequest product) {
        throw new UnsupportedOperationException("Function not implemented");
    }

    @NotNull
    @Transactional
    public Product create(@NotNull @Valid ProductCreateRequest product) {
        throw new UnsupportedOperationException("Function not implemented");
    }

    @Transactional
    public void deleteById(@Min(1) long id) {
        throw new UnsupportedOperationException("Function not implemented");
    }

    @NotNull
    @Transactional
    public Product save(@NotNull @Valid Product product) {
        throw new UnsupportedOperationException("Function not implemented");
    }
}

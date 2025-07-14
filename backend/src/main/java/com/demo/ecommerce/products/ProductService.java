package com.demo.ecommerce.products;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Optional<Product> findById(@Min(1) long id) {
        return findById(id, true);
    }

    public Optional<Product> findById(@Min(1) long id, boolean showOnlyActive) {
        throw new UnsupportedOperationException("Function not implemented");
    }

    @NotNull
    @Transactional
    public Product updateById(@Min(1) long id, @NotNull @Valid Product product) {
        throw new UnsupportedOperationException("Function not implemented");
    }

    @NotNull
    @Transactional
    public Product create(@NotNull @Valid Product product) {
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

package com.demo.ecommerce.products;

import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Optional<Product> findById(long id) {
        return findById(id, true);
    }

    public Optional<Product> findById(long id, boolean showOnlyActive) {
        throw new UnsupportedOperationException("Function not implemented");
    }

    public Product updateById(long id, Product product) {
        throw new UnsupportedOperationException("Function not implemented");
    }

    public Product create(Product product) {
        throw new UnsupportedOperationException("Function not implemented");
    }

    public void deleteById(long id) {
        throw new UnsupportedOperationException("Function not implemented");
    }

    public Product save(Product product) {
        throw new UnsupportedOperationException("Function not implemented");
    }
}

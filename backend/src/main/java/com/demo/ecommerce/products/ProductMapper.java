package com.demo.ecommerce.products;

import com.demo.ecommerce.products.dto.ProductCreateRequest;
import com.demo.ecommerce.products.dto.ProductResponse;
import com.demo.ecommerce.products.dto.ProductUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target="user", ignore = true)
    Product createToEntity(ProductCreateRequest request);
    ProductResponse entityToResponse(Product product);
    void updateProduct(ProductUpdateRequest request, @MappingTarget Product product);
}

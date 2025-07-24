package com.demo.ecommerce.products;

import com.demo.ecommerce.products.dto.ProductCreateRequest;
import com.demo.ecommerce.products.dto.ProductResponse;
import com.demo.ecommerce.products.dto.ProductUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    /**
     * Maps a {@link ProductCreateRequest} to a {@link Product} entity.
     * <p>
     * The mapping ignores the 'user' field of the target entity.
     *
     * @param request the request object containing product creation data
     * @return a new {@link Product} entity populated with data from the request
     */
    @Mapping(target="user", ignore = true)
    Product createToEntity(ProductCreateRequest request);
    /**
     * Converts a {@link Product} entity to a {@link ProductResponse}.
     *
     * @param product the {@link Product} entity to be converted.
     * @return a {@link ProductResponse} containing details of the product.
     */
    ProductResponse entityToResponse(Product product);
    /**
     * Updates the fields of an existing {@link Product} entity based on the information provided in a
     * {@link ProductUpdateRequest}.
     *
     * @param request The {@link ProductUpdateRequest} containing the updated data for the product.
     * @param product The target {@link Product} entity to be updated.
     */
    void updateProduct(ProductUpdateRequest request, @MappingTarget Product product);
}

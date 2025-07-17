package com.demo.ecommerce.products.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductUpdateRequest {
    @NotBlank(message = "Product name cannot be empty")
    String name;
    String description;
    @Builder.Default
    boolean active = true;
}
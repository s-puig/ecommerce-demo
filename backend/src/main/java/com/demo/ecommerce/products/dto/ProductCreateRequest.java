package com.demo.ecommerce.products.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductCreateRequest {
    @NotBlank(message = "Product name cannot be empty")
    String name;
    String description;
    @Builder.Default
    @Min(value = 1, message = "User id has to be a positive integer")
    long ownerId = 0;
    @Builder.Default
    boolean active = true;
}

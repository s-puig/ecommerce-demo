package com.demo.ecommerce.products.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder

public class ProductResponse {
    long id;
    long owner_id;
    String name;
    String description;
    boolean active;
    Instant createdAt;
    Instant updatedAt;
    int version;
}

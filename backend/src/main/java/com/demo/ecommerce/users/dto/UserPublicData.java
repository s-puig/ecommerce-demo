package com.demo.ecommerce.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder

public class UserPublicData {
    @Min(1)
    private Long id;
    @NotBlank
    private String name;
    @Email
    private String email;
}

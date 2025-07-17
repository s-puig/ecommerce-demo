package com.demo.ecommerce.users.dto;

import com.demo.ecommerce.users.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserCreate {
    @NotBlank
    String name;
    @Email
    String email;
    @NotBlank
    String password;
    @NotNull
    Role role;
}

package com.demo.ecommerce.users.dto;

import com.demo.ecommerce.users.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserCreate {
    String name;
    String email;
    String password;
    Role role;
}

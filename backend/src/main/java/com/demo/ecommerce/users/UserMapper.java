package com.demo.ecommerce.users;

import com.demo.ecommerce.users.dto.UserCreate;
import com.demo.ecommerce.users.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse entityToResponse(User user);
    User createToEntity(UserCreate user);
}

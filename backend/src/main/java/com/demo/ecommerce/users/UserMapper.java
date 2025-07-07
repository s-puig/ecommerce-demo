package com.demo.ecommerce.users;

import com.demo.ecommerce.users.dto.UserCreate;
import com.demo.ecommerce.users.dto.UserPublicData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserPublicData entityToResponse(User user);
    User createToEntity(UserCreate user);
}

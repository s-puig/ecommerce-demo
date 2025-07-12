package com.demo.ecommerce.users;

import com.demo.ecommerce.users.dto.UserCreate;
import com.demo.ecommerce.users.dto.UserPublicData;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserPublicData entityToResponse(User user);
    User createToEntity(UserCreate user);
    void updateUser(UserCreate userCreate, @MappingTarget User user);
}

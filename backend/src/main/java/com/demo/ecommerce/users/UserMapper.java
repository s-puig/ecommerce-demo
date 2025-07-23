package com.demo.ecommerce.users;

import com.demo.ecommerce.users.dto.UserCreate;
import com.demo.ecommerce.users.dto.UserPublicData;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Maps a {@link User} entity to a {@link UserPublicData} DTO.
     *
     * @param user the {@link User} object containing user details to be mapped
     * @return a new {@link UserPublicData} object initialized with data from the {@link User} entity
     */
    UserPublicData entityToResponse(User user);
    /**
     * Maps a {@link UserCreate} DTO to a {@link User} entity.
     *
     * @param user the {@link UserCreate} object containing user details to be mapped
     * @return a new {@link User} entity initialized with data from the {@link UserCreate} DTO
     */
    User createToEntity(UserCreate user);
    /**
     * Updates a {@link User} entity with data from a {@link UserCreate} DTO.
     * This method maps the fields from the provided {@link UserCreate} object to the target {@link User}
     * entity.
     *
     * @param userCreate The {@link UserCreate} object containing the new data for the user.
     * @param user       The target {@link User} entity to be updated with new values from the DTO.
     */
    void updateUser(UserCreate userCreate, @MappingTarget User user);
}

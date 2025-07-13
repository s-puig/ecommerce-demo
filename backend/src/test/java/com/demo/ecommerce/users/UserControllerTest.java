package com.demo.ecommerce.users;

import com.demo.ecommerce.common.annotations.ImportTestContext;
import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.users.dto.UserCreate;
import com.demo.ecommerce.users.dto.UserPublicData;
import com.fasterxml.jackson.databind.ObjectMapper;
/*import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;*/
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import java.util.EnumSet;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("Unit")
@ImportTestContext(UserMapperImpl.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void testController() {
        assert(true);
    }

    @Test
    void get_existingUser() throws Exception {
        Long userId = 10L;
        String name = "Test";
        String email = "test@outlook.com";

        User user = User.builder()
                .id(userId)
                .name(name)
                .email(email)
                .build();

        UserPublicData userDto = new UserPublicData(userId, name, email);

        when(userService.find(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    void get_userNotFound() throws Exception {
        Long userId = 10L;
        when(userService.find(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_user() throws Exception {
        String name = "Test";
        String email = "test@outlook.com";
        String password = "TestPassword";
        EnumSet<Role> roles = EnumSet.of(Role.CUSTOMER);
        UserCreate newUser = new UserCreate(name, email, password, roles);
        User user = User.builder()
                .id(1L)
                .name(name)
                .role(roles)
                .email(email)
                .password(password)
                .build();

        when(userService.createUser(any(UserCreate.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated());
    }

    @Test
    void update_user() throws Exception {
        Long userId = 10L;
        String name = "Test";
        String email = "test@outlook.com";
        String password = "TestPassword";
        EnumSet<Role> roles = EnumSet.of(Role.CUSTOMER);
        UserCreate newUser = new UserCreate(name, email, password, roles);
        User user = User.builder()
                .id(userId)
                .name(name)
                .email(email)
                .role(roles)
                .build();

        when(userService.updateUser(eq(userId), any(UserCreate.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk());
    }

    @Test
    void update_user_not_found() throws Exception {
        Long userId = 10L;
        String name = "Test";
        String email = "test@outlook.com";
        String password = "TestPassword";
        EnumSet<Role> roles = EnumSet.of(Role.CUSTOMER);
        UserCreate newUser = new UserCreate(name, email, password, roles);

        when(userService.updateUser(eq(userId), any(UserCreate.class))).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isNotFound());
    }
}

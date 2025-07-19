package com.demo.ecommerce.users;

import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.users.dto.UserCreate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Integration")
@Transactional
@SpringBootTest
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public static User.UserBuilder testUser(){
        return User.builder().name("Test").email("test@outlook.com").role(Role.CUSTOMER).password("Pass123");
    }

    @DisplayName("Find an existing user by id")
    @Test
    public void findByIdExists() {
        User user = userRepository.save(testUser().build());
        assertTrue(userService.findById(user.getId()).isPresent());
    }

    @DisplayName("Find a non-existing user is empty")
    @Test
    public void findByIdNotFound() {
        final long ID = Long.MAX_VALUE;
        assertFalse(userRepository.existsById(ID));
        assertTrue(userService.findById(ID).isEmpty());
    }

    @DisplayName("Creates an user")
    @Test
    public void create() {
        String name = "Test1";
        String email = "test1@outlook.com";
        String password = "TestPass123";
        Role role = Role.CUSTOMER;

        User user = userService.createUser(new UserCreate(name, email, password, role));

        assertTrue(userRepository.existsById(user.getId()));
    }

    @DisplayName("Update an existing user by id")
    @Test
    public void updateByIdExists(){
        User ogUser = userRepository.save(testUser().build());

        String name = "NewTest";
        String email = "new@mail.com";
        Role role = Role.ADMINISTRATOR;
        UserCreate newUserCommand = new UserCreate(name, email, "TestPass123", role);

        assertNotEquals(newUserCommand.getName(), ogUser.getName());
        assertNotEquals(newUserCommand.getEmail(), ogUser.getEmail());
        assertNotEquals(newUserCommand.getRole(), ogUser.getRole());

        User newUser = userService.updateUser(ogUser.getId(), newUserCommand);

        assertEquals(newUserCommand.getName(), newUser.getName());
        assertEquals(newUserCommand.getEmail(), newUser.getEmail());
        assertEquals(newUserCommand.getRole(), newUser.getRole());
    }

    @DisplayName("Update an non-existing user by id throws a ResourceNotFoundException")
    @Test
    public void updateByIdNotFound(){
        final long ID = Long.MAX_VALUE;
        assertFalse(userRepository.existsById(ID));
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(ID, UserCreate.builder()
                .name("NewTest")
                .email("new@mail.com")
                .password("Pass123")
                .role(Role.ADMINISTRATOR)
                .build()));
    }

    @DisplayName("Delete an existing user by id")
    @Test
    public void deleteByIdExists() {
        User user = userRepository.save(testUser().build());
        assertTrue(userRepository.existsById(user.getId()));

        userService.deleteById(user.getId());

        assertFalse(userRepository.existsById(user.getId()));
    }

    @DisplayName("Delete a non-existing user by id throws a ResourceNotFoundException")
    @Test
    public void deleteByIdNotFound(){
        final long ID = Long.MAX_VALUE;
        assertFalse(userRepository.existsById(ID));

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(ID));
    }
}

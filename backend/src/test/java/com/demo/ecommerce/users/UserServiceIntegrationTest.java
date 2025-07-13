package com.demo.ecommerce.users;

import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.users.dto.UserCreate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void beforeEach() {
        userRepository.save(User.builder().name("Test").email("test@outlook.com").role(Role.CUSTOMER).password("Pass123").build());
    }

    @DisplayName("Find an existing user by id")
    @Test
    public void findByIdExists() {
        assertTrue(userService.findById(1).isPresent());
    }

    @DisplayName("Find a non-existing user is empty")
    @Test
    public void findByIdNotFound() {
        assertTrue(userService.findById(2).isEmpty());
    }

    @DisplayName("Creates an user")
    @Test
    public void create() {
        assertFalse(userRepository.existsById(2L));
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
        assertTrue(userRepository.existsById(1L));
        //noinspection OptionalGetWithoutIsPresent
        User ogUser = userRepository.findById(1L).get();

        String name = "NewTest";
        String email = "new@mail.com";
        Role role = Role.ADMINISTRATOR;
        UserCreate newUserCommand = new UserCreate(name, email, "Pass123", role);

        assertNotEquals(newUserCommand.getName(), ogUser.getName());
        assertNotEquals(newUserCommand.getEmail(), ogUser.getEmail());
        assertNotEquals(newUserCommand.getRole(), ogUser.getRole());

        User newUser = userService.updateUser(1L, newUserCommand);

        assertEquals(newUserCommand.getName(), newUser.getName());
        assertEquals(newUserCommand.getEmail(), newUser.getEmail());
        assertEquals(newUserCommand.getRole(), newUser.getRole());
    }

    @DisplayName("Update an non-existing user by id throws a ResourceNotFoundException")
    @Test
    public void updateByIdNotFound(){
        assertFalse(userRepository.existsById(Long.MAX_VALUE));

        String name = "NewTest";
        String email = "new@mail.com";
        Role role = Role.ADMINISTRATOR;

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(Long.MAX_VALUE, new UserCreate(name, email, "Pass123", role)));


    }

    @DisplayName("Delete an existing user by id")
    @Test
    public void deleteByIdExists() {
        assertTrue(userRepository.existsById(1L));

        userService.deleteById(1);

        assertFalse(userRepository.existsById(1L));
    }

    @DisplayName("Delete a non-existing user by id throws a ResourceNotFoundException")
    @Test
    public void deleteByIdNotFound(){
        assertFalse(userRepository.existsById(Long.MAX_VALUE));

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(Long.MAX_VALUE));
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE users ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }
}

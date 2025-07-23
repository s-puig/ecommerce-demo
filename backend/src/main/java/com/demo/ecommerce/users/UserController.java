package com.demo.ecommerce.users;

import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.users.dto.UserCreate;
import com.demo.ecommerce.users.dto.UserPublicData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    /**
     * Retrieves a user by their ID and returns the user's public data.
     *
     * @param id the ID of the user to retrieve
     * @return a ResponseEntity containing the UserPublicData if found, or a NOT_FOUND status if not found
     * @throws ResourceNotFoundException if no user with the given ID is found
     */
    @Operation(summary = "Get user by id")
    @GetMapping("{id}")
    public ResponseEntity<UserPublicData> getUser(@PathVariable long id){
        Optional<User> user = userService.findById(id);
        if (user.isEmpty()) throw new ResourceNotFoundException("User with id %s does not exist".formatted(id));
        return ResponseEntity.ok().body(userMapper.entityToResponse(user.get()));
    }

    /**
     * Updates a user by their ID.
     *
     * @param id   the ID of the user to update
     * @param user the {@link UserCreate} object containing the updated user data
     * @return a ResponseEntity containing the updated {@link UserPublicData}
     * @throws ResourceNotFoundException if no user is found with the specified ID
     */
    @Operation(summary = "Update user by id")
    @PutMapping("{id}")
    public ResponseEntity<UserPublicData> updateUser(@PathVariable long id, @Valid @RequestBody UserCreate user) {
        return ResponseEntity.ok().body(userMapper.entityToResponse(userService.updateUser(id, user)));
    }

    /**
     * Creates a new user in the system.
     *
     * @param createUser The UserCreate object containing the details of the user to be created.
     * @return A ResponseEntity containing the newly created UserPublicData with HTTP status code 201 (Created).
     */
    @Operation(summary = "Create new user")
    @PostMapping
    public ResponseEntity<UserPublicData> addUser(@Valid @RequestBody UserCreate createUser) {
        User user = userService.createUser(createUser);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").build(user.getId());
        return ResponseEntity.created(location).body(userMapper.entityToResponse(user));
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     * @throws ResourceNotFoundException if no user is found with the specified ID
     */
    @Operation(summary = "Delete user by id")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) {
        userService.deleteById(id);
    }
}

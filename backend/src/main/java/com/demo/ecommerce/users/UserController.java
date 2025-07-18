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

    @Operation(summary = "Get user by id")
    @GetMapping("{id}")
    public ResponseEntity<UserPublicData> getUser(@PathVariable long id){
        Optional<User> user = userService.findById(id);
        if (user.isEmpty()) throw new ResourceNotFoundException("User with id %s does not exist".formatted(id));
        return ResponseEntity.ok().body(userMapper.entityToResponse(user.get()));
    }

    @Operation(summary = "Update user by id")
    @PutMapping("{id}")
    public ResponseEntity<UserPublicData> updateUser(@PathVariable long id, @Valid @RequestBody UserCreate user) {
        return ResponseEntity.ok().body(userMapper.entityToResponse(userService.updateUser(id, user)));
    }

    @Operation(summary = "Create new user")
    @PostMapping
    public ResponseEntity<UserPublicData> addUser(@Valid @RequestBody UserCreate createUser) {
        User user = userService.createUser(createUser);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").build(user.getId());
        return ResponseEntity.created(location).body(userMapper.entityToResponse(user));
    }

    @Operation(summary = "Delete user by id")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) {
        userService.deleteById(id);
    }

    /*  Patches a User resource by using JSON Merge (see RFC 7000).
        <p>This method accepts a JSON payload with one or more fields to update.
        Fields not provided in the request will remain unchanged.</p>
        <p>Example request body:</p>
        <pre>
        {
            "email": "mail@no-reply.com",
            "phone": "+34 123-456-789"
        }
        </pre>
        @param id Id of the user
        @param user User with fields to be updated
        @return Response containing the patched UserDTO
        @throws ResourceNotFoundException if no user with the given ID exists
    */
    /*
    TODO: Finish merge patch
    @Operation(summary = "Patch a user")
    @PatchMapping(path = "{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserPublicData> patchMergeUser(@PathVariable long id, @RequestBody UserPublicData user) {
        return ResponseEntity.badRequest().body(user);
    }
    */


    /*
     * Applies a JSON Patch (RFC 6902) to a User resource.
     *
     * <p>The request body must be a JSON Patch document describing the operations
     * to apply (add, remove, replace, etc.).</p>
     *
     * <p>Example request body:</p>
     * <pre>
     * [
     *   { "op": "replace", "path": "/email", "value": "new.email@example.com" },
     *   { "op": "add", "path": "/phone", "value": "123-456-7890" }
     * ]
     * </pre>
     *
     * @param id the ID of the user to patch
     * @param patch the JSON Patch document
     * @return ResponseEntity containing the updated UserDTO
     * @throws ResourceNotFoundException if no user with the given ID exists
     * @throws PatchFailedException if the patch cannot be applied
     */
    /*
    TODO: Finish patch
    @PatchMapping(path = "{id}", consumes = "application/json-patch+json")
    public ResponseEntity<UserResponse> patchUser(@PathVariable long id, @RequestBody String user) {
        return ResponseEntity.ok().body(userMapper.entityToResponse(userService.find(id).get()));
    }
    */
}

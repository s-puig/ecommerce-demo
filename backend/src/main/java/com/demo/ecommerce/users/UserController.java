package com.demo.ecommerce.users;

import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.users.dto.UserCreate;
import com.demo.ecommerce.users.dto.UserPublicData;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Optional<User> user = userService.find(id);
        if (user.isEmpty()) throw new ResourceNotFoundException("User with id %s does not exist".formatted(id));
        return ResponseEntity.ok().body(userMapper.entityToResponse(user.get()));
    }

    @Operation(summary = "Replace user by id")
    @PutMapping("{id}")
    public ResponseEntity<UserPublicData> replaceUser(@PathVariable long id, @RequestBody UserCreate user) {
        throw new UnsupportedOperationException();
    }

    @Operation(summary = "Create new user")
    @PostMapping
    public ResponseEntity<UserPublicData> addUser(@RequestBody UserCreate createUser) {
        return ResponseEntity.ok().body(userMapper.entityToResponse(userService.createUser(createUser)));
    }


    /**  Patches a User resource by using JSON Merge (see RFC 7000).
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
    **/
    @Operation(summary = "Patch a user")
    @PatchMapping(path = "{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserPublicData> patchMergeUser(@PathVariable long id, @RequestBody UserPublicData user) {
        return ResponseEntity.badRequest().body(user);
    }



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
    /*@PatchMapping(path = "{id}", consumes = "application/json-patch+json")
    public ResponseEntity<UserResponse> patchUser(@PathVariable long id, @RequestBody String user) {
        return ResponseEntity.ok().body(userMapper.entityToResponse(userService.find(id).get()));
    }*/
}

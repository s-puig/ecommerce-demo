package com.demo.ecommerce.users;

import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.users.dto.UserCreate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve
     * @return an Optional containing the {@link User} entity if found, or an empty Optional otherwise
     */
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    /**
     * Creates a new user by encoding their password and saving it to the database.
     *
     * @param user The {@link User} object containing the details of the new user.
     * @return The saved {@link User}.
     */
    private User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return save(user);
    }

    /**
     * Creates a new user based on the provided user creation details.
     *
     * @param userCreate The {@link UserCreate} object containing the details of the new user.
     * @return The created {@link User} entity.
     */
    // TODO: We probably shouldn't use a controller DTO for domain mapping, instead should be some kind of command
    //  but it should be fine since it is a small app
    @Transactional
    public User createUser(@Valid UserCreate userCreate) {
        User user = userMapper.createToEntity(userCreate);

        return create(user);
    }

    /**
     * Updates an existing user by their unique identifier.
     * This method retrieves a user by the specified ID and updates it with the details provided in the {@link UserCreate} DTO.
     *
     * @param id         the unique identifier of the user to update
     * @param userUpdate the {@link UserCreate} object containing the new data for the user
     * @return the updated {@link User} entity
     * @throws ResourceNotFoundException if no user is found with the specified ID
     */
    @Transactional
    public User updateUser(long id, @Valid UserCreate userUpdate) {
        Optional<User> maybeUser = findById(id);

        if (maybeUser.isEmpty()) throw new ResourceNotFoundException();

        User user = maybeUser.get();
        userMapper.updateUser(userUpdate, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return user;
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     * @throws ResourceNotFoundException if no user is found with the specified ID
     */
    @Transactional
    public void deleteById(long id) {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException();
        userRepository.deleteById(id);
    }

    /**
     * Saves a given user entity.
     *
     * @param user The {@link User} object to be saved.
     * @return The saved {@link User} entity.
     */
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
}

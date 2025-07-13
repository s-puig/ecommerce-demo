package com.demo.ecommerce.users;

import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import com.demo.ecommerce.users.dto.UserCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<User> find(long id) {
        return userRepository.findById(id);
    }

    private User create(User user) throws RuntimeException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return save(user);
    }

    // TODO: We probably shouldn't use a controller DTO for domain mapping, instead should be some kind of command
    //  but it should be fine since it is a small app
    @Transactional
    public User createUser(UserCreate userCreate) throws RuntimeException {
        User user = userMapper.createToEntity(userCreate);

        return create(user);
    }

    @Transactional
    public User updateUser(long id, UserCreate userUpdate) throws RuntimeException {
        Optional<User> maybeUser = find(id);

        if (maybeUser.isEmpty()) throw new ResourceNotFoundException();

        User user = maybeUser.get();
        userMapper.updateUser(userUpdate, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return user;
    }

    @Transactional
    public void deleteById(long id) throws ResourceNotFoundException {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException();
        userRepository.deleteById(id);
    }

    @Transactional
    // TODO: Throw errors on validation error.
    public User save(User user) {
        return userRepository.save(user);
    }
}

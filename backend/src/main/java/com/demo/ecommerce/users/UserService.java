package com.demo.ecommerce.users;

import com.demo.ecommerce.users.dto.UserCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    // TODO: We probably shouldn't use a controller DTO for domain mapping, instead should be some kind of command
    //  but it should be fine since it is a small app
    public User createUser(UserCreate userCreate) {
        User user = userMapper.createToEntity(userCreate);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return save(user);
    }

    // TODO: Throw errors on validation error.
    public User save(User user) {
        return userRepository.save(user);
    }
}

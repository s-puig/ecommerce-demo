package com.demo.ecommerce.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Optional<User> find(long id) {
        return userRepository.findById(id);
    }

    // TODO: Throw errors on validation error.
    public User save(User user) {
        return userRepository.save(user);
    }
}

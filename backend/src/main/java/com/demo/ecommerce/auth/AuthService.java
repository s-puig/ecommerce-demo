package com.demo.ecommerce.auth;

import com.demo.ecommerce.auth.dto.AuthTokens;
import com.demo.ecommerce.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    UserService userService;

    public AuthTokens login(long id, String password) {
        throw new UnsupportedOperationException();
    }

    public AuthTokens refresh(String refreshToken) {
        throw new UnsupportedOperationException();
    }
}

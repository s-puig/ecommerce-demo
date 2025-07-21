package com.demo.ecommerce.auth;

import com.demo.ecommerce.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @PostMapping("login")
    public ResponseEntity<?> login(){
        throw new UnsupportedOperationException();
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refresh(){
        throw new UnsupportedOperationException();
    }
}

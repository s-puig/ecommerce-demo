package com.demo.ecommerce.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class AuthConfig {
    @Value("${auth.secret}")
    private String secret;
    // Default value of 5 minutes
    @Value("${auth.jwt.expirationMs:300000}")
    private long expirationJwtMs;
    // Default value of 2 days
    @Value("${auth.refresh.expirationMs:172800000}")
    private long expirationRefreshMs;

    public SecurityFilterChain authentificationFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.addFilterBefore(new AuthJwtFilter(), UsernamePasswordAuthenticationFilter.class).build();
    }
}

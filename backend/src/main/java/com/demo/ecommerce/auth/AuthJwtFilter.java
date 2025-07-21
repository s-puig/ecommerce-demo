package com.demo.ecommerce.auth;

import com.demo.ecommerce.auth.dto.AuthDetails;
import com.demo.ecommerce.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthJwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;

        String jwtToken = authHeader.substring(7);
        Claims claims = Jwts.parser().decryptWith(Keys.hmacShaKeyFor("testSecret".getBytes())).build().parseSignedClaims(jwtToken).getPayload();
        final long userId = (long) claims.get("userId");
        final Role role = (Role) claims.get("role");

        AuthDetails authDetails = AuthDetails.builder().userId(userId).role(role).build();
        SecurityContextHolder.getContext().setAuthentication(authDetails);
    }
}


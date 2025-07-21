package com.demo.ecommerce.auth.dto;

public record AuthTokens(String jwtToken, String refreshToken) {
}

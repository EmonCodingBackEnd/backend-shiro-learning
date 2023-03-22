package com.coding.shiro.springboot.example03.common.jwt;

public class JwtUnauthorizedException extends RuntimeException {
    public JwtUnauthorizedException(String message) {
        super(message);
    }
}

package com.coding.shiro.springboot.example03.common;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth0.jwt.exceptions.JWTVerificationException;

@ControllerAdvice
public class PermissionException {
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public String authenticationException(Exception e) {
        return "权限认证失败！" + e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public String unauthorizedException(Exception e) {
        return "权限授权失败，无权限！" + e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(JWTVerificationException.class)
    public String jwtVerificationException(Exception e) {
        return "jwtToken校验失败！" + e.getMessage();
    }
}

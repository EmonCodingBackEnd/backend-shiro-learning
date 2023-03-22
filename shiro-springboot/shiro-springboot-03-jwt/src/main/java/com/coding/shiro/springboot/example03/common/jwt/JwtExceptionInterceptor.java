package com.coding.shiro.springboot.example03.common.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        Exception exception = (Exception)request.getAttribute(JwtTokenManager.$X_APP_SESSION_EXP);
        if (exception != null) {
            throw exception;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}

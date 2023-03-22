package com.coding.shiro.springboot.example03.common.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.ObjectUtils;

import com.coding.shiro.springboot.example03.common.jwt.JwtTokenManager;
import com.coding.shiro.springboot.example03.common.jwt.JwtUnauthorizedException;

import lombok.RequiredArgsConstructor;

/**
 * 自定义登录验证过滤器
 */
@RequiredArgsConstructor
public class ShiroJwtAuthcFilter extends FormAuthenticationFilter {

    private final JwtTokenManager jwtTokenManager;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 判断request请求中是否带有jwt令牌
        String jwtAuthHeader = WebUtils.toHttp(request).getHeader(JwtTokenManager.TOKEN_HEADER);
        // 如果有，走JWT校验
        if (!ObjectUtils.isEmpty(jwtAuthHeader)) {
            String jwtAuthToken = jwtTokenManager.fetchToken(jwtAuthHeader);
            boolean verifyJwtToken = jwtTokenManager.verifyJwtToken(jwtAuthToken, 3600);
            if (verifyJwtToken) {
                return super.isAccessAllowed(request, response, mappedValue);
            } else {
                return false;
            }
        }

        // 如果没有，走原始校验
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 判断request请求中是否带有jwt令牌
        String jwtAuthHeader = WebUtils.toHttp(request).getHeader(JwtTokenManager.TOKEN_HEADER);
        // 如果有，抛出异常
        if (!ObjectUtils.isEmpty(jwtAuthHeader)) {
            request.setAttribute(JwtTokenManager.$X_APP_SESSION_EXP,
                new JwtUnauthorizedException("jwt token verify failed"));
            return true;
        }
        // 如果没有：走原始方式
        return super.onAccessDenied(request, response);
    }
}

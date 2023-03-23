package com.coding.shiro.springboot.example03.common.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.ObjectUtils;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.coding.shiro.springboot.example03.common.jwt.JwtTokenManager;

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
            // 校验jwt令牌，过期容错时间位10年，表示不校验令牌是否过期
            String verifyResult = jwtTokenManager.verifyJwtToken(jwtAuthToken, 10 * 360 * 24 * 3600);
            if (ObjectUtils.isEmpty(verifyResult)) {
                return super.isAccessAllowed(request, response, mappedValue);
            } else {
                request.setAttribute(JwtTokenManager.$X_APP_SESSION_EXP, new JWTVerificationException(verifyResult));
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
        // 如果有，走JWT校验
        if (!ObjectUtils.isEmpty(jwtAuthHeader)) {
            // 如果存在jwt令牌校验异常，继续执行后续过滤器
            Exception exception = (Exception)request.getAttribute(JwtTokenManager.$X_APP_SESSION_EXP);
            if (exception != null) {
                return true;
            }
            // jwt令牌中的时间不会变化，如果不校验时间是否过期，jwt令牌校验通过后，
            // 在后续subject中也会发现sessionDAO维护的sessionId数据丢失，进而发现未认证
            else {
                String message = "会话丢失，请重新认证！";
                request.setAttribute(JwtTokenManager.$X_APP_SESSION_EXP, new JWTVerificationException(message));
                return true;
            }
        }

        // 如果没有：走原始方式
        return super.onAccessDenied(request, response);
    }
}

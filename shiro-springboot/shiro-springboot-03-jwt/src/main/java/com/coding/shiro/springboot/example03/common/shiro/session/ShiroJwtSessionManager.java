package com.coding.shiro.springboot.example03.common.shiro.session;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.ObjectUtils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.coding.shiro.springboot.example03.common.jwt.JwtTokenManager;

import lombok.RequiredArgsConstructor;

/**
 * 自定义会话管理器：同时支持jwt和cookie方式
 */
@RequiredArgsConstructor
public class ShiroJwtSessionManager extends DefaultWebSessionManager {

    public static final String JWT_SESSION_ID_SOURCE = "jwt";

    private final JwtTokenManager jwtTokenManager;

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 判断request请求中是否带有jwt令牌
        String jwtAuthHeader = WebUtils.toHttp(request).getHeader(JwtTokenManager.TOKEN_HEADER);
        // 如果没有则执行默认的cookie获得sessionId的方式
        if (ObjectUtils.isEmpty(jwtAuthHeader)) {
            return super.getSessionId(request, response);
        } else {
            String jwtAuthToken = jwtTokenManager.fetchToken(jwtAuthHeader);
            String jwtId;
            try {
                // jwt令牌解码失败，直接返回null处理，交给后续的 ShiroJwtAuthcFilter 插件执行严格拦截
                DecodedJWT decodedJWT = jwtTokenManager.decodeJwtToken(jwtAuthToken);
                jwtId = decodedJWT.getId();
            } catch (Exception e) {
                jwtId = null;
            }

            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, JWT_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, jwtId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED,
                isSessionIdUrlRewritingEnabled());
            return jwtId;
        }
    }
}

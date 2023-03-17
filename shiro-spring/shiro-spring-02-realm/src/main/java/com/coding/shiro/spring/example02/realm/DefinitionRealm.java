package com.coding.shiro.spring.example02.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.ObjectUtils;

public class DefinitionRealm extends AuthorizingRealm {
    /**
     * 获取认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取登录名
        String principal = token.getPrincipal().toString();
        // 查询用户密码
        SecurityService securityService = new SecurityServiceImpl();
        String password = securityService.findPasswordByLoginName(principal);
        if (ObjectUtils.isEmpty(password)) {
            throw new UnknownAccountException("账号不存在");
        }
        // 传递账号和密码
        SimpleAuthenticationInfo simpleAuthenticationInfo =
            new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
        return simpleAuthenticationInfo;
    }

    /**
     * 获取鉴权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
}

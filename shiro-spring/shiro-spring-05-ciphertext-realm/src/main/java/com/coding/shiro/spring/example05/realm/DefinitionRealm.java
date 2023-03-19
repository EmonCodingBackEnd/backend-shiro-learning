package com.coding.shiro.spring.example05.realm;

import java.util.Map;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.CollectionUtils;

import com.coding.shiro.spring.example05.service.SecurityService;
import com.coding.shiro.spring.example05.service.impl.SecurityServiceImpl;
import com.coding.shiro.spring.example05.tools.DigestUtil;

public class DefinitionRealm extends AuthorizingRealm {

    public DefinitionRealm() {
        // 指定密码匹配方式位SHA1
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(DigestUtil.SHA1);
        // 指定密码迭代次数
        matcher.setHashIterations(DigestUtil.ITERATIONS);
        // 使用父类方法使匹配方式生效
        setCredentialsMatcher(matcher);
    }

    /**
     * 获取认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取登录名
        String principal = token.getPrincipal().toString();
        // 查询用户密码
        SecurityService securityService = new SecurityServiceImpl();
        Map<String, String> passwordMap = securityService.findPasswordByLoginName(principal);
        if (CollectionUtils.isEmpty(passwordMap)) {
            throw new UnknownAccountException("账号不存在");
        }
        String salt = passwordMap.get("salt");
        String password = passwordMap.get("password");
        // 传递账号和密码
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
            // 身份
            token.getPrincipal(),
            // 加密口令
            password,
            // 干扰因子
            ByteSource.Util.bytes(salt),
            // realmName
            getName());
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

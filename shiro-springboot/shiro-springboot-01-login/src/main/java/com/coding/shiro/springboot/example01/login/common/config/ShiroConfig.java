package com.coding.shiro.springboot.example01.login.common.config;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.coding.shiro.springboot.example01.login.realm.MyRealm;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ShiroConfig {
    private final MyRealm myRealm;

    /*@Bean
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        // 配置多 realm 的认证策略
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
        return modularRealmAuthenticator;
    }*/

    public RememberMeManager rememberMeManager() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // 设置跨域
        // cookie.setDomain("*");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30 * 24 * 60 * 60);

        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(cookie);
        cookieRememberMeManager.setCipherKey("1234567890987654".getBytes(StandardCharsets.UTF_8));
        return cookieRememberMeManager;
    }

    // 配置SecurityManager
    @Bean
    public DefaultWebSecurityManager webSecurityManager() {
        // 1.创建 defaultWebSecurityManager 对象
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();

        // 2.创建加密对象，设置相关属性
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 2.1.采用MD5加密
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 2.2.采用迭代3次加密
        hashedCredentialsMatcher.setHashIterations(3);
        // 3.将加密对象存储到 myRealm 中
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        // 4.将 myRealm 存入 defaultWebSecurityManager 对象
        webSecurityManager.setRealms(Collections.singletonList(myRealm));
        // 4.5.设置rememberMe
        webSecurityManager.setRememberMeManager(rememberMeManager());
        // 5.返回
        return webSecurityManager;
    }

    // 配置Shiro内置过滤器拦截范围
    @Bean
    public DefaultShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition shiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
        // 设置不认证可以访问的资源
        shiroFilterChainDefinition.addPathDefinition("/myController/login", "anon"); // 匿名过滤器
        shiroFilterChainDefinition.addPathDefinition("/myController/userLogin", "anon");
        // 设置登出过滤器，其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        shiroFilterChainDefinition.addPathDefinition("/myController/logout", "logout"); // 登出过滤器，【注意】请注意顺序，logout过滤器要在authc之前
        // 设置需要进行登录认证的拦截范围
        shiroFilterChainDefinition.addPathDefinition("/**", "authc"); // 认证拦截过滤器
        // 添加存在用户的过滤器(rememberMe)
        shiroFilterChainDefinition.addPathDefinition("/**", "user"); // 用户过滤器

        return shiroFilterChainDefinition;
    }

    // 用于解析thymeleaf中的shiro:相关属性
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}

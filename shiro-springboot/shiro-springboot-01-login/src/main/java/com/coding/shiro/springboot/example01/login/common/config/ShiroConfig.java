package com.coding.shiro.springboot.example01.login.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.coding.shiro.springboot.example01.login.common.auth.realm.MyRealm;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.RequiredArgsConstructor;
import net.sf.ehcache.CacheManager;

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

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        InputStream inputStream;
        try {
            inputStream = ResourceUtils.getInputStreamForPath("classpath:ehcache/ehcache-shiro.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CacheManager cacheManager = new CacheManager(inputStream);
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
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
        myRealm.setAuthorizationCacheName("loginRolePsCache");
        webSecurityManager.setRealms(Collections.singletonList(myRealm));
        // 4.5.设置rememberMe
        webSecurityManager.setRememberMeManager(rememberMeManager());
        // 4.6.设置ehCache缓存管理器
        webSecurityManager.setCacheManager(ehCacheManager());
        // 5.返回
        return webSecurityManager;
    }

    // 配置Shiro内置过滤器拦截范围：方法1
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

    // ==================================================华丽的分割线==================================================
    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     *
     * Filter Chain定义说明 <br>
     * 1、一个URL可以配置多个Filter，使用逗号分隔<br>
     * 2、当设置多个过滤器时，全部验证通过，才视为通过<br>
     * 3、部分过滤器可指定参数，如perms，roles<br>
     */
    // 配置Shiro内置过滤器拦截范围：方法2
    // @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(webSecurityManager());

        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 设置不认证可以访问的资源
        filterChainDefinitionMap.put("/myController/login", "anon"); // 匿名过滤器
        filterChainDefinitionMap.put("/myController/userLogin", "anon");
        // 设置登出过滤器，其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/myController/logout", "logout"); // 登出过滤器，【注意】请注意顺序，logout过滤器要在authc之前
        // 设置需要进行登录认证的拦截范围
        filterChainDefinitionMap.put("/**", "authc"); // 认证拦截过滤器
        // 添加存在用户的过滤器(rememberMe)
        filterChainDefinitionMap.put("/**", "user"); // 用户过滤器

        // 配置不会被拦截的链接 顺序判断
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
}

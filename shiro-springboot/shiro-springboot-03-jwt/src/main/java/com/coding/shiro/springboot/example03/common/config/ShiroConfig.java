package com.coding.shiro.springboot.example03.common.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.coding.shiro.springboot.example03.common.jwt.JwtTokenManager;
import com.coding.shiro.springboot.example03.common.shiro.cache.ShiroRedisCacheManager;
import com.coding.shiro.springboot.example03.common.shiro.filter.KickedOutAuthorizationFilter;
import com.coding.shiro.springboot.example03.common.shiro.filter.RolesOrAuthorizationFilter;
import com.coding.shiro.springboot.example03.common.shiro.filter.ShiroJwtAuthcFilter;
import com.coding.shiro.springboot.example03.common.shiro.realm.DefinitionRealm;
import com.coding.shiro.springboot.example03.common.shiro.session.ShiroJwtSessionManager;
import com.coding.shiro.springboot.example03.common.shiro.session.ShiroRedisSessionDAO;
import com.coding.shiro.springboot.example03.domain.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ShiroConfig {
    private final UsersService usersService;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;
    private final JwtTokenManager jwtTokenManager;

    @Bean
    ShiroRedisCacheManager shiroRedisCacheManager() {
        return new ShiroRedisCacheManager(redissonClient, objectMapper);
    }

    @Bean
    public DefinitionRealm definitionRealm() {
        DefinitionRealm definitionRealm = new DefinitionRealm(usersService, redissonClient);
        // 设置缓存管理器
        definitionRealm.setAuthorizationCacheName("loginRolePsCache");
        definitionRealm.setCacheManager(shiroRedisCacheManager());
        return definitionRealm;
    }

    @Bean
    public ShiroRedisSessionDAO shiroRedisSessionDAO() {
        ShiroRedisSessionDAO shiroRedisSessionDAO = new ShiroRedisSessionDAO(redissonClient);
        shiroRedisSessionDAO.setGlobalSessionTimeoutInMills(300 * 1000);
        return shiroRedisSessionDAO;
    }

    @Bean
    public ShiroJwtSessionManager sessionManager() {
        ShiroJwtSessionManager sessionManager = new ShiroJwtSessionManager(jwtTokenManager);
        sessionManager.setSessionDAO(shiroRedisSessionDAO());
        sessionManager.setSessionValidationSchedulerEnabled(false); // 默认true
        sessionManager.setSessionValidationInterval(900 * 1000); // 默认1小时
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setGlobalSessionTimeout(600 * 1000); // 默认30分钟；全局超时时间，可以被Subject.getSession().setTimeout(long)覆盖
        sessionManager.setDeleteInvalidSessions(true); // 是否删除无效的Session，默认true
        sessionManager.setSessionIdUrlRewritingEnabled(false); // 取消URL后面的JSESSIONID，默认false
        return sessionManager;
    }

    // 配置SecurityManager
    @Bean
    public DefaultWebSecurityManager webSecurityManager() {
        // 创建 defaultWebSecurityManager 对象
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();

        // 将 myRealm 存入 defaultWebSec`urityManager 对象
        webSecurityManager.setRealms(Collections.singletonList(definitionRealm()));
        // 设置Session管理器
        webSecurityManager.setSessionManager(sessionManager());

        return webSecurityManager;
    }

    // 配置Shiro内置过滤器拦截范围：第一步
    @Bean
    public DefaultShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition shiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
        // 设置不认证可以访问的资源
        shiroFilterChainDefinition.addPathDefinition("/myController/login", "anon"); // 匿名过滤器
        shiroFilterChainDefinition.addPathDefinition("/jwt/login", "anon"); // 匿名过滤器
        shiroFilterChainDefinition.addPathDefinition("/myController/userLogin", "anon");
        // 设置登出过滤器，其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        shiroFilterChainDefinition.addPathDefinition("/myController/logout", "logout"); // 登出过滤器，【注意】请注意顺序，logout过滤器要在authc之前
        // 使用自定义过滤器
        shiroFilterChainDefinition.addPathDefinition("/myController/userLoginRolesCustomFilter",
            "role-or[admin,otherRole]"); // 认证拦截过滤器
        // 设置需要进行登录认证的拦截范围
        shiroFilterChainDefinition.addPathDefinition("/**", "kicked-out,jwt-authc"); // 认证拦截过滤器

        return shiroFilterChainDefinition;
    }

    // 用于解析thymeleaf中的shiro:相关属性
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    // ==================================================华丽的分割线==================================================

    private Map<String, Filter> filterMap() {
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("role-or", new RolesOrAuthorizationFilter());
        filterMap.put("kicked-out",
            new KickedOutAuthorizationFilter(redissonClient, shiroRedisSessionDAO(), sessionManager()));
        filterMap.put("jwt-authc", new ShiroJwtAuthcFilter(jwtTokenManager));
        return filterMap;
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     *
     * Filter Chain定义说明 <br>
     * 1、一个URL可以配置多个Filter，使用逗号分隔<br>
     * 2、当设置多个过滤器时，全部验证通过，才视为通过<br>
     * 3、部分过滤器可指定参数，如perms，roles<br>
     * 该方式不完全等同于定义 shiroFilterChainDefinition()
     * ，具体参考：{@linkplain org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration}
     */
    // 配置Shiro内置过滤器拦截范围：第二步；如果不需要自定义过滤器，不建议配置
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
        @Value("#{ @environment['shiro.loginUrl'] ?: '/login.jsp' }") String loginUrl,
        @Value("#{ @environment['shiro.successUrl'] ?: '/' }") String successUrl,
        @Value("#{ @environment['shiro.unauthorizedUrl'] ?: null }") String unauthorizedUrl) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        shiroFilterFactoryBean.setSuccessUrl(successUrl);
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        shiroFilterFactoryBean.setSecurityManager(webSecurityManager());

        shiroFilterFactoryBean.setFilters(filterMap());

        // 配置不会被拦截的链接 顺序判断
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        return shiroFilterFactoryBean;
    }
}

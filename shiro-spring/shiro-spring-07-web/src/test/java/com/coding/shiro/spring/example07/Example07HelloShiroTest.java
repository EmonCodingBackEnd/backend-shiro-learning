package com.coding.shiro.spring.example07;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

public class Example07HelloShiroTest {

    private Subject shiroLogin(String loginName, String password) {
        // 1.导入权限ini文件构建权限工厂
        Environment environment = new BasicIniEnvironment("classpath:shiro.ini");
        // 2.构造安全管理器
        SecurityManager securityManager = environment.getSecurityManager();
        // 3.使用SecurityUtils工具生效安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        // 4.使用SecurityUtils工具获得主体
        Subject subject = SecurityUtils.getSubject();
        // 5.创建账号token对象
        AuthenticationToken token = new UsernamePasswordToken("emon", "emon123");
        // 6.完成登录
        try {
            // Subject.login=>SecurityManager.login=>Authenticator.authenticate=>Realm.getAuthenticationInfo=>CredentialsMatcher.doCredentialsMatch
            subject.login(token);
            System.out.println("认证结果：" + subject.isAuthenticated());
            System.out.println("登录成功！");
        } catch (UnknownAccountException e) {
            System.out.println("用户不存在！");
            throw e;
        } catch (IncorrectCredentialsException e) {
            System.out.println("密码错误！");
            throw e;
        } catch (AuthenticationException e) {
            System.out.println("登录失败！");
            throw e;
        } catch (UnauthorizedException e) {
            System.out.println("权限验证失败");
            throw e;
        }
        return subject;
    }

    @Test
    public void testPermissionRealm() {
        Subject subject = shiroLogin("emon", "emon123");
        // 判断用户是否已经登录
        System.out.println("是否登录成功 = " + subject.isAuthenticated());

        // ----------检查当前用户的角色信息----------
        System.out.println("是否有管理员角色 = " + subject.hasRole("admin"));

        // ----------如果当前用户有此角色，无返回值。若没有此权限，则抛 UnauthorizedException ----------
        try {
            subject.checkRole("coder");
            System.out.println("有coder角色");
        } catch (AuthorizationException e) {
            System.out.println("没有coder角色");
        }

        // ----------检查当前用户的资源权限信息----------
        System.out.println("是否有查看订单列表的资源权限 = " + subject.isPermitted("order:list"));

        // ----------如果当前用户有此资源权限，无返回值。若没有此权限，则抛 UnauthorizedException ----------
        try {
            subject.checkPermissions("order:add", "order:del");
            System.out.println("有添加和删除订单资源的权限");
        } catch (AuthorizationException e) {
            System.out.println("没有添加和删除订单资源的权限");
        }
    }
}

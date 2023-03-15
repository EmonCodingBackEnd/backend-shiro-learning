package com.coding.shiro.springboot.example01.login;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

public class ShiroRun {
    public static void main(String[] args) {
        // 1.初始化获取SecurityManager
        /*IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();*/
        Environment environment = new BasicIniEnvironment("classpath:shiro.ini");
        SecurityManager securityManager = environment.getSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);
        // 2.获取Subject对象
        Subject subject = SecurityUtils.getSubject();
        // 3.创建token对象，web应用用户名密码从页面传递
        AuthenticationToken token = new UsernamePasswordToken("emon", "emon123");
        // 4.完成登录
        try {
            subject.login(token);
            System.out.println("认证结果：" + subject.isAuthenticated());
            System.out.println("登录成功！");
            // 5、判断角色
            boolean hasRole = subject.hasRole("role1");
            System.out.println("用户是否有角色role1=" + hasRole);
            // 6、判断权限
            boolean permitted = subject.isPermitted("user:insert");
            System.out.println("用户是否拥有权限user:insert=" + permitted);
            // 检查失败时会抛出异常
            subject.checkPermission("user:select");
            System.out.println("用户是否拥有权限user:select=" + permitted);
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户不存在！");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误！");
        } catch (AuthenticationException e) {
            e.printStackTrace();
            System.out.println("登录失败！");
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            System.out.println("权限验证失败");
        }
    }
}

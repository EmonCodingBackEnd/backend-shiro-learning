package com.coding.shiro.spring.example01;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

public class Example01HelloShiro {
    public static void main(String[] args) {
        // 1.导入权限ini文件构建权限工厂
        // Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        Environment environment = new BasicIniEnvironment("classpath:shiro.ini");
        // 2.构造安全管理器
        // SecurityManager instance = factory.getInstance();
        SecurityManager securityManager = environment.getSecurityManager();
        // 3.使用SecurityUtils工具生效安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        // 4.使用SecurityUtils工具获得主体
        Subject subject = SecurityUtils.getSubject();
        // 5.创建账号token对象
        AuthenticationToken token = new UsernamePasswordToken("emon", "emon123");
        // 6.完成登录
        try {
            subject.login(token);
            System.out.println("认证结果：" + subject.isAuthenticated());
            System.out.println("登录成功！");
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

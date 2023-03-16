package com.coding.shiro.springboot.example01.login.web;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {
    // 跳转到登录页面
    @GetMapping("/myController/login")
    public String login() {
        return "login";
    }

    // 登录认证
    @GetMapping("/myController/userLogin")
    public String userLogin(String name, String pwd, @RequestParam(defaultValue = "false") boolean rememberMe,
        HttpSession session) {
        // 1.获取 Subject 对象
        Subject subject = SecurityUtils.getSubject();
        // 2.封装请求数据到 token
        AuthenticationToken token = new UsernamePasswordToken(name, pwd, rememberMe);
        // 3.调用 login 方法进行登录认证
        try {
            subject.login(token);
            // return "登录成功";
            session.setAttribute("user", token.getPrincipal().toString());
            return "main";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return "登录失败！";
        }
    }

    // 登录认证验证rememberMe
    @GetMapping("/myController/userLoginRm")
    public String userLoginRm(HttpSession session) {
        session.setAttribute("user", "rememberMe");
        return "main";
    }
}

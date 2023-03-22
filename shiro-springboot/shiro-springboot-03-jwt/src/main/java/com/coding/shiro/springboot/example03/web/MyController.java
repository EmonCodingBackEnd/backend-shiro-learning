package com.coding.shiro.springboot.example03.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson2.JSONObject;
import com.coding.shiro.springboot.example03.common.jwt.JwtTokenManager;
import com.coding.shiro.springboot.example03.common.shiro.session.ShiroUser;
import com.coding.shiro.springboot.example03.common.shiro.token.SimpleToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyController {

    private final ObjectMapper objectMapper;
    private final JwtTokenManager jwtTokenManager;

    // 跳转到登录页面
    @GetMapping("/myController/login")
    public String login() {
        return "login";
    }

    // 登录认证
    @GetMapping("/myController/userLogin")
    public String userLogin(String name, String pwd, HttpSession session) {
        // 1.获取 Subject 对象
        Subject subject = SecurityUtils.getSubject();
        // 2.封装请求数据到 token
        AuthenticationToken token = new SimpleToken(name, pwd, "1");
        // 3.调用 login 方法进行登录认证
        try {
            subject.login(token);
            // return "登录成功";
            session.setAttribute("user", token.getPrincipal().toString());
            return "main";
        } catch (AuthenticationException e) {
            session.setAttribute("msg", e.getMessage());
            e.printStackTrace();
            return "login";
        }
    }

    // 登录认证验证角色
    @RequiresRoles(value = {"admin", "otherRole"}, logical = Logical.OR)
    @GetMapping("/myController/userLoginRoles")
    @ResponseBody
    public String userLoginRoles() {
        System.out.println("登录认证验证角色");
        return "验证角色成功！";
    }

    // 登录认证自定义过滤验证角色
    @GetMapping("/myController/userLoginRolesCustomFilter")
    @ResponseBody
    public String userLoginRolesCustomFilter() {
        System.out.println("登录认证自定义过滤验证角色");
        return "自定义过滤验证角色成功！";
    }

    // 登录认证验证权限
    @RequiresPermissions({"user:edit", "user:delete"})
    @GetMapping("/myController/userLoginPss")
    @ResponseBody
    public String userLoginPss() {
        System.out.println("登录认证验证权限");
        return "验证权限成功！";
    }

    // ==================================================华丽的分隔线==================================================

    @GetMapping("/jwt/login")
    @ResponseBody
    public String jwtLogin(String name, String pwd, HttpSession session) {
        // 1.获取 Subject 对象
        Subject subject = SecurityUtils.getSubject();
        // 2.封装请求数据到 token
        AuthenticationToken token = new SimpleToken(name, pwd, "1");
        // 3.调用 login 方法进行登录认证
        try {
            subject.login(token);
            // 登录成功后办法令牌
            subject = SecurityUtils.getSubject();
            Serializable sessionId = subject.getSession().getId();
            ShiroUser shiroUser = (ShiroUser)subject.getPrincipal();
            Map<String, Object> claims = new HashMap<>();
            claims.put("shiroUser", JSONObject.toJSONString(shiroUser));
            String jwtToken = jwtTokenManager.issueJwtToken(sessionId.toString(), shiroUser.getId(), claims,
                subject.getSession().getTimeout());
            return jwtToken;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequiresRoles(value = {"admin", "userMgr"}, logical = Logical.AND)
    @GetMapping("/jwt/roles")
    @ResponseBody
    public String jwtRoles() {
        System.out.println("登录认证验证角色");
        return "jwt验证角色成功！";
    }

    @RequiresPermissions({"user:edit", "user:delete1"})
    @GetMapping("/jwt/perms")
    @ResponseBody
    public String jwtPerms() {
        System.out.println("登录认证验证权限");
        return "jwt验证权限成功！";
    }

}

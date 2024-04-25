package com.coding.shiro.cas.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class TestController {

    @Value("${cas.server-url-prefix}")
    private String CAS_URL;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "word";
    }

    // 登出
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:" + CAS_URL + "/logout";
    }
}

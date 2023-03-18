package com.coding.shiro.spring.example07.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.UsernamePasswordToken;

import com.coding.shiro.spring.example07.service.LoginService;
import com.coding.shiro.spring.example07.service.impl.LoginServiceImpl;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = -3273637471060149524L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        String protocol = req.getProtocol();
        // 获得用户名和密码
        String loginName = req.getParameter("loginName");
        String password = req.getParameter("password");
        // 构建登录使用的token
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password, protocol);
        // 登录操作
        LoginService loginService = new LoginServiceImpl();
        boolean loginSuccess = loginService.login(token);
        // 如果登录成功，跳转home.jsp
        if (loginSuccess) {
            req.getRequestDispatcher("/home").forward(req, resp);
            return;
        }
        // 如果登录失败，跳转继续登录页面
        resp.sendRedirect("login.jsp");
    }
}

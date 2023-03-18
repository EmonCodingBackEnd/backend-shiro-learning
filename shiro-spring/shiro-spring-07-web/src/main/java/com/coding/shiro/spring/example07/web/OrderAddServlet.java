package com.coding.shiro.spring.example07.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 添加订单
 */
@WebServlet(urlPatterns = "/order-add")
public class OrderAddServlet extends HttpServlet {
    private static final long serialVersionUID = -8575095444392785712L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/order-add.jsp").forward(req, resp);
    }
}

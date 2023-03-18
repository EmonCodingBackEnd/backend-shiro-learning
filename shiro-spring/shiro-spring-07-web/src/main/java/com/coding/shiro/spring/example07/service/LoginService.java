package com.coding.shiro.spring.example07.service;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 登录服务
 */
public interface LoginService {

    /**
     * 登录方法
     * 
     * @param token - 登录对象
     * @return - 是否登录成功
     */
    boolean login(UsernamePasswordToken token);

    /**
     * 登出方法
     */
    void logout();
}

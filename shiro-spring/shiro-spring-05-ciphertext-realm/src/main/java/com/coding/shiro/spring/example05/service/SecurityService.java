package com.coding.shiro.spring.example05.service;

import java.util.Map;

/**
 * 模拟数据库操作服务接口
 */
public interface SecurityService {

    /**
     * 查找用户密码
     * 
     * @param loginName - 用户名称
     * @return - 密码
     */
    Map<String, String> findPasswordByLoginName(String loginName);
}

package com.coding.shiro.spring.example06.service;

import java.util.List;
import java.util.Map;

/**
 * 模拟数据库操作服务接口
 */
public interface SecurityService {

    /**
     * 根据用户名称查找用户密码
     * 
     * @param loginName - 登录名称
     * @return - 密码
     */
    Map<String, String> findPasswordByLoginName(String loginName);

    /**
     * 根据用户名称查找角色
     * 
     * @param loginName - 登录名称
     * @return
     */
    List<String> findRoleByLoginName(String loginName);

    /**
     * 根据用户名称查找资源权限
     * 
     * @param loginName - 登录名称
     * @return
     */
    List<String> findPermissionByLoginName(String loginName);
}

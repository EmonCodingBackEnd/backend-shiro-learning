package com.coding.shiro.springboot.example01.login.domain.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coding.shiro.springboot.example01.login.domain.entity.Users;

/**
 * @author 问秋
 * @description 针对表【users(用户表)】的数据库操作Service
 * @createDate 2023-03-16 10:29:04
 */
public interface UsersService extends IService<Users> {

    /**
     * 根据用户名称获取用户的基本信息
     * 
     * @param name
     * @return
     */
    Users getUserInfoByName(String name);

    /**
     * 根据用户名称获取用户的角色信息
     * 
     * @param name
     * @return
     */
    List<String> getUserRoleInfoByName(String name);

    /**
     * 根据用户名称获取用户的角色权限信息
     *
     * @param name
     * @return
     */
    List<String> getUserRolePsInfoByName(String name);

}

package com.coding.shiro.springboot.example01.login.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coding.shiro.springboot.example01.login.domain.entity.Users;

/**
 * @author 问秋
 * @description 针对表【users(用户表)】的数据库操作Service
 * @createDate 2023-03-16 10:29:04
 */
public interface UsersService extends IService<Users> {

    Users getUserInfoByName(String name);

}

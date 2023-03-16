package com.coding.shiro.springboot.example01.login.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coding.shiro.springboot.example01.login.domain.entity.Users;
import com.coding.shiro.springboot.example01.login.domain.service.UsersService;
import com.coding.shiro.springboot.example01.login.domain.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author 问秋
* @description 针对表【users(用户表)】的数据库操作Service实现
* @createDate 2023-03-16 10:29:04
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}





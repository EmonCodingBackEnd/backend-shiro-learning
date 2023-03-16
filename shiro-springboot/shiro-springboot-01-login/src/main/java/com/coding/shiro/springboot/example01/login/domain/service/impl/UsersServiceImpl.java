package com.coding.shiro.springboot.example01.login.domain.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coding.shiro.springboot.example01.login.domain.entity.Users;
import com.coding.shiro.springboot.example01.login.domain.mapper.UsersMapper;
import com.coding.shiro.springboot.example01.login.domain.service.UsersService;

/**
 * @author 问秋
 * @description 针对表【users(用户表)】的数据库操作Service实现
 * @createDate 2023-03-16 10:29:04
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Override
    @Transactional(readOnly = true)
    public Users getUserInfoByName(String name) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Users::getName, name);
        return this.baseMapper.selectOne(queryWrapper);
    }
}

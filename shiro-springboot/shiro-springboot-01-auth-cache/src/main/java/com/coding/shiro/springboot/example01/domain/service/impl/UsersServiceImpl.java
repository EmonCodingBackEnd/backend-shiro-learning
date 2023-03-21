package com.coding.shiro.springboot.example01.domain.service.impl;

import java.util.List;

import com.coding.shiro.springboot.example01.domain.entity.Users;
import com.coding.shiro.springboot.example01.domain.mapper.UsersMapper;
import com.coding.shiro.springboot.example01.domain.service.UsersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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

    @Override
    public List<String> getUserRoleInfoByName(String name) {
        return this.baseMapper.getUserRoleInfoMapper(name);
    }

    @Override
    public List<String> getUserRolePsInfoByName(String name) {
        return this.baseMapper.getUserRolePsInfoMapper(name);
    }
}

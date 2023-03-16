package com.coding.shiro.springboot.example01.login.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.shiro.springboot.example01.login.domain.entity.Users;

/**
 * @author 问秋
 * @description 针对表【users(用户表)】的数据库操作Mapper
 * @createDate 2023-03-16 10:29:03
 * @Entity com.coding.shiro.springboot.example01.login.domain.entity.Users
 */
public interface UsersMapper extends BaseMapper<Users> {

    @Select("select name from roles where id in ( select rid from role_user where uid = ( select id from users where name = #{principal} ) ) ")
    List<String> getUserRoleInfoMapper(@Param("principal") String principal);

    @Select("select info from permissions where id in ( select pid from role_ps where rid in ( select rid from role_user where uid = ( select id from users where name = #{principal})))")
    List<String> getUserRolePsInfoMapper(@Param("principal") String principal);
}

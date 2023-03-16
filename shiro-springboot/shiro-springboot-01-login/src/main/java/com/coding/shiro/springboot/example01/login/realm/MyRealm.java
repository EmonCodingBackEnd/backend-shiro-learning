package com.coding.shiro.springboot.example01.login.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import com.coding.shiro.springboot.example01.login.domain.entity.Users;
import com.coding.shiro.springboot.example01.login.domain.service.UsersService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MyRealm extends AuthorizingRealm {

    private final UsersService usersService;

    // 自定义授权方法：获取当前登录用户的角色、权限信息，返回给 Shiro 用来进行授权认证
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("进入自定义授权方法");
        // 1.获取用户身份信息
        String principal = principals.getPrimaryPrincipal().toString();
        // 2.调用业务层获取用户的角色信息（数据库）
        List<String> roles = usersService.getUserRoleInfoByName(principal);
        System.out.println("roles = " + roles);
        // 2.5.调用业务层获取用户的权限信息（数据库）
        List<String> pss = usersService.getUserRolePsInfoByName(principal);
        System.out.println("pss = " + pss);
        // 3.创建对象，封装当前登录用户的角色、权限信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 4.存储角色
        info.addRoles(roles);
        info.addStringPermissions(pss);
        // 5.返回信息
        return info;
    }

    /*
    1.自定义登录认证方法，shiro的login方法底层会调用该类的认证方法进行认证
    2.需要配置自定义的realm生效，在ini文件中配置，或者在SpringBoot中配置
    该方法只是获取进行对比的信息，认证逻辑还是按照shiro底层认证逻辑完成
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 1.获取身份信息
        String principal = token.getPrincipal().toString();
        // 2.获取凭证信息
        String credentials = new String((char[])token.getCredentials());
        System.out.println("认证用户信息：" + principal + "---" + credentials);
        // 3.获取数据库中存储的用户信息
        String pwdInfo;
        Users userInfo = usersService.getUserInfoByName(principal);
        if (userInfo != null && principal.equalsIgnoreCase(userInfo.getName())) {
            // 3.1.数据库中存放的加盐迭代3次的迭代密码
            pwdInfo = userInfo.getPwd();
        } else {
            throw new RuntimeException("不合法的用户！");
        }
        // 4.创建封装校验逻辑对象，封装数据返回
        AuthenticationInfo info = new SimpleAuthenticationInfo(
            // 身份信息
            token.getPrincipal(),
            // 真实凭证信息
            pwdInfo,
            // 加盐值
            ByteSource.Util.bytes("salt"),
            // 用户名称
            token.getPrincipal().toString());
        return info;
    }
}

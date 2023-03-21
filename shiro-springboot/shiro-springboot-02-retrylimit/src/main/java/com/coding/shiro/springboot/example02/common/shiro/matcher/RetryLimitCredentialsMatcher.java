package com.coding.shiro.springboot.example02.common.shiro.matcher;

import java.time.Duration;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import lombok.RequiredArgsConstructor;

/**
 * 自定义密码比较器：限制密码重试次数
 */
@RequiredArgsConstructor
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {

    public static final String DEFAULT_LOGIN_KEY_PREFIX = "shiro:login:retryNum";
    private final String keyPrefix = DEFAULT_LOGIN_KEY_PREFIX;

    private final RedissonClient redissonClient;

    private static final Long RETRY_LIMIT_NUM = 4L;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String loginName = (String)token.getPrincipal();
        // 后去系统中是否已有登录次数缓存，缓存对象结果预期位：“用户名--登录次数”
        // 如果之前没有登录缓存，则创建一个登录次数缓存
        RAtomicLong atomicLong = redissonClient.getAtomicLong(keyPrefix + loginName);
        long retryNums = atomicLong.get();
        // 如果缓存次数已经超过限制，则驳回本次登录请求
        if (retryNums > RETRY_LIMIT_NUM) {
            atomicLong.expire(Duration.ofMinutes(10));
            throw new ExcessiveAttemptsException("密码次数错误5次，请稍后重试！");
        }
        // 将缓存记录的登录次数加1，设置指定时间内有效
        atomicLong.incrementAndGet();
        atomicLong.expire(Duration.ofMinutes(10));
        // 验证用户本次输入的账号密码，如果登录成功，则清除掉登录次数的缓存
        boolean flag = super.doCredentialsMatch(token, info);
        if (flag) {
            atomicLong.delete();
        }
        return flag;
    }
}

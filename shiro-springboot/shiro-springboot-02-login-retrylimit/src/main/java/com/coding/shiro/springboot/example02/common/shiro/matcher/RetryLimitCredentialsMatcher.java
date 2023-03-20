package com.coding.shiro.springboot.example02.common.shiro.matcher;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * 自定义密码比较器
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {}

/*
 * <b>文件名</b>：ShiroUtil.java
 *
 * 文件描述：
 *
 *
 * 2017-10-11 下午2:28:25
 */

package com.coding.shiro.springboot.example01.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * shiro工具类
 */
public class ShiroUtil {

    /**
     * 获得shiro的session
     */
    public static Session getShiroSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 获得shiro的sessionId
     */
    public static String getShiroSessionId() {
        return getShiroSession().getId().toString();
    }

    /**
     * 是否登陆
     */
    public static Boolean isAuthenticated() {
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }

}

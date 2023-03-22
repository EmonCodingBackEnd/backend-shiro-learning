package com.coding.shiro.springboot.example03.common.shiro.filter;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;
import org.springframework.util.ObjectUtils;

import com.coding.shiro.springboot.example03.common.shiro.session.ShiroJwtSessionManager;
import com.coding.shiro.springboot.example03.common.shiro.session.ShiroRedisSessionDAO;
import com.coding.shiro.springboot.example03.common.shiro.session.ShiroUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义过滤器：控制同一个账号的并发登录数量，做出踢出操作
 */
@Slf4j
@RequiredArgsConstructor
public class KickedOutAuthorizationFilter extends AccessControlFilter {

    public static final String DEFAULT_LOGIN_KEY_PREFIX = "shiro:login:concurrency:";
    private final String keyPrefix = DEFAULT_LOGIN_KEY_PREFIX;

    private static final Long CONCURRENCY_NUM = 2L;

    private final RedissonClient redissonClient;
    private final ShiroRedisSessionDAO shiroRedisSessionDAO;
    private final ShiroJwtSessionManager defaultWebSessionManager;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
        throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 只针对登录用户处理，首先判断是否登录
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated()) {
            return true;
        }
        // 使用RedissonClient创建队列
        Serializable sessionId = subject.getSession().getId();
        String loginName = ((ShiroUser)subject.getPrincipal()).getLoginName();
        RDeque<Object> deque = redissonClient.getDeque(keyPrefix + loginName);
        // 判断当前sessionId是否存在于此用户的队列
        boolean contains = deque.contains(sessionId);
        // 不存在则放入队列尾部==>存入sessionId
        if (!contains) {
            deque.addLast(sessionId);
        }
        // 判断当前队列大小是否超过限定词账号的可在线人数
        // 超过
        if (deque.size() > CONCURRENCY_NUM) {
            // 从队列头部拿到用户sessionId
            Serializable firstSessionId = (Serializable)deque.removeFirst();
            // sessionManager根据sessionId拿到session
            Session session = null;
            try {
                session = defaultWebSessionManager.getSession(new DefaultSessionKey(firstSessionId));
            } catch (UnknownSessionException e) {
                log.info("session已失效，sessionId={}", firstSessionId);
            } catch (ExpiredSessionException e) {
                log.info("session已过期，sessionId={}", firstSessionId);
            } catch (SessionException e) {
                log.info("位置session错误！，sessionId={}", firstSessionId);
            }
            // sessionDao中移除session会话
            if (!ObjectUtils.isEmpty(session)) {
                shiroRedisSessionDAO.delete(session);
            }
        }
        // 未超过：放过操作
        return true;
    }
}

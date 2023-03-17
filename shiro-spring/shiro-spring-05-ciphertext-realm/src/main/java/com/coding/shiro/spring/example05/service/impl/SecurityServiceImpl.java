package com.coding.shiro.spring.example05.service.impl;

import java.util.Map;

import com.coding.shiro.spring.example05.service.SecurityService;
import com.coding.shiro.spring.example05.tools.DigestUtil;

public class SecurityServiceImpl implements SecurityService {
    @Override
    public Map<String, String> findPasswordByLoginName(String loginName) {
        return DigestUtil.entryptPassword("emon123");
    }
}

package com.coding.shiro.spring.example02.service.impl;

import com.coding.shiro.spring.example02.service.SecurityService;

public class SecurityServiceImpl implements SecurityService {
    @Override
    public String findPasswordByLoginName(String loginName) {
        return "emon123";
    }
}

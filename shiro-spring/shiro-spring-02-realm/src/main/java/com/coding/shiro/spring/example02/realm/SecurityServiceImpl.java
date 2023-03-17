package com.coding.shiro.spring.example02.realm;

public class SecurityServiceImpl implements SecurityService {
    @Override
    public String findPasswordByLoginName(String loginName) {
        return "emon123";
    }
}

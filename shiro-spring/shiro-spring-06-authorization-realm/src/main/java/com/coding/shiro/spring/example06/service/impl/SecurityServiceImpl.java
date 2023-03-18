package com.coding.shiro.spring.example06.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.coding.shiro.spring.example06.service.SecurityService;
import com.coding.shiro.spring.example06.tools.DigestUtil;

public class SecurityServiceImpl implements SecurityService {
    @Override
    public Map<String, String> findPasswordByLoginName(String loginName) {
        return DigestUtil.entryptPassword("emon123");
    }

    @Override
    public List<String> findRoleByLoginName(String loginName) {
        List<String> list = new ArrayList<>();
        list.add("admin");
        list.add("dev");
        return list;
    }

    @Override
    public List<String> findPermissionByLoginName(String loginName) {
        List<String> list = new ArrayList<>();
        list.add("order:list");
        list.add("order:add");
        list.add("order:del");
        return list;
    }
}

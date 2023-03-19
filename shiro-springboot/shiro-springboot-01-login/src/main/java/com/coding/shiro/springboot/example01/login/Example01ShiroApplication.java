package com.coding.shiro.springboot.example01.login;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.coding.shiro.springboot.example01.login.domain.mapper")
public class Example01ShiroApplication {
    public static void main(String[] args) {
        SpringApplication.run(Example01ShiroApplication.class, args);
    }
}
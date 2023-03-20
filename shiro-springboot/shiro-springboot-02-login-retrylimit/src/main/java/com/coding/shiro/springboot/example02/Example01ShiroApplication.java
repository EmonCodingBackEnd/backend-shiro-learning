package com.coding.shiro.springboot.example01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.coding.shiro.springboot.example02.domain.mapper")
public class Example01ShiroApplication {
    public static void main(String[] args) {
        SpringApplication.run(Example01ShiroApplication.class, args);
    }
}
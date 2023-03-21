package com.coding.shiro.springboot.example03;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.coding.shiro.springboot.example03.domain.mapper")
public class Example03ShiroApplication {
    public static void main(String[] args) {
        SpringApplication.run(Example03ShiroApplication.class, args);
    }
}
package com.coding.shiro.cas;

import net.unicon.cas.client.configuration.EnableCasClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCasClient
@SpringBootApplication
@MapperScan("com.coding.shiro.cas")
public class ExampleCasApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleCasApplication.class, args);
    }
}
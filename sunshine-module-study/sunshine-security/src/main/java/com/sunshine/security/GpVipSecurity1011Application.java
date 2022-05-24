package com.sunshine.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.sunshine.security.mapper")
@SpringBootApplication(scanBasePackages = "com.sunshine")
public class GpVipSecurity1011Application {

    public static void main(String[] args) {
        SpringApplication.run(GpVipSecurity1011Application.class, args);
    }

}

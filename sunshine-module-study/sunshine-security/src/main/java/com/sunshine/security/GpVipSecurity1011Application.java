package com.sunshine.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@MapperScan(basePackages = "com.sunshine.security.mapper")
@SpringBootApplication(scanBasePackages = "com.sunshine")
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
public class GpVipSecurity1011Application {

    public static void main(String[] args) {
        SpringApplication.run(GpVipSecurity1011Application.class, args);
    }
}

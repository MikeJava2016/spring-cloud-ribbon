package com.sunshine.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@MapperScan(basePackages = "com.sunshine.security.mapper")
@SpringBootApplication(scanBasePackages = "com.sunshine")
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
public class GpVipSecurity1011Application {

    public static void main(String[] args) {
        SpringApplication.run(GpVipSecurity1011Application.class, args);
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        commonsMultipartResolver.setMaxUploadSize(10485760000L);
        commonsMultipartResolver.setMaxInMemorySize(40960);
        return commonsMultipartResolver;
    }
}

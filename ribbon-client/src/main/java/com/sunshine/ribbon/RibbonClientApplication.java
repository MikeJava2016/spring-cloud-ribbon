package com.sunshine.ribbon;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

@EnableApolloConfig
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.sunshine.api.feign")
@ComponentScan(value = "com.sunshine")
@SpringBootApplication
public class RibbonClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonClientApplication.class, args);
    }



    private void test(){
        ClassPathResource classPathResource = new ClassPathResource("1.XML");
        FileSystemXmlApplicationContext factory = new FileSystemXmlApplicationContext("1.xml");

    }
}

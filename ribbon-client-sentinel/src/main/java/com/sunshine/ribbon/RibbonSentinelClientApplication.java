package com.sunshine.ribbon;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
@EnableEurekaClient
@EnableApolloConfig
@EnableFeignClients(basePackages = "com.sunshine.api.feign")
@ComponentScan(value = "com.sunshine")
@SpringBootApplication
public class RibbonSentinelClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonSentinelClientApplication.class, args);
    }

}

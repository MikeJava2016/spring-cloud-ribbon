package com.sunshine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//https://gitee.com/mirrors/fizz-gateway?_from=gitee_search
@SpringBootApplication(scanBasePackages={"com.sunshine"})
@EnableFeignClients(basePackages = "com.sunshine")
public class RibbonGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonGatewayApplication.class, args);
    }

}

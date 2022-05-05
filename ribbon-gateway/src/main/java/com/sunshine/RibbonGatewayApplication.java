package com.sunshine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//https://gitee.com/mirrors/fizz-gateway?_from=gitee_search
@SpringBootApplication(scanBasePackages={"com.sunshine"})
@MapperScan("com.sunshine.mapper")
public class RibbonGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonGatewayApplication.class, args);
    }

}

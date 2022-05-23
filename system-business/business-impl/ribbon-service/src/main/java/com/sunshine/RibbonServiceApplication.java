package com.sunshine;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableApolloConfig
@EnableFeignClients(basePackages = "com.sunshine")
@EnableEurekaClient
@SpringBootApplication(scanBasePackages={"com.sunshine"})
@MapperScan("com.sunshine.mapper")
public class RibbonServiceApplication {

    public static void main(String[] args) {
//        System.setProperty("apollo.refreshInterval","3000");
        SpringApplication.run(RibbonServiceApplication.class, args);
    }

}

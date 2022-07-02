package com.sunshine;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.sunshine.springvc.configuration.SpringWebConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@EnableApolloConfig
@EnableFeignClients(basePackages = "com.sunshine")
@EnableEurekaClient
@SpringBootApplication(scanBasePackages={"com.sunshine"},exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
@MapperScan("com.sunshine.mapper")
public class RibbonServiceApplication {

    public static void main(String[] args) {
//        System.setProperty("apollo.refreshInterval","3000");
        SpringApplication.run(RibbonServiceApplication.class, args);
    }


    @Bean
    @Order(200)
    @ConditionalOnClass(value = {RequestMappingHandlerAdapter.class})
    public SpringWebConfig springWebConfig(){
        return new SpringWebConfig();
    }
}

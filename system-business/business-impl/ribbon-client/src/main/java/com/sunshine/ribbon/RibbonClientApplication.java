package com.sunshine.ribbon;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.sunshine.configuration.ApplicationEvent.EventConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@EnableApolloConfig
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients(basePackages ={ "com.sunshine"})
@ComponentScan("com.sunshine")
@SpringBootApplication(exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
public class RibbonClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(RibbonClientApplication.class, args);
        EventConfig.EventPublisher eventPublisher = applicationContext.getBean(EventConfig.EventPublisher.class);
        eventPublisher.publishEvent(new EventConfig.MyEvent(applicationContext,"我是事件!",EventConfig.MyEventEnum.one));
    }

}

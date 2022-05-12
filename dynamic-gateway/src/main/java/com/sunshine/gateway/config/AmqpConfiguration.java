package com.sunshine.gateway.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/12 19:27
 **/
@Configuration
public class AmqpConfiguration {
    // 两个交换机
    @Bean("spring-gateway")
    public TopicExchange getTopicExchange(){
        return new TopicExchange("spring-gateway");
    }


    @Bean("spring-gateway")
    public Queue getSecondQueue(){
        return new Queue("spring-gateway");
    }
}


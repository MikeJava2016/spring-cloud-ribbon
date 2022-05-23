package com.sunshine.gateway.listener;

import com.sunshine.gateway.service.ConfigRefreshService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;
import org.springframework.cloud.alibaba.nacos.client.NacosPropertySource;
import org.springframework.cloud.alibaba.nacos.client.NacosPropertySourceLocator;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description 实现自定义nacos配置动态更新监听事件（模仿RefreshEventListener类写法，当nacos配置发生变更，会主动推送到注册配置应用，并通过RefreshEvent事件触发相关监听器事件）
 * @Author JL
 * @Date 2021/09/16
 * @Version V1.0
 */
@Component
@RabbitListener(queues = "spring-gateway")
public class AmqpGatewayConsumer {

    private static Log log = LogFactory.getLog(AmqpGatewayConsumer.class);

    @Autowired
    private ConfigRefreshService configRefreshService;

    @RabbitHandler
    public void process(String gatewayConfig){
        log.info(gatewayConfig);
        configRefreshService.setGatewayConfig(gatewayConfig);
    }

}

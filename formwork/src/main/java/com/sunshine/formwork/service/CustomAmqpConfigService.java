package com.sunshine.formwork.service;


import com.alibaba.fastjson.JSON;
import com.sunshine.common.enums.StatusUpdateEnum;
import com.sunshine.formwork.bean.GatewayNacosConfigBean;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/12 13:59
 **/
@Component
public class CustomAmqpConfigService implements CustomConfigService {

    @Autowired
    AmqpTemplate amqpTemplate;

//    String exchange, String routingKey, Object object
    @Override
    public void publishBalancedConfig(String balancedId, StatusUpdateEnum statusUpdateEnum) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setRouteId(balancedId);
        gatewayNacosConfig.setStatusUpdateEnum(statusUpdateEnum);
        gatewayNacosConfig.setType("balancedId");
        amqpTemplate.convertAndSend("spring-gateway","spring-gateway", JSON.toJSONString(gatewayNacosConfig));
    }

    @Override
    public void publishRouteConfig(String routeId, StatusUpdateEnum statusUpdateEnum) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setRouteId(routeId);
        gatewayNacosConfig.setStatusUpdateEnum(statusUpdateEnum);
        gatewayNacosConfig.setType("routeId");
        amqpTemplate.convertAndSend("spring-gateway","spring-gateway",JSON.toJSONString(gatewayNacosConfig));
    }

    @Override
    public void publishRegServerConfig(Long regServerId, StatusUpdateEnum statusUpdateEnum) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setRegServerId(regServerId);
        gatewayNacosConfig.setStatusUpdateEnum(statusUpdateEnum);
        gatewayNacosConfig.setType("regServerId");
        amqpTemplate.convertAndSend("spring-gateway","spring-gateway",JSON.toJSONString(gatewayNacosConfig));
    }

    @Override
    public void publishClientConfig(String clientId, StatusUpdateEnum statusUpdateEnum) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setClientId(clientId);
        gatewayNacosConfig.setStatusUpdateEnum(statusUpdateEnum);
        gatewayNacosConfig.setType("clientId");
        amqpTemplate.convertAndSend("spring-gateway","spring-gateway",JSON.toJSONString(gatewayNacosConfig));
    }

    @Override
    public void publishIpConfig(String ip, StatusUpdateEnum statusUpdateEnum) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setIp(ip);
        gatewayNacosConfig.setStatusUpdateEnum(statusUpdateEnum);
        gatewayNacosConfig.setType("ip");
        amqpTemplate.convertAndSend("spring-gateway","spring-gateway",JSON.toJSONString(gatewayNacosConfig));
    }

    @Override
    public void publishGroovyScriptConfig(Long groovyScriptId, StatusUpdateEnum statusUpdateEnum) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setGroovyScriptId(groovyScriptId);
        gatewayNacosConfig.setStatusUpdateEnum(statusUpdateEnum);
        gatewayNacosConfig.setType("groovyScriptId");
        amqpTemplate.convertAndSend("groovyScriptId","groovyScriptId",JSON.toJSONString(gatewayNacosConfig));
    }
}

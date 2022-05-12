package com.sunshine.formwork.service;

import com.alibaba.nacos.api.exception.NacosException;
import com.sunshine.formwork.bean.GatewayNacosConfigBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description 将自定义配置推送到nacos配置中心
 * @Author JL
 * @Date 2021/09/23
 * @Version V1.0
 */
@Slf4j
@Service
public class CustomNacosConfigService implements CustomConfigService {
    @Resource
    private NacosConfigProperties configProperties;

    /**
     * 将网关负载均衡配置推送到nacos中
     *
     * @param balancedId
     */
    @Override
    public void publishBalancedConfig(final String balancedId) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setBalancedId(balancedId);
        publishConfigToNacos(gatewayNacosConfig);
    }

    /**
     * 将网关路由配置推送到nacos中
     *
     * @param routeId
     */
    @Override
    public void publishRouteConfig(final String routeId) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setRouteId(routeId);
        publishConfigToNacos(gatewayNacosConfig);
    }

    /**
     * 将注册到网关路由的客户端服务配置推送到nacos中
     *
     * @param regServerId
     */
    @Override
    public void publishRegServerConfig(final Long regServerId) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setRegServerId(regServerId);
        publishConfigToNacos(gatewayNacosConfig);
    }

    /**
     * 将网关客户端ID推送到nacos中
     *
     * @param clientId
     */
    @Override
    public void publishClientConfig(final String clientId) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setClientId(clientId);
        publishConfigToNacos(gatewayNacosConfig);
    }

    /**
     * 将IP鉴权配置推送到nacos中
     *
     * @param ip
     */
    @Override
    public void publishIpConfig(final String ip) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setIp(ip);
        publishConfigToNacos(gatewayNacosConfig);
    }

    /**
     * 将groovyScript规则引擎动态脚本ID配置推送到nacos中
     *
     * @param groovyScriptId
     */
    @Override
    public void publishGroovyScriptConfig(final Long groovyScriptId) {
        GatewayNacosConfigBean gatewayNacosConfig = new GatewayNacosConfigBean();
        gatewayNacosConfig.setGroovyScriptId(groovyScriptId);
        publishConfigToNacos(gatewayNacosConfig);
    }

    /**
     * 将网关配置推送到nacos中, dataId 的完整格式如下：
     * {prefix}-${spring.profiles.active}.${file-extension}
     *
     * @param gatewayNacosConfig
     */
    private void publishConfigToNacos(GatewayNacosConfigBean gatewayNacosConfig) {
        String dataId = configProperties.getPrefix() + "." + configProperties.getFileExtension();
        publishConfig(dataId, configProperties.getGroup(), gatewayNacosConfig.getGatewayConfig());
    }

    /**
     * 推送配置到nacos指定dataId的group
     *
     * @param dataId
     * @param group
     * @param content
     */
    private void publishConfig(String dataId, String group, String content) {
        try {
            configProperties.configServiceInstance().publishConfig(dataId, group, content);
        } catch (NacosException e) {
            log.error("推送配置到Nacos异常！" + e.getErrMsg(), e);
        }
    }

}

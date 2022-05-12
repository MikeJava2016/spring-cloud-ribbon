package com.sunshine.formwork.service;

import com.sunshine.common.enums.StatusUpdateEnum;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/12 13:56
 **/
public interface CustomConfigService {
    /**
     * 将网关负载均衡配置推送到中
     *
     * @param balancedId
     */
    void publishBalancedConfig(final String balancedId, final StatusUpdateEnum statusUpdateEnum);

    /**
     * 将网关路由配置推送到中
     *
     * @param routeId
     */
    void publishRouteConfig(final String routeId, final StatusUpdateEnum statusUpdateEnum);

    /**
     * 将注册到网关路由的客户端服务配置推送到中
     *
     * @param regServerId
     */
    void publishRegServerConfig(final Long regServerId, final StatusUpdateEnum statusUpdateEnum);

    /**
     * 将网关客户端ID推送到中
     *
     * @param clientId
     */
    void publishClientConfig(final String clientId, final StatusUpdateEnum statusUpdateEnum);

    /**
     * 将IP鉴权配置推送到中
     *
     * @param ip
     */
    void publishIpConfig(final String ip, final StatusUpdateEnum statusUpdateEnum);

    /**
     * 将groovyScript规则引擎动态脚本ID配置推送到中
     *
     * @param groovyScriptId
     */
    void publishGroovyScriptConfig(final Long groovyScriptId, final StatusUpdateEnum statusUpdateEnum);

}

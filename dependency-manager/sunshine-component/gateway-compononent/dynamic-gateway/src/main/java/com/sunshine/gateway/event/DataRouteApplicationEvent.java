package com.sunshine.gateway.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Description 创建自定义网关路由事件（已过时，启用nacos配置监听事件，参见：NacosConfigRefreshEventListener）
 * @Author JL
 * @Date 2020/05/27
 * @Version V1.0
 */
@Deprecated
public class DataRouteApplicationEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DataRouteApplicationEvent(Object source) {
        super(source);
    }
}

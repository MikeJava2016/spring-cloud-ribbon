package com.sunshine.gateway.filter;

import com.sunshine.controller.RouteRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/6 10:26
 **/
public class SunshineGlobalFilter implements GlobalFilter, Ordered {

    private final static Logger logger = LoggerFactory.getLogger(SunshineGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info(" SunshineGlobalFilter custom global filter");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

package com.sunshine.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/6 20:08
 **/
//@Configuration
public class RateLimiterConfig {

    private final static Logger logger = LoggerFactory.getLogger(RateLimiterConfig.class);

    @Primary
    @Bean(value = "remoteAddrKeyResolver")
    public KeyResolver remoteAddrKeyResolver(){
        return exchange -> {
            String hostAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            logger.info(" 限流规则 ip {}.",hostAddress);
            return Mono.just(hostAddress);
        };
    }

    /**
     * 按照path限流
     * @return
     */
    @Bean(value = "pathAddrKeyResolver")
    public KeyResolver pathAddrKeyResolver(){
        return exchange -> {
            Route route =   exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            logger.info(" 限流规则 path {}.",route.getUri());
            return Mono.just(exchange.getRequest().getPath().toString());
        };
    }

}

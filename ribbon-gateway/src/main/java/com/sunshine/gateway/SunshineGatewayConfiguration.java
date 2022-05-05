package com.sunshine.gateway;

import com.sunshine.gateway.filter.RSAGateWayFilterFactory;
import com.sunshine.gateway.predicate.AuthRoutePredicateFactory;
import com.sunshine.gateway.route.SunshineRouteDefinitionRepository;
import com.sunshine.service.RouteService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayClassPathWarningAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 8:40
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.gateway.enabled", matchIfMissing = true)
@EnableConfigurationProperties
@AutoConfigureBefore({ HttpHandlerAutoConfiguration.class,
        WebFluxAutoConfiguration.class })
@AutoConfigureAfter({ GatewayLoadBalancerClientAutoConfiguration.class,
        GatewayClassPathWarningAutoConfiguration.class })
@ConditionalOnClass(DispatcherHandler.class)
public class SunshineGatewayConfiguration {

    @Bean
    public AuthRoutePredicateFactory authRoutePredicateFactory(){
        return new AuthRoutePredicateFactory();
    }

    @Bean
    public RSAGateWayFilterFactory rsaGateWayFilterFactory(){
        return new RSAGateWayFilterFactory();
    }

    @Bean
    public SunshineRouteDefinitionRepository routeDefinitionRepository(GatewayProperties gatewayProperties, RouteService routeService){
        return new SunshineRouteDefinitionRepository(gatewayProperties, routeService);
    }
}

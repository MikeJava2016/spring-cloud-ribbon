package com.sunshine.gateway;

import com.sunshine.gateway.filter.RSAGateWayFilterFactory;
import com.sunshine.gateway.filter.SunshineGlobalFilter;
import com.sunshine.gateway.predicate.AuthRoutePredicateFactory;
import com.sunshine.gateway.route.SunshineRouteDefinitionRepository;
import com.sunshine.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.cloud.gateway.filter.GlobalFilter;
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

    private final static Logger logger = LoggerFactory.getLogger(SunshineGatewayConfiguration.class);

    @Bean
    public AuthRoutePredicateFactory authRoutePredicateFactory(){
        AuthRoutePredicateFactory authRoutePredicateFactory = new AuthRoutePredicateFactory();
        logger.info(" create bean authRoutePredicateFactory");
        return authRoutePredicateFactory;
    }

    @Bean
    public RSAGateWayFilterFactory rsaGateWayFilterFactory(){
        RSAGateWayFilterFactory rsaGateWayFilterFactory = new RSAGateWayFilterFactory();
        logger.info(" create bean rsaGateWayFilterFactory");
        return rsaGateWayFilterFactory;
    }

    @Bean
    public SunshineRouteDefinitionRepository routeDefinitionRepository(GatewayProperties gatewayProperties, RouteService routeService){
        SunshineRouteDefinitionRepository sunshineRouteDefinitionRepository = new SunshineRouteDefinitionRepository(gatewayProperties, routeService);
        logger.info(" create bean sunshineRouteDefinitionRepository");
        return sunshineRouteDefinitionRepository;
    }

    @Bean
    public GlobalFilter customFilter() {
        SunshineGlobalFilter sunshineGlobalFilter = new SunshineGlobalFilter();
        logger.info(" create bean sunshineGlobalFilter");
        return sunshineGlobalFilter;
    }


}

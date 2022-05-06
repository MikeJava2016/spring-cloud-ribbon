package com.sunshine.gateway.route;

import com.sunshine.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 9:01
 **/
public class SunshineRouteDefinitionRepository implements RouteDefinitionRepository {

    private final static Logger logger = LoggerFactory.getLogger(SunshineRouteDefinitionRepository.class);
    private  final GatewayProperties gatewayProperties;
    private final RouteService routeService;

    public SunshineRouteDefinitionRepository(GatewayProperties gatewayProperties, RouteService routeService) {
        this.gatewayProperties = gatewayProperties;
        this.routeService = routeService;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        logger.info("SunshineRouteDefinitionRepository  getRouteDefinitions");
        // 获取缓存的配置
        List<RouteDefinition> gatewayPropertiesRoutes = this.gatewayProperties.getRoutes();
        gatewayPropertiesRoutes.addAll(routeService.getRouteList());
        return Flux.fromIterable(gatewayPropertiesRoutes);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {

        return route.flatMap(r -> {
            this.routeService.save(r);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap((id) -> {
            this.routeService.delete(id);
            return Mono.empty();
        });
    }

}

package com.sunshine.gateway.route;

import com.alibaba.fastjson.JSON;
import com.sunshine.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
//        List<RouteDefinition> routes = Collections.synchronizedList(gatewayProperties.getRoutes());
//        Set<String> routeIdSet = routes.stream().map(RouteDefinition::getId).collect(Collectors.toSet());
//        logger.info("进入了查询方法 共获取路由信息数量：{}", routes.size());
//        logger.info(JSON.toJSONString(routes));
        // 从持久化层获取自定义路由信息
//        List<RouteDefinition> routeDefinitions = routeService.getRouteList()
//                .stream()
//                .filter(x->!routeIdSet.contains(x.getId())).collect(Collectors.toList());
//        routes.addAll(routeDefinitions);
        return Flux.fromIterable(routeService.getRouteList());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        logger.info("进入了新增方法");
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

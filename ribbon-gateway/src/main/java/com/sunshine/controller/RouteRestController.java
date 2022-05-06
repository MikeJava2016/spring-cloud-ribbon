package com.sunshine.controller;

import com.alibaba.fastjson.JSON;
import com.sunshine.entity.Result;
import com.sunshine.entity.RouteForm;
import com.sunshine.service.DynamicRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 10:20
 **/
@RestControllerEndpoint(id = "sunshineRouteRest")
@RequestMapping("/api/route")
@Configuration
public class RouteRestController {

    private final static Logger logger = LoggerFactory.getLogger(RouteRestController.class);

    @Autowired
    private DynamicRouteService dynamicRouteService;

    @GetMapping
    public Result all(){
        return this.dynamicRouteService.getAll();
    }
    


    /**
     * 增加路由
     *
     * @param routeForm 路由模型
     *
     * @return
     */
    @PostMapping("/add2")
    public Flux<Boolean> add(@RequestBody RouteForm routeForm) {
        logger.info("RouteRestController add param = {} ", JSON.toJSON(routeForm));
        return this.dynamicRouteService.add(routeForm);
    }


    /**
     * 更新路由
     *
     * @param routeForm 路由模型
     * @return
     */
    @PutMapping("/put")
    public Mono<Boolean> update(@RequestBody RouteForm routeForm) {
        logger.info("RouteRestController update param = {} ", JSON.toJSON(routeForm));
        return this.dynamicRouteService.update(routeForm);
    }

    /**
     * 查询
     *
     * @param routeId 路由Id
     * @return
     */
    @GetMapping("/{routeId}")
    public Mono<RouteDefinition> get(@PathVariable String routeId) {
        logger.info("RouteRestController get routeId param = {} ", routeId);
         RouteDefinition  result = this.dynamicRouteService.getOne(routeId);
        return Mono.just(result);
    }

    /**
     * 删除路由
     *
     * @param routeId 路由Id
     * @return
     */
    @DeleteMapping("/{routeId}")
    public Mono delete(@PathVariable String routeId) {
        logger.info("RouteRestController delete routeId param = {} ", routeId);
        this.dynamicRouteService.delete(routeId);
        return Mono.just(true);
    }

    @GetMapping("/flush")
    public Mono<Void> flush() {
        dynamicRouteService.flushRoute();
        return Mono.just(null);
    }
}

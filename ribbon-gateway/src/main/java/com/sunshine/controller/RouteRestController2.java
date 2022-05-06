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

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 10:20
 **/
@RestControllerEndpoint(id = "sunshineRouteRest2")
@RequestMapping("/api/route2")
@Configuration
public class RouteRestController2 {

    private final static Logger logger = LoggerFactory.getLogger(RouteRestController2.class);

    @Autowired
    private DynamicRouteService dynamicRouteService;
    /**
     * 增加路由
     *
     * @param routeForm 路由模型
     *
     * @return
     */
    @PostMapping("/add2")
    public Result<?> add(@RequestBody RouteForm routeForm) {
        logger.info("RouteRestController2 add param = {} ", JSON.toJSON(routeForm));
        this.dynamicRouteService.add(routeForm);
        return Result.success(routeForm);
    }


    /**
     * 更新路由
     *
     * @param routeForm 路由模型
     * @return
     */
    @PutMapping("/put")
    public Result<?> update(@RequestBody RouteForm routeForm) {
        logger.info("RouteRestController2 update param = {} ", JSON.toJSON(routeForm));
        return Result.success(routeForm);
    }

    /**
     * 查询
     *
     * @param routeId 路由Id
     * @return
     */
    @GetMapping("/{routeId}")
    public Result<?> get(@PathVariable String routeId) {
        logger.info("RouteRestController2 get routeId param = {} ", routeId);
        return Result.success(routeId);
    }

    /**
     * 删除路由
     *
     * @param routeId 路由Id
     * @return
     */
    @DeleteMapping("/{routeId}")
    public Result<?> delete(@PathVariable String routeId) {
        logger.info("RouteRestController2 delete routeId param = {} ", routeId);
        return Result.success(routeId);
    }

}

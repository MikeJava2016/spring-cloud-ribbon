package com.sunshine.controller;

import com.sunshine.entity.Result;
import com.sunshine.entity.RouteForm;
import com.sunshine.service.DynamicRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 10:20
 **/
@RestController
@RequestMapping("/api/route")
public class RouteRestController {
    @Autowired
    private DynamicRouteService dynamicRouteService;



    /**
     * 增加路由
     *
     * @param routeForm 路由模型
     *
     * @return
     */
    @PostMapping
    public Result<Boolean> add(@RequestBody RouteForm routeForm) {
        return this.dynamicRouteService.add(routeForm);
    }


    /**
     * 更新路由
     *
     * @param routeForm 路由模型
     * @return
     */
    @PutMapping
    public Result update(@RequestBody RouteForm routeForm) {
        return this.dynamicRouteService.update(routeForm);
    }

    /**
     * 删除路由
     *
     * @param id 路由Id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        Result<Boolean> delete = this.dynamicRouteService.delete(id);
        return Result.success();
    }

    @GetMapping("/flush")
    public Result flush() {
        dynamicRouteService.flushRoute();
        return Result.success();
    }
}

package com.sunshine.gateway.rest;

import com.sunshine.formwork.entity.Route;
import com.sunshine.utils.Constants;
import com.sunshine.gateway.cache.RouteCache;
import com.sunshine.utils.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 触发熔断机制响应控制器
 * @Author jianglong
 * @Date 2020/05/26
 * @Version V1.0
 */
@Slf4j
@RestController
@ResponseStatus(value = HttpStatus.GATEWAY_TIMEOUT)
public class FallbackController {

    /**
     * 触发熔断机制的回调方法
     * @return
     */
    @RequestMapping(value = "/fallback", method = {RequestMethod.GET,RequestMethod.POST})
    public ApiResult fallback(@RequestParam(required = false) String routeId) {
        log.error("触发熔断机制的回调方法:fallback,routeId={}", routeId);
        return new ApiResult(Constants.FAILED,"提示：服务响应超时，触发熔断机制，请联系运维人员处理。此消息由网关服务返回！",null);
    }

    /**
     * 触发自定义熔断机制的回调方法
     * @return
     */
    @RequestMapping(value = "/fallback/custom", method = {RequestMethod.GET,RequestMethod.POST})
    public ApiResult fallbackCustom(@RequestParam String routeId) {
        log.error("触发自定义熔断机制的回调方法:fallback,routeId={}", routeId);
        Route route =  RouteCache.get(routeId);
        if (route != null){
            return new ApiResult(Constants.FAILED,"提示：" + route.getFallbackMsg(),null);
        }
        return new ApiResult(Constants.FAILED,"提示：服务响应超时，触发自定义熔断机制，请联系运维人员处理。此消息由网关服务返回！",null);
    }

}

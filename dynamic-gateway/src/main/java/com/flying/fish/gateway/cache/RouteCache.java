package com.flying.fish.gateway.cache;

import com.flying.fish.formwork.entity.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 缓存请求服务路由信息
 * @Author jianglong
 * @Date 2020/05/26
 * @Version V1.0
 */
public class RouteCache {

    private static ConcurrentHashMap<String,Route> cacheMap = new ConcurrentHashMap<>();

    public static void put(final String key,final Route value){
        Assert.notNull(key, "hash map key cannot is null");
        Assert.notNull(value, "hash map value cannot is null");
        cacheMap.put(key, value);
    }

    public static Route get(final String key){
        return cacheMap.get(key);
    }

    public static synchronized void remove(final String key){
        if (cacheMap.containsKey(key)){
            cacheMap.remove(key);
        }
    }

    public static synchronized void clear(){
        cacheMap.clear();
    }

    public static  Route convert(RouteDefinition routeDefinition){
        Route route = new Route();
        route.setId(routeDefinition.getId());
        route.setName(routeDefinition.getId());
        route.setSystemCode(routeDefinition.getId());
        route.setGroupCode("other_api");
        route.setUri(String.valueOf(routeDefinition.getUri()));
        route.setPath(routeDefinition.getUri().getPath());
//        route.getMethod(routeDefinition.get);
//        route.setHost();
//        route.setRemoteAddr();
//        route.setHeader();
//        route.setFilterGatewaName();
//        route.setFilterHystrixName();
//        route.setFilterRateLimiterName();
//        route.setFilterAuthorizeName();
//        route.setFallbackMsg();
//        route.setFallbackTimeout();
//        route.setReplenishRate();
//        route.setBurstCapacity();
//        route.setWeightName();
//        route.setWeight();
//        route.setStatus();
        route.setStripPrefix(1);
//        route.setRequestParameter();
//        route.setRewritePath();
//        route.setAccessHeader();
//        route.setAccessIp();
//        route.setAccessParameter();
//        route.setAccessTime();
//        route.setAccessCookie();
        return route;
    }
}

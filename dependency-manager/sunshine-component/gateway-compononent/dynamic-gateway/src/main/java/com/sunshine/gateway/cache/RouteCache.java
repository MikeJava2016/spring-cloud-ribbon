package com.sunshine.gateway.cache;

import com.sunshine.formwork.entity.Route;
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

}

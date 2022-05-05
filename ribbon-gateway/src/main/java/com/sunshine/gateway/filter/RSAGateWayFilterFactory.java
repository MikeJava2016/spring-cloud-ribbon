package com.sunshine.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import java.net.URI;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 8:47
 **/
public class RSAGateWayFilterFactory extends AbstractGatewayFilterFactory {
    @Override
    public GatewayFilter apply(Object config) {
        //返回Gateway对象
        return (exchange, chain) -> {
            MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();

            //获取参数
            System.out.println(queryParams.get("a"));
            System.out.println("你好");

            //获取URI
            URI newUri = exchange.getRequest().getURI();
            String rawPath = newUri.getRawPath();
            System.out.println(rawPath);

            rawPath = rawPath + "/2";
            //修改URI
            ServerHttpRequest build = exchange.getRequest().mutate().path(rawPath).build();

            return chain.filter(exchange.mutate().request(build).build());
        };
    }
}

package com.sunshine.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunshine.api.feign.service.AuthLoginClientAPI;
import com.sunshine.common.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 8:45
 **/
public class AuthLoginGlobalFilter implements GlobalFilter, Ordered {

    private final List<String> ignoreUrls;

    private final AuthLoginClientAPI authLoginClientAPI;

    private final static Logger logger = LoggerFactory.getLogger(AuthLoginGlobalFilter.class);

    public AuthLoginGlobalFilter(List<String> ignoreUrls, AuthLoginClientAPI authLoginClientAPI) {
        this.ignoreUrls = ignoreUrls;
        this.authLoginClientAPI = authLoginClientAPI;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (!CollectionUtils.isEmpty(ignoreUrls) && ignoreUrls.contains(request.getPath().value())) {
            return chain.filter(exchange);
        }
        String access_token = request.getHeaders().getFirst("access_token");

        if (StringUtils.isEmpty(access_token)) {
            return onError(exchange, "尚未登录");
        }

        Result<String> result = authLoginClientAPI.validToken(access_token);
        if (!result.isSuccess()) {
            return onError(exchange, result.getMessage());
        }
        authLoginClientAPI.refreshToken(access_token);
        ServerHttpRequest newRequest = request.mutate().header("uid", result.getData()).build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    private Mono<Void> onError(final ServerWebExchange exchange, final String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        Result rd = Result.fail(HttpStatus.UNAUTHORIZED.value(), message);
        ObjectMapper objectMapper = new ObjectMapper();
        String rs = "";
        try {
            rs = objectMapper.writeValueAsString(rd);
        } catch (JsonProcessingException e) {
            logger.error("occur Exception:" + e);
        }
        DataBuffer buffer = response.bufferFactory().wrap(rs.getBytes());
        return response.writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

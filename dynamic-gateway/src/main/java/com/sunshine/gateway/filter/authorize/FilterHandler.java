package com.sunshine.gateway.filter.authorize;

import com.sunshine.formwork.entity.Route;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * @Description 责任链设计模式，抽象业务父类
 * @Author jianglong
 * @Date 2020/05/25
 * @Version V1.0
 */
public abstract class FilterHandler {

    private FilterHandler handler = null;
    private Route route;

    public FilterHandler(FilterHandler handler, Route route) {
        this.handler = handler;
        this.route = route;
    }


    public FilterHandler(FilterHandler handler) {
        this.handler = handler;
    }

    public FilterHandler() {
    }

    public void handler(ServerHttpRequest request, Route route){
        this.route = route;
//        filter处理逻辑
        handleRequest(request);
//        下一处理
        nextHandle(request);
    }

    /**
     * filter处理逻辑
     * @param request
     */
    public abstract void handleRequest(ServerHttpRequest request);

    public void nextHandle(ServerHttpRequest request){
        if (handler != null){
            handler.handler(request,route);
        }
    }

    public FilterHandler getHandler() {
        return handler;
    }

    public Route getRoute() {
        return route;
    }
}

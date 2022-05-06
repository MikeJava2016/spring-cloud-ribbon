package com.sunshine.service;

import com.alibaba.fastjson.JSON;
import com.sunshine.mapper.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 10:23
 **/
@Service
public class RouteService {

    @Autowired
    private RouteMapper routeMapper;


    public List<RouteDefinition> getRouteList() {
        List<RouteInfo> list = list();
        return list.stream().map(x -> getRouteDefinition(x)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private RouteDefinition getRouteDefinition(RouteInfo x) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(x.getRouteId());
        routeDefinition.setPredicates(JSON.parseArray(x.getPredicates(), PredicateDefinition.class));
        routeDefinition.setFilters(JSON.parseArray(x.getFilters(), FilterDefinition.class));
        try {
            routeDefinition.setUri(new URI(x.getUri()));
            routeDefinition.setOrder(x.getOrderNum());
            routeDefinition.setMetadata(new HashMap<>());
            return routeDefinition;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<RouteInfo> list() {

        // 添加缓存
        return this.routeMapper.selectAll();
    }

    public void save(RouteInfo routeInfo) {
        this.routeMapper.insert(routeInfo);
    }

    public void save(RouteDefinition routeDefinition) {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setRouteId(routeDefinition.getId());
        routeInfo.setPredicates(JSON.toJSONString(routeDefinition.getPredicates()));
        routeInfo.setFilters(JSON.toJSONString(routeDefinition.getFilters()));
        routeInfo.setUri(routeDefinition.getUri().toString());
        routeInfo.setOrderNum(routeDefinition.getOrder());
        routeInfo.setAppId("1");
        this.save(routeInfo);
    }

    public void delete(String routeId) {
        this.routeMapper.delete(routeId);
    }

    public RouteDefinition getOne(String routeId) {
        RouteInfo x = this.routeMapper.getOne(routeId);
        return getRouteDefinition(x);
    }
}

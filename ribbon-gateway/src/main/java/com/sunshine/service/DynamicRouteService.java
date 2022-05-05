package com.sunshine.service;

import com.alibaba.fastjson.JSON;
import com.sunshine.entity.Result;
import com.sunshine.entity.RouteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 10:22
 **/
@Component
public class DynamicRouteService implements ApplicationEventPublisherAware {

    @Autowired
    private   RouteDefinitionWriter routeDefinitionWriter;
    @Autowired
    private   RouteService routeService;

    private ApplicationEventPublisher publisher;




    /**
     * 增加路由
     *
     * @param routeForm
     * @return
     */
    public Result<Boolean> add(RouteForm routeForm) {
        RouteDefinition definition = convert(routeForm);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        System.out.println(JSON.toJSONString(definition));
        enduranceRule(routeForm.getName(),definition);
        return Result.success(true);
    }

    /**
     * 更新路由
     *
     * @param routeForm
     * @return
     */
    public Result<Boolean> update(RouteForm routeForm) {
        RouteDefinition definition = convert(routeForm);
        try {
            delete(definition.getId());
        } catch (Exception e) {
            return Result.fail("未知路由信息");
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return Result.success(true);
        } catch (Exception e) {
            return Result.fail("路由信息修改失败!");
        }
    }

    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    public Result<Boolean> delete(String id) {
        Mono<ResponseEntity<Object>> responseEntityMono = this.routeDefinitionWriter.delete(Mono.just(id))
                .then(Mono.defer(() -> {
                    Mono<ResponseEntity<Object>> just = Mono.just(ResponseEntity.ok().build());
                    //todo 删除数据库数据
                    routeService.delete(id);
                    return just;
                }))
                .onErrorReturn(ResponseEntity.notFound().build());
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return Result.success();
    }


    public void flushRoute() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 数据落库
     */
    public void enduranceRule(String name, RouteDefinition definition) {
        String id = definition.getId();
        List<PredicateDefinition> predicates = definition.getPredicates();
        List<FilterDefinition> filters = definition.getFilters();
        int order = definition.getOrder();
        URI uri = definition.getUri();
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setName(name);
        routeInfo.setRouteId(id);
        routeInfo.setUri(uri.toString());
        routeInfo.setPredicates(JSON.toJSONString(predicates));
        routeInfo.setFilters(JSON.toJSONString(filters));
        routeInfo.setEnabled(1);
        routeInfo.setDescription("aaa");
        routeInfo.setOrderNum(order);
        routeInfo.setDeleteFlag(false);
        routeService.save(routeInfo);
    }


    /**
     * 把自定义请求模型转换为
     *
     * @param form
     * @return
     */
    private RouteDefinition convert(RouteForm form) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(form.getId());
        definition.setOrder(form.getOrder());
        //设置断言
        List<PredicateDefinition> predicateDefinitions = form.getPredicates().stream().distinct().map(predicateInfo -> {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(predicateInfo.getArgs());
            predicate.setName(predicateInfo.getName());
            return predicate;
        }).collect(Collectors.toList());
        definition.setPredicates(predicateDefinitions);

        // 设置过滤
        List<FilterDefinition> filterList = form.getFilters().stream().distinct().map(x -> {
            FilterDefinition filter = new FilterDefinition();
            filter.setName(x.getName());
            filter.setArgs(x.getArgs());
            return filter;
        }).collect(Collectors.toList());
        definition.setFilters(filterList);
        // 设置URI,判断是否进行负载均衡
        URI uri;
        if (form.getUri().startsWith("http")) {
            uri = UriComponentsBuilder.fromHttpUrl(form.getUri()).build().toUri();
        } else {
            uri = URI.create(form.getUri());
        }
        definition.setUri(uri);
        return definition;
    }
}

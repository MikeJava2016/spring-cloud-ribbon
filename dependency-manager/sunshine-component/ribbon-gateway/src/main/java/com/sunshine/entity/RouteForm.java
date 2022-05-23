package com.sunshine.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * @author lei
 * @create 2022-04-11 15:24
 * @desc 路由模型
 **/

public class RouteForm {

    private String id;

    private String name;

    private List<PredicateInfo> predicates = new ArrayList<>();


    private List<FilterInfo> filters = new ArrayList<>();


    private String uri;

    private int order = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PredicateInfo> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<PredicateInfo> predicates) {
        this.predicates = predicates;
    }

    public List<FilterInfo> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterInfo> filters) {
        this.filters = filters;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

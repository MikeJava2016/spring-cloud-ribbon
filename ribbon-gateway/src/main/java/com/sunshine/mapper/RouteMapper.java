package com.sunshine.mapper;

import com.sunshine.service.RouteInfo;

import java.util.List;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/5 12:36
 **/
public interface RouteMapper {
    int insert(RouteInfo routeInfo);

    List<RouteInfo> selectAll();

    int delete(String routeId);

    RouteInfo getOne(String routeId);
}

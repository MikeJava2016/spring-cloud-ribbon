package com.sunshine.formwork.bean;

import com.sunshine.formwork.entity.Monitor;
import com.sunshine.formwork.entity.Route;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description
 * @Author JL
 * @Date 2021/04/16
 * @Version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RouteRsp extends Route {
    private Monitor monitor;
}

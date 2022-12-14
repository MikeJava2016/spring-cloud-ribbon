package com.sunshine.formwork.bean;

import lombok.Data;

/**
 * @Description 过滤器开关
 * @Author jianglong
 * @Date 2020/05/14
 * @Version V1.0
 */
@Data
public class RouteFilterBean {
    private Boolean ipChecked;
    private Boolean tokenChecked;
    private Boolean idChecked;
}

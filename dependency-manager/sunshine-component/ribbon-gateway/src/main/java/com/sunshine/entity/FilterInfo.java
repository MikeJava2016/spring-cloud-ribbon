package com.sunshine.entity;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lei
 * @create 2022-04-11 15:22
 * @desc 过滤模型
 **/
public class FilterInfo {

    private String name;

    private Map<String, String> args = new LinkedHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}

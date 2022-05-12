package com.sunshine.common.enums;

public enum StatusUpdateEnum {
    INSERT, UPDATE,DELETE,START,STOP;

    public static void main(String[] args) {
        System.out.println(StatusUpdateEnum.INSERT.name());
        System.out.println(StatusUpdateEnum.valueOf(StatusUpdateEnum.INSERT.name()));
    }
}

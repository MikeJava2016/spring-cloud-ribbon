package com.sunshine.common.enums;

public enum LoginTypeEnum {
    NORMAL(0, "帐号密码登录"),
    PHONE_PWD(1, "手机号与密码登录"),
    PHONE_CODE(2, "手机验证码登录"),
    WECHAT(3, "微信授权登录");

    private int code;
    private String description;

    LoginTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

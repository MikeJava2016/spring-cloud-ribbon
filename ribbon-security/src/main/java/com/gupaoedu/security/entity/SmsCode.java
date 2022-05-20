package com.gupaoedu.security.entity;

import org.joda.time.LocalDateTime;

/**
 * @version v1
 * @Description 短信验证码类
 * @Author huzhanglin
 * @Date 2022/5/20 21:13
 **/
public class SmsCode {

    private String phoneNumber;
    private String code;
    private LocalDateTime expireTime;

    public SmsCode() {
    }

    public SmsCode(String phoneNumber, String code, LocalDateTime expireTime) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpired() {
        return  LocalDateTime.now().isAfter(expireTime);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

}

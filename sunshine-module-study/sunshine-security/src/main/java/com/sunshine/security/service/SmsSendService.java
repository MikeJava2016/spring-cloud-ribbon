package com.sunshine.security.service;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/17 22:27
 **/

import javax.servlet.http.HttpServletRequest;

/**
 * 短信发送接口
 */
public interface SmsSendService {
    /**
     * 短信发送
     * @param PhoneNumbers 手机号
     * @return
     */
    int sendSms(String PhoneNumbers, HttpServletRequest request);
}
package com.sunshine.security.controller;

import com.sunshine.common.util.Result;
import com.sunshine.security.entity.SmsCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDateTime;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 21:14
 **/
@Controller
@Slf4j
public class SmsController {

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/smscode")
    @ResponseBody
    public Result<String> sms(@RequestParam(required = true) String mobile, HttpSession session) {
        //这里我偷懒了，具体实现可以调用短信验证提供商的api
        SmsCode smsCode = new SmsCode(mobile, RandomStringUtils.randomNumeric(6), LocalDateTime.now().plusSeconds(60));
        String smsKey = "smscode:" + mobile;
        redissonClient.getBucket(smsKey).set(smsCode.getCode(), 2000, TimeUnit.MINUTES);
        log.info(smsKey + "的验证码是：" + smsCode.getCode());
        return Result.success("发送验证码成功");

    }
}

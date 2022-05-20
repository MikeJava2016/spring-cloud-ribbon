package com.gupaoedu.security.controller;

import com.gupaoedu.security.entity.SmsCode;
import com.gupaoedu.security.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 21:14
 **/
@Controller
public class SmsController {

    @Autowired
    private UserService userService;

    @GetMapping("/smscode")
    @ResponseBody
    public Map<String,String> sms(@RequestParam String mobile, HttpSession session) {
        Map<String,String> map = new HashMap<>(16);
        if(null == userService.queryByPhoneNumber(mobile)){
            map.put("jsonmsg","手机号未注册");

        }else{
            //这里我偷懒了，具体实现可以调用短信验证提供商的api
            SmsCode smsCode = new SmsCode(mobile, RandomStringUtils.randomNumeric(6), LocalDateTime.now().plusSeconds(60));
            session.setAttribute("sms_key",smsCode);
            System.out.println(smsCode.getCode());
            map.put("jsonmsg","发送验证码成功");
        }
        return map;
    }
}

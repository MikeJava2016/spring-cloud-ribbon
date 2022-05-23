package com.sunshine.security.service.impl;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/17 22:28
 **/

import com.sunshine.security.service.SmsSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 发送短信验证码
 */
//@Service
public class SmsSendServiceImpl implements SmsSendService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int sendSms(String PhoneNumbers, HttpServletRequest request) {

        //1.生成一个手机短信验证码
        String code = "1234";
//        String code = RandomUtil.randomNumbers(4);
        //2.将验证码发到session中
        HttpSession session = request.getSession();
        session.setAttribute("sms_code",code);
        //3.发送短信
       /* SendSmsResponse response = toSendSms(PhoneNumbers, code);
        logger.info("向手机号" + PhoneNumbers + "发送的验证码为：:" + code);
        if (response.getCode().equals("ok")){
            return Result.ok("获取验证码成功");
        }else {
            return Result.build(500,"获取验证码失败");
        }*/
        return 0;
    }
}

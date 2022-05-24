package com.sunshine.security.config.phoneNumber;

import com.alibaba.fastjson.JSONObject;
import com.sunshine.common.util.web.HttpRequestUtil;
import com.sunshine.common.util.web.PropertyUtils;
import com.sunshine.security.config.common.CommonSpringSecurity;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version v1
 * @Description 只是校验验证码是否正确
 * @Author huzhanglin
 * @Date 2022/5/20 21:24
 **/
@Component
public class SmsCodeValidateFilter extends OncePerRequestFilter {

    @Autowired
    private RedissonClient redissonClient;

    private final String pattern;
    private final String httpMethod;

    public SmsCodeValidateFilter() {
        this.pattern =   PropertyUtils.getPropertiesValue("spring-security.properties", "sms_login_path", "/sms/login");
        this.httpMethod = "POST";
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return false;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {
        if (StringUtils.equals(pattern, request.getRequestURI())
                && StringUtils.equalsIgnoreCase(request.getMethod(), httpMethod)) {
            logger.info("开始校验手机验证码。。。");
            try {
                //验证谜底与用户输入是否匹配
                this.validateSmsCode(request);
            } catch (AuthenticationException e) {

                CommonSpringSecurity.AUTHENTICATION_FAILURE_HANDLER.onAuthenticationFailure(request, response, e);
                return;
            }
            logger.info("结束校验手机验证码。。。");
        }
        filterChain.doFilter(request, response);

    }

    private void validateSmsCode(HttpServletRequest request) throws AuthenticationException {
        Logger logger = LoggerFactory.getLogger(getClass());
        String requestJson = HttpRequestUtil.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(requestJson);
        String mobileInRequest = jsonObject.getString("mobile");
        String codeInRequest =  this.getSmsCode(jsonObject,"smsCode");

        if (StringUtils.isEmpty(mobileInRequest)) {
            logger.error("SmsCodeValidateFilter--->" + "手机号码不能为空");
            throw new UsernameNotFoundException("手机号码不能为空");
        }

        if (StringUtils.isEmpty(codeInRequest)) {
            logger.error("SmsCodeValidateFilter--->" + "短信验证码不能为空");
            throw new UsernameNotFoundException("短信验证码不能为空");
        }

         /*

        if (!codeInSession.getPhoneNumber().equals(mobileInRequest)) {
            logger.error("SmsCodeValidateFilter--->" + "短信发送目标与您输入的手机号不一致");
            throw new SessionAuthenticationException("短信发送目标与您输入的手机号不一致");
        }*/
        RBucket<String> bucket = redissonClient.getBucket("smscode" + ":" + mobileInRequest);
        String cacheSmsCode = bucket.get();
        if (cacheSmsCode ==null){
            logger.error("SmsCodeValidateFilter--->" + "短信验证码无效");
            throw new SessionAuthenticationException("短信验证码无效,请从新获取");
        }
        if (!codeInRequest.equals(cacheSmsCode)){
            logger.error("SmsCodeValidateFilter--->" + "短信验证码不正确");
            throw new SessionAuthenticationException("短信验证码不正确");
        }
    }

    private String getSmsCode(JSONObject jsonObject, String smsCodeParameter) {
        String mobile = jsonObject.getString(smsCodeParameter);

        Assert.hasText(smsCodeParameter, "smsCode parameter must not be empty or null");

        return mobile;
    }
}

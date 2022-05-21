package com.gupaoedu.security.config.phoneNumber;

import com.alibaba.fastjson.JSONObject;
import com.gupaoedu.security.entity.SmsCode;
import com.gupaoedu.security.service.UserService;
import com.sunshine.common.util.web.HttpRequestUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 21:24
 **/
@Component
public class SmsCodeValidateFilter extends OncePerRequestFilter {

    private final String pattern;
    private final String httpMethod;

    public SmsCodeValidateFilter( ) {
        this.pattern = "/sms/login";
        this.httpMethod = "POST";
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    private AuthenticationFailureHandler publicAuthenticationFailureHandler
            = (request, response, exception) -> {
                String mes = exception.getMessage();
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(mes);
                out.flush();
                out.close();
            };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {
        if (StringUtils.equals(pattern, request.getRequestURI())
                && StringUtils.equalsIgnoreCase(request.getMethod(),httpMethod)) {
            logger.info("开始校验手机号。。。");
            try {
                //验证谜底与用户输入是否匹配
                this.validate(request);
            } catch (AuthenticationException e) {
                publicAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }

        }
        filterChain.doFilter(request, response);

    }

    private void validate(HttpServletRequest request) throws SessionAuthenticationException {
        Logger logger = LoggerFactory.getLogger(getClass());
        HttpSession session = request.getSession();
        SmsCode codeInSession = (SmsCode) session.getAttribute("sms_key");
        String requestJson = HttpRequestUtil.getRequestBody(request);
        JSONObject jsonObject = JSONObject.parseObject(requestJson);
        String mobileInRequest = jsonObject.getString("mobile");
        String codeInRequest = jsonObject.getString("smsCode");

        codeInSession = new SmsCode(mobileInRequest,codeInRequest, LocalDateTime.now().plusYears(3));

        logger.info("SmsCodeValidateFilter--->" + codeInSession.toString() + mobileInRequest + codeInRequest);

        if (StringUtils.isEmpty(mobileInRequest)) {
            logger.error("SmsCodeValidateFilter--->" + "手机号码不能为空");
            throw new SessionAuthenticationException("手机号码不能为空");
        }

        if (StringUtils.isEmpty(codeInRequest)) {
            logger.error("SmsCodeValidateFilter--->" + "短信验证码不能为空");
            throw new SessionAuthenticationException("短信验证码不能为空");
        }

        if (Objects.isNull(codeInSession)) {
            logger.error("SmsCodeValidateFilter--->" + "短信验证码不存在");
            throw new SessionAuthenticationException("短信验证码不存在");
        }

        if (codeInSession.isExpired()) {
            session.removeAttribute("sms_key");
            logger.error("SmsCodeValidateFilter--->" + "短信验证码已经过期");
            throw new SessionAuthenticationException("短信验证码已经过期");
        }

        if (!codeInSession.getCode().equals(codeInRequest)) {
            logger.error("SmsCodeValidateFilter--->" + "短信验证码不正确");
            throw new SessionAuthenticationException("短信验证码不正确");
        }

        if (!codeInSession.getPhoneNumber().equals(mobileInRequest)) {
            logger.error("SmsCodeValidateFilter--->" + "短信发送目标与您输入的手机号不一致");
            throw new SessionAuthenticationException("短信发送目标与您输入的手机号不一致");
        }

        User user = userService.queryByPhoneNumber(mobileInRequest);
        if (Objects.isNull(user)) {
            logger.error("SmsCodeValidateFilter--->" + "您输入的手机号不是系统的注册用户");
            throw new SessionAuthenticationException("您输入的手机号不是系统的注册用户");
        }
        session.removeAttribute("sms_key");

    }
}

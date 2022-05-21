package com.sunshine.security.config.common;

import cn.hutool.json.JSONUtil;
import com.sunshine.common.util.JsonMapper;
import com.sunshine.common.util.web.PropertyUtils;
import com.sunshine.security.config.phoneNumber.SmsCodeAuthenticationSecurityConfig;
import com.sunshine.security.config.phoneNumber.SmsCodeAuthenticationToken;
import com.sunshine.common.util.JwtUtils;
import com.sunshine.common.util.Result;
import com.sunshine.common.util.web.ApplicationContextUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/21 11:03
 **/
public class CommonSpringSecurity {

    private static final Logger logger = LoggerFactory.getLogger(CommonSpringSecurity.class);

    private static final String token_invalidate = PropertyUtils.getPropertiesValue("spring-security.properties", "token_invalidate", "2");
    /**
     * 登录失
     */
    public static final AuthenticationFailureHandler AUTHENTICATION_FAILURE_HANDLER = (req, resp, e) -> {
        String mes = "";
        logger.error("登录失败", e);
        mes = SmsCodeAuthenticationSecurityConfig.getString(e, mes);
        logger.error("error:", e);
        failResponse(resp, mes, null, null);
    };


    /**
     * 登录成功
     */
    public static final AuthenticationSuccessHandler AUTHENTICATION_SUCCESS_HANDLER = (req, resp, authentication) -> {
        Object principal = authentication.getPrincipal();
        String uid = "0";
        if (authentication instanceof SmsCodeAuthenticationToken) {
            SmsCodeAuthenticationToken smsCodeAuthenticationToken = (SmsCodeAuthenticationToken) authentication;
            uid = smsCodeAuthenticationToken.getUid();
        }
        String token = JwtUtils.createToken(uid, "security", null);
        RedissonClient redissonClient = ApplicationContextUtils.getBean(RedissonClient.class);
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        redissonClient.getBucket(token).set(JsonMapper.toJsonString(authentication1), Integer.parseInt(token_invalidate), TimeUnit.MINUTES);
        successResponse(resp, "登录成功", token, null);
    };
    /**
     * 注销成功
     */
    public static final LogoutSuccessHandler LOGOUT_SUCCESS_HANDLER = (req, resp, authentication) -> {
        successResponse(resp, "注销成功", null, null);
    };

    /**
     *
     */
    public static final AuthenticationEntryPoint AUTHENTICATION_ENTRY_POINT = (req, resp, authException) -> {
        failResponse(resp, "尚未登录，请先登录", null, null);
    };


    /**
     * 写入数据
     *
     * @param resp
     * @param mes
     * @throws IOException
     */
    public static void writeJson(int code, HttpServletResponse resp, String mes, String token, Object data) throws IOException {
        Result<Object> result = new Result<>();
        result.code(0).message(mes).token(token).data(data);
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String repsonse = JSONUtil.toJsonStr(result);
        logger.info(repsonse);
        out.write(repsonse);
        out.flush();
        out.close();
    }

    /**
     * 写入数据
     *
     * @param resp
     * @param mes
     * @throws IOException
     */
    public static void successResponse(HttpServletResponse resp, String mes, String token, Object data) throws IOException {
        writeJson(0, resp, mes, token, data);
    }

    /**
     * 写入数据
     *
     * @param resp
     * @param mes
     * @throws IOException
     */
    public static void successResponse(HttpServletResponse resp, String mes, String token) throws IOException {
        writeJson(0, resp, mes, token, null);
    }

    /**
     * 写入数据
     *
     * @param resp
     * @param mes
     * @throws IOException
     */
    public static void failResponse(HttpServletResponse resp, String mes, String token, Object data) throws IOException {
        writeJson(-1, resp, mes, token, data);
    }

    /**
     * 写入数据
     *
     * @param resp
     * @param mes
     * @throws IOException
     */
    public static void failResponse(HttpServletResponse resp, String mes) throws IOException {
        writeJson(-1, resp, mes, null, null);
    }
}

package com.sunshine.security.config.common;

import cn.hutool.json.JSONUtil;
import com.sunshine.common.util.JsonMapper;
import com.sunshine.common.util.Result;
import com.sunshine.common.util.web.ApplicationContextUtils;
import com.sunshine.common.util.web.PropertyUtils;
import com.sunshine.security.config.phoneNumber.PhoneNumberAuthenticationToken;
import com.sunshine.utils.pwd.JwtUtils;
import org.joda.time.Duration;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
     * 带token的跨域访问完美解决
     * "/**"
     * @return
     */
    public static final CorsConfigurationSource corsConfigurationSource(String path) {
        CorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");    //同源配置，*表示任何请求都视为同源，若需指定ip和端口可以改为如“localhost：8080”，多个以“，”分隔；
        corsConfiguration.addAllowedHeader("*");//header，允许哪些header，本案中使用的是token，此处可将*替换为token；
        corsConfiguration.addAllowedMethod("*");    //允许的请求方法，PSOT、GET等
        ((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration(path, corsConfiguration); //配置允许跨域访问的url
        return source;
    }

    public static String getString(AuthenticationException e, String mes) {
        if (e instanceof LockedException) {
            mes = "账户被锁定，请联系管理员!";
        } else if (e instanceof CredentialsExpiredException) {
            mes = "密码过期，请联系管理员!";
        } else if (e instanceof AccountExpiredException) {
            mes = "账户过期，请联系管理员!";
        } else if (e instanceof DisabledException) {
            mes = "账户被禁用，请联系管理员!";
        } else if (e instanceof BadCredentialsException) {
            mes = "用户名或者密码输入错误，请重新输入!";
        }else {
            mes = e.getMessage();
        }
        return mes;
    }

    /**
     * 登录失
     */
    public static final AuthenticationFailureHandler AUTHENTICATION_FAILURE_HANDLER = (req, resp, e) -> {
        String mes = "";
        logger.error("登录失败", e);
        e.printStackTrace();
        mes = getString(e, mes);
        logger.error("error:", e);
        failResponse(resp, mes, null, null);
    };


    /**
     * 登录成功
     */
    public static final AuthenticationSuccessHandler AUTHENTICATION_SUCCESS_HANDLER = (req, resp, authentication) -> {
        logger.info("登录成功");
        Object principal = authentication.getPrincipal();
        String uid = "0";
        if (authentication instanceof PhoneNumberAuthenticationToken) {
            PhoneNumberAuthenticationToken phoneNumberAuthenticationToken = (PhoneNumberAuthenticationToken) authentication;
            uid = phoneNumberAuthenticationToken.getUid();
        }

        String jwtToken = JwtUtils.createToken(uid, "admin", Duration.standardHours(2));
        logger.info(" jjwt 加密 token = {}",jwtToken);
        RedissonClient redissonClient = ApplicationContextUtils.getBean(RedissonClient.class);
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        redissonClient.getBucket(jwtToken).set(JsonMapper.toJsonString(authentication1), Integer.parseInt(token_invalidate), TimeUnit.MINUTES);
        successResponse(resp, "登录成功", jwtToken, null);
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
        logger.error(authException.getMessage());
        authException.printStackTrace();
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
        result.code(code).message(mes).token(token).data(data);
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

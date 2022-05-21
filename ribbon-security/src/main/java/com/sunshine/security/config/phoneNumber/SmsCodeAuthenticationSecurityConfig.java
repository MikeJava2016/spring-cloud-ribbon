package com.sunshine.security.config.phoneNumber;

import com.sunshine.security.config.common.CommonSpringSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 21:43
 **/
@Component("smsCodeAuthenticationSecurityConfig")
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("smsCodeUserDetailsService")
    private SmsCodeUserDetailsService smsCodeUserDetailsService;


    @Override
    public void configure(HttpSecurity http) {
        // 短信登录的请求 post 方式的 /sms/login
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter("/sms/login", "POST");
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        // 处理成功
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(CommonSpringSecurity.AUTHENTICATION_SUCCESS_HANDLER);
        //登录失败响应
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(CommonSpringSecurity.AUTHENTICATION_FAILURE_HANDLER);

        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailService(smsCodeUserDetailsService);
        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


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
        }
        return mes;
    }
}

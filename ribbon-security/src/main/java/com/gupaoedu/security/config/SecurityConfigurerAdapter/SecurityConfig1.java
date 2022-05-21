package com.gupaoedu.security.config.SecurityConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gupaoedu.security.config.phoneNumber.SmsCodeAuthenticationSecurityConfig;
import com.gupaoedu.security.config.phoneNumber.SmsCodeValidateFilter;
import com.gupaoedu.security.web.filter.ReadRequestBodyFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.io.PrintWriter;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 12:34
 * 如果登录地址是 /foo/login，那么通过 sang 和 javaboy 两个用户可以登录成功。
 * 如果登录地址是 /bar/login，那么通过 sang 和 江南一点雨 两个用户可以登录成功。
 **/
@Configuration
@EnableWebSecurity
public class SecurityConfig1 extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SmsCodeValidateFilter smsCodeValidateFilter;

    /*@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("123").roles("admin");
    }*/

    /**
     * 用来配置忽略掉的 URL 地址，一般对于静态文件，我们可以采用此操作。
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**","/phone.html","/smscode");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new ReadRequestBodyFilter(), WebAsyncManagerIntegrationFilter.class);
        // 添加拦截器
        http.addFilterBefore(smsCodeValidateFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .anyRequest().authenticated()

                .and()// 表示结束当前标签，上下文回到HttpSecurity，开启新一轮的配置。
                .formLogin()// 表示登录相关的页面/接口不要被拦截。
                .loginPage("/login.html")
                .permitAll()
                // 登录成功
                .successHandler((req, resp, authentication) -> {
                    logger.info("登录成功");
                    Object principal = authentication.getPrincipal();
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write(new ObjectMapper().writeValueAsString(principal));
                    out.flush();
                    out.close();
                })
                //  登录失败
                .failureHandler((req, resp, e) -> {
                    String mes = "";
                    logger.error("登录失败", e);
                    mes = SmsCodeAuthenticationSecurityConfig.getString(e, mes);
                    logger.error("error:", e);
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write(mes);
                    out.flush();
                    out.close();
                })
//        注销登录
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((req, resp, authentication) -> {
                    logger.info("注销成功");
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write("注销成功");
                    out.flush();
                    out.close();
                })
                .permitAll()

                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, authException) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write("尚未登录，请先登录");
                            out.flush();
                            out.close();
                        });
         http.apply(smsCodeAuthenticationSecurityConfig);
    }

    @Bean
    HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        你希望请求地址中可以出现 //
        firewall.setAllowUrlEncodedDoubleSlash(false);
//        如果请求地址中出现 %，这个请求也将被拒绝。URL 编码后的 % 是 %25，所以 %25 也不能出现在 URL 地址中。
        firewall.setAllowUrlEncodedPercent(true);

        firewall.setAllowBackSlash(true);
        firewall.setAllowUrlEncodedSlash(true);
//        请求地址中存在 . 编码之后的字符 %2e、%2E,则请求将被拒绝。
//        firewall.setAllowUrlEncodedPeriod(true);
        return firewall;
    }

}

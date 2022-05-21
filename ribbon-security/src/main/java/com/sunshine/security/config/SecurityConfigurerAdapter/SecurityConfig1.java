package com.sunshine.security.config.SecurityConfigurerAdapter;

import com.sunshine.security.config.common.CommonSpringSecurity;
import com.sunshine.security.config.phoneNumber.SmsCodeAuthenticationSecurityConfig;
import com.sunshine.security.config.phoneNumber.SmsCodeValidateFilter;
import com.sunshine.security.web.filter.ReadRequestBodyFilter;
import com.sunshine.common.util.web.PropertyUtils;
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

    /**
     * 不需要权限
     */
    private static final String BASE_EXCLUDE_PATH = "/js/**,/css/**,/images/**";
    /**
     * 配置文件
     */
    private static final String SPRING_SECURITY_CUSTOMER_PROPERTIES = "spring-security.properties";

    /**
     * 自定义不需要权限既可以访问
     */
    private static final String CUSTOMER_EXCLUDE_PATH = "customer_exclude_path";

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig1.class);

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SmsCodeValidateFilter smsCodeValidateFilter;

    String customerExcludePath = PropertyUtils.getPropertiesValue(SPRING_SECURITY_CUSTOMER_PROPERTIES, CUSTOMER_EXCLUDE_PATH, "");


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

        web.ignoring()
                .antMatchers(BASE_EXCLUDE_PATH.split(","))
                .antMatchers(customerExcludePath.split(","));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new ReadRequestBodyFilter(), WebAsyncManagerIntegrationFilter.class);
        // 添加拦截器
        http.addFilterBefore(smsCodeValidateFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests() // 设置哪些页面可以直接访问，哪些需要验证
                .antMatchers(BASE_EXCLUDE_PATH.split(",")).permitAll()  // 放过
                .and()
                .authorizeRequests() // 设置哪些页面可以直接访问，哪些需要验证
                .antMatchers(customerExcludePath.split(",")).permitAll()  // 放过

                .and()
                .authorizeRequests()
                .anyRequest().authenticated()

                .and()// 表示结束当前标签，上下文回到HttpSecurity，开启新一轮的配置。
                .formLogin()// 表示登录相关的页面/接口不要被拦截。
                .loginPage("/login.html")
                .permitAll()
                // 登录成功
                .successHandler(CommonSpringSecurity.AUTHENTICATION_SUCCESS_HANDLER)
                //  登录失败
                .failureHandler(CommonSpringSecurity.AUTHENTICATION_FAILURE_HANDLER)
//        注销登录
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(CommonSpringSecurity.LOGOUT_SUCCESS_HANDLER)
                .permitAll()

                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(CommonSpringSecurity.AUTHENTICATION_ENTRY_POINT);
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

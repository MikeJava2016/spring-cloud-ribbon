package com.sunshine.security.config.SecurityConfigurerAdapter;

import com.sunshine.common.util.web.PropertyUtils;
import com.sunshine.security.config.common.CommonSpringSecurity;
import com.sunshine.security.config.common.JwtAuthenticationTokenFilter;
import com.sunshine.security.config.phoneNumber.PhoneNumberAuthenticationFilter;
import com.sunshine.security.config.phoneNumber.SmsCodeValidateFilter;
import com.sunshine.security.service.impl.PhoneNumnerUserDetailsService;
import com.sunshine.utils.web.ReadRequestBodyFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * @version v1
 * @Description 短信验证登录配置类   业务比较复杂，我们也可以配置多个 HttpSecurity，
 * 实现对 WebSecurityConfigurerAdapter 的多次扩展
 * @Author huzhanglin
 * @Date 2022/5/20 12:34
 **/
@Configuration
@EnableWebSecurity
@Order(100)
public class SecurityConfigPhoneNumberConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ObjectPostProcessor postProcessor;

    private String smsLoginPath = "/sms/login";

    private String method = "POST";

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

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfigPhoneNumberConfig.class);

    @Autowired
    private SmsCodeValidateFilter smsCodeValidateFilter;

    @Autowired
    private PhoneNumnerUserDetailsService phoneNumnerUserDetailsService;

    private String customerExcludePath = PropertyUtils.getPropertiesValue(SPRING_SECURITY_CUSTOMER_PROPERTIES, CUSTOMER_EXCLUDE_PATH, "");

    @Autowired
    private List<AuthenticationProvider> authenticationProviders;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(postProcessor);
        super.configure(auth);
    }

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

        // 带token的跨域访问完美解决
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().configurationSource(CommonSpringSecurity.corsConfigurationSource("/**"));

        http.authorizeRequests() // 设置哪些页面可以直接访问，哪些需要验证
                .antMatchers(BASE_EXCLUDE_PATH.split(",")).permitAll()  // 放过
                .and()
                .authorizeRequests() // 设置哪些页面可以直接访问，哪些需要验证
                .antMatchers(customerExcludePath.split(",")).permitAll()  // 放过
                .and()


//                .antMatcher("/captchImage").anonymous() // 允许访问
//                .and()

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
                .defaultSuccessUrl("/1", false)
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
//        http.apply(smsCodeAuthenticationSecurityConfig);

        // 短信登录的请求 post 方式的 /sms/login
        AuthenticationManager authenticationManager = new ProviderManager(authenticationProviders);
        PhoneNumberAuthenticationFilter phoneNumberAuthenticationFilter = new PhoneNumberAuthenticationFilter(smsLoginPath, method, authenticationManager);

        // 处理成功
        phoneNumberAuthenticationFilter.setAuthenticationSuccessHandler(CommonSpringSecurity.AUTHENTICATION_SUCCESS_HANDLER);
        //登录失败响应
        phoneNumberAuthenticationFilter.setAuthenticationFailureHandler(CommonSpringSecurity.AUTHENTICATION_FAILURE_HANDLER);

        // 注意过滤器的顺序
        http.addFilterBefore(new ReadRequestBodyFilter(), WebAsyncManagerIntegrationFilter.class);
        http.addFilterBefore(smsCodeValidateFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(phoneNumberAuthenticationFilter, SmsCodeValidateFilter.class);
        http.addFilterAfter(jwtAuthenticationTokenFilter,SmsCodeValidateFilter.class);
    }

    @Bean
    HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        //你希望请求地址中可以出现 //
        firewall.setAllowUrlEncodedDoubleSlash(false);
        //如果请求地址中出现 %，这个请求也将被拒绝。URL 编码后的 % 是 %25，所以 %25 也不能出现在 URL 地址中。
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowBackSlash(true);
        firewall.setAllowUrlEncodedSlash(true);
        //请求地址中存在 . 编码之后的字符 %2e、%2E,则请求将被拒绝。
        //firewall.setAllowUrlEncodedPeriod(true);
        return firewall;
    }

    /**
     * 允许跨域调用的过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        //允许所有域名进行跨域调用
        config.addAllowedOrigin("*");
        //允许跨越发送cookie
        config.setAllowCredentials(true);
        //放行全部原始头信息
        config.addAllowedHeader("*");
        //允许所有请求方法跨域调用
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}

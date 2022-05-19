package com.gupaoedu.security.config;

import com.gupaoedu.security.config.phoneNumber.PhoneNumberAuthenticationFilter;
import com.gupaoedu.security.config.phoneNumber.PhoneNumberAuthenticationProvider;
import com.gupaoedu.security.config.phoneNumber.PhoneNumberCache;
import com.gupaoedu.security.config.phoneNumber.PhoneNumberDetailServiceImpl;
import com.gupaoedu.security.config.wx.WXProvider;
import com.gupaoedu.security.service.UserService;
import com.gupaoedu.security.service.impl.UserNameDetailServiceImpl;
import com.gupaoedu.security.service.impl.UserOpenIdDetailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class MyWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    WXProvider wxProvider;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean("phoneNumberAuthenticationFilter")
    public PhoneNumberAuthenticationFilter phoneNumberAuthenticationFilter(){
        return new PhoneNumberAuthenticationFilter("/phone/login");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //将项目中静态资源路径开放出来
        web.ignoring().antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**");
    }


    @Autowired
    @Qualifier("phoneNumberAuthenticationFilter")
    private PhoneNumberAuthenticationFilter phoneNumberAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        /*http.cors().configurationSource(corsConfigurationSource());
        http.addFilterBefore(new SunshineSecurityFilter(), BasicAuthenticationFilter.class);
              http.authorizeRequests() // 设置哪些页面可以直接访问，哪些需要验证
                .antMatchers("/login.html", "/error.html").permitAll() // 放过
                .anyRequest().authenticated() // 剩下的所有的地址都是需要在认证状态下才可以访问
                .and()
                .formLogin()
                .loginPage("/login.html") // 指定指定要的登录页面
                .loginProcessingUrl("/login.do") // 处理认证路径的请求
                .defaultSuccessUrl("/home.html")
                .failureForwardUrl("/error.html")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .and().csrf().disable();
*/
        http
                .cors().configurationSource(corsConfigurationSource());
        //关闭csrf
        http.csrf().disable()
                //不通过session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //登录接口允许匿名访问
                .antMatchers("/**/login").anonymous()
                .anyRequest().authenticated();

        http
                //添加token认证的过滤器到security的过滤链前
//                .addFilterAt(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
                .addFilterAt(phoneNumberAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //
    }

    /**
     * 带token的跨域访问完美解决
     *
     * @return
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");    //同源配置，*表示任何请求都视为同源，若需指定ip和端口可以改为如“localhost：8080”，多个以“，”分隔；
        corsConfiguration.addAllowedHeader("*");//header，允许哪些header，本案中使用的是token，此处可将*替换为token；
        corsConfiguration.addAllowedMethod("*");    //允许的请求方法，PSOT、GET等
        ((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**", corsConfiguration); //配置允许跨域访问的url
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 定义认证管理器
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "myAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(daoAuthenticationProvider, wxProvider,phoneNumberAuthenticationProvider));
        //不擦除认证密码，擦除会导致TokenBasedRememberMeServices因为找不到Credentials再调用UserDetailsService而抛出UsernameNotFoundException
        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }

    /**
     * 配置provider(WXProvider) 微信的一套（根据openid直接登入系统）
     *
     * @return
     */
    @Bean("wxProvider")
    public WXProvider getWXProvider(@Autowired UserOpenIdDetailServiceImpl userOpenIdDetailServiceImpl) {
        WXProvider wxProvider = new WXProvider();
        wxProvider.setUserOpenIdDetailsService(userOpenIdDetailServiceImpl);
        return wxProvider;
    }

    /**
     * 配置provider(DaoAuthenticationProvider) 用户名密码的一套
     *
     * @return
     */
    @Bean("daoAuthentication")
    public DaoAuthenticationProvider daoAuthenticationProvider(@Autowired PasswordEncoder passwordEncoder,
                                                               @Autowired UserNameDetailServiceImpl userNameDetailServiceImpl) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userNameDetailServiceImpl);
        daoAuthenticationProvider.setUserCache(new SunshineUserCache());
        return daoAuthenticationProvider;
    }

    @Autowired
    private PhoneNumberDetailServiceImpl phoneNumberDetailServiceImpl;

    @Autowired
    @Qualifier("phoneNumberAuthenticationProvider")
    private AuthenticationProvider phoneNumberAuthenticationProvider;

    @Bean("phoneNumberAuthenticationProvider")
    public PhoneNumberAuthenticationProvider phoneNumberAuthenticationProvider(@Autowired PasswordEncoder passwordEncoder,
                                                                       @Autowired PhoneNumberDetailServiceImpl phoneNumberDetailServiceImpl) {
        PhoneNumberAuthenticationProvider phoneNumberAuthenticationProvider = new PhoneNumberAuthenticationProvider();
        phoneNumberAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        phoneNumberAuthenticationProvider.setUserDetailsService(phoneNumberDetailServiceImpl);
        phoneNumberAuthenticationProvider.setUserCache(new PhoneNumberCache());
        return phoneNumberAuthenticationProvider;
    }



}

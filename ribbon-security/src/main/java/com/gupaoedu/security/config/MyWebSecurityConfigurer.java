package com.gupaoedu.security.config;

import com.gupaoedu.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
public class MyWebSecurityConfigurer extends WebSecurityConfigurerAdapter  {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*auth.inMemoryAuthentication()
                .withUser("lisi")
                .password("{noop}123")
                .authorities("ADMIN");*/

        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //将项目中静态资源路径开放出来
        web.ignoring().antMatchers( "/css/**", "/fonts/**", "/img/**", "/js/**");
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.cors().configurationSource(corsConfigurationSource());
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

     //
    }

    /**
     * 带token的跨域访问完美解决
     * @return
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfigurationSource source =   new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");	//同源配置，*表示任何请求都视为同源，若需指定ip和端口可以改为如“localhost：8080”，多个以“，”分隔；
        corsConfiguration.addAllowedHeader("*");//header，允许哪些header，本案中使用的是token，此处可将*替换为token；
        corsConfiguration.addAllowedMethod("*");	//允许的请求方法，PSOT、GET等
        ((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**",corsConfiguration); //配置允许跨域访问的url
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        System.out.println(123);
        return new BCryptPasswordEncoder();
    }


}

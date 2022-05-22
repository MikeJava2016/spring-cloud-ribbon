package com.sunshine.security.config.SecurityConfigurerAdapter;

import com.sunshine.security.config.common.CommonSpringSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MyWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();




    @Override
    public void configure(WebSecurity web) throws Exception {
        //将项目中静态资源路径开放出来
        web.ignoring().antMatchers("/static/css/**", "/fonts/**", "/img/**", "/static/js/**");
    }


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
                .cors().configurationSource(CommonSpringSecurity.corsConfigurationSource("/**"));
        //关闭csrf
        http.csrf().disable()
                //不通过session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //登录接口允许匿名访问
                .antMatchers("/phone/login").anonymous()
                .anyRequest().authenticated();
        //添加token认证的过滤器到security的过滤链前
//                .addFilterAt(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }




}

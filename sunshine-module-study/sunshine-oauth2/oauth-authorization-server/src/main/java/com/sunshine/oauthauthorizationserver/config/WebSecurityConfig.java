package com.sunshine.oauthauthorizationserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/14 22:37
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(OauthAuthorizationServerConfig.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("configure(HttpSecurity http)...");
        http.csrf().disable()
                .authorizeRequests()
                //.antMatchers("/gp/gp1").hasAnyAuthority("p1")
                //.antMatchers("/gp/gp2").hasAnyAuthority("p2")
                //.antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        logger.info("configure(AuthenticationManagerBuilder auth)...");
        auth.inMemoryAuthentication()
                .withUser("root")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .authorities("ROOT")
        ;
    }
}

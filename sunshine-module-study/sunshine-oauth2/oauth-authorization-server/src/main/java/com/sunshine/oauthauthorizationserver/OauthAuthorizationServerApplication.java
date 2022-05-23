package com.sunshine.oauthauthorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableWebSecurity(debug = true)
public class OauthAuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthAuthorizationServerApplication.class, args);
    }

}

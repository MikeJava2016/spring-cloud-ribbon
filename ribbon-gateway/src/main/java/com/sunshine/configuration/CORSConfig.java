package com.sunshine.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CORSConfig {
    /**
     * 网关统一配置跨域
     */
    @Bean
    public CorsWebFilter corsConfig(){
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*"); //允许任意请求头
        corsConfiguration.addAllowedMethod("*"); //允许任意方法
        corsConfiguration.addAllowedOrigin("*"); //允许任意请求来源
        corsConfiguration.setAllowCredentials(true); //允许携带cookie

        //表示所有请求都需要跨域
        corsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        return new CorsWebFilter(corsConfigurationSource);
    }

}

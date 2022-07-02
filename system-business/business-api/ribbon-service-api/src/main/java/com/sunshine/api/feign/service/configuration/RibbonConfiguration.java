package com.sunshine.api.feign.service.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/9 10:52
 **/
@Configuration
public class RibbonConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(RibbonConfiguration.class);

    @Bean
    public RequestInterceptor sunshinRequestInterceptor() {
        logger.info(" RibbonConfiguration  sunshinRequestInterceptor");
        return new RequestInterceptor(){

            @Override
            public void apply(RequestTemplate template) {
                logger.info(" RibbonConfiguration  sunshinRequestInterceptor template --------");
                template.header("username","huzhanglin");

            }
        };
    }

}

package com.sunshine.configuration.ribbon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/8 上午 01:17
 **/
@Configuration
public class RibbonAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer(){
        return new RestTemplateCustomizer() {
            @Override
            public void customize(RestTemplate restTemplate) {
                logger.info("customer restTemplate");
                HashMap<String, String> map = new HashMap<>();
                map.put("x-client","ribbon-client");
                restTemplate.setDefaultUriVariables(map);
            }
        };
    }
}

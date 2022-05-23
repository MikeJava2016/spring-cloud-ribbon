package com.sunshine.configuration.rest;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ResponseTimeWeightedRule;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateAutoConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(RestTemplateAutoConfiguration.class);

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        builder.additionalInterceptors()
//        builder.additionalCustomizers()
        return builder.build();
    }

    @Bean(value = "restTemplate3")
    @LoadBalanced
    public RestTemplate restTemplate3(RestTemplateBuilder builder) {
        return builder.build();
    }


//    @Bean
    public IRule iRule(){
        return new ResponseTimeWeightedRule();
    }

//    @Bean
    public MyPing iPing(){
        return new MyPing();
    }

    static public class MyPing implements IPing{
        @Override
        public boolean isAlive(Server server) {
            logger.info(" ping ...{}:{}",server.getHost(),server.getPort());
            return true;
        }
    }

}

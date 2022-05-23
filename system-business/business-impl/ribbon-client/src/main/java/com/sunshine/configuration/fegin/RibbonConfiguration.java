package com.sunshine.configuration.fegin;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
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
    public IRule WeightedResponseTimeRule(){
        WeightedResponseTimeRule weightedResponseTimeRule = new WeightedResponseTimeRule();
        logger.info("RibbonConfiguration weightedResponseTimeRule ");
        return weightedResponseTimeRule;
    }
}

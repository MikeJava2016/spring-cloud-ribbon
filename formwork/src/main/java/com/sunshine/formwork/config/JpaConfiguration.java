package com.sunshine.formwork.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/12 9:19
 **/
@EnableJpaRepositories(basePackages = {"com.sunshine.formwork.dao"})
@EntityScan(value = {"com.sunshine.formwork.entity"})
@Configuration
public class JpaConfiguration {
}

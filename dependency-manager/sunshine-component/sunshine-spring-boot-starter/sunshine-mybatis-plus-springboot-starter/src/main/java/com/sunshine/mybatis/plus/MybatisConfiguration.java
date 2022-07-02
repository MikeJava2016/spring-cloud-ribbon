package com.sunshine.mybatis.plus;

import com.baomidou.mybatisplus.extension.plugins.IllegalSQLInterceptor;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.sunshine.mybatis.plus.intercept.SM4Interceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MybatisConfiguration {

    @Bean
    public Interceptor sqlExplainInterceptor(){
        log.info("Mybatis sqlExplainInterceptor starting...");
        return new SqlExplainInterceptor();
    }

    @Bean
    public Interceptor paginationInterceptor(){
        log.info("Mybatis PaginationInterceptor starting...");
        return new PaginationInterceptor();
    }

    @Bean
    public Interceptor interceptor2(){
        log.info("Mybatis OptimisticLockerInterceptor starting...");
        return new OptimisticLockerInterceptor();
    }

    @Bean
    public Interceptor interceptor4(){
        log.info("Mybatis IllegalSQLInterceptor starting...");
        return new IllegalSQLInterceptor();
    }

    @Bean
    public Interceptor SM4Interceptor(){
        log.info("Mybatis SM4Interceptor starting...");

        return new SM4Interceptor();
    }

    @Bean
    public MybatisBatchExecutor mybatisBatchExecutor(){
        log.info("Mybatis MybatisBatchExecutor starting...");
        return new MybatisBatchExecutor();
    }
}

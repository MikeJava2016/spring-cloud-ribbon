package com.sunshine.security.config.common;

import com.baomidou.mybatisplus.extension.plugins.IllegalSQLInterceptor;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfiguration {

    @Bean
    public Interceptor sqlExplainInterceptor(){
        return new SqlExplainInterceptor();
    }

    @Bean
    public Interceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

    @Bean
    public Interceptor interceptor2(){
        return new OptimisticLockerInterceptor();
    }

    @Bean
    public Interceptor interceptor4(){
        return new IllegalSQLInterceptor();
    }
}

package com.sunshine.utils.configuration;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean(value = "threadPoolInstance")
    public ExecutorService createThreadPoolInstance() {
        ThreadFactoryBuilder factoryBuilder = new ThreadFactoryBuilder().setNamePrefix("pool");
        ExecutorService service = new ThreadPoolExecutor(2, 5,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5), factoryBuilder.build());
        return service;
    }
}

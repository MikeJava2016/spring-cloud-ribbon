package com.sunshine.utils.configuration;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);

    @Bean(value = "threadPoolInstance")
    public ExecutorService createThreadPoolInstance() {
        ThreadFactoryBuilder factoryBuilder = new ThreadFactoryBuilder().setNamePrefix("pool");
        ExecutorService service = new ThreadPoolExecutor(2, 5,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5), factoryBuilder.build());
        logger.debug("ThreadPoolConfig init threadPoolInstance finish.");
        return service;
    }



    @Bean(value = "scheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        logger.debug("ThreadPoolConfig init scheduledExecutorService finish.");
        return scheduledExecutorService;
    }
}

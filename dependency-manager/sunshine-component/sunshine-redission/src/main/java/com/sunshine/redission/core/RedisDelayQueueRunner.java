package com.sunshine.redission.core;

import cn.hutool.extra.spring.SpringUtil;
import com.sunshine.redission.common.enums.RedisDelayQueueEnum;
import com.sunshine.redission.utils.RedisDelayQueueUtil;
import com.sunshine.redission.utils.RedissonDistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisDelayQueueRunner implements CommandLineRunner {

    @Autowired
    private RedisDelayQueueUtil redisDelayQueueUtil;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private RedissonDistributedLocker redissonDistributedLocker;

    @Override
    public void run(String... args) {
        new Thread(() -> {
            while (true) {
               try {
                    RedisDelayQueueEnum[] queueEnums = RedisDelayQueueEnum.values();
                    for (RedisDelayQueueEnum queueEnum : queueEnums) {
                        // 获取锁
                        boolean acquireLock = redissonDistributedLocker.tryLock(queueEnum.getCode(), 700, 300, TimeUnit.MICROSECONDS);
                        if (!acquireLock){
                            break;
                        }
                        Object value = redisDelayQueueUtil.getDelayQueue(queueEnum.getCode());
                        if (value != null) {
                            RedisDelayQueueHandle redisDelayQueueHandle = SpringUtil.getBean(queueEnum.getBeanId());
                            redisDelayQueueHandle.execute(value);
                        }
                        // 释放锁
                        redissonDistributedLocker.unlock(queueEnum.getCode());
                    }
                } catch (InterruptedException e) {
                    log.error("(Redis延迟队列异常中断) {}", e.getMessage());
                }
            }
        }).start();
        log.info("(Redis延迟队列启动成功)");


        Map<String, String> map1 = new HashMap<>();
        map1.put("orderId", "100");
        map1.put("remark", "订单支付超时，自动取消订单");

        Map<String, String> map2 = new HashMap<>();
        map2.put("orderId", "200");
        map2.put("remark", "订单超时未评价，系统默认好评");

        // 添加订单支付超时，自动取消订单延迟队列。为了测试效果，延迟10秒钟
        redisDelayQueueUtil.addDelayQueue(map1, 10, TimeUnit.SECONDS, RedisDelayQueueEnum.ORDER_PAYMENT_TIMEOUT.getCode());

        // 订单超时未评价，系统默认好评。为了测试效果，延迟20秒钟
        redisDelayQueueUtil.addDelayQueue(map2, 20000, TimeUnit.SECONDS, RedisDelayQueueEnum.ORDER_TIMEOUT_NOT_EVALUATED.getCode());

    }


    @Bean
    @ConditionalOnMissingBean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }


    @Bean(name = "threadPool")
    @ConditionalOnMissingBean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16); // 核心线程数目
        executor.setMaxPoolSize(64); // 指定最大线程数
        executor.setQueueCapacity(320); // 队列中最大的数目
        executor.setThreadNamePrefix("taskThreadPool_"); // 线程名称前缀
// rejection-policy：当pool已经达到max size的时候，如何处理新任务
// CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
// 对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.setKeepAliveSeconds(60); // 线程空闲后的最大存活时间

        executor.initialize();
        log.info("taskThreadPool_ 线程池启动成功");
        return executor;
    }
}

package com.sunshine;

import com.sunshine.manage.task.MonitorTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Description 动态路由配置管理
 * @Author jianglong
 * @Date 2020/05/27
 * @Version V1.0
 */
@Slf4j
@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.sunshine"})
@EnableEurekaClient
@EnableScheduling
@EnableFeignClients(basePackages = "com.sunshine")
public class DynamicManageApplication {

    @Resource
    private MonitorTaskService monitorTaskService;

    public static void main(String[] args) {
        SpringApplication.run(DynamicManageApplication.class, args);
    }

    /**
     * 执行监控程序
     */
    @PostConstruct
    public void runMonitor(){
        log.info("运行网关路由监控任务...");
        monitorTaskService.executeMonitorTask();
    }
}

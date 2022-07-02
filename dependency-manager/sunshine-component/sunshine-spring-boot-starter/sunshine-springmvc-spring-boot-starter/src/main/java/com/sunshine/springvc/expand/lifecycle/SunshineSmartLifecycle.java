package com.sunshine.springvc.expand.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration(proxyBeanMethods = false)
public class SunshineSmartLifecycle implements ApplicationContextAware, SmartLifecycle, Ordered {

    private ApplicationContext applicationContext;

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void start() {
        System.out.println("---------------------------");

        SunshineFactoryBean.SunshineFactoryInnerBean innerBean = applicationContext.getBean(SunshineFactoryBean.SunshineFactoryInnerBean.class);
        System.out.println(" sun -1+>"+ innerBean);
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}

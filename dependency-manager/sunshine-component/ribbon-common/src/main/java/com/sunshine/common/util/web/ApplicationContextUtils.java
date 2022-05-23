package com.sunshine.common.util.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static  <T> T getBean(T t){
        return (T) applicationContext.getBean(t.getClass());
    }

    public static Object getBean(String name){
        return  applicationContext.getBean(name);
    }

    public static<T> T getBean(Class<T>  clazz){
        return  applicationContext.getBean(clazz);
    }
}

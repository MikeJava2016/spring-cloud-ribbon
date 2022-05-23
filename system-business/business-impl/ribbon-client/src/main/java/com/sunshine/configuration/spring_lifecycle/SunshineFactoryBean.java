package com.sunshine.configuration.spring_lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/4 16:18
 **/
@Component
public class SunshineFactoryBean implements FactoryBean<SunshineFactoryBean.SunshineFactoryInnerBean> , InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public SunshineFactoryInnerBean getObject() throws Exception {
        System.out.println("[SunshineFactoryBean] getObject");
        return new SunshineFactoryInnerBean();
    }

    @Override
    public Class<?> getObjectType() {
        return SunshineFactoryBean.SunshineFactoryInnerBean.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public class SunshineFactoryInnerBean {
    }
}

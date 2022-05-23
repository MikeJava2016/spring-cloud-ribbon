package com.sunshine.configuration.spring_lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.beans.PropertyDescriptor;

/**
 * @version v1
 * @Description BeanPostProcess接口只在bean的初始化阶段进行扩展（注入spring上下文前后），
 * 而InstantiationAwareBeanPostProcessor接口在此基础上增加了3个方法，把可扩展的范围增加了实例化阶段和属性注入阶段
 * @Author huzhanglin
 * @Date 2022/5/4 15:10
 **/
//@Component
public class SunshineInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    /**
     * 实例化bean之前，相当于new这个bean之前
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("[SunshineInstantiationAwareBeanPostProcessor] before instantiation " + beanName);
        return null;
    }


    /**
     * 实例化bean之后，相当于new这个bean之后
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("[SunshineInstantiationAwareBeanPostProcessor] after instantiation " + beanName);
        return true;
    }

    /**
     * bean已经实例化完成，在属性注入时阶段触发
     * @param pvs
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        System.out.println("[SunshineInstantiationAwareBeanPostProcessor] postProcessPropertyValues " + beanName);
        return pvs;
    }

    /**
     * ：bean已经实例化完成，在属性注入时阶段触发，@Autowired,@Resource等注解原理基于此方法实现
     * @param pvs
     * @param pds
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        System.out.println("[SunshineInstantiationAwareBeanPostProcessor] postProcessPropertyValues " + beanName);
        return pvs;
    }

    /**
     * 初始化bean之前，相当于把bean注入spring上下文之前
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("[SunshineInstantiationAwareBeanPostProcessor] before initialization " + beanName);
        return bean;
    }

    /**
     * ：初始化bean之后，相当于把bean注入spring上下文之后
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("[SunshineInstantiationAwareBeanPostProcessor] after initialization " + beanName);
        return bean;
    }
}

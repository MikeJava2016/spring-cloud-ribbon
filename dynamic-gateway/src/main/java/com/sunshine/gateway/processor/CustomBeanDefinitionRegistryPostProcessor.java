package com.sunshine.gateway.processor;

import com.sunshine.gateway.filter.CustomWeightCalculatorWebFilter;
import com.sunshine.gateway.listener.NacosConfigRefreshEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @Description 对指定WeightCalculatorWebFilter过滤器进行潜换，使用CustomWeightCalculatorWebFilter潜换方案
 * @Author JL
 * @Date 2021/10/12
 * @Version V1.0
 */
@Component
public class CustomBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private static Log log = LogFactory.getLog(NacosConfigRefreshEventListener.class);

    private static final String BEAN_NAME = "weightCalculatorWebFilter";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 查找同名的bean，如果存在则用自定义bean覆盖
        RootBeanDefinition beanDefinition = (RootBeanDefinition) registry.getBeanDefinition(BEAN_NAME);
        if (beanDefinition != null) {
            log.debug(beanDefinition.getBeanClassName()+"====="+beanDefinition);
            registry.removeBeanDefinition(BEAN_NAME);
            beanDefinition = new RootBeanDefinition(CustomWeightCalculatorWebFilter.class);
            registry.registerBeanDefinition(BEAN_NAME, beanDefinition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}

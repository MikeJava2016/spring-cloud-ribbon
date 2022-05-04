package com.sunshine.configuration.spring_lifecycle;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 其作用是在bean的初始化阶段，如果对一个方法标注了@PostConstruct，
 * 会先调用这个方法。这里重点是要关注下这个标准的触发点，
 * 这个触发点是在postProcessBeforeInitialization之后，InitializingBean.afterPropertiesSet之前。
 *
 * 使用场景：用户可以对某一方法进行标注，来进行初始化某一个属性
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/4 15:43
 **/
@Component
public class NormalBeanA {
    public NormalBeanA() {
        System.out.println("NormalBean constructor");
    }

    @PostConstruct
    public void init(){
        System.out.println("[PostConstruct] NormalBeanA");
    }
}

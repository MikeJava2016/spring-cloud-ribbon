package com.sunshine.security.aop;

import com.sunshine.security.GpVipSecurity1011Application;
import com.sunshine.security.aop.demo.CustomerService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@MapperScan(basePackages = "com.sunshine.security.mapper")
@SpringBootApplication(scanBasePackages = "com.sunshine")
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass =true)
public class AopTest {
    @Bean
    public CustomerServiceInterceptor customerServiceInterceptor () {
        return new CustomerServiceInterceptor();
    }

    @Autowired
    private CustomerService customerService;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(GpVipSecurity1011Application.class, args);
        CustomerService bean = applicationContext.getBean(CustomerService.class);
        bean.doSomething1();
        bean.doSomething3();
        System.out.println("==================================");
//        结果如下 ： @EnableAspectJAutoProxy
//        do some important things before...
//        CustomerServiceImpl.doSomething1()
//        CustomerServiceImpl.doSomething2()
//        do some important things before...
//        CustomerServiceImpl.doSomething3()
//        do some important things before...
 //                ==================================
    }


//    修改类，不要出现“自调用”的情况：这是Spring文档中推荐的“最佳”方案；
//    若一定要使用“自调用”，那么this.doSomething2()替换为：((CustomerService)
//            AopContext.currentProxy()).doSomething2()；此时需要修改spring的aop配置：
//    @EnableAspectJAutoProxy(exposeProxy = true)
}

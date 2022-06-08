package com.sunshine.security.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopAspect {

    @Before("execution(public * *(..))")
    public void  all(){
        System.out.println("execution Before");
    }

}

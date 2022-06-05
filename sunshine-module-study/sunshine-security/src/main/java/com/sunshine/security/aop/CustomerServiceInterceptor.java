package com.sunshine.security.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class CustomerServiceInterceptor {

    @Before("execution(* com.sunshine.security.*.*(..))")
    public void doBefore() {
        System.out.println("do some important things before...");
    }
}
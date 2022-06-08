package com.sunshine.security.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CustomerServiceInterceptor {

    @Before("execution(* com.sunshine.security.*.*(..))")
    public void doBefore() {
        System.out.println("do some important things before...");
    }

    @Before("within(com.sunshine.security.controller..*)")
    public void within() {
        System.out.println("do within...");
    }

    @Before("this(com.sunshine.security.controller.SmsController)")
    public void doThis() {
        System.out.println("do doThis...");
    }

//    Around advice is declared using the @Around annotation.
//    The first parameter of the advice method must be of type ProceedingJoinPoint.
    @Around("within(com.sunshine.security.controller..*)")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        log.info("start around = {}",pjp.getArgs());
        // start stopwatch
        Object retVal = pjp.proceed();
        // stop stopwatch
        log.info("start end = {}",retVal);
        return retVal;
    }

//    target(com.xyz.service.AccountService)
//args(java.io.Serializable)
//    @target(org.springframework.transaction.annotation.Transactional)

 }
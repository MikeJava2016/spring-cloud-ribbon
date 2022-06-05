package com.sunshine.security.aop.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Transactional
    public void doSomething1() {
        System.out.println(this);
        System.out.println("CustomerServiceImpl.doSomething1()");
        doSomething2();
    }

    public void doSomething2() {
        System.out.println("CustomerServiceImpl.doSomething2()");
    }

    @Transactional
    @Override
    public void doSomething3() {
        System.out.println(this);
        System.out.println("CustomerServiceImpl.doSomething3()");
/*        ((CustomerService)
                AopContext.currentProxy()).doSomething2();*/

    }
}
package com.sunshine.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 18:48
 **/
@RestController
public class HelloController {
    /**
     *  http://localhost:8081/hello
     * @return
     */
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
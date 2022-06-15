package com.sunshine.security.controller;

import com.sunshine.security.config.annotation.IgnoreAuth;
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
    @GetMapping(path = "/hello",headers = "header1=a")
    public String hello() {
        return "hello";
    }

    /**
     *  http://localhost:8081/saygoogbug
     * @return
     */
    @GetMapping("/saygoogbug")
    @IgnoreAuth
    public String saygoogbug() {
        return "saygoogbug";
    }

}
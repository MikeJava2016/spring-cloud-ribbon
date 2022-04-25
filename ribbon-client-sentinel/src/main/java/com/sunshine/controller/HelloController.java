package com.sunshine.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hello")
@RestController
public class HelloController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${sentinel.flowRules}")
    private String flowRules;

    /**
     * http://localhost:8080/hello/hello
     * @return
     */
    @GetMapping("/hello")
    public String hello() {
        logger.info("flowRules = {}",flowRules);
        logger.info("HelloController hello");
        return "hello";
    }


}

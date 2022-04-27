package com.sunshine.controller;

import com.sunshine.annotation.SunShine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hello")
@RestController
public class HelloController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * http://localhost:8080/hello/hello
     * @return
     */
    @GetMapping("/hello")
    @SunShine
    public String hello() {
        logger.info("HelloController hello");
        return "hello";
    }


}

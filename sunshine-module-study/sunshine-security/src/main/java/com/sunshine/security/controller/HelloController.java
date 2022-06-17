package com.sunshine.security.controller;

import com.sunshine.security.config.annotation.IgnoreAuth;
import com.sunshine.utils.common.Result;
import com.sunshine.utils.web.WebAsyncUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 18:48
 **/
@RestController
@Slf4j
public class HelloController {
    /**
     * http://localhost:8081/hello
     *
     * @return
     */
    @GetMapping(path = "/hello", headers = "header1=a")
    public String hello() {
        return "hello";
    }

    /**
     * http://localhost:8081/saygoogbug
     *
     * @return
     */
    @GetMapping("/saygoogbug")
    @IgnoreAuth
    public String saygoogbug() {
        return "saygoogbug";
    }

    @GetMapping("/saygoogbug2")
    public WebAsyncTask<Result> saygoogbug2() {
        return WebAsyncUtils.instance(() -> "1234");
    }

}
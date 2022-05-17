package com.gupaoedu.security.controller;

import com.gupaoedu.security.service.SmsSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 让每一个人的职业生涯不留遗憾
 *
 * @author 波波老师【咕泡学院】
 */
@Controller
public class BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/login.html")
    public String loginPage(){
        return "/login.html";
    }

    @GetMapping("/home.html")
    public String home(){
        return "/home.html";
    }

    @GetMapping("/")
    public String basePage(){
        return "/home.html";
    }

    @GetMapping("/error.html")
    public String error(){
        return "/error.html";
    }


}

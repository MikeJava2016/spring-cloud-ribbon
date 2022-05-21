package com.gupaoedu.security.controller;

import com.alibaba.fastjson.JSON;
import com.gupaoedu.security.entity.SysUser;
import com.gupaoedu.security.service.LoginService;
import com.sunshine.common.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 让每一个人的职业生涯不留遗憾
 *
 * @author 波波老师【咕泡学院】
 */
//@Controller
public class BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired(required = false)
    private LoginService loginService;

    @GetMapping("/login")
    public String loginPage(){
        return "/login.html";
    }

    @GetMapping("/home")
    public String home(){
        return "/home.html";
    }

    /*@GetMapping("/")
    public String basePage(){
        return "/home.html";
    }*/

    @GetMapping("/error")
    public String error(){
        return "/error.html";
    }

    /**
     * {
     *     "username":"root",
     *     "password":"123456"
     * }
     * @param sysUser
     * @return
     */
    @ResponseBody
    @PostMapping("/login/common")
    public Result login(@RequestBody SysUser sysUser){
        logger.info(JSON.toJSONString(sysUser));
       return loginService.login(sysUser);
    }


    /**
     * {
     *     "username":"root",
     *     "password":"123456"
     * }
     * @param sysUser
     * @return
     */
    @ResponseBody
    @PostMapping("/login/wx")
    public Result wxLogin(@RequestBody SysUser sysUser){
        logger.info(JSON.toJSONString(sysUser));
        return loginService.wxLogin(sysUser);
    }

    /**
     * {
     *     "username":"root",
     *     "password":"123456"
     * }
     * @param sysUser
     * @return
     */
    @ResponseBody
    @PostMapping("/ok")
    public Result ok(@RequestBody SysUser sysUser){
        logger.info(JSON.toJSONString(sysUser));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Result.success(authentication.getAuthorities());
    }

}

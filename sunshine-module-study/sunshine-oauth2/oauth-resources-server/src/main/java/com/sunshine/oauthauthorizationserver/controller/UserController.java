package com.sunshine.oauthauthorizationserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/15 20:26
 **/
@RestController
public class UserController {
    @PreAuthorize(value = "hasAnyAuthority('ROOT')")
    @RequestMapping("/gp/query1")
    public String query1(){
        return "query1 ...";
    }
    @PreAuthorize(value = "hasAnyAuthority('USER')")
    @RequestMapping("/gp/query2")
    public String query2(){
        return "query2 ...";
    }
}

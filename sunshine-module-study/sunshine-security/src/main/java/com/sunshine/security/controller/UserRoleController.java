package com.sunshine.security.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sunshine.security.service.UserRoleService;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description:
 */
@RestController
@RequestMapping("/security/user-role-model" )
public class UserRoleController {

    @Autowired
    public  UserRoleService service;

}

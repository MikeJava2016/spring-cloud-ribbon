package com.sunshine.security.controller;


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

    public final UserRoleService service;

    public UserRoleController(UserRoleService service) {
        this.service = service;
    }
}

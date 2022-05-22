package com.sunshine.security.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sunshine.security.service.RoleService;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description:
 */
@RestController
@RequestMapping("/security/role-model" )
public class RoleController {
    @Autowired
    public RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }
}

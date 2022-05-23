package com.sunshine.security.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sunshine.common.util.Result;
import com.sunshine.security.entity.UserModel;
import com.sunshine.security.service.UserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;


/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description:
 */
@RestController
@RequestMapping("/security/user-model" )
public class UserController {

    @Autowired
    public UserModelService userModelService;

    @PostMapping
    public Result<UserModel> save(@RequestBody UserModel userModel) {
        boolean save = userModelService.save(userModel);
        if (save) {
            return Result.success(userModel);
        }
        return Result.fail("保存失败" );
    }

    @PutMapping
    public Result<UserModel> update(@RequestBody UserModel userModel) {

        boolean update = userModelService.updateById(userModel);
        if (update) {
            return Result.success(userModel);
        }
        return Result.fail("保存失败" );
    }

    @GetMapping("/{id}" )
    public Result<UserModel> get(@PathVariable("id" ) Serializable id) {

        UserModel userModel = userModelService.getById(id);

        return Result.success(userModel);

    }

    @DeleteMapping("/{id}" )
    public Result<UserModel> delete(@RequestBody UserModel userModel) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",userModel.getId());
        updateWrapper.set("enabled" , userModel.getEnabled());
        updateWrapper.set("locked",userModel.getLocked());
        userModelService.update(updateWrapper);
        return Result.success(userModel);

    }
}

package com.sunshine.api.feign.service;

import com.sunshine.common.util.Result;
import com.sunshine.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ribbon-service",contextId = "user")
public interface UserFeignSerivce {

    @GetMapping(value = "/user/{id}")//对应提供者方法路径
    Result<User> getOne(@PathVariable("id") Long id);

    @PostMapping(value = "/user")//对应提供者方法路径
    Result<User> post(@RequestBody User user);

    @PutMapping(value = "/user")//对应提供者方法路径
    Result<User> put(@RequestBody User user);

    @PutMapping(value = "/{id}")//对应提供者方法路径
    Result<User> delete(@PathVariable("id")Long id);

    @GetMapping(value = "/username/{username}")//对应提供者方法路径
    Result<User> getInfoByUserName(@PathVariable("username") String username);
}

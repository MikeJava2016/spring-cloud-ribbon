package com.sunshine.api.feign.service;

import com.sunshine.common.base.Result;
import com.sunshine.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "ribbon-service",contextId = "user"/*,configuration = {RibbonConfiguration.class}*/)
public interface UserFeignSerivce {

    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)//对应提供者方法路径
    Result<User> getOne(@PathVariable("id") Long id);

//    @PostMapping(value = "/user")//对应提供者方法路径
    @RequestMapping(value = "/user",method = RequestMethod.POST)//对应提供者方法路径
    Result<User> post(@RequestBody User user);

//    @PutMapping(value = "/user")//对应提供者方法路径
    @RequestMapping(value = "/user",method = RequestMethod.PUT)//对应提供者方法路径
    Result<User> put(@RequestBody User user);

//    @PutMapping(value = "/{id}")//对应提供者方法路径
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)//对应提供者方法路径
    Result<User> delete(@PathVariable("id")Long id);

//    @GetMapping(value = "/username/{username}")//对应提供者方法路径
    @RequestMapping(value = "/username/{username}",method = RequestMethod.GET)//对应提供者方法路径
    Result<User> getInfoByUserName(@PathVariable("username") String username);
}

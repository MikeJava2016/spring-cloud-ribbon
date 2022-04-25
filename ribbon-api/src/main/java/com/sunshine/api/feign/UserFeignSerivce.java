package com.sunshine.api.feign;

import com.sunshine.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ribbon-service")
public interface UserFeignSerivce {

    @GetMapping(value = "/user/{id}")//对应提供者方法路径
    User getOne(@PathVariable("id") String id);

    @PostMapping(value = "/user")//对应提供者方法路径
    User post(@RequestBody User user);

    @PutMapping(value = "/user")//对应提供者方法路径
    User put(@RequestBody User user);

    @PutMapping(value = "/{id}")//对应提供者方法路径
    User delete(@PathVariable("id")String id);
}

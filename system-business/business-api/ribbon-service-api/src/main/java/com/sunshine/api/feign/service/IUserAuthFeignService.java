package com.sunshine.api.feign.service;

import com.sunshine.common.ao.LoginUserDto;
import com.sunshine.common.base.Result;
import com.sunshine.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 9:06
 **/
@FeignClient(name = "ribbon-service",contextId = "user-auth")
public interface IUserAuthFeignService {

    @GetMapping(value = "/token",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<String> validToken(@RequestParam("token") String token);

    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<User> login(@RequestBody LoginUserDto loginUserDto);

}

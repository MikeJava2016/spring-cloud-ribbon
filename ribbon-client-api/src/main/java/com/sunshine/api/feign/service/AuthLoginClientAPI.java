package com.sunshine.api.feign.service;

import com.sunshine.common.ao.LoginUserDto;
import com.sunshine.common.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "ribbon-client",contextId = "user-auth")
public interface AuthLoginClientAPI {

    @GetMapping(value = "/token",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<String> validToken(@RequestParam("token") String token);

    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<String> login(@RequestBody LoginUserDto loginUserDto);

    @GetMapping(value = "/refreshToken",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<String> refreshToken(@RequestParam("token") String token);
}
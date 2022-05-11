package com.example.demo;

import com.sunshine.common.util.Result;
import com.sunshine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequestMapping("/userfeign")
@RestController
public class UserFeignController {

    @PostMapping
    public Result<User> postUser(@RequestBody User user) {
        User rest = new User(1L);
        Result<User> userResult = new Result<>();
        userResult.setData(rest);
        userResult.setCode(0);
        userResult.setMsg("success");
        return userResult;
    }


    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Autowired
        List<HttpMessageConverter<?>> converters;


        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new BaseHandlerMethodArgumentResolver(converters));
        }

    }
}
package com.sunshine.controller.feign;

import com.sunshine.api.feign.UserFeignSerivce;
import com.sunshine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/userfeign")
@RestController
public class UserFeignController {

    @Autowired
    private UserFeignSerivce userFeignSerivce;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") String id){
        return userFeignSerivce.getOne(id);
    }

    @PostMapping
    public User postUser(@RequestBody User user ){
        return userFeignSerivce.post(user);
    }

    @PutMapping("/{id}")
    public User putUser(@RequestBody User user ){
        return userFeignSerivce.put(user);
    }

    @DeleteMapping("/{id}")
    public User putUser(@PathVariable("id") String id){
        return userFeignSerivce.delete(id);
    }
}

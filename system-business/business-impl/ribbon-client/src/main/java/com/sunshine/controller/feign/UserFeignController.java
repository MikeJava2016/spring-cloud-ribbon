package com.sunshine.controller.feign;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.sunshine.api.feign.service.UserFeignSerivce;
import com.sunshine.common.annontation.Login;
import com.sunshine.common.base.Result;
import com.sunshine.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/userfeign")
@RestController
public class UserFeignController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${mysql.password}")
    private String password;

    @Autowired
    @Lazy
    private UserFeignSerivce userFeignSerivce;

    /**
     * url http://localhost:18080/userfeign/1
     * @param id
     * @return
     */
    @GetMapping("/{id}")
//    @SentinelResource(value = "userfeign", blockHandler = "blockHandler2", fallback = "fallbackHandle2")
    public Result getUserById(@PathVariable("id")Long id) {
        return userFeignSerivce.getOne(id);
    }



    @PostMapping
    public Result<User> postUser(@Login @RequestBody User user) {
        return userFeignSerivce.post(user);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Result putUser(@RequestBody User user) {
        return userFeignSerivce.put(user);
    }

    @DeleteMapping("/{id}")
    public Result<User> putUser(@PathVariable("id") Long id) {
        return userFeignSerivce.delete(id);
    }

    public User blockHandler2(BlockException blockException) {
        logger.info("调用payment---blockHandler");
        return new User(0L);
    }

    public User fallbackHandler2() {
        logger.info("调用payment----fallbackHandler");
        return new User(0L);
    }


}

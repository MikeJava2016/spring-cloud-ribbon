package com.sunshine.controller;

import com.sunshine.api.feign.service.UserFeignSerivce;
import com.sunshine.common.base.Result;
import com.sunshine.entity.User;
import com.sunshine.service.UserService;
import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ribbon-service/api")
public class UserController implements UserFeignSerivce {

    private SnowflakeShardingKeyGenerator snowflakeShardingKeyGenerator = new SnowflakeShardingKeyGenerator();

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // url:http://localhost:18085/user/1
    @Override
    @ResponseBody
    public Result<User> getOne(Long id) {
        logger.info(" id = {}", id);
         return Result.success(userService.selectById(id));
    }

    @Override
    @ResponseBody
    public Result<User> post(User user) {
        Comparable<?> comparable = snowflakeShardingKeyGenerator.generateKey();

        user.setId((Long) comparable);
        int count =  userService.insert(user);
        logger.info("count = {}" ,count);
        logger.info(" user = {}", user);
        return Result.success(user);
    }

    @Override
    @ResponseBody
    public Result<User> put(User user) {
        logger.info("put user = {}", user);
        return Result.success(user);
    }

    @Override
    @ResponseBody
    public Result<User> delete(Long id) {
        logger.info("delete id = {}", id);
        return Result.success(new User(id));
    }

    @Override
    @ResponseBody
    public Result<User> getInfoByUserName(String username) {
        User user = userService.selectByUsername(username);
        return Result.success(user);
    }
}
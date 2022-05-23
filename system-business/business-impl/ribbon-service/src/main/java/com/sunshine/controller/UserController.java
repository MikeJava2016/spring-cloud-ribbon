package com.sunshine.controller;

import com.sunshine.api.feign.service.UserFeignSerivce;
import com.sunshine.common.util.Result;
import com.sunshine.entity.User;
import com.sunshine.mapper.UserMapper;
import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserFeignSerivce {

    private SnowflakeShardingKeyGenerator snowflakeShardingKeyGenerator = new SnowflakeShardingKeyGenerator();

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserMapper userMapper;

    // url:http://localhost:8085/user/1
    @Override
    public Result<User> getOne(Long id) {
        logger.info(" id = {}", id);
         return Result.success(userMapper.selectById(id));
    }

    @Override
    public Result<User> post(User user) {
        Comparable<?> comparable = snowflakeShardingKeyGenerator.generateKey();

        user.setId((Long) comparable);
        userMapper.insert(user);
        logger.info(" user = {}", user);
        return Result.success(user);
    }

    @Override
    public Result<User> put(User user) {
        logger.info("put user = {}", user);
        return Result.success(user);
    }

    @Override
    public Result<User> delete(Long id) {
        logger.info("delete id = {}", id);
        return Result.success(new User(id));
    }

    @Override
    public Result<User> getInfoByUserName(String username) {
        User user = userMapper.selectByUsername(username);
        return Result.success(user);
    }
}
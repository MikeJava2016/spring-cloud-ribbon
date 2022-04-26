package com.sunshine.controller;
import com.sunshine.api.feign.UserFeignSerivce;
import com.sunshine.entity.User;
import com.sunshine.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserFeignSerivce {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserMapper userMapper;
        // url:http://localhost:8085/user/1
    @Override
    public User getOne(Long id) {
        logger.info(" id = {}", id);
        User user = userMapper.selectById(id);
        return new User(id);
    }

    @Override
    public User post(User user) {
        logger.info(" user = {}", user);
        return user;
    }

    @Override
    public User put(User user) {
        logger.info("put user = {}", user);
        return user;
    }

    @Override
    public User delete(Long id) {
        logger.info("delete id = {}", id);
        return new User(id);
    }
}
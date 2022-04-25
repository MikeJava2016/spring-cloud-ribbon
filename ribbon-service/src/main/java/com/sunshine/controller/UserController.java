package com.sunshine.controller;
import com.sunshine.api.feign.UserFeignSerivce;
import com.sunshine.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserFeignSerivce {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public User getOne(String id) {
        logger.info(" id = {}", id);
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
    public User delete(String id) {
        logger.info("delete id = {}", id);
        return new User(id);
    }
}
package com.sunshine.service.impl;

import com.sunshine.entity.User;
import com.sunshine.mapper.UserMapper;
import com.sunshine.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/18 下午 03:26
 **/
@Service
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectById(Long id) {
        this.realMethod();
        logger.info(" UserServiceImpl selectById param = {}",id);
        return userMapper.selectById(id);
    }

    public void realMethod() {
        System.out.println("realMethod");
        realMethodThrow();
    }

    public void realMethodThrow() {
        System.out.println("realMethod");
        throw new UnsupportedOperationException("error");
    }

    @Override
    public int insert(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}

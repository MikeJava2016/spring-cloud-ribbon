package com.sunshine.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 21:30
 **/
public class SunshineUserCache implements UserCache {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ConcurrentHashMap<String,UserDetails> cache = new ConcurrentHashMap<>();


    @Override
    public UserDetails getUserFromCache(String username) {
        logger.info("SunshineUserCache getUserFromCache username = {}",username);
        return cache.get(username);
    }

    @Override
    public void putUserInCache(UserDetails user) {
        logger.info("SunshineUserCache putUserInCache username = {}",user.getUsername());
        cache.put(user.getUsername(), user);
    }

    @Override
    public void removeUserFromCache(String username) {
        logger.info("SunshineUserCache removeUserFromCache username = {}",username);
        cache.remove(username);
    }
}

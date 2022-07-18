package com.sunshine.service;

import com.sunshine.entity.User;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/18 下午 03:25
 **/
public interface UserService {
    User selectById(Long id);

    int insert(User user);

    User selectByUsername(String username);
}

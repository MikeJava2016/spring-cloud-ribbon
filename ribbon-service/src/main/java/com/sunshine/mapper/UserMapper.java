package com.sunshine.mapper;

import com.sunshine.entity.User;

public interface UserMapper{

    User selectById(Long id);

    int insert(User user);
}

package com.sunshine.mapper;

import com.sunshine.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper{

    User selectById(@Param("id") Long id);

    int insert(User user);

    User selectByUsername(@Param("username")String username);
}

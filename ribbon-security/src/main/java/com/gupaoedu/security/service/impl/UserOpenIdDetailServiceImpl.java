package com.gupaoedu.security.service.impl;

import com.gupaoedu.security.entity.LoginUser;
import com.gupaoedu.security.entity.SysUser;
import com.gupaoedu.security.service.UserOpenIdDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 8:45
 **/
//@Service
public class UserOpenIdDetailServiceImpl implements UserOpenIdDetailsService {
    @Override
    public UserDetails loadUserByOpenId(String openId) {
        //根据用户名在数据库中查询用户信息
//        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("open_id", openId);
//        SysUser sysUser = userMapper.selectOne(queryWrapper);
        SysUser sysUser = new SysUser();
        List<String> permissions = new ArrayList<>(Arrays.asList("admin","common"));

        //再封装成UserDetails实现类
        LoginUser loginUser = new LoginUser(sysUser,permissions);
        loginUser.setSysUser(sysUser);
        return loginUser;
    }
}

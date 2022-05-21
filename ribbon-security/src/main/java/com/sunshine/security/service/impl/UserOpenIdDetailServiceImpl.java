package com.sunshine.security.service.impl;

import com.sunshine.security.entity.LoginUser;
import com.sunshine.security.entity.SysUser;
import com.sunshine.security.service.UserOpenIdDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> permissions = new ArrayList<>(Arrays.asList("admin", "common"));

        //
        LoginUser loginUser = new LoginUser(sysUser, permissions.stream().map(one ->
                new SimpleGrantedAuthority(one)).collect(Collectors.toList()));
        loginUser.setSysUser(sysUser);
        return loginUser;
    }
}

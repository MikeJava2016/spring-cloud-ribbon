package com.sunshine.security.service.impl;

import com.sunshine.security.entity.LoginUser;
import com.sunshine.security.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 9:00
 **/
//@Service
public class UserNameDetailServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserNameDetailServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info(" 模拟从数据库中查询数据");
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setId("1");
        sysUser.setPassword("$2a$10$MVrNL17JJayG3d/xtwg5quFpr5A3mzUMD2tuniKFW.tPjAc61lO9O");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("root"));
        authorities.add(new SimpleGrantedAuthority("write"));
        authorities.add(new SimpleGrantedAuthority("read"));

        return new LoginUser(sysUser, authorities);
    }
}

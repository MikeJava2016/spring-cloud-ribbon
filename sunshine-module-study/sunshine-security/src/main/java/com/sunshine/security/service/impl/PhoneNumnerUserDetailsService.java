package com.sunshine.security.service.impl;

import com.sunshine.security.entity.LoginUser;
import com.sunshine.security.entity.SysUser;
import com.sunshine.security.entity.UserModel;
import com.sunshine.security.service.UserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/24 16:44
 **/
@Service
public class PhoneNumnerUserDetailsService implements UserDetailsService {
    @Autowired
    private UserModelService userModelService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        UserModel userModel = userModelService.getByPhone(phone);
        if (userModel == null){
            //

        }
        List<String> roleNames = userModelService.getRoleNames(userModel.getId());
        SysUser sysUser = new SysUser();
        sysUser.setUsername(userModel.getUsername());
        sysUser.setPassword(userModel.getPassword());
        sysUser.setPhone(phone);
        LoginUser loginUser = new LoginUser(sysUser, roleNames.stream().map(one ->
                new SimpleGrantedAuthority(one)).collect(Collectors.toList()));
        loginUser.setSysUser(sysUser);
        return loginUser;
    }
}

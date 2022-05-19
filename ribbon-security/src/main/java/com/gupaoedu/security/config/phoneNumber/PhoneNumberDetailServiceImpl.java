package com.gupaoedu.security.config.phoneNumber;

import com.gupaoedu.security.entity.LoginUser;
import com.gupaoedu.security.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/19 12:40
 **/
@Component
public class PhoneNumberDetailServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        SysUser sysUser = new SysUser();
        List<String> permissions = new ArrayList<>(Arrays.asList("admin","common"));

        //再封装成UserDetails实现类
        LoginUser loginUser = new LoginUser(sysUser,permissions);
        loginUser.getSysUser().setPhone(phoneNumber);
        loginUser.setSysUser(sysUser);
        return loginUser;
    }
}

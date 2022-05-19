package com.gupaoedu.security.config.phoneNumber;

import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/19 9:28
 **/
public class PhoneNumberCache implements UserCache {
    @Override
    public UserDetails getUserFromCache(String phoneNumber) {
        return null;
    }

    @Override
    public void putUserInCache(UserDetails user) {

    }

    @Override
    public void removeUserFromCache(String phoneNumber) {

    }
}

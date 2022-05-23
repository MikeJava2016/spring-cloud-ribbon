package com.sunshine.security.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 8:45
 **/
public interface UserOpenIdDetailsService {

      UserDetails loadUserByOpenId(String openId);
}

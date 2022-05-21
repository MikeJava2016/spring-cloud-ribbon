package com.gupaoedu.security.service.impl;

import com.gupaoedu.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 让每一个人的职业生涯不留遗憾
 *
 * @author 波波老师【咕泡学院】
 */
@Service
public class UserNamePasswordServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *   实现自定义的认证流程
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /*if(!"zhang".equals(username)){
            return null;
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority("ROLE_ROOT");
        authorities.add(auth);
        UserDetails user = new User(username
                ,"$2a$10$YOWyHqvtg.gqrbiSTlYQx.nu2j0psWsrs/JIiuzav7IDX7r93WGIe"
                ,true
                ,true
                ,true
                ,true
                ,authorities);
        return user;*/
        return null;
    }

    @Override
    public User queryByPhoneNumber(String mobile) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority("ROLE_ROOT");
        authorities.add(auth);
        User user = new User(mobile
                ,"$2a$10$YOWyHqvtg.gqrbiSTlYQx.nu2j0psWsrs/JIiuzav7IDX7r93WGIe"
                ,true
                ,true
                ,true
                ,true
                ,authorities);
        return user;
    }
}

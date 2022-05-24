package com.sunshine.security.config.phoneNumber;

import com.sunshine.security.entity.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @version v1
 * @Description 只是校验手机号
 * @Author huzhanglin
 * @Date 2022/5/20 21:30
 **/
@Component
public class PhoneNumberAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    @Qualifier("phoneNumnerUserDetailsService")
    private UserDetailsService userDetailsService;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PhoneNumberAuthenticationToken authenticationToken = (PhoneNumberAuthenticationToken) authentication;

        String mobile =  authenticationToken.getMobile();
        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);

        if (userDetails == null) {
            logger.error("无法根据手机号[{}]获取用户信息", mobile);
            throw new InternalAuthenticationServiceException("无法根据手机号获取用户信息");
        }
        LoginUser loginUser = (LoginUser) userDetails;
        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        PhoneNumberAuthenticationToken authenticationResult = new PhoneNumberAuthenticationToken.SmsCodeAuthenticationTokenBuilder()
                .username(loginUser.getUsername())
                .uid(loginUser.getSysUser().getId())
                .userDetails(userDetails)
                .authenticatedbuilder(authorities);

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
        return PhoneNumberAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

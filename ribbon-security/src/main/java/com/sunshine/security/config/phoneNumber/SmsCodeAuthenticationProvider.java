package com.sunshine.security.config.phoneNumber;

import com.sunshine.common.util.web.ApplicationContextUtils;
import com.sunshine.security.entity.LoginUser;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.Objects;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 21:30
 **/
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RedissonClient redissonClient = ApplicationContextUtils.getBean(RedissonClient.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

        String mobile = (String) authenticationToken.getPrincipal();
        String smsKey = "smscode:" + authenticationToken.getMobile();
        Object redisValue = redissonClient.getBucket(smsKey).get();
        if (Objects.isNull(redisValue)){
            logger.error("验证码已失效：", mobile);
            throw new InternalAuthenticationServiceException("验证码已失效");
        }
        if (!authenticationToken.getSmsCode().equals(redisValue)){
            logger.error("验证码不正确：", mobile);
            throw new InternalAuthenticationServiceException("验证码不正确");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);

        if (userDetails == null) {
            logger.error("无法根据手机号[{}]获取用户信息", mobile);
            throw new InternalAuthenticationServiceException("无法根据手机号获取用户信息");
        }
        LoginUser loginUser = (LoginUser) userDetails;
        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken.SmsCodeAuthenticationTokenBuilder()
                .username(loginUser.getUsername())
                .uid(loginUser.getSysUser().getId())
                .userDetails(userDetails)
                .authenticatedbuilder(authorities);

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailService() {
        return userDetailsService;
    }

    public void setUserDetailService(UserDetailsService userDetailService) {
        this.userDetailsService = userDetailService;
    }
}

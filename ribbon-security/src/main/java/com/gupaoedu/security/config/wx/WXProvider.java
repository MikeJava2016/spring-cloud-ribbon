package com.gupaoedu.security.config.wx;

import com.gupaoedu.security.entity.LoginUser;
import com.gupaoedu.security.service.UserOpenIdDetailsService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 8:54
 **/
@Data
public class WXProvider implements AuthenticationProvider {

    final private UserOpenIdDetailsService userOpenIdDetailsService;

    public WXProvider(UserOpenIdDetailsService userOpenIdDetailsService) {
        this.userOpenIdDetailsService = userOpenIdDetailsService;
    }

    /**
     * 取到authentication中的openId，根据openId查询信息，能查到信息表示登陆成功
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WXAuthenticationToken wxAuthenticationToken = (WXAuthenticationToken) authentication;
        LoginUser loginUser = (LoginUser) userOpenIdDetailsService.loadUserByOpenId(wxAuthenticationToken.getOpenId());
        if(loginUser == null){
            throw new RuntimeException("登陆失败");
        }
        return  new WXAuthenticationToken(loginUser);
    }

    /**
     * 配置当前Provider对应的wxAuthenticationToken
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (WXAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
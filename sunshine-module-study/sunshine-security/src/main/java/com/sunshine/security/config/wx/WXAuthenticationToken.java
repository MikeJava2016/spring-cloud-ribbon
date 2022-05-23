package com.sunshine.security.config.wx;

import com.sunshine.security.entity.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 8:38
 **/
public class WXAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String openId;

    public WXAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public WXAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }


    public WXAuthenticationToken(String openId) {
        super(null, null);
        this.openId = openId;
    }

    public WXAuthenticationToken(LoginUser loginUser) {
        super(loginUser.getUsername(), loginUser.getPassword(),loginUser.getAuthorities());
        this.openId = loginUser.getSysUser().getOpenId();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}

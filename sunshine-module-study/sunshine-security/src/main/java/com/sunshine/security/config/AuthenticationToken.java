package com.sunshine.security.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/21 14:33
 **/
public class AuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 531;

    protected final String username;

    protected final String uid;


    /**
     * 在 UsernamePasswordAuthenticationToken 中该字段代表登录的用户名，
     * 在这里就代表登录的手机号码
     */
    protected final Object principal;

    public AuthenticationToken(String username, String uid, Object principal,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = username;
        this.uid = uid;
        this.principal = principal;
    }


    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException("AuthenticationToken getCredentials");
//        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }
}

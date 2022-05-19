package com.gupaoedu.security.config.phoneNumber;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/19 9:00
 **/
public class PhoneNumberAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1503;


    private final Object principal;
    private Object credentials;
    private final Object uuid;


    public PhoneNumberAuthenticationToken(String phoneNumber, String code,String uuid) {
        super(null);
        this.principal = phoneNumber;
        this.credentials = code;
        this.uuid = uuid;
        setAuthenticated(false);
    }


    public PhoneNumberAuthenticationToken(Object phoneNumber, Object code,Object uuid,
                                               Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = phoneNumber;
        this.credentials = code;
        this.uuid = uuid;
        super.setAuthenticated(true); // must use super, as we override
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }

    public Object getUuid() {
        return uuid;
    }
}

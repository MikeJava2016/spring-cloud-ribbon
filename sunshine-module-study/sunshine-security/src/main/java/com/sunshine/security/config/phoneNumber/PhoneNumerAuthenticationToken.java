package com.sunshine.security.config.phoneNumber;

import com.sunshine.security.config.AuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @version v1
 * @Description 手机号校验
 * @Author huzhanglin
 * @Date 2022/5/20 21:30
 **/
public class PhoneNumerAuthenticationToken extends AuthenticationToken {

    private String mobile;


    public String getMobile() {
        return mobile;
    }

    private void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 构建一个没有鉴权的 SmsCodeAuthenticationToken
     */
    private PhoneNumerAuthenticationToken(Object principal, final String username, final String uid) {
        super(username, uid, principal, null);
        // must use super, as we override
        setAuthenticated(false);
    }

    /**
     * 构建拥有鉴权的 SmsCodeAuthenticationToken
     */
    private PhoneNumerAuthenticationToken(Collection<? extends GrantedAuthority> authorities, final Object principal, final String username, final String uid) {
        super(username, uid, principal, authorities);
        // must use super, as we override
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.principal;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
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
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }


    public static class SmsCodeAuthenticationTokenBuilder {
        /**
         * 手机号
         */
        private String mobile;

        /**
         * 用户名
         */
        private String username;

        /**
         * 用户id
         */
        private String uid;

        private Object principal;

        private UserDetails userDetails;


        public SmsCodeAuthenticationTokenBuilder() {
        }


        public SmsCodeAuthenticationTokenBuilder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public SmsCodeAuthenticationTokenBuilder username(String username) {
            this.username = username;
            return this;
        }

        public SmsCodeAuthenticationTokenBuilder uid(String uid) {
            this.uid = uid;
            return this;
        }

        public SmsCodeAuthenticationTokenBuilder principal(Object principal) {
            this.principal = principal;
            return this;
        }

        /**
         * 构建一个没有鉴权的
         *
         * @return
         */
        public PhoneNumerAuthenticationToken noAuthenticatedbuilder() {
            PhoneNumerAuthenticationToken phoneNumerAuthenticationToken = new PhoneNumerAuthenticationToken(principal, username, uid);
            phoneNumerAuthenticationToken.setMobile(mobile);
            phoneNumerAuthenticationToken.setDetails(userDetails);
            return phoneNumerAuthenticationToken;
        }

        /**
         * 构建一个有鉴权的
         *
         * @param authorities
         * @return
         */
        public PhoneNumerAuthenticationToken authenticatedbuilder(Collection<? extends GrantedAuthority> authorities) {
            PhoneNumerAuthenticationToken phoneNumerAuthenticationToken = new PhoneNumerAuthenticationToken(authorities, principal, username, uid);
            phoneNumerAuthenticationToken.setMobile(mobile);
            phoneNumerAuthenticationToken.setDetails(userDetails);
            return phoneNumerAuthenticationToken;
        }

        /**
         * 构建一个有鉴权的
         *
         * @param authorities
         * @return
         */
        public PhoneNumerAuthenticationToken authenticatedbuilder2(Collection<String> authorities) {
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            authorities.forEach(one -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(one));
            });
            PhoneNumerAuthenticationToken phoneNumerAuthenticationToken = new PhoneNumerAuthenticationToken(grantedAuthorities, principal, username, uid);
            phoneNumerAuthenticationToken.setMobile(mobile);
            phoneNumerAuthenticationToken.setDetails(userDetails);
            return phoneNumerAuthenticationToken;
        }

        /**
         * 构建一个有鉴权的
         *
         * @param
         * @return
         */
        public PhoneNumerAuthenticationToken authenticatedbuilder() {
            PhoneNumerAuthenticationToken phoneNumerAuthenticationToken = new PhoneNumerAuthenticationToken(null, principal, username, uid);
            phoneNumerAuthenticationToken.setMobile(mobile);
            phoneNumerAuthenticationToken.setDetails(userDetails);
            return phoneNumerAuthenticationToken;
        }

        public SmsCodeAuthenticationTokenBuilder userDetails(UserDetails userDetails) {
            this.userDetails = userDetails;
            return this;
        }
    }
}



package com.sunshine.security.config.phoneNumber;

import com.sunshine.security.config.AuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @version v1
 * @Description 短信验证吗
 * @Author huzhanglin
 * @Date 2022/5/20 21:30
 **/
public class SmsCodeAuthenticationToken extends AuthenticationToken {

    private String mobile;

    private String smsCode;

    public String getMobile() {
        return mobile;
    }

    private void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    /**
     * 构建一个没有鉴权的 SmsCodeAuthenticationToken
     */
    private SmsCodeAuthenticationToken(Object principal, final String username, final String uid) {
        super(username, uid, principal, null);
        // must use super, as we override
        setAuthenticated(false);
    }

    /**
     * 构建拥有鉴权的 SmsCodeAuthenticationToken
     */
    private SmsCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities, final Object principal, final String username, final String uid) {
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
         * 验证码
         */
        private String smsCode;

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

        public SmsCodeAuthenticationTokenBuilder smsCode(String smsCode) {
            this.smsCode = smsCode;
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
        public SmsCodeAuthenticationToken noAuthenticatedbuilder() {
            SmsCodeAuthenticationToken smsCodeAuthenticationToken = new SmsCodeAuthenticationToken(principal, username, uid);
            smsCodeAuthenticationToken.setMobile(mobile);
            smsCodeAuthenticationToken.setSmsCode(smsCode);
            smsCodeAuthenticationToken.setDetails(userDetails);
            return smsCodeAuthenticationToken;
        }

        /**
         * 构建一个有鉴权的
         *
         * @param authorities
         * @return
         */
        public SmsCodeAuthenticationToken authenticatedbuilder(Collection<? extends GrantedAuthority> authorities) {
            SmsCodeAuthenticationToken smsCodeAuthenticationToken = new SmsCodeAuthenticationToken(authorities, principal, username, uid);
            smsCodeAuthenticationToken.setMobile(mobile);
            smsCodeAuthenticationToken.setSmsCode(smsCode);
            smsCodeAuthenticationToken.setDetails(userDetails);
            return smsCodeAuthenticationToken;
        }

        /**
         * 构建一个有鉴权的
         *
         * @param authorities
         * @return
         */
        public SmsCodeAuthenticationToken authenticatedbuilder2(Collection<String> authorities) {
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            authorities.forEach(one -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(one));
            });
            SmsCodeAuthenticationToken smsCodeAuthenticationToken = new SmsCodeAuthenticationToken(grantedAuthorities, principal, username, uid);
            smsCodeAuthenticationToken.setMobile(mobile);
            smsCodeAuthenticationToken.setSmsCode(smsCode);
            smsCodeAuthenticationToken.setDetails(userDetails);
            return smsCodeAuthenticationToken;
        }

        /**
         * 构建一个有鉴权的
         *
         * @param
         * @return
         */
        public SmsCodeAuthenticationToken authenticatedbuilder() {
            SmsCodeAuthenticationToken smsCodeAuthenticationToken = new SmsCodeAuthenticationToken(null, principal, username, uid);
            smsCodeAuthenticationToken.setMobile(mobile);
            smsCodeAuthenticationToken.setSmsCode(smsCode);
            smsCodeAuthenticationToken.setDetails(userDetails);
            return smsCodeAuthenticationToken;
        }

        public SmsCodeAuthenticationTokenBuilder userDetails(UserDetails userDetails) {
            this.userDetails = userDetails;
            return this;
        }
    }
}



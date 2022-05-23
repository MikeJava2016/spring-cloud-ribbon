package com.sunshine.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 8:33
 **/
public class LoginUser extends User {
    private SysUser sysUser;

    private Collection<GrantedAuthority> authorities;

    private boolean accountNonExpired = true;

    public LoginUser(SysUser sysUser,Collection<GrantedAuthority> authorities) {
        super(sysUser.getUsername(),sysUser.getPassword(),authorities);
        if (Objects.isNull(sysUser)) {
            throw new IllegalArgumentException("参数异常");
        }

        if (!CollectionUtils.isEmpty(authorities)) {
            this.authorities=authorities;
        }
        this.sysUser = sysUser;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public Collection getAuthorities() {
        return authorities;
    }

    public LoginUser setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public LoginUser addAuthorities(GrantedAuthority authority) {
        this.authorities.add(authority);
        return this;
    }


    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

package com.gupaoedu.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 8:33
 **/
public class LoginUser implements UserDetails {
    private SysUser sysUser;

    List<GrantedAuthority> authorities;

    private boolean accountNonExpired = true;

    public LoginUser(SysUser sysUser, List<String> authorities) {
        if (Objects.isNull(sysUser)) {
            throw new IllegalArgumentException("参数异常");
        }
        this.sysUser = sysUser;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(authorities.size());
        if (!CollectionUtils.isEmpty(authorities)) {
            for (String authority : authorities) {
                grantedAuthorities.add(new SimpleGrantedAuthority(authority));
            }
        }
        this.authorities = grantedAuthorities;
    }


    public LoginUser() {
        this.sysUser = new SysUser();
        this.authorities = new ArrayList<>();
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
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
        return accountNonExpired;
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

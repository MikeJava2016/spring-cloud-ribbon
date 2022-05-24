package com.sunshine.security.config.phoneNumber;

import com.sunshine.security.entity.LoginUser;
import com.sunshine.security.entity.SysUser;
import com.sunshine.security.entity.UserModel;
import com.sunshine.security.service.RoleService;
import com.sunshine.security.service.UserModelService;
import com.sunshine.security.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 21:32
 **/
@Service("smsCodeUserDetailsService")
public class SmsCodeUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserModelService userModelService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        UserModel userModel = userModelService.getByPhone(phone);
        if (userModel == null) {
            logger.error(" 手机号不存在 phone = {}", phone);
            throw new UsernameNotFoundException("手机号不存在");
        }
        List<String> roles = userModelService.getRoleNames(userModel.getId());
        final Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (roles.size() > 0) {
            roles.stream().forEach(oname -> authorities.add(new SimpleGrantedAuthority(oname)));
        }
        SysUser sysUser = new SysUser();
        sysUser.setUsername(userModel.getUsername());
        sysUser.setId(userModel.getId().toString());
        sysUser.setUsername(userModel.getUsername());
        return new LoginUser(sysUser, authorities);
    }
}

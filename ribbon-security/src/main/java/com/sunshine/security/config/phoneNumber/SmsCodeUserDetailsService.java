package com.sunshine.security.config.phoneNumber;

import com.sunshine.security.entity.LoginUser;
import com.sunshine.security.entity.SysUser;
import com.sunshine.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 21:32
 **/
@Service("smsCodeUserDetailsService")
public class SmsCodeUserDetailsService  implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
  /*  @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
*/
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin"));
        // 从数据库中取出用户信息
        User user = userService.queryByPhoneNumber(phone);
        // 判断用户是否存在
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 添加权限
      /* List<UserRole> userRoles = userRoleService.listByUserId(user.getId());
        for (UserRole userRole : userRoles) {
            Role role = roleService.selectById(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }*/
        // 返回UserDetails实现类
        SysUser sysUser = new SysUser();
        sysUser.setUsername("huzhanglin");
        sysUser.setId("23");
        sysUser.setUsername(user.getUsername());
        sysUser.setPassword(user.getPassword());
        return new LoginUser(sysUser, authorities);
    }
}
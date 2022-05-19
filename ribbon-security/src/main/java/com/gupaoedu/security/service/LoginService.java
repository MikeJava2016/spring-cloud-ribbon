package com.gupaoedu.security.service;

import com.gupaoedu.security.entity.SysUser;
import com.sunshine.common.util.Result;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 8:28
 **/
public interface LoginService {

    /**
     * 用户名密码登陆
     * @param sysUser
     * @return
     */
      Result login(SysUser sysUser);


      Result phoneLogin(SysUser sysUser);

    /**
     * 微信登陆（利用id登陆）
     * @param sysUser
     * @return
     */
      Result wxLogin(SysUser sysUser);

    /**
     * 退出接口
     * @return
     */
     Result loginOut();
}

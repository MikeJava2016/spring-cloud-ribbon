package com.gupaoedu.security.service.impl;

import com.gupaoedu.security.config.wx.WXAuthenticationToken;
import com.gupaoedu.security.entity.LoginUser;
import com.gupaoedu.security.entity.SysUser;
import com.gupaoedu.security.service.LoginService;
import com.sunshine.common.util.JwtUtils;
import com.sunshine.common.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/18 8:30
 **/
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    @Qualifier("myAuthenticationManager")
    AuthenticationManager authenticationManager;

  /*  @Autowired
    RedisCache redisCache;*/

    @Override
    public Result login(SysUser sysUser) {
        //选则security带的token（继承Authentication）需要赋值用户名密码
        //选用不同的token，会通过循环找到token对应的provider，利用provider进行验证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword());
        String token = this.authenticate(usernamePasswordAuthenticationToken);
        return Result.success(token, "登录成功");
    }

    private String authenticate(UsernamePasswordAuthenticationToken authentication) {
        Authentication authenticate = authenticationManager.authenticate(authentication);
        //认证通过，使用userID生成token，再将token返回前端
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        SysUser sysUser1 = loginUser.getSysUser();
        //若果认证没通过给出提示
        if (sysUser1 == null) {
            throw new RuntimeException("登录失败");
        }
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("username", sysUser1.getUsername());
        String token = JwtUtils.getToken(claims);

        //完整的用户信息存入redis userId作为key
        //  redisCache.setCacheObject("login:"+sysUser1.getUserId(),loginUser);

        return token;
    }

    @Override
    public Result phoneLogin(SysUser sysUser) {
        return null;
    }

    @Override
    public Result wxLogin(SysUser sysUser) {
        //选则自定义的微信token（继承Authentication）只赋值openid即可
        //选用不同的token，会通过循环找到token对应的provider，利用provider进行验证
        String openId = sysUser.getOpenId();

        WXAuthenticationToken wxAuthenticationToken = new WXAuthenticationToken(openId);
        String token = this.authenticate(wxAuthenticationToken);
        return Result.success(token, "退出成功");
    }

    @Override
    public Result loginOut() {
        //获取SecurityContextHolder的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //删除redis值
        // redisCache.deleteObject("login:"+loginUser.getSysUser().getUserId());
        return Result.success("退出成功");
    }
}

package com.sunshine.controller;

import com.sunshine.api.feign.service.IUserAuthFeignService;
import com.sunshine.common.ao.LoginUserDto;
import com.sunshine.common.util.Result;
import com.sunshine.entity.User;
import com.sunshine.mapper.UserMapper;
import com.sunshine.utils.pwd.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 9:25
 **/
@RestController
public class UserAuthController implements IUserAuthFeignService {

    private final static Logger logger = LoggerFactory.getLogger(UserAuthController.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result validToken(String token) {
        if (StringUtils.isBlank(token)) {
            return Result.fail("token为空");
        }
        try {
            // key todo
            Map<String, Object> claims = JwtUtils.parseToken(token,"ribbon-service");
            return Result.success((claims.get("id").toString()));
        } catch (ExpiredJwtException e) {
            return Result.fail("token已过期");
        } catch (SignatureException e) {
            return Result.fail("签名校验失败");
        }
    }

    @Override
    public Result login(LoginUserDto loginUserDto) {
        User user = userMapper.selectByUsername(loginUserDto.getUsername());
        if (Objects.isNull(user)) {
            return Result.fail("用户名不存在");
        }
        if (!user.getPassword().equals(loginUserDto.getPassword())) {
            return Result.fail("用户名或者密码错误");
        }
        return Result.success(user);
    }
}

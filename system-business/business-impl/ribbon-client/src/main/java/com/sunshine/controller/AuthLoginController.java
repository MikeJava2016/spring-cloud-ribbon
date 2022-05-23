package com.sunshine.controller;

import com.sunshine.annotation.SunShine;
import com.sunshine.api.feign.service.AuthLoginClientAPI;
import com.sunshine.api.feign.service.IUserAuthFeignService;
import com.sunshine.common.annontation.Validate;
import com.sunshine.common.ao.LoginUserDto;
import com.sunshine.common.exception.BizException;
import com.sunshine.common.util.Result;
import com.sunshine.modules.login.AbstractLogin;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 14:38
 **/
@RequestMapping("/")
@RestController
public class AuthLoginController implements AuthLoginClientAPI {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Lazy
    private IUserAuthFeignService userAuthFeignService;
    /**
     * http://localhost:8080/hello/hello
     * @return
     */
    @SunShine(supported = false)
    public Result<String> login(@RequestBody LoginUserDto loginUserDto) {
        logger.info("HelloController hello");
        AbstractLogin abstractLogin = AbstractLogin.loginMap.get(loginUserDto.getLoginType());
        if (abstractLogin == null) {
            throw new BizException("暂不支持该种登录类型");
        }
        Result<String> result = abstractLogin.doLogin(loginUserDto);
        return result;
    }


    @Override
    public Result<String> validToken(String token) {
        if (StringUtils.isBlank(token)) {
            throw new BizException("token 为空");
        }
       return AbstractLogin.validToken(token);
    }

    @Override
    public Result<String> refreshToken(String token) {
        return AbstractLogin.refreshToken(token);
    }
}

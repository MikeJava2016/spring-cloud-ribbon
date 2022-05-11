package com.sunshine.modules.login;

import com.sunshine.api.feign.service.IUserAuthFeignService;
import com.sunshine.common.enums.LoginTypeEnum;
import com.sunshine.common.ao.LoginUserDto;
import com.sunshine.common.exception.BizException;
import com.sunshine.common.util.Result;
import com.sunshine.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 12:42
 **/

public class NormalLoginProcessor extends AbstractLogin{

    private final IUserAuthFeignService iUserAuthFeignService;

    public NormalLoginProcessor(IUserAuthFeignService iUserAuthFeignService) {
        this.iUserAuthFeignService = iUserAuthFeignService;

    }

    @Override
    public int getLoginType() {
        return LoginTypeEnum.NORMAL.getCode();
    }

    @Override
    public void validate(LoginUserDto authLoginDto) {
       /* if(StringUtils.isBlank(authLoginDto.getUsername())||StringUtils.isBlank(authLoginDto.getPassword())){
            throw new ValidException("帐号或者密码不能为空");
        }*/
    }

    @Override
    public Map doProcessor(LoginUserDto authLoginDto) {
        logger.info("begin NormalLoginProcessor.doProcessor:"+authLoginDto);
        Result<User> result = iUserAuthFeignService.login(authLoginDto);
        if (result.isSuccess()){
             Map<String, Object> map = new HashMap<>();
             map.put("id",result.getData().getId().toString());
             return map;
        }
        throw new BizException(result.getMessage());
    }
}

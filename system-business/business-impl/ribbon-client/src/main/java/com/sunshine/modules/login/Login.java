package com.sunshine.modules.login;

import com.sunshine.common.ao.LoginUserDto;
import com.sunshine.common.base.Result;
import com.sunshine.common.exception.BizException;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 12:27
 **/
public interface Login {

    Result doLogin(LoginUserDto loginUserDto) throws BizException;

}

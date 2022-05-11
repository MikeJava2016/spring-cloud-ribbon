package com.sunshine.modules.login;

import com.sunshine.common.exception.BizException;
import com.sunshine.common.util.Result;
import com.sunshine.common.ao.LoginUserDto;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 12:27
 **/
public interface Login {

    Result doLogin(LoginUserDto loginUserDto) throws BizException;

}

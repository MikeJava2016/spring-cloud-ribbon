package com.sunshine.common.ao;

import javax.validation.constraints.NotNull;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 10:21
 **/
public class LoginUserDto extends BaseAo {
    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "用户名不能为空")
    private String password;

    /**
     * @see com.sunshine.common.enums.LoginTypeEnum
     */
    @NotNull(message = "登录类型不能为空")
    private Integer loginType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    @Override
    public void validate() {

    }
}

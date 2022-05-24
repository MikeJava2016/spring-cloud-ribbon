package com.sunshine.security.service;

import com.sunshine.security.entity.RoleModel;
import com.sunshine.security.entity.UserModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description: 服务类
 */

public interface UserModelService extends IService<UserModel> {

    UserModel getByPhone(String phone);

    List<RoleModel> getRoles(Integer uid);

    List<String> getRoleNames(Integer uid);
}

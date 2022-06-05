package com.sunshine.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunshine.security.entity.UserRoleModel;
import com.sunshine.security.mapper.UserRoleMapper;
import com.sunshine.security.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description: 服务实现类
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleModel> implements UserRoleService {

}

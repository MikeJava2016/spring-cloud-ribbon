package com.sunshine.security.service.impl;

import com.sunshine.security.entity.UserRoleModel;
import com.sunshine.security.mapper.UserRoleMapper;
import com.sunshine.security.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description: 服务实现类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleModel> implements UserRoleService {

}

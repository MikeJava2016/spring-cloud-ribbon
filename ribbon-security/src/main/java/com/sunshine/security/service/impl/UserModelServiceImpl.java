package com.sunshine.security.service.impl;

import com.sunshine.security.entity.UserModel;
import com.sunshine.security.mapper.UserMapper;
import com.sunshine.security.service.UserModelService;
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
public class UserModelServiceImpl extends ServiceImpl<UserMapper, UserModel> implements UserModelService {

}

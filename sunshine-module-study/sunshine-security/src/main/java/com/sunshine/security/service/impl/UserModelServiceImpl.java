package com.sunshine.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunshine.security.entity.RoleModel;
import com.sunshine.security.entity.UserModel;
import com.sunshine.security.entity.UserRoleModel;
import com.sunshine.security.mapper.RoleMapper;
import com.sunshine.security.mapper.UserMapper;
import com.sunshine.security.mapper.UserRoleMapper;
import com.sunshine.security.service.UserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description: 服务实现类
 */
@Service
public class UserModelServiceImpl extends ServiceImpl<UserMapper, UserModel> implements UserModelService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserModel getByPhone(String phone) {
        QueryWrapper<UserModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        return getBaseMapper().selectOne(queryWrapper);
    }

    @Override
    public List<RoleModel> getRoles(Integer uid) {
        QueryWrapper<UserRoleModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        List<UserRoleModel> userRoleModels = userRoleMapper.selectList(queryWrapper);
        if (userRoleModels.size()>0){
            List<Integer> rids = userRoleModels.stream().map(UserRoleModel::getRid).collect(Collectors.toList());
            if (rids.size() > 0) {
                List<RoleModel> roleModels = roleMapper.selectBatchIds(rids);
                return roleModels;
            }
        }
        List<RoleModel> list = new ArrayList<>();
        return list;
    }

    @Override
    public List<String> getRoleNames(Integer uid) {
        List<RoleModel> roles = this.getRoles(uid);
        if (roles == null || roles.size() == 0){
            return new ArrayList<>();
        }
        return roles.stream().map(RoleModel::getName).collect(Collectors.toList());
    }
}

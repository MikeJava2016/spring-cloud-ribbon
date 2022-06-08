package com.sunshine.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunshine.security.entity.DeptModel;
import com.sunshine.security.mapper.DeptMapper;
import com.sunshine.security.service.DeptService;
import com.sunshine.utils.common.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, DeptModel> implements DeptService {
    @Override
    public List<DeptModel> selectByCondition(DeptModel deptModel) {
        return getBaseMapper().selectByCondition(deptModel);
    }

    @Override
    @Transactional(rollbackFor ={ BaseException.class,RuntimeException.class})
    public boolean onSave(DeptModel deptModel) {
        int insert = getBaseMapper().insert(deptModel);
        return true;
    }
}

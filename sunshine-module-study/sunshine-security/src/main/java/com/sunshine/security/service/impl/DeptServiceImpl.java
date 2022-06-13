package com.sunshine.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunshine.security.entity.DeptModel;
import com.sunshine.security.mapper.DeptMapper;
import com.sunshine.security.service.DeptService;
import com.sunshine.utils.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DeptServiceImpl extends ServiceImpl<DeptMapper, DeptModel> implements DeptService {
    @Override
    public List<DeptModel> selectByCondition(DeptModel deptModel) {
        return getBaseMapper().selectByCondition(deptModel);
    }

    @Override
    @Transactional(rollbackFor ={ BaseException.class,RuntimeException.class})
    @ShardingTransactionType(TransactionType.XA)
    public boolean onSave(DeptModel deptModel) {
//        log.info("全局事务id ：" + RootContext.getXID());
        int insert = getBaseMapper().insert(deptModel);
        return true;
    }
}

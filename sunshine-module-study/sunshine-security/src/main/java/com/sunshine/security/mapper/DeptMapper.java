package com.sunshine.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunshine.security.entity.DeptModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptMapper extends BaseMapper<DeptModel> {
    List<DeptModel> selectByCondition(@Param("deptModel") DeptModel deptModel);
}

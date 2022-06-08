package com.sunshine.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunshine.security.entity.DeptModel;

import java.util.List;

/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description: 服务类
 */
public interface DeptService extends IService<DeptModel> {
   List<DeptModel> selectByCondition(DeptModel status);


   boolean onSave(DeptModel deptModel);
}

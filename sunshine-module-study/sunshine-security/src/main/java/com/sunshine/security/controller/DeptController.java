package com.sunshine.security.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunshine.common.util.Result;
import com.sunshine.security.config.common.AuthenticationToken;
import com.sunshine.security.entity.DeptModel;
import com.sunshine.security.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @PostMapping
    public Result<DeptModel> save(@RequestBody DeptModel deptModel) {
        deptModel.setCreateTime(new Date());
        deptModel.setUpdateTime(new Date());
        AuthenticationToken authentication = (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getUid();
        long createId = Long.parseLong(uid);
        deptModel.setCreateId(createId);
        deptModel.setUpdateId(createId);
        boolean save = deptService.onSave(deptModel);
        if (save) {
            return Result.success(deptModel);
        }
        return Result.fail("保存失败");
    }

    @GetMapping
    public Result<List<DeptModel>> get(DeptModel deptModel) {
        List<DeptModel> list = deptService.selectByCondition(deptModel);
        if (CollectionUtil.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                DeptModel one = list.get(i);
                if (one.getLevel().equals(1)) {
                    QueryWrapper queryWrapper2 = new QueryWrapper();
                    queryWrapper2.eq("status1", 1);
                    queryWrapper2.eq("p_id",one.getId());
                    List<DeptModel> list2 = deptService.list(queryWrapper2);
                    one.setChildren(list2);
                }
            }
        }
        return Result.success(list);

    }

}

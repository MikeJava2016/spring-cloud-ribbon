package com.sunshine.security.controller;

import com.sunshine.common.util.Result;
import com.sunshine.security.entity.DeptModel;
import com.sunshine.security.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @PostMapping
    public Result<DeptModel> save(@RequestBody DeptModel deptModel) {
        deptModel.setCreateTime(new Date());
        deptModel.setUpdateTime(new Date());
        boolean save = deptService.save(deptModel);
        if (save) {
            return Result.success(deptModel);
        }
        return Result.fail("保存失败");
    }

}

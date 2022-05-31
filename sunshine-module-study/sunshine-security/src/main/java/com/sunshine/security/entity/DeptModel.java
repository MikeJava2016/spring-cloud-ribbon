package com.sunshine.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description:
 */
@Data
@TableName("dept")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@ApiModel(value = "dept对象", description = "")
public class DeptModel extends BaseData {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("dept_name1")
    private String deptName1;

    // 1 启用  2 禁用
    @TableField("status1")
    private int status1;

    @TableField("sort1")
    private int sort1;

    @TableField("describe1")
    private String describe1;

    @TableField("p_id")
    private Integer pId;
    /**
     * 部门等级 1 2 3 4
     */
    @TableField("level")
    private Integer level;

    @TableField("responsible_person")
    private String responsiblePerson;

    @TableField("mobile")
    private String mobile;

    @TableField("email")
    private String email;

    @TableField(exist = false)
    private List<DeptModel> children;

}

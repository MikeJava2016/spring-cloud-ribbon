package com.sunshine.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description: 
 */
@Data
@TableName("role")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@ApiModel(value="RoleModel对象", description="")
public class RoleModel implements Serializable {

private static final long serialVersionUID = 1L;

                @TableId(value = "id", type = IdType.AUTO)
                private Integer id;

    @TableField("name")
        private String name;

    @TableField("nameZh")
        private String nameZh;


        }

package com.sunshine.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: huzhanglin
 * @Date: 2022-05-22
 * @Description: 
 */
@Data
@TableName("user_role")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@ApiModel(value="UserRoleModel对象", description="")
public class UserRoleModel implements Serializable {

private static final long serialVersionUID = 1L;

                @TableId(value = "id", type = IdType.AUTO)
                private Integer id;

    @TableField("uid")
        private Integer uid;

    @TableField("rid")
        private Integer rid;


        }

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
@TableName("user")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@ApiModel(value="UserModel对象", description="")
public class UserModel implements Serializable {

private static final long serialVersionUID = 1L;

                @TableId(value = "id", type = IdType.AUTO)
                private Integer id;

    @TableField("username")
        private String username;

    @TableField("password")
        private String password;

    @TableField("enabled")
        private Boolean enabled;

    @TableField("locked")
        private Boolean locked;


        }

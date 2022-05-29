package com.sunshine.security.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BaseData implements Serializable {
    /**
     * 创建
     */
    @TableField("create_id")
    private Long createId;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_id")
    private Long updateId;

    @TableField("update_time")
    private Date updateTime;
}

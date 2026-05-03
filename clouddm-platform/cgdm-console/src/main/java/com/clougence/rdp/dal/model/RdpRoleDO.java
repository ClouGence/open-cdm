package com.clougence.rdp.dal.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/6 18:50
 */
@Getter
@Setter
@TableName(value = "rdp_role", autoResultMap = true)
public class RdpRoleDO {

    @TableId(type = IdType.AUTO)
    private Long         id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date         gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date         gmtModified;

    private String       ownerUid;

    private String       roleName;

    private String       aliasName;

    private boolean      innerTag;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> roleAuthLabels;

}

package com.clougence.clouddm.console.web.dal.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/2/29 11:48
 */
@Getter
@Setter
@TableName(value = "dm_res_auth")
public class DmResAuthDO {

    @TableId(type = IdType.AUTO)
    private Long         id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date         gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date         gmtModified;

    private String       ownerUid;

    private Long         resId;

    private String       resInstId;

    private String       resDesc;

    private String       resPath;

    private String       level1;

    private String       level2;

    private String       level3;

    private String       level4;

    private AuthKind     kindType;

    private Date         startTime;

    private Date         endTime;

    @TableField(value = "res_auth_label", typeHandler = JacksonTypeHandler.class)
    private List<String> authLabels = new ArrayList<>();
}

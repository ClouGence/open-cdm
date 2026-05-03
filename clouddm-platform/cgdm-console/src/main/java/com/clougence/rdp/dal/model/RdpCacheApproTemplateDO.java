package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;
import com.clougence.rdp.dal.handler.RdpApprovalTypeHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/5/18 14:42
 */
@Getter
@Setter
@TableName(value = "rdp_cache_appro_template")
public class RdpCacheApproTemplateDO {

    @TableId(type = IdType.AUTO)
    private Long            id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date            gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date            gmtModified;

    private String          primaryUid;

    @TableField(typeHandler = RdpApprovalTypeHandler.class)
    private RdpApprovalType approvalType;

    private String          templateName;

    private String          templateIdentity;

    private String          approUrl;

    private String          templateContent;
}

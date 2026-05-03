package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "rdp_ticket_auth", autoResultMap = true)
public class RdpAuthTicketDO {

    @TableId(type = IdType.AUTO)
    private Long     id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date     gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date     gmtModified;

    private String   rdpTicketInsId;

    private String   applyAuthInfo;

    private AuthKind kindType;

    private Boolean  deleted;
}

package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.dal.enumeration.RegisterStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "rdp_sso_register")
public class RdpSsoRegisterDO {

    @TableId(type = IdType.AUTO)
    private Long           id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtModified;

    private String         requestId;
    private String         unionId;
    private String         nickname;
    private RegisterStatus registerStatus;
}

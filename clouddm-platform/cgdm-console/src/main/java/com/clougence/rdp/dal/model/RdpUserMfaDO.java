package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.dal.enumeration.mfa.MfaStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/13 09:55
 */
@Getter
@Setter
@TableName(value = "rdp_user_mfa")
public class RdpUserMfaDO {

    @TableId(type = IdType.AUTO)
    private Long      id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date      gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date      gmtModified;

    private Long      userId;

    private String    uid;

    private MfaStatus mfaStatus;

    private String    mfaKey;

    private String    resetMfaKey;
}

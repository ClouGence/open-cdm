package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.enumeration.AreaCode;

import lombok.Data;

/**
 * @author bucketli 2020/2/27 19:53
 */
@Data
@TableName(value = "rdp_verify_code")
public class RdpVerifyDO {

    @TableId(type = IdType.AUTO)
    private Long           id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtModified;

    private AccountType    accountType;

    private String         uid;

    private VerifyType     verifyType;

    private String         email;

    private String         phone;

    private AreaCode       phoneAreaCode;

    private String         verifyCode;

    private VerifyCodeType verifyCodeType;

    private Date           verifyCodeSendTime;

    private Integer        failTimes;

    private Date           lastFailDate;
}

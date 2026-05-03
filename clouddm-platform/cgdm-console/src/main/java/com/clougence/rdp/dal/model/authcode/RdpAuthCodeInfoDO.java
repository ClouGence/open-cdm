package com.clougence.rdp.dal.model.authcode;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

@Data
@TableName(value = "rdp_auth_code_info")
public class RdpAuthCodeInfoDO {

    @TableId(type = IdType.AUTO)
    private Long    id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtModified;

    private Boolean remind;

    private String  authCode;

    private String  secondAuthCode;

}

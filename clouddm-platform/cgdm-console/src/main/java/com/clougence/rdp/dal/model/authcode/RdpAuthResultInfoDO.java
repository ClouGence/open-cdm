package com.clougence.rdp.dal.model.authcode;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

@Data
@Deprecated
@TableName(value = "rdp_auth_result_info")
public class RdpAuthResultInfoDO {

    @TableId(type = IdType.AUTO)
    private Long    id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtModified;

    private Date    reportTime;

    private Boolean active;

    private String  authResultStatus;

    private String  msg;

    private Boolean success;

}

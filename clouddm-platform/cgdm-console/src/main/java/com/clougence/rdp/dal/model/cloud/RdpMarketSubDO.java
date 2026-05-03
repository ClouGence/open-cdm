package com.clougence.rdp.dal.model.cloud;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Deprecated
@TableName(value = "rdp_market_sub")
public class RdpMarketSubDO {

    @TableId(type = IdType.AUTO)
    private Long   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtModified;

    private String uid;

    private String marketType;

    private String awsCustomerId;

    private String awsProductCode;

    private String awsAccountId;

    private String subStatus;
}

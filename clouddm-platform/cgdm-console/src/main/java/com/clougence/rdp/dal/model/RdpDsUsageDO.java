package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.base.metadata.rdp.enumeration.DsUsageEndpoint;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;

import lombok.Data;

/**
 * @author wanshao create time is 2019/12/11 10:11 下午 finished
 **/
@Data
@TableName(value = "rdp_ds_usage")
public class RdpDsUsageDO {

    @TableId(type = IdType.AUTO)
    private Long            id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date            gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date            gmtModified;

    private Long            dsId;

    private ResourceType    resType;

    private Long            resId;

    private String          resInstanceId;

    private DsUsageEndpoint endpoint;
}

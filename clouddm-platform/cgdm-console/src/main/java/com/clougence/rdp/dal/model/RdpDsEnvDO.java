package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.constant.auth.CanBeReplaced;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/13 09:55
 */
@Getter
@Setter
@TableName(value = "rdp_ds_env")
public class RdpDsEnvDO {

    @TableId(type = IdType.AUTO)
    private Long   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtModified;

    @CanBeReplaced
    private String ownerUid;

    private String envName;

    private String description;
}

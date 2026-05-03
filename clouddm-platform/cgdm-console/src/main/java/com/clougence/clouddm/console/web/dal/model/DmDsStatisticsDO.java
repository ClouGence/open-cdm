package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/8/29 15:50
 */
@Getter
@Setter
@TableName(value = "dm_ds_statistics")
public class DmDsStatisticsDO {

    @TableId(type = IdType.AUTO)
    private Long   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtModified;

    private Long   dataSourceId;

    private String dataSourceType;

    private Date   lastTime;

    private Long   execCounts;
}

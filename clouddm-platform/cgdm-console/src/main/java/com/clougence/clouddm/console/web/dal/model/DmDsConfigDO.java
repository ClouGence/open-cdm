package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.dal.enumeration.DataSourceStatus;
import com.clougence.rdp.dal.enumeration.HostType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
@TableName(value = "dm_ds_config")
public class DmDsConfigDO {

    @TableId(type = IdType.AUTO)
    private Long             id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtModified;

    private String           uid;

    private Long             dataSourceId;

    private DataSourceType   dataSourceType;

    private DataSourceStatus status;

    private String           statusMessage;

    private String           configInstanceId;

    private long             bindClusterId;

    private long             bindEnvId;

    private HostType         hostType;

    private boolean          enableDevops;
}

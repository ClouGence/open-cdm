package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;
import com.clougence.rdp.constant.KvConfValType;

import lombok.Data;

/**
 * @author bucketli 2020/11/9 13:23
 */
@Data
@TableName(value = "rdp_ds_kv_base_config")
public class RdpDsKvBaseConfigDO {

    @TableId(type = IdType.AUTO)
    private Long          id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date          gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date          gmtModified;

    private Long          dataSourceId;

    private String        configName;

    private DsConfigGroup configGroup;

    private boolean       display;

    private String        descKey;

    private boolean       valueRequire;

    private String        valueValidRegex;

    private String        configValue;

    private String        defaultValue;

    private String        valueAdvance;

    private boolean       readOnly;

    private boolean       isSecret;

    private KvConfValType confValType;
}

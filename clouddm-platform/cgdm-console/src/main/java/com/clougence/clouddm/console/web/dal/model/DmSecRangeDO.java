package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.dal.enumeration.SecMatchMode;
import com.clougence.clouddm.console.web.dal.enumeration.SecRangeType;
import com.clougence.clouddm.console.web.dal.handler.SecRangeTypeHandler;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/7/10 15:19
*/
@Getter
@Setter
@TableName(value = "dm_sec_range")
public class DmSecRangeDO {

    @TableId(type = IdType.AUTO)
    private Long           id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtModified;

    private String         ownerUid;

    private long           refSpec;

    private long           refId;

    private SecMatchMode   matchMode;

    @TableField(typeHandler = SecRangeTypeHandler.class)
    private SecRangeType   rangeType;

    private String         levelPrefix;

    @TableField(value = "level_nodes", typeHandler = JacksonTypeHandler.class)
    private List<String>   levelNodes;

    private boolean        chooseAll;

    @TableField("ref_ds_type")
    private DataSourceType referDsType;

    @TableField("table_level")
    private TargetType     tableLevelType; // Table or View or Materialized
}

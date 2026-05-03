package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.MetaInformationType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_meta_information_cache")
public class DmMetaInformationCacheDO {

    @TableId(type = IdType.AUTO)
    private Long                id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                gmtModified;

    private String              primaryUid;

    private Long                dsId;

    private String              path;

    private MetaInformationType type;

    private String              context;
}

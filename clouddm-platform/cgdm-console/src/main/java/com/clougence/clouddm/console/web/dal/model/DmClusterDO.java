package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.constants.CloudOrIdcName;

import lombok.Getter;
import lombok.Setter;

/**
 * The type ClusterDO do.
 *
 * @author wanshao create time is 2019/12/11 10:10 下午 finish
 */
@Getter
@Setter
@TableName(value = "dm_cluster")
public class DmClusterDO {

    @TableId(type = IdType.AUTO)
    private Long           id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtModified;

    private String         clusterName;

    private String         clusterDesc;

    private String         region;

    private CloudOrIdcName cloudOrIdcName;

    private String         uid;
}

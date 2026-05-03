package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.DmProjectVersionType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
@TableName(value = "dm_project_version")
public class DmProjectVersionDO {

    @TableId(type = IdType.AUTO)
    private Long                 id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                 gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                 gmtModified;

    @TableField("owner_uid")
    private String               ownerUid;

    @TableField("ref_project_id")
    private long                 refProjectId;

    @TableField("ref_devops_id")
    private long                 refDevopsId;

    @TableField("ref_change_id")
    private long                 refChangeId;

    @TableField("version")
    private Date                 version;

    @TableField("commit_id")
    private String               commitId;

    @TableField("content")
    private String               content;

    @TableField("type")
    private DmProjectVersionType type;
}

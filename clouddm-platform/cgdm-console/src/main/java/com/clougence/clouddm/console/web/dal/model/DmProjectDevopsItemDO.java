package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
@TableName(value = "dm_project_devops_item")
public class DmProjectDevopsItemDO {

    @TableId(type = IdType.AUTO)
    private Long   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date   gmtModified;

    @TableField("owner_uid")
    private String ownerUid;

    @TableField("ref_project_id")
    private long   refProjectId;

    @TableField("ref_devops_id")
    private long   refDevopsId;

    @TableField("content_name")
    private String contentName;

    @TableField("content_index")
    private int    contentIndex;

    @TableField("content")
    private String content;
}

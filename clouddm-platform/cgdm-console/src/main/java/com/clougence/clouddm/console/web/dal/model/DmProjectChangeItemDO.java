package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeItemType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020/11/9 13:23
 */
@Getter
@Setter
@TableName(value = "dm_project_change_item")
public class DmProjectChangeItemDO {

    @TableId(type = IdType.AUTO)
    private Long             id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtModified;

    @TableField("owner_uid")
    private String           ownerUid;

    @TableField("ref_project_id")
    private long             refProjectId;

    @TableField("ref_change_id")
    private long             refChangeId;

    @TableField("ref_change_item_type")
    private DmChangeItemType changeItemType;

    @TableField("content_name")
    private String           contentName;

    @TableField("content_index")
    private int              contentIndex;

    @TableField("content")
    private String           content;
}

package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.ImType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
@TableName(value = "dm_project_msg")
public class DmProjectMsgDO {

    @TableId(type = IdType.AUTO)
    private Long    id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtModified;

    @TableField("owner_uid")
    private String  ownerUid;

    @TableField("ref_project_id")
    private long    refProjectId;

    @TableField("ref_msg_id")
    private long    refMsgId;

    @TableField("ref_msg_type")
    private ImType  refMsgType;

    @TableField("enable")
    private boolean enable;

    @TableField("language")
    private String  language;

    @TableField("event_project_status")
    private boolean eventProjectStatus;

    @TableField("event_project_config")
    private boolean eventProjectConfig;

    @TableField("event_change_life")
    private boolean eventChangeLife;

    @TableField("event_change_notice")
    private boolean eventChangeNotice;
}

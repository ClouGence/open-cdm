package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStatus;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStep;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020/11/9 13:23
 */
@Getter
@Setter
@TableName(value = "dm_project_change")
public class DmProjectChangeDO {

    @TableId(type = IdType.AUTO)
    private Long                      id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                      gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                      gmtModified;

    @TableField("owner_uid")
    private String                    ownerUid;

    @TableField("ref_project_id")
    private long                      refProjectId;

    @TableField("ref_devops_id")
    private long                      refDevopsId;

    @TableField("change_name")
    private String                    changeName;

    @TableField("change_branch")
    private String                    changeBranch;

    @TableField("change_time")
    private Date                      changeTime;

    @TableField("current_step")
    private ProjectChangeStep         currentStep;

    @TableField("current_status")
    private ProjectChangeStatus       currentStatus;

    @TableField("schedule_time")
    private Date                      scheduleTime;

    @TableField("version")
    private int                       version;

    @TableField("remark")
    private String                    remark;

    @TableField("try_times")
    private int                       tryTimes;

    @TableField("last_commit_id")
    private String                    lastCommitId;

    @TableField("lock_status")
    private boolean                   lockStatus;

    @TableField(value = "flow_walked", typeHandler = JacksonTypeHandler.class)
    private DmProjectChangeFlowWalked flowWalked;
}

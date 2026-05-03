package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeApproveStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeCheckStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeExecStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
@TableName(value = "dm_project")
public class DmProjectDO {

    @TableId(type = IdType.AUTO)
    private Long                    id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                    gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                    gmtModified;

    @TableField("owner_uid")
    private String                  ownerUid;

    @TableField("project_uid")
    private String                  projectUid;

    @TableField("project_code")
    private String                  projectCode;

    @TableField("project_name")
    private String                  projectName;

    @TableField("project_desc")
    private String                  projectDesc;

    @TableField("project_status")
    private ProjectStatus           projectStatus;

    @TableField("project_mark")
    private String                  projectMark;

    @TableField("flow_check")
    private DmChangeCheckStrategy   flowCheck;

    @TableField("flow_approve")
    private DmChangeApproveStrategy flowApprove;

    @TableField("flow_execute")
    private DmChangeExecStrategy    flowExecute;

    @TableField(value = "options", typeHandler = JacksonTypeHandler.class)
    private DmProjectOption         options;
}

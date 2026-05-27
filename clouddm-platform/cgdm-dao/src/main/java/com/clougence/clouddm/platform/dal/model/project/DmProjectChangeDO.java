/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.platform.dal.model.project;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.platform.dal.model.project.ProjectChangeStatus;
import com.clougence.clouddm.platform.dal.model.project.ProjectChangeStep;

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
    private RsProjectChangeFlowWalkedObj flowWalked;
}

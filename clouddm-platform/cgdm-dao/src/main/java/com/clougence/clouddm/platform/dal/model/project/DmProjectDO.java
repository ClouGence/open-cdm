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
import com.clougence.clouddm.platform.dal.model.project.ChangeApproveStrategy;
import com.clougence.clouddm.platform.dal.model.project.ChangeCheckStrategy;
import com.clougence.clouddm.platform.dal.model.project.ChangeExecStrategy;
import com.clougence.clouddm.platform.dal.model.project.ProjectStatus;

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
    private ChangeCheckStrategy   flowCheck;

    @TableField("flow_approve")
    private ChangeApproveStrategy flowApprove;

    @TableField("flow_execute")
    private ChangeExecStrategy    flowExecute;

    @TableField(value = "options", typeHandler = JacksonTypeHandler.class)
    private RsProjectOptionObj         options;
}

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
package com.clougence.clouddm.platform.dal.model.execution;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.platform.dal.model.execution.AsyncTaskProcessType;
import com.clougence.clouddm.platform.dal.model.execution.AsyncTaskStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * The type async_task do.
 *
 * @author mode create time is 2019/12/11 10:10 下午 finish
 */
@Getter
@Setter
@TableName(value = "dm_exec_async_task")
public class DmExecAsyncTaskDO {

    @TableId(type = IdType.AUTO)
    private Long                   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                   gmtModified;

    private String                 title;

    private String                 description;

    private String                 bizId;

    private String                 bizType;

    private String                 dependOnBizId;

    private String                 dependOnBizType;

    @TableField(value = "owner_uid")
    private String                 uid;

    private String                 handlerName;

    private String                 handlerType;

    private String                 configData;

    private boolean                showInDock;

    private AsyncTaskProcessType processType;

    private String                 processValue;

    private boolean                fastFail;

    /**
     * Legacy field from the old console ownership model for async tasks.
     * Retained only for backward compatibility during single-console cleanup.
     */
    @Deprecated
    private String                 consoleIp;

    private AsyncTaskStatus      status;

    private String                 statusMsg;

    private Date                   timeOfStart;

    private Date                   timeOfLast;

    private Date                   timeOfFinish;
}

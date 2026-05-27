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
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecType;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_exec_auto_job")
public class DmExecAutoJobDO {

    @TableId(type = IdType.AUTO)
    private Long              id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtModified;
    private Long              dataSourceId;
    private String            uid;
    private String            bizId;
    private SQLJobBizType     dependOnBizType;
    private String            dependOnBizId;
    private AutoExecJobStatus status;
    private Date              lastReportTime;
    private String            workerSeqNumber;
    private AutoExecType    execType;
    private Date              endTime;
    private Date              scheduleTime;
    private String            queryId;
    private String            primaryUid;
    private Boolean           normal;

    @TableField(value = "levels", typeHandler = JacksonTypeHandler.class)
    private List<String>      levels;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private RsExecAutoJobConfigObj config;
}

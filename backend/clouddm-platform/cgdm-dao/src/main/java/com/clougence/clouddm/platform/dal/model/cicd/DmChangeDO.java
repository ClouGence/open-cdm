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
package com.clougence.clouddm.platform.dal.model.cicd;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_change")
public class DmChangeDO {
    @TableId(type = IdType.AUTO)
    private Long         id;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date         gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date         gmtModified;
    @TableField("owner_uid")
    private String       ownerUid;
    @TableField("trigger_uid")
    private String       triggerUid;
    @TableField("ref_flow_id")
    private long         refFlowId;
    @TableField("ref_batch_id")
    private Long         refBatchId;
    @TableField("ref_parent_change_id")
    private Long         refParentChangeId;
    @TableField("change_name")
    private String       changeName;
    @TableField("change_time")
    private Date         changeTime;
    @TableField("change_branch")
    private String       changeBranch;
    @TableField("current_step")
    private ChangeStep   currentStep;
    @TableField("current_status")
    private ChangeStatus currentStatus;
    @TableField("schedule_time")
    private Date         scheduleTime;
    @TableField("version")
    private int          version;
    @TableField("remark")
    private String       remark;
    @TableField("try_times")
    private int          tryTimes;
    @TableField("last_commit_id")
    private String       lastCommitId;
    @TableField("lock_status")
    private boolean      lockStatus;
}

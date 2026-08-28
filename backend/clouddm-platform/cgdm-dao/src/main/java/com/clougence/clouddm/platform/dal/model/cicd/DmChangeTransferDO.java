/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.clouddm.platform.dal.model.cicd;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("dm_change_transfer")
public class DmChangeTransferDO {
    @TableId(type = IdType.AUTO)
    private Long                 id;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                 gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                 gmtModified;
    @TableField("owner_uid")
    private String               ownerUid;
    @TableField("ref_batch_id")
    private long                 refBatchId;
    @TableField("ref_source_flow_id")
    private long                 refSourceFlowId;
    @TableField("ref_source_change_id")
    private long                 refSourceChangeId;
    @TableField("ref_target_flow_id")
    private long                 refTargetFlowId;
    @TableField("ref_target_change_id")
    private Long                 refTargetChangeId;
    @TableField("transfer_status")
    private ChangeTransferStatus transferStatus;
    @TableField("schedule_time")
    private Date                 scheduleTime;
    @TableField("try_times")
    private int                  tryTimes;
    @TableField("last_error")
    private String               lastError;
}

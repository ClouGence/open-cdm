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
@TableName("dm_change_batch")
public class DmChangeBatchDO {
    @TableId(type = IdType.AUTO)
    private Long              id;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtModified;
    @TableField("owner_uid")
    private String            ownerUid;
    @TableField("ref_root_flow_id")
    private long              refRootFlowId;
    @TableField("ref_root_change_id")
    private long              refRootChangeId;
    @TableField("batch_status")
    private ChangeBatchStatus batchStatus;
}

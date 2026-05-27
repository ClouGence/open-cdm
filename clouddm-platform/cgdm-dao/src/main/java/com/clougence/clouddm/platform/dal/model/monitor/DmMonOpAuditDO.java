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
package com.clougence.clouddm.platform.dal.model.monitor;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/4/13 12:25
 */
@Getter
@Setter
@TableName(value = "dm_mon_op_audit")
public class DmMonOpAuditDO {

    @TableId(type = IdType.AUTO)
    private Long          id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date          gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date          gmtModified;

    /**
     * operate user
     */
    private String        uid;

    private String        ownerUid;

    private String        userName;

    private Date          operateDate;

    private String        sourceIp;

    /**
     * log detail info path for console
     */
    private String        ip;

    private String        contextIn;

    private String        contextOut;

    /**
     * audi type
     * eg: CREATE_JOB、START_JOB
     */
    private AuditType     auditType;

    /**
     * resource type
     * eg: DataJob、DataSource、Account
     */
    private ResourceType  resourceType;

    /**
     * resource id
     * eg: DataJobId、DataSourceId、AccountUid
     */
    private String        resourceValue;

    private String        resourceName;

    private String        operationUri;

    private SecurityLevel securityLevel;

    private String        uuidKey;

    private String        logInfo;

    private String        logPath;
}

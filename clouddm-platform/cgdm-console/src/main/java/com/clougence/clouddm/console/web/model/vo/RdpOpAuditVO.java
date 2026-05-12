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
package com.clougence.clouddm.console.web.model.vo;

import java.util.Date;

import com.clougence.clouddm.console.web.global.jwtsession.SecurityLevel;
import com.clougence.clouddm.console.web.dal.model.RdpOpAuditDO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/4/13 13:26
 */
@Getter
@Setter
public class RdpOpAuditVO {

    private Long          id;

    private Date          gmtCreate;

    private Date          gmtModified;

    private String        uid;

    private String        userName;

    private Date          operateDate;

    private String        sourceIp;

    private String        contextIn;

    private String        contextOut;

    private String        resourceValue;

    private String        resourceName;

    private String        auditType;

    private String        auditTypeDesc;

    private String        resourceType;

    private String        resourceTypeDesc;

    private SecurityLevel securityLevel;

    private String        operationUri;

    private String        uuidKey;

    private Boolean       isExistsLog;

    private ResourceVO    resourceVO;

    private String        logPathWorkerIp;

    public RdpOpAuditVO convertFromDO(RdpOpAuditDO auditDO) {
        this.id = auditDO.getId();
        this.gmtCreate = auditDO.getGmtCreate();
        this.gmtModified = auditDO.getGmtModified();
        this.uid = auditDO.getUid();
        this.userName = auditDO.getUserName();
        this.operateDate = auditDO.getOperateDate();
        this.contextIn = auditDO.getContextIn();
        this.contextOut = auditDO.getContextOut();
        this.resourceValue = auditDO.getResourceValue();
        this.resourceName = auditDO.getResourceName();
        this.sourceIp = auditDO.getSourceIp();
        this.securityLevel = auditDO.getSecurityLevel();
        this.uuidKey = auditDO.getUuidKey();

        if (auditDO.getOperationUri() != null) {
            this.operationUri = auditDO.getOperationUri();
        }

        if (auditDO.getAuditType() != null) {
            this.auditType = auditDO.getAuditType().name();
            this.auditTypeDesc = DmI18nUtils.getMessage(auditDO.getAuditType().name());
        }

        if (auditDO.getResourceType() != null) {
            this.resourceType = auditDO.getResourceType().name();
            this.resourceTypeDesc = DmI18nUtils.getMessage(auditDO.getResourceType().name());
        }

        if (auditDO.getIp() != null) {
            this.logPathWorkerIp = auditDO.getIp();
        }

        return this;
    }
}

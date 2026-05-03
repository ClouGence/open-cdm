package com.clougence.rdp.controller.model.vo;

import java.util.Date;

import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.dal.model.RdpOpAuditDO;
import com.clougence.rdp.util.RdpI18nUtils;

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
            this.auditTypeDesc = RdpI18nUtils.getMessage(auditDO.getAuditType().name());
        }

        if (auditDO.getResourceType() != null) {
            this.resourceType = auditDO.getResourceType().name();
            this.resourceTypeDesc = RdpI18nUtils.getMessage(auditDO.getResourceType().name());
        }

        if (auditDO.getIp() != null) {
            this.logPathWorkerIp = auditDO.getIp();
        }

        return this;
    }
}

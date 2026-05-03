package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.constant.operation.AuditType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/4/13 12:25
 */
@Getter
@Setter
@TableName(value = "rdp_op_audit")
public class RdpOpAuditDO {

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

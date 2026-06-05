package com.clougence.clouddm.console.web.model.fo;

import java.util.Date;

import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;

import lombok.Data;

/**
 * @author bucketli 2021/2/1 21:26
 */
@Data
public class QueryOpAuditFO {

    private Date          opStart;

    private Date          opEnd;

    private SecurityLevel securityLevel;

    private String        auditType;

    private String        resourceType;

    private String        userNameLike;

    private PageData      pageData;

    private String        uid;
}

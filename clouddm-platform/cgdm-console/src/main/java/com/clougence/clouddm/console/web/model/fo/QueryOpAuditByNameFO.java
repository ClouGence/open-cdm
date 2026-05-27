package com.clougence.clouddm.console.web.model.fo;

import java.util.Date;

import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author bucketli 2021/2/1 21:26
 */
@Data
public class QueryOpAuditByNameFO {

    @NotBlank(message = "{notblank.username}")
    private String        userName;

    private Date          opStart;

    private Date          opEnd;

    private SecurityLevel securityLevel;

    private String        auditType;

    private String        resourceType;

    private PageData      pageData;
}

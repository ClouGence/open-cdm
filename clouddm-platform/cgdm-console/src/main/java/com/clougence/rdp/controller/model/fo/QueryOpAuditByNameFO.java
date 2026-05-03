package com.clougence.rdp.controller.model.fo;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.clougence.rdp.constant.auth.SecurityLevel;

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

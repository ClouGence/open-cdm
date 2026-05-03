package com.clougence.rdp.controller.model.fo;

import java.util.Date;

import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.controller.model.enumeration.ExportFileType;

import lombok.Data;

/**
 * @author chunlin create time is 2024/11/4
 */
@Data
public class ExportOpAuditFO {

    private ExportFileType exportType;

    private Date           opStart;

    private Date           opEnd;

    private SecurityLevel  securityLevel;

    private String         auditType;

    private String         resourceType;

    private String         userNameLike;

    private String         uid;

    private String         puid;
}

package com.clougence.clouddm.console.web.model.fo.audit;

import java.util.Date;

import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.rdp.controller.model.fo.PageData;

import lombok.Data;

@Data
public class SqlAuditFO {

    private Date         opStart;

    private Date         opEnd;

    private SecQueryKind sqlKind;

    private Requester    requester;

    private Long         dsId;

    private String       resourceType;

    private String       resourcePath;

    private PageData     pageData;

    private SqlStatus    status;

    private String       userUid;
}

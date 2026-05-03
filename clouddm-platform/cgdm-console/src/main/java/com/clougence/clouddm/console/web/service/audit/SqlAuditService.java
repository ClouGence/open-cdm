package com.clougence.clouddm.console.web.service.audit;

import java.util.Date;
import java.util.List;

import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.console.web.model.vo.audit.SqlAuditVO;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Requester;

public interface SqlAuditService {

    List<SqlAuditVO> queryUserAllAudit(String puid, String uid, SecQueryKind sqlKind, String resourcePath, Long dsId, Requester requester, SqlStatus status, Date start, Date end,
                                       long startId, int pageSize);
}

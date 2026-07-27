package com.clougence.clouddm.console.web.service.security;

import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface AuditService {

    void prepareAudit(Long dsId, QueryRequest request);

    void recordAudit(SqlExecNotifyDTO audit, String wsn);
}

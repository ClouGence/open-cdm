package com.clougence.clouddm.console.web.service.security;

import java.util.List;

import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface AuditService {

    void recordAudit(List<SqlExecNotifyDTO> audits, String wsn);

}

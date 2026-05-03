package com.clougence.clouddm.api.console.sqlaudit;

import java.util.Date;
import java.util.List;

import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;

/**
 * @author bucketli 2021/1/16 11:44
 */
@RSocketApiClass
public interface SqlAuditRService {

    void reportSqlAudit(WorkerIdentity identity, Date sendTime, List<SqlExecNotifyDTO> audits);
}

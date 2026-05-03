package com.clougence.clouddm.console.web.provider;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.sqlaudit.SqlAuditRService;
import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.console.web.global.notify.ConsoleSqlNotifyService;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/16 11:54
 */
@Slf4j
@Service
@RSocketApiClass
public class SqlAuditRServiceProvider extends AbstractBasicProvider implements SqlAuditRService {

    @Resource
    private ConsoleSqlNotifyService sqlNotifyService;

    @Override
    public void reportSqlAudit(WorkerIdentity identity, Date sendTime, List<SqlExecNotifyDTO> audits) {
        if (!this.checkAccessKey(identity) || CollectionUtils.isEmpty(audits)) {
            return;
        }
        log.info("receive worker auditLog request,date:" + sendTime);
        this.sqlNotifyService.sqlExecNotify(audits, identity.getWorkerSeqNumber());
    }
}

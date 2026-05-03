package com.clougence.clouddm.console.web.global.notify;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.console.web.component.execute.AfterSqlExecuteService;
import org.springframework.transaction.annotation.Transactional;
import com.clougence.clouddm.console.web.service.security.AuditService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsoleSqlNotifyServiceImpl implements ConsoleSqlNotifyService {

    @Resource
    private AfterSqlExecuteService afterSqlExecuteService;
    @Resource
    private AuditService           auditService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void sqlExecNotify(List<SqlExecNotifyDTO> audits, String wsn) {
        auditService.recordAudit(audits, wsn);
        afterSqlExecuteService.handleAfterSqlSuccess(audits);
    }
}

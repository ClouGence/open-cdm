/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.worker.component.autoexec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.api.console.autoexec.ExecJobRService;
import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecJobDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecMessageDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecTaskDTO;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.sdk.execute.session.rdb.KillCurrentQueryAble;
import com.clougence.clouddm.worker.component.notify.SidecarSqlNotifyService;
import com.clougence.clouddm.worker.component.report.ReportUtils;
import com.clougence.clouddm.worker.component.resource.TaskDsResourceManager;
import com.clougence.clouddm.worker.component.session.SessionAgent;
import com.clougence.clouddm.worker.component.session.SessionManager;
import com.clougence.utils.ThreadUtils;

@Service
@Scope("prototype")
public class AutoExecJob implements Runnable {

    @Resource
    private TaskDsResourceManager    backgroundRM;
    @Resource
    private SessionManager           sessionManager;
    @Resource
    private ConfigRService           configRService;
    @Resource
    private ExecJobRService          execJobRService;
    @Resource
    private SidecarSqlNotifyService  sidecarSqlNotifyService;

    private AutoExecJobDTO           job;
    private SessionAgent             sessionAgent;

    private List<AutoExecMessageDTO> messageList = new LinkedList<>();

    private Long                     jobId;

    private long                     runningTaskId;

    private WorkerIdentity           workerIdentity;

    private final AtomicInteger      status      = new AtomicInteger(RUNNING);

    private static final int         SUCCESS     = 0;
    private static final int         FAILED      = 1;
    private static final int         PAUSE       = 2;
    private static final int         RUNNING     = 3;
    private static final Logger      log         = LoggerFactory.getLogger("sql-audit");

    public void init(Long jobId) {
        this.jobId = jobId;
    }

    public void run() {
        try {
            this.job = execJobRService.fetchJobInfo(identity(), jobId);
            if (this.job == null || this.job.isJobIsExecByAnother() || this.job.isJobNotExists()) {
                log.warn("job not exists or job is exec by another worker");
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (status.get() == PAUSE) {
            sendMessage(AutoExecMessageDTO.jobPauseMessage(job.getJobId()), true);
            log.info("job paused");
            return;
        }

        DataSourceConfig dataSourceConfig = configRService.fetchDsConfig(job.getDsId(), job.getDsType());
        try {
            this.sessionAgent = (SessionAgent) sessionManager.createSession(backgroundRM, dataSourceConfig, job.getContextDTO());
            String currentQueryId = this.sessionAgent.getCurrentQueryId();
            sendMessage(AutoExecMessageDTO.createQueryIdMessage(job.getJobId(), currentQueryId), true);
            log.info("create session success,query id: " + currentQueryId);
        } catch (Throwable e) {
            sendMessage(AutoExecMessageDTO.createSessionFailed(job.getJobId(), e.getMessage()), true);
            log.error("create session failed", e);
            return;
        }

        if (job.getTaskList().isEmpty()) {
            log.warn("no sql need exec");
            sendMessage(AutoExecMessageDTO.jobFinishMessage(job.getJobId()), true);
            return;
        }

        try {
            sessionAgent.executeQuery(con -> {
                log.info("job start");
                doJob(con);
                if (status.get() == SUCCESS) {
                    log.info("job success");
                    sendMessage(AutoExecMessageDTO.jobFinishMessage(job.getJobId()), true);
                } else if (status.get() == FAILED) {
                    log.error("job failed");
                    sendMessage(AutoExecMessageDTO.jobFailedMessage(job.getJobId(), this.runningTaskId), true);
                } else if (status.get() == PAUSE) {
                    log.warn("job paused");
                    sendMessage(AutoExecMessageDTO.jobPauseMessage(job.getJobId()), true);

                }
                return null;
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                sessionAgent.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

    }

    private void doJob(Connection con) throws SQLException {
        boolean transaction = job.isEnableTransactional();
        try {
            if (transaction) {
                log.info("transaction  start");
                con.setAutoCommit(false);
                sidecarSqlNotifyService.startTransaction(sessionAgent.getSessionId());
            }

            if (!doBatch(con, job.getTaskList())) {
                if (transaction) {
                    con.rollback();
                    sidecarSqlNotifyService.rollbackSession(sessionAgent.getSessionId());
                    log.warn("transaction rollback");
                    sendMessage(AutoExecMessageDTO.transactionRollbackMessage(job.getJobId()), false);
                }
                return;
            }

            if (transaction) {
                con.commit();
                sidecarSqlNotifyService.confirmSession(sessionAgent.getSessionId());
                log.info("transaction group commit");
                sendMessage(AutoExecMessageDTO.transactionFinishMessage(job.getJobId()), false);
            }
        } catch (Throwable e) {
            if (transaction) {
                con.rollback();
                sidecarSqlNotifyService.rollbackSession(sessionAgent.getSessionId());
                log.warn("transaction rollback");
                sendMessage(AutoExecMessageDTO.transactionRollbackMessage(job.getJobId()), false);

            }
            status.set(FAILED);
            return;
        } finally {
            con.setAutoCommit(true);
        }
        status.set(SUCCESS);
    }

    // return if exec all sql , if not is was be pause
    private boolean doBatch(Connection con, List<AutoExecTaskDTO> list) throws SQLException {
        int retryCount = 0;
        for (int i = 0; i < list.size(); i++) {
            AutoExecTaskDTO taskDTO = list.get(i);
            if (status.get() == PAUSE) {
                return false;
            }

            this.runningTaskId = taskDTO.getTaskId();
            log.info("sql start exec,sql order:{}，task id: {},sql:[{}]", taskDTO.getExecOrder(), taskDTO.getTaskId(), taskDTO.getExecSql());
            sendMessage(AutoExecMessageDTO.taskStartMessage(taskDTO.getTaskId()), true);
            sidecarSqlNotifyService.recodeSqlForAutoExec(job.getUid(), taskDTO.getExecSql(), job.getRequester(), job.getDsId(), sessionAgent.getSessionId(), job.getLevels());
            PreparedStatement ps = con.prepareStatement(taskDTO.getExecSql());

            try {
                ps.execute();
                long affectLine = getUpdateCount(ps);
                affectLine = Math.max(0, affectLine);
                log.info("sql exec success,affect line: {}", affectLine);
                if (job.isEnableTransactional()) {
                    sidecarSqlNotifyService
                        .finishForAutoExec(sessionAgent.getSessionId(), null, affectLine, taskDTO.getExecSql(), SqlStatus.WAIT_CONFIRM, job.getLevels(), job.getDsId());
                    sendMessage(AutoExecMessageDTO.taskWaitConfirmMessage(taskDTO.getTaskId(), affectLine, retryCount + 1), false);
                } else {
                    sidecarSqlNotifyService
                        .finishForAutoExec(sessionAgent.getSessionId(), null, affectLine, taskDTO.getExecSql(), SqlStatus.SUCCESS, job.getLevels(), job.getDsId());
                    sendMessage(AutoExecMessageDTO.taskFinishMessage(taskDTO.getTaskId(), affectLine, retryCount + 1), false);
                }
                retryCount = 0;
            } catch (Throwable e) {
                sidecarSqlNotifyService.finishForAutoExec(sessionAgent.getSessionId(), e.getMessage(), 0L, taskDTO.getExecSql(), SqlStatus.FAILURE, job.getLevels(), job.getDsId());
                if (job.getErrorStrategy() == ErrorStrategy.RETRY && retryCount < job.getRetryCount()) {
                    log.warn("sql exec failed,wait next retry,retry count :{},error msg:{}", retryCount + 1, e.getMessage());
                    sendMessage(AutoExecMessageDTO.taskRetryMessage(taskDTO.getTaskId()), true);
                    ThreadUtils.safeSleep(job.getRetryWaitTime() * 1000);
                    i--;
                    retryCount++;
                } else if (job.getErrorStrategy() == ErrorStrategy.SKIP) {
                    log.info("sql skip :{}", taskDTO.getExecSql());
                    sendMessage(AutoExecMessageDTO.taskSkipMessage(job.getJobId(), runningTaskId), false);
                } else {
                    log.error("sql exec failed, sql order {}, sql:[{}]failed, error msg:{}", taskDTO.getExecOrder(), taskDTO.getExecSql(), e.getMessage());;
                    sendMessage(AutoExecMessageDTO.taskFailMessage(taskDTO.getTaskId(), e.getMessage(), retryCount + 1), false);
                    throw e;
                }
            }
        }
        return true;
    }

    protected long getUpdateCount(PreparedStatement ps) throws SQLException {
        long affectedLine = 0;
        ps.getResultSet();
        affectedLine += sessionAgent.getUpdateCount(ps);

        // mores
        while (ps.getMoreResults() || ps.getUpdateCount() != -1) {
            if (ps.getUpdateCount() == -1) {
                continue;
            }
            long updateCount = ps.getUpdateCount();
            affectedLine += updateCount;
        }
        return affectedLine;
    }

    public void pause() throws Exception {
        // already pause() by another or not start
        if (!this.status.compareAndSet(RUNNING, PAUSE) || this.sessionAgent == null) {
            return;
        }
        log.warn("job start pause");
        if (sessionAgent.isExecuting() && sessionAgent instanceof KillCurrentQueryAble) {
            try {
                ((KillCurrentQueryAble) sessionAgent).killCurrentQuery();
            } catch (UnsupportedOperationException e) {
                log.warn("not support killCurrentQuery");
            }
        }
    }

    private WorkerIdentity identity() throws Exception {
        if (this.workerIdentity == null) {
            this.workerIdentity = ReportUtils.getIdentity();
        }
        return this.workerIdentity;
    }

    private void sendMessage(AutoExecMessageDTO message, boolean immediately) {
        this.messageList.add(message);
        if (immediately) {
            while (true) {
                try {
                    this.execJobRService.reportExecMessage(identity(), this.messageList);
                    this.messageList = new LinkedList<>();
                    return;
                } catch (Exception e) {
                    log.error("reportExecMessage error", e);
                    // wait next
                    ThreadUtils.sleep(5000);
                }
            }
        }
    }

}

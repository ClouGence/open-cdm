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
package com.clougence.clouddm.worker.component.notify;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import com.clougence.clouddm.api.console.status.ConsoleStatusRService;
import jakarta.annotation.Resource;

import com.clougence.clouddm.sdk.execute.resultset.echo.*;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.sqlaudit.SqlAuditRService;
import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.api.console.sqlaudit.Type;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.execute.ExecuteVariables;
import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.worker.component.report.ReportUtils;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SidecarSqlNotifyServiceImpl implements SidecarSqlNotifyService, UnifiedPostConstruct {

    private Thread                          thread;
    private BlockingDeque<SqlExecNotifyDTO> queue;
    private final AtomicBoolean             running = new AtomicBoolean();

    @Resource
    private SqlAuditRService                auditRService;
    private WorkerIdentity                  workerIdentity;

    private WorkerIdentity identity() throws Exception {
        if (this.workerIdentity == null) {
            this.workerIdentity = ReportUtils.getIdentity();
        }
        return this.workerIdentity;
    }

    @Override
    public void finishForAutoExec(String sessionId, String message, Long affectLine, String sql, SqlStatus result, List<String> levels, Long dsId) {
        SqlExecNotifyDTO dto = new SqlExecNotifyDTO();
        dto.setSessionId(sessionId);
        dto.setMessage(message);
        dto.setSqlStatus(result);
        dto.setLine(affectLine);
        dto.setTime(new Date());
        dto.setSql(sql);
        dto.setLevels(levels);
        dto.setDsId(dsId);
        this.queue.add(dto);
    }

    @Override
    public void confirmSession(String sessionId) {
        SqlExecNotifyDTO sqlExecNotifyDTO = new SqlExecNotifyDTO();
        sqlExecNotifyDTO.setSessionId(sessionId);
        sqlExecNotifyDTO.setType(Type.COMMIT);
        sqlExecNotifyDTO.setTime(new Date());
        this.queue.add(sqlExecNotifyDTO);
    }

    @Override
    public void rollbackSession(String sessionId) {
        SqlExecNotifyDTO sqlExecNotifyDTO = new SqlExecNotifyDTO();
        sqlExecNotifyDTO.setSessionId(sessionId);
        sqlExecNotifyDTO.setType(Type.ROLLBACK);
        sqlExecNotifyDTO.setTime(new Date());
        this.queue.add(sqlExecNotifyDTO);
    }

    @Override
    public void startTransaction(String sessionId) {
        SqlExecNotifyDTO sqlExecNotifyDTO = new SqlExecNotifyDTO();
        sqlExecNotifyDTO.setSessionId(sessionId);
        sqlExecNotifyDTO.setType(Type.START_TRANSACTION);
        sqlExecNotifyDTO.setTime(new Date());
        this.queue.add(sqlExecNotifyDTO);
    }

    @Override
    public void finishForConsoleQuery(QueryRequest query, Result result) {
        switch (result.getResultType()) {
            case Phase: {
                if (result instanceof ResultPhase) {
                    if (((ResultPhase) result).getPhaseType() == ResultPhaseType.After) {
                        this.addToQueue(query, result, SqlStatus.SUCCESS, 0, result.getVariables());
                    }
                }
                break;
            }
            // fail
            case Message: {
                ResultMessage resultMessage = (ResultMessage) result;
                if (!resultMessage.isNotify()) {
                    return;
                }
                if (resultMessage.getLevel() == MessageLevel.Error) {
                    addToQueue(query, result, SqlStatus.FAILURE, 0, result.getVariables());
                } else if (resultMessage.getLevel() == MessageLevel.Info) {
                    addToQueue(query, result, SqlStatus.SUCCESS, 0, result.getVariables());
                }
                break;
            }
            case ResultCount: {
                ResultCount resultCount = (ResultCount) result;
                // create table .... count = -1
                addToQueue(query, result, SqlStatus.SUCCESS, Math.max(0, resultCount.getUpdateCount()), result.getVariables());
                break;
            }
        }
    }

    private void addToQueue(QueryRequest query, Result result, SqlStatus sqlStatus, long affectLine, Map<String, String> variables) {
        SqlExecNotifyDTO dto = new SqlExecNotifyDTO();
        dto.setSessionId(result.getSessionId());
        dto.setQueryId(query.getQueryId());
        dto.setMessage(result.getMessage());
        dto.setSql(result.getQuerySql());
        dto.setSqlStatus(sqlStatus);
        dto.setLine(affectLine);
        dto.setTime(new Date());
        dto.setType(Type.SQL_END);
        dto.setRewrite(query.isHasRewrite());
        dto.setRewriteTag(query.getRewriteTag());
        dto.setOriginalSql(query.getOriginalBody());

        List<String> levels = new ArrayList<>();
        String catalog = variables.get(ExecuteVariables.CURRENT_CATALOG);
        String schema = variables.get(ExecuteVariables.CURRENT_SCHEMA);
        if (StringUtils.isNotEmpty(catalog)) {
            levels.add(catalog);
        }
        if (StringUtils.isNotEmpty(schema)) {
            levels.add(schema);
        }

        dto.setDsId(Long.valueOf(variables.get(ExecuteVariables.DS_ID)));

        dto.setLevels(levels);

        this.queue.add(dto);
    }

    @Override
    public void beginForConsoleQuery(QueryRequest query, String sessionId) {
        SqlExecNotifyDTO dto = new SqlExecNotifyDTO();
        dto.setExplain(query.isUseExplain());
        dto.setUid(query.getVariables().get(ExecuteVariables.CURRENT_UID));
        dto.setTime(new Date());
        dto.setSql(query.getQueryBody());
        dto.setRequester(query.getRequester());
        dto.setSessionId(sessionId);
        dto.setQueryId(query.getQueryId());
        dto.setDsId(Long.valueOf(query.getVariables().get(ExecuteVariables.DS_ID)));
        dto.setType(Type.SQL_START);
        dto.setRewrite(query.isHasRewrite());
        dto.setRewriteTag(query.getRewriteTag());
        dto.setOriginalSql(query.getOriginalBody());
        dto.setClientIp(query.getVariables().get(ExecuteVariables.CLIENT_IP));
        dto.setSqlStatus(SqlStatus.RUNNING);
        List<String> levels = new ArrayList<>();
        String catalog = query.getVariables().get(ExecuteVariables.CURRENT_CATALOG);
        String schema = query.getVariables().get(ExecuteVariables.CURRENT_SCHEMA);
        if (StringUtils.isNotEmpty(catalog)) {
            levels.add(catalog);
        }
        if (StringUtils.isNotEmpty(schema)) {
            levels.add(schema);
        }
        dto.setLevels(levels);

        this.queue.add(dto);
    }

    @Override
    public void recodeSqlForAutoExec(String uid, String sql, Requester requester, Long dsId, String sessionId, List<String> levels) {
        SqlExecNotifyDTO dto = new SqlExecNotifyDTO();
        dto.setUid(uid);
        dto.setTime(new Date());
        dto.setSql(sql);
        dto.setRequester(requester);
        dto.setSessionId(sessionId);
        dto.setDsId(dsId);
        dto.setLevels(levels);
        dto.setSqlStatus(SqlStatus.RUNNING);
        dto.setType(Type.SQL_START);
        this.queue.add(dto);

    }

    @Override
    public void init() throws Exception {
        if (this.running.compareAndSet(false, true)) {
            this.queue = new LinkedBlockingDeque<>();
            this.thread = ThreadUtils.daemonThread(this::loopSchedule);
            this.thread.setName("Sql Notify Thread");
            this.thread.start();
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            if (this.thread != null) {
                this.thread.interrupt();
            }
        }
    }

    protected void loopSchedule() {
        while (true) {
            try {
                doReport();
                if (!this.running.get()) {
                    log.warn("[SQL RECODE TASK] thread exit, (" + Thread.currentThread().getName() + ")");
                    return;
                }
                ThreadUtils.sleep(1000);
            } catch (Throwable e) {
                log.error("[Sql RECODE TASK] error " + e.getMessage(), e);
                ThreadUtils.sleep(5000);
            }
        }
    }

    private void doReport() {
        while (true) {
            List<SqlExecNotifyDTO> drain = this.drain(50);
            if (drain.isEmpty()) {
                return;
            }
            try {
                this.auditRService.reportSqlAudit(identity(), new Date(), drain);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                for (int i = drain.size() - 1; i >= 0; i--) {
                    this.queue.addFirst(drain.get(i));
                }
                return;
            }
        }
    }

    private List<SqlExecNotifyDTO> drain(int count) {
        int added = 0;
        List<SqlExecNotifyDTO> list = new LinkedList<>();
        while (added < count) {
            SqlExecNotifyDTO dto = this.queue.poll();
            if (dto == null) {
                break;
            }
            list.add(dto);
            ++added;
        }
        return list;
    }
}

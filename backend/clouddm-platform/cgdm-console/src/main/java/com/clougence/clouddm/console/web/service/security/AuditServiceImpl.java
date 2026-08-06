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
package com.clougence.clouddm.console.web.service.security;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.api.console.sqlaudit.Type;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.global.notify.DmWorkerRegisterNotify;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpHostUtil;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlAuditDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.Setter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Service
public class AuditServiceImpl implements AuditService, DmWorkerRegisterNotify, UnifiedPostConstruct {

    private final Logger                logger = LoggerFactory.getLogger("sql-audit");
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private final AtomicBoolean         inited = new AtomicBoolean();
    @Resource
    private SystemDal                   systemDal;
    @Resource
    private ExecutionDal                execDal;
    @Resource
    private DataSourceDal               dsDal;
    @Resource
    private RdpUserService              userService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void prepareAudit(Long dsId, String auditUid, QueryRequest request) {
        if (request == null) {
            return;
        }
        DmExecSqlAuditDO exists = this.execDal.sqlAuditMapper().queryByQueryId(request.getQueryId());
        if (exists != null && request.getRequester() == Requester.CONSOLE) {
            throw new IllegalStateException("Duplicate SQL audit ACK: " + request.getQueryId());
        }
        if (exists != null) {
            return;
        }
        String userName = auditUid;
        if (StringUtils.isNotBlank(auditUid)) {
            DmAuthUserDO user = this.userService.getUserByUid(auditUid);
            if (user != null) {
                if (StringUtils.isNotBlank(user.getUsername())) {
                    userName = user.getUsername();
                } else if (StringUtils.isNotBlank(user.getAccount())) {
                    userName = user.getAccount();
                } else if (StringUtils.isNotBlank(user.getBindAccount())) {
                    userName = user.getBindAccount();
                }
            }
        }
        DmDsDO dsDO = this.dsDal.dsMapper().queryDsIdentityById(dsId);

        DmExecSqlAuditDO auditDO = new DmExecSqlAuditDO();
        auditDO.setQueryId(request.getQueryId());
        auditDO.setBehaviors(request.getRelations());
        auditDO.setExecSql(getString(request.getQueryBody()));
        auditDO.setOriginalSql(request.isHasRewrite() ? getString(request.getOriginalBody()) : null);
        auditDO.setDsId(dsId);
        auditDO.setUid(auditUid);
        auditDO.setUserName(userName);
        auditDO.setLogIp(RdpHostUtil.getHostIp());
        auditDO.setRequester(request.getRequester());
        auditDO.setDataSourceType(dsDO.getDataSourceType());
        auditDO.setDsDesc(dsDO.getInstanceId() + "(" + dsDO.getInstanceDesc() + ")");
        auditDO.setStatus(SqlStatus.PENDING);
        this.execDal.sqlAuditMapper().insert(auditDO);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void recordAudit(SqlExecNotifyDTO audit, String wsn) {
        LogInfo logInfo = recodeSql(audit, wsn);
        if (logInfo != null) {
            logger.info(logInfo.toString());
        }
    }

    private LogInfo recodeSql(SqlExecNotifyDTO dto, String wsn) {
        if (dto.getType() == Type.COMMIT) {
            execDal.sqlAuditMapper().confirmSession(dto.getSessionId());
            return LogInfo.getCommitLogInfo(dto);
        } else if (dto.getType() == Type.ROLLBACK) {
            execDal.sqlAuditMapper().rollbackSession(dto.getSessionId());
            return LogInfo.getRollbackLogInfo(dto);
        } else if (dto.getType() == Type.START_TRANSACTION) {
            return LogInfo.getStartTransaction(dto);
        } else if (dto.getType() == Type.SQL_START) {
            if (StringUtils.isBlank(dto.getQueryId())) {
                return null;
            }
            DmExecSqlAuditDO auditDO = this.startPreparedAudit(dto, wsn);
            if (auditDO != null) {
                return LogInfo.getStartLogInfo(auditDO, dto);
            }
            return null;
        }

        String message = getString(dto.getMessage());
        if (StringUtils.isBlank(dto.getQueryId())) {
            return null;
        }
        int updated = this.execDal.sqlAuditMapper().completeByQueryId(dto.getQueryId(), dto.getSessionId(), dto.getStatus().name(), dto.getAffectLine(), message, dto.getTime());
        if (updated == 0) {
            return null;
        }

        DmExecSqlAuditDO auditDO = this.execDal.sqlAuditMapper().queryByQueryId(dto.getQueryId());
        return LogInfo.getEndLogInfo(dto, auditDO);
    }

    private DmExecSqlAuditDO startPreparedAudit(SqlExecNotifyDTO dto, String wsn) {
        DmExecSqlAuditDO auditDO = this.execDal.sqlAuditMapper().queryByQueryId(dto.getQueryId());
        if (auditDO == null) {
            return null;
        }

        auditDO.setSessionId(dto.getSessionId());
        auditDO.setClientIp(dto.getClientIp());
        auditDO.setWorkSeqNumber(wsn);
        auditDO.setOperateTime(dto.getTime());
        auditDO.setStatus(SqlStatus.RUNNING);
        auditDO.setAffectLine(0);
        auditDO.setEndTime(null);
        auditDO.setMessage(null);
        int updated = this.execDal.sqlAuditMapper().markRunningByQueryId(auditDO);
        return updated == 0 ? null : auditDO;
    }

    private String getString(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() > 65535) {
            str = str.substring(0, 65500);
            str += "...";
        }
        return str;
    }

    @Override
    public void notifyRegister(String wsn) {
        this.execDal.sqlAuditMapper().updateErrorSql(wsn);
    }

    @Override
    public void init() throws Exception {
        if (!this.inited.compareAndSet(false, true)) {
            return;
        }
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "Sql-audit-delete-%s"));
        this.scheduledThreadPoolExecutor.scheduleWithFixedDelay(this::deleteTimeoutLog, 1, 30, TimeUnit.MINUTES);
    }

    @Override
    public void stop() {

    }

    private void deleteTimeoutLog() {
        for (DmAuthUserDO rdpUserDO : userService.listPrimaryUser()) {
            Date now = new Date();
            DmSysUserConfDO configDO = systemDal.userConfMapper().queryByUidAndConfigName(rdpUserDO.getUid(), RootUserConfig.Fields.sqlAuditRetentionDays);
            int day = 30;
            String configValue = configDO.getConfigValue();
            if (StringUtils.isNotEmpty(configValue) && StringUtils.isNumeric(configValue)) {
                try {
                    day = Integer.parseInt(configValue);
                    if (day > 60) {
                        day = 60;
                    } else if (day < 1) {
                        day = 1;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            Date date = new Date(now.getTime() - (long) day * 24 * 60 * 60 * 1000);

            int deleteCount;

            do {
                deleteCount = execDal.sqlAuditMapper().deleteAuditBeforeDate(rdpUserDO.getUid(), date);
            } while (deleteCount > 0);
        }
    }

    @Setter
    private static class LogInfo {

        // all
        private Type      type;

        private String    sql;
        private String    uid;
        private String    username;
        private String    clientIp;
        private String    sessionId;
        private Requester requester;
        private Long      dsId;
        private String    wsn;

        private String    message;
        private Date      time;

        private long      affectLine;

        private SqlStatus sqlStatus;

        public static LogInfo getStartLogInfo(DmExecSqlAuditDO auditDO, SqlExecNotifyDTO dto) {
            LogInfo logInfo = new LogInfo();
            logInfo.setType(Type.SQL_START);
            logInfo.setSql(auditDO.getExecSql());
            logInfo.setUid(auditDO.getUid());
            logInfo.setDsId(auditDO.getDsId());
            logInfo.setUsername(auditDO.getUserName());
            logInfo.setClientIp(auditDO.getClientIp());
            logInfo.setSessionId(auditDO.getSessionId());
            logInfo.setRequester(auditDO.getRequester());
            logInfo.setMessage(auditDO.getMessage());
            logInfo.setWsn(auditDO.getWorkSeqNumber());
            logInfo.setTime(dto.getTime());
            return logInfo;
        }

        public static LogInfo getEndLogInfo(SqlExecNotifyDTO dto, DmExecSqlAuditDO auditDO) {
            LogInfo logInfo = new LogInfo();
            logInfo.setType(Type.SQL_END);
            logInfo.setSql(auditDO.getExecSql());
            logInfo.setAffectLine(dto.getAffectLine());
            logInfo.setMessage(dto.getMessage());
            logInfo.setTime(dto.getTime());
            logInfo.setSessionId(dto.getSessionId());
            logInfo.setSqlStatus(dto.getStatus());
            logInfo.setAffectLine(dto.getAffectLine());
            return logInfo;
        }

        public static LogInfo getCommitLogInfo(SqlExecNotifyDTO dto) {
            LogInfo logInfo = new LogInfo();
            logInfo.setType(Type.COMMIT);
            logInfo.setTime(dto.getTime());
            logInfo.setSessionId(dto.getSessionId());
            return logInfo;
        }

        public static LogInfo getRollbackLogInfo(SqlExecNotifyDTO dto) {
            LogInfo logInfo = new LogInfo();
            logInfo.setType(Type.ROLLBACK);
            logInfo.setTime(dto.getTime());
            logInfo.setSessionId(dto.getSessionId());
            return logInfo;
        }

        public static LogInfo getStartTransaction(SqlExecNotifyDTO dto) {
            LogInfo logInfo = new LogInfo();
            logInfo.setType(Type.START_TRANSACTION);
            logInfo.setTime(dto.getTime());
            logInfo.setSessionId(dto.getSessionId());
            return logInfo;
        }

        @Override
        public String toString() {
            String result = "";
            String formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(this.time);
            if (type == Type.SQL_START) {
                result = String
                    .format("%s sessionId: %s, [START] uid: %s, username:%s, clientIp: %s, wsn: %s, requester: %s, dsId: %3s, sql: %s", formatTime, this.sessionId, this.uid, this.username, this.clientIp, this.wsn, this.requester, this.dsId, this.sql);
            } else if (type == Type.SQL_END) {
                result = String
                    .format("%s sessionId: %s, [%s] affectLine: %d, sql: %s, message: %s", formatTime, this.sessionId, this.sqlStatus, this.affectLine, this.sql, this.message);
            } else if (type == Type.COMMIT) {
                result = String.format("%s sessionId: %s, [COMMIT]", formatTime, this.sessionId);
            } else if (type == Type.ROLLBACK) {
                result = String.format("%s sessionId: %s, [ROLLBACK]", formatTime, this.sessionId);
            } else if (type == Type.START_TRANSACTION) {
                result = String.format("%s sessionId: %s, [START_TRANSACTION]", formatTime, this.sessionId);
            }

            return result;
        }

    }
}

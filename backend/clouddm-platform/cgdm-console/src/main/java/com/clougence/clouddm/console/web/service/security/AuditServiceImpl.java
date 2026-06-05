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
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.api.console.sqlaudit.Type;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.notify.DmWorkerRegisterNotify;
import com.clougence.clouddm.console.web.service.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSqlAuditDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.schema.umi.struts.UmiTypes;
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
    private ExecutionDal                executionDal;
    @Resource
    private DataSourceDal               dsDal;
    @Resource
    private QueryAnalysisService        queryAnalysisService;
    @Resource
    private RdpUserService              rdpUserService;
    @Resource
    private DmDsConfigService           dmDsConfigService;
    @Resource
    private DmConsoleConfig             rdpConsoleConfig;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void recordAudit(List<SqlExecNotifyDTO> audits, String wsn) {
        List<LogInfo> logInfos = recodeSql(audits, wsn);
        for (LogInfo info : logInfos) {
            logger.info(info.toString());
        }
    }

    private List<LogInfo> recodeSql(List<SqlExecNotifyDTO> list, String wsn) {
        List<LogInfo> result = new ArrayList<>();
        for (SqlExecNotifyDTO dto : list) {
            if (dto.getType() == Type.COMMIT) {
                executionDal.sqlAuditMapper().confirmSession(dto.getSessionId());
                result.add(LogInfo.getCommitLogInfo(dto));
            } else if (dto.getType() == Type.ROLLBACK) {
                executionDal.sqlAuditMapper().rollbackSession(dto.getSessionId());
                result.add(LogInfo.getRollbackLogInfo(dto));
            } else if (dto.getType() == Type.START_TRANSACTION) {
                result.add(LogInfo.getStartTransaction(dto));
            } else if (dto.getType() == Type.SQL_START) {
                DmDsDO rdpDataSourceDO = dsDal.dsMapper().queryDsIdentityById(dto.getDsId());
                DmExecSqlAuditDO auditDO = new DmExecSqlAuditDO();
                if (dto.isExplain()) {
                    auditDO.setSqlKind(SecQueryKind.EXPLAIN);
                } else {
                    try {
                        List<SplitScript> splitScripts = queryAnalysisService.analysisSplit(rdpDataSourceDO.getDataSourceType(), dto.getSql(), null, 1, 0);
                        auditDO.setSqlKind(splitScripts.get(0).getType().getAuditKind());
                    } catch (Throwable e) {
                        // some sql can't analysis
                        auditDO.setSqlKind(SecQueryKind.OTHER);
                    }
                }

                auditDO.setSessionId(dto.getSessionId());
                auditDO.setExecSql(getString(dto.getSql()));
                auditDO.setOperateTime(dto.getTime());

                if (dto.isRewrite()) {
                    auditDO.setOriginalSql(getString(dto.getOriginalSql()));
                }

                DsConfig dsConfig = dmDsConfigService.dsConstantSettings(rdpDataSourceDO.getDataSourceType());
                List<String> levels = dsConfig.getCategories().getLevels();
                Map<String, Object> map = new HashMap<>();

                for (int i = 0; i < dto.getLevels().size(); i++) {
                    UmiTypes umiTypes = UmiTypes.valueOfCode(levels.get(i + 2));
                    if (umiTypes == UmiTypes.Catalog) {
                        map.put(SessionSpi.PARAMS_DEFAULT_DB, dto.getLevels().get(i));
                    } else {
                        map.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, dto.getLevels().get(i));
                    }
                }
                try {
                    DataSourceConfig dataSourceConfig = dmDsConfigService.fetchDsConfigFromDM(rdpDataSourceDO.getId(), rdpDataSourceDO.getDataSourceType());
                    Map<RuleDomain, List<ResObject>> objs = queryAnalysisService.analysisResourceV2(dataSourceConfig, dto.getSql(), map);

                    List<String> collect = objs.values().stream().flatMap(List::stream).map(obj -> {
                        return obj.toDsResPath().getResPath();
                    }).distinct().sorted().collect(Collectors.toList());
                    String resource = String.join(",", collect);
                    auditDO.setResource(getString(resource));
                } catch (Throwable e) {
                    logger.error(e.getMessage());
                    auditDO.setResource("");
                }

                auditDO.setLogIp(rdpConsoleConfig.getConsolePackageMode().getLocalIpOrHostName());
                auditDO.setWorkSeqNumber(wsn);
                auditDO.setClientIp(dto.getClientIp());
                auditDO.setDsId(dto.getDsId());
                auditDO.setDataSourceType(rdpDataSourceDO.getDataSourceType());

                auditDO.setDsDesc(rdpDataSourceDO.getInstanceId() + "(" + rdpDataSourceDO.getInstanceDesc() + ")");

                auditDO.setUid(dto.getUid());
                DmAuthUserDO userByUid = rdpUserService.getUserByUid(dto.getUid());
                if (userByUid == null) {
                    auditDO.setUserName(dto.getUid());
                } else {
                    auditDO.setUserName(userByUid.getUsername());
                }

                auditDO.setPrimaryUid(rdpUserService.getPrimaryUser(dto.getUid()).getUid());
                auditDO.setStatus(SqlStatus.RUNNING);
                auditDO.setRequester(dto.getRequester());
                this.executionDal.sqlAuditMapper().insert(auditDO);
                result.add(LogInfo.getStartLogInfo(auditDO, dto));
            } else {
                String message = getString(dto.getMessage());
                this.executionDal.sqlAuditMapper().updateBySessionId(dto.getSessionId(), dto.getSqlStatus().name(), dto.getLine(), message, dto.getTime());
                result.add(LogInfo.getEndLogInfo(dto));
            }
        }
        return result;
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
        this.executionDal.sqlAuditMapper().updateErrorSql(wsn);
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
        for (DmAuthUserDO rdpUserDO : rdpUserService.listPrimaryUser()) {
            Date now = new Date();
            DmSysUserConfDO configDO = systemDal.userConfMapper().queryByUidAndConfigName(rdpUserDO.getUid(), UserDefinedConfig.Fields.sqlAuditRetentionDays);
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
                deleteCount = executionDal.sqlAuditMapper().deleteAuditBeforeDate(rdpUserDO.getUid(), date);
            } while (deleteCount > 0);
        }
    }

    @Setter
    private static class LogInfo {

        // all
        private Type         type;

        private String       sql;
        private String       uid;
        private String       username;
        private String       clientIp;
        private String       sessionId;
        private Requester    requester;
        private Long         dsId;
        private String       resource;
        private String       wsn;

        private String       message;
        private Date         time;

        private SecQueryKind sqlKind;
        private long         affectLine;

        private SqlStatus    sqlStatus;

        public static LogInfo getStartLogInfo(DmExecSqlAuditDO auditDO, SqlExecNotifyDTO dto) {
            LogInfo logInfo = new LogInfo();
            logInfo.setType(Type.SQL_START);
            logInfo.setSql(dto.getSql());
            logInfo.setUid(auditDO.getUid());
            logInfo.setDsId(auditDO.getDsId());
            logInfo.setUsername(auditDO.getUserName());
            logInfo.setClientIp(auditDO.getClientIp());
            logInfo.setSessionId(auditDO.getSessionId());
            logInfo.setRequester(auditDO.getRequester());
            logInfo.setMessage(auditDO.getMessage());
            logInfo.setWsn(auditDO.getWorkSeqNumber());
            logInfo.setTime(dto.getTime());
            logInfo.setSqlKind(auditDO.getSqlKind());
            logInfo.setResource(auditDO.getResource());
            return logInfo;
        }

        public static LogInfo getEndLogInfo(SqlExecNotifyDTO dto) {
            LogInfo logInfo = new LogInfo();
            logInfo.setType(Type.SQL_END);
            logInfo.setSql(dto.getSql());
            logInfo.setAffectLine(dto.getLine());
            logInfo.setMessage(dto.getMessage());
            logInfo.setTime(dto.getTime());
            logInfo.setSessionId(dto.getSessionId());
            logInfo.setSqlStatus(dto.getSqlStatus());
            logInfo.setAffectLine(dto.getLine());
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
                    .format("%s sessionId: %s, [START] uid: %s, username:%s, clientIp: %s, wsn: %s, requester: %s, dsId: %3s, resource: %s, sqlKind: %s, sql: %s", formatTime, this.sessionId, this.uid, this.username, this.clientIp, this.wsn, this.requester, this.dsId, this.resource, this.sqlKind, this.sql);
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

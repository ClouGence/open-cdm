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
package com.clougence.clouddm.console.web.service.editor.query;

import java.io.StringReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.api.console.sqlaudit.Type;
import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
import com.clougence.clouddm.api.sidecar.session.execute.ResultPhaseOfBatch;
import com.clougence.clouddm.api.sidecar.session.execute.StatusDTO;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.analysis.*;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.model.QueryRelationAuthResult;
import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckContext;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckSession;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesEngine;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.execute.QueryService;
import com.clougence.clouddm.console.web.constants.RewriteTags;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryFO;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryType;
import com.clougence.clouddm.console.web.model.vo.editor.query.MessageLevel;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsQueryResult;
import com.clougence.clouddm.console.web.service.editor.DsQueryEditorService;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.console.web.service.security.AuditService;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecFileDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSessionDO;
import com.clougence.clouddm.platform.dal.model.execution.FileStatus;
import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resultset.echo.*;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.service.secrules.RuleLevel;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.sysobj.SysObjectRegistrySpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsoleQueryService implements UnifiedPostConstruct, ConsoleQueryApi {
    private static final int     MAX_QUERY_INPUT_LENGTH = 2 * 1024 * 1024;

    @Resource
    private ExecutionDal         executionDal;
    @Resource
    private ConsoleConfig        config;
    @Resource
    private ApplicationContext   appContext;
    @Resource
    private DsQueryEditorService queryEditorService;
    @Resource
    private DmDsConfigService    dmDsConfigService;
    @Resource
    private SystemDal            systemDal;
    @Resource
    private SecRulesEngine       ruleCheckService;
    @Resource
    private DmAuthServiceForBiz  authCheckService;
    @Resource
    private QueryAnalysisService analysisService;
    @Resource
    private QueryService         queryService;
    @Resource
    private AuditService         auditService;
    @Resource
    private DmEnvParamService    dmEnvParamService;
    private QueryTaskExecutor    queryExecutor;

    @Override
    public void init() throws Exception {
        this.queryExecutor = new QueryTaskExecutor(this.appContext.getClassLoader(), 10);
    }

    @Override
    public void stop() {
        this.queryExecutor.close();
    }

    @Override
    public void offerQueryRequest(WsQueryFO fo, Consumer<WsQueryResult> consumer) {
        if (!this.authCheckService.checkRoleAuthWithoutError(fo.getPrimaryUserId(), fo.getCurrentUserId(), SecRoleAuthLabel.DM_QUERY_CONSOLE)) {
            String message = RdpAuthUtils.missRoleAuthMsg(SecRoleAuthLabel.DM_QUERY_CONSOLE);
            consumer.accept(BuildResMsgUtils.buildHintMsg(fo, message, MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildDone(fo));
            return;
        }

        WsQueryType queryType = fo.getQueryType();
        String curUid = fo.getCurrentUserId();
        String sessionId = fo.getSessionId();

        // 1. miss session id
        if (StringUtils.isBlank(sessionId)) {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NEED_SESSION_ID_ERROR.name());
            consumer.accept(BuildResMsgUtils.buildHintMsg(fo, message, MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildDone(fo));
            return;
        }

        // 2. fix bad worker wsn
        try {
            this.queryService.testSessionWorker(curUid, sessionId);
        } catch (ErrorMessageException e) {
            DmExecSessionDO sessionInfo = this.queryService.getSessionInfo(curUid, sessionId);
            this.queryService.closeSession(curUid, sessionId);

            if (!sessionInfo.toRdbCtx().isRdbAutoCommit()) {
                String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_WORKER_STATUS_OFFLINE_RESET_SESSION_ERROR.name(), sessionInfo.getWsn());
                consumer.accept(BuildResMsgUtils.buildHintMsg(fo, message, MessageLevel.Error));
                consumer.accept(BuildResMsgUtils.buildDone(fo));
                return;
            }
        }

        // 3. do operate
        switch (queryType) {
            case SwitchCtx:
                if (this.queryService.isExecuting(curUid, sessionId)) {
                    this.executingCheckAndResponseIt(fo, consumer);
                } else {
                    this.switchCtx(fo, consumer);
                }
                break;
            case RequestQuery:
                if (this.queryService.isExecuting(curUid, sessionId)) {
                    this.executingCheckAndResponseIt(fo, consumer);
                } else {
                    this.requestQuery(fo, consumer, false);
                }
                break;
            case RequestPlan:
                if (this.queryService.isExecuting(curUid, sessionId)) {
                    this.executingCheckAndResponseIt(fo, consumer);
                } else {
                    this.requestQuery(fo, consumer, true);
                }
                break;
            case CancelQuery:
                this.cancelQuery(fo, consumer);
                break;
            case TxCommit:
                if (this.queryService.isExecuting(curUid, sessionId)) {
                    this.executingCheckAndResponseIt(fo, consumer);
                } else {
                    this.txCommit(fo, consumer);
                }
                break;
            case TxRollback:
                if (this.queryService.isExecuting(curUid, sessionId)) {
                    this.executingCheckAndResponseIt(fo, consumer);
                } else {
                    this.txRollback(fo, consumer);
                }
                break;
            case TxStatus:
                if (this.queryService.isExecuting(curUid, sessionId)) {
                    this.executingCheckAndResponseIt(fo, consumer);
                } else {
                    this.txStatus(fo, consumer);
                }
                break;
            case RecoveryStatus:
                this.recoveryStatus(fo, consumer);
                break;
        }
    }

    // ------------------------------------------------------------------------
    //                                                         for RequestQuery
    // ------------------------------------------------------------------------

    // 4. operate of query
    private void requestQuery(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer, boolean isExplain) {
        QueryCtx ctx;
        try {
            ctx = this.createQueryCtx(queryDTO);
        } catch (ErrorMessageException e) {
            log.error(e.getErrorMessage(), e);
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, e.getErrorMessage(), MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return;
        }

        String curUid = queryDTO.getCurrentUserId();
        String sessionId = queryDTO.getSessionId();

        // 4.1. no_sql_select
        if (StringUtils.isBlank(queryDTO.getQueryString())) {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NO_SQL_SELECT_ERROR.name());
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return;
        }
        String queryString = queryDTO.getQueryString();
        if (queryString.length() > MAX_QUERY_INPUT_LENGTH) {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_SQL_TOO_LARGE_ERROR.name(), MAX_QUERY_INPUT_LENGTH);
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return;
        }

        // 4.2. check quota
        if (!this.queryEditorService.hasMoreSessionQuota(curUid)) {
            ctx.resetStatus();
            String quota = String.valueOf(this.queryEditorService.getMaxTxSessionUserQuota());
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_WINDOW_LIMIT_ERROR.name(), quota);

            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return;
        }

        // 4.3. context status check.
        if (ctx.getQueryStatus() != QueryStatus.Free) {
            ctx.resetStatus();
        }

        // 4.4. query limit.
        int curQueueSize = this.queryExecutor.getQueueSize();
        int maxQueueSize = this.config.getConsoleQueryQueueSize();
        if (curQueueSize >= maxQueueSize) {
            log.warn("[" + curUid + "] submit query to queue failed, the queue is full. curSize = " + curQueueSize + ", maxSize = " + maxQueueSize);
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_QUEUE_FULL_ERROR.name());
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return;
        }

        // 4.5. async query
        ctx.setQueryStatus(QueryStatus.Prepare);
        ctx.setStartTime(System.currentTimeMillis());
        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_STAGE_PREPARE_MESSAGE.name());
        consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, false));
        consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Info));
        this.queryExecutor.submitTask(() -> {
            try {
                return asyncQueryPrepare(queryDTO, consumer, ctx, isExplain);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                String str = e.getClass().getSimpleName() + ":" + e.getMessage();
                str = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_UNEXPECTED_ERROR2.name(), str);
                consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, str, MessageLevel.Error));
                consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
                consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
                return ExitCode.finish();
            }
        });
    }

    private static final RuleLevel[] CHECK_LEVELS_FORCE  = new RuleLevel[] { RuleLevel.FAILURE, RuleLevel.TICKET };
    private static final RuleLevel[] CHECK_LEVELS_NORMAL = new RuleLevel[] { RuleLevel.FAILURE, RuleLevel.TICKET, RuleLevel.SUGGEST };

    private List<QueryRequest> prepareQueryRequests(WsQueryFO queryDTO, QueryCtx ctx, boolean isExplain) {
        AnalysisQueryOptions options = AnalysisQueryOptions.builder()
            .currentUid(queryDTO.getCurrentUserId())
            .dataSourceId(ctx.getLevels().dsDO().getId())
            .levels(ctx.getLevels().levelsParam())
            .build();
        int codeLine = queryDTO.getBasicCodeLine();
        int codeColumn = queryDTO.getBasicCodeColumn();
        List<QueryArg> queryArgs = queryDTO.getQueryArgs();
        List<QueryRequest> requests;
        try (StringReader reader = new StringReader(queryDTO.getQueryString())) {
            try (Stream<QueryRequest> analyzed = this.analysisService.analysisRequestsStream(ctx.getDsConfig(), reader, queryArgs, codeLine, codeColumn, options)) {
                requests = analyzed.collect(Collectors.toCollection(ArrayList::new));
            }
        }

        //
        SessionSpi sessionSpi = ctx.getSessionSpi();
        QueryRequest temp = sessionSpi.createQueryRequest(ctx.getDsConfig());
        temp.setRequester(Requester.CONSOLE);
        if (this.isUsingCacheResult()) {
            temp.getResultConf().setCacheResult(true);
            temp.getResultConf().setReceiveMode(queryDTO.getReceiveMode() == null ? ReceiveMode.PAGINATED : queryDTO.getReceiveMode());
        } else {
            temp.getResultConf().setCacheResult(false);
            temp.getResultConf().setReceiveMode(queryDTO.getReceiveMode() == null ? ReceiveMode.PAGE_FULL : queryDTO.getReceiveMode());
        }

        // for Explain
        RewriteSpi rewriteSpi = null;
        SqlParserParameters parameters = SqlParserParameters.empty();
        if (isExplain) {
            parameters = this.dmDsConfigService.fetchSqlParserParameters(ctx.getDsConfig(), ctx.getLevels().levelsParam());
            parameters = parameters.put(SqlParserParameters.EXPECT_PLAN, Boolean.TRUE.toString());
            rewriteSpi = ctx.getSqlEngine().rewriteSpi(parameters);
            if (rewriteSpi == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NOT_SUPPORT_EXPLAIN_SQL.name()));
            }
        }
        temp.setUseExplain(isExplain);

        for (int i = 0; i < requests.size(); i++) {
            QueryRequest analyzed = requests.get(i);
            QueryRequest clone = temp.clone();
            clone.setQueryId(sessionSpi.newQueryId());
            clone.setUseExplain(isExplain);
            clone.setQueryBody(analyzed.getQueryBody());
            clone.setQueryArgs(analyzed.getQueryArgs());
            clone.setQueryTypes(analyzed.getQueryTypes());
            clone.setRelations(analyzed.getRelations());
            clone.setDsType(analyzed.getDsType());
            clone.setColumnList(analyzed.getColumnList());
            clone.setUsingValueProcess(analyzed.isUsingValueProcess());
            clone.setHasRewrite(analyzed.isHasRewrite());
            clone.setRewriteTag(analyzed.getRewriteTag());
            clone.setOriginalBody(analyzed.getOriginalBody());
            if (isExplain) {
                this.prepareQueryRequests4Explain(queryDTO, ctx, clone, rewriteSpi, parameters);
            }
            clone.getResultConf().setRefreshStatus(i == requests.size() - 1);
            requests.set(i, clone);
        }
        return requests;
    }

    private void prepareQueryRequests4Explain(WsQueryFO queryDTO, QueryCtx ctx, QueryRequest request, RewriteSpi rewriteSpi, SqlParserParameters parameters) {
        RewriteContext rewriteContext = new RewriteContext();
        rewriteContext.setParameters(parameters);
        String beforeExplain = request.getQueryBody();
        String explainQuery = rewriteSpi.rewriteToExplain(request.getQueryId(), beforeExplain, rewriteContext);
        if (StringUtils.isBlank(explainQuery)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NOT_SUPPORT_EXPLAIN_SQL.name()));
        }

        request.setOriginalBody(StringUtils.defaultIfBlank(request.getOriginalBody(), beforeExplain));
        request.setHasRewrite(true);
        request.addRewriteTag(RewriteTags.EXPLAIN);
        request.setQueryBody(explainQuery);

        List<QueryRequest> explainRequests;
        try (StringReader reader = new StringReader(explainQuery)) {
            AnalysisQueryOptions options = AnalysisQueryOptions.builder()
                .currentUid(queryDTO.getCurrentUserId())
                .dataSourceId(ctx.getLevels().dsDO().getId())
                .levels(ctx.getLevels().levelsParam())
                .parameters(parameters)
                .skip(QueryAnalysisFeature.REWRITE, QueryAnalysisFeature.LINEAGE, QueryAnalysisFeature.MASKING)
                .build();
            try (Stream<QueryRequest> analyzed = this.analysisService
                .analysisRequestsStream(ctx.getDsConfig(), reader, request.getQueryArgs(), queryDTO.getBasicCodeLine(), queryDTO.getBasicCodeColumn(), options)) {
                explainRequests = analyzed.toList();
            }
        }

        if (explainRequests.size() != 1) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NOT_SUPPORT_EXPLAIN_SQL.name()));
        }
        QueryRequest analyzed = explainRequests.get(0);
        request.setQueryTypes(analyzed.getQueryTypes());
        request.setRelations(analyzed.getRelations());
    }

    private ExitCode asyncQueryPrepare(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer, QueryCtx ctx, boolean isExplain) {
        String curUid = queryDTO.getCurrentUserId();
        String sessionId = queryDTO.getSessionId();

        // 4.6. analyze query requests
        String authMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_STAGE_AUTH_MESSAGE.name());
        consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, authMsg, MessageLevel.Info));
        String analysisMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_STAGE_ANALYSIS_MESSAGE.name());
        consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, analysisMsg, MessageLevel.Info));
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_STAGE_REQUEST_MESSAGE.name());
        consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, msg, MessageLevel.Info));

        SqlParserParameters parameters = this.dmDsConfigService.fetchSqlParserParameters(ctx.getDsConfig(), ctx.getLevels().levelsParam());
        List<QueryRequest> requests;
        try {
            requests = this.prepareQueryRequests(queryDTO, ctx, isExplain);
        } catch (AntlerSyntaxException e) {
            CodeLocation location = e.offsetLocation(queryDTO.getBasicCodeLine(), queryDTO.getBasicCodeColumn());
            String syntaxMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_SYNTAX_ANALYSIS_ERROR.name(), location.getLineNumber(), location.getColumnNumber());
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, syntaxMsg, MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return processAsyncQueryReturn(ExitCode.finish(), queryDTO, ctx);
        } catch (ErrorMessageException e) {
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, e.getErrorMessage(), MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return processAsyncQueryReturn(ExitCode.finish(), queryDTO, ctx);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            String str = e.getClass().getSimpleName() + ":" + e.getMessage();
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, str, MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return processAsyncQueryReturn(ExitCode.finish(), queryDTO, ctx);
        }

        // 4.7. check rules & auth & other...
        if (!specialCheck(queryDTO, consumer, ctx, parameters, requests)) {
            return processAsyncQueryReturn(ExitCode.finish(), queryDTO, ctx);
        }

        // 4.8. prepare Session
        String eventMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_STAGE_SESSION_MESSAGE.name());
        consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, eventMsg, MessageLevel.Info));
        boolean hasSession = this.queryService.hasSession(curUid, sessionId);
        if (!hasSession) {
            // create session
            try {
                this.queryService.createSession(curUid, ctx.getLevels(), ctx.getCtxDTO());
            } catch (Throwable e) {
                ctx.resetStatus();
                String message;
                if (e instanceof ErrorMessageException) {
                    message = ((ErrorMessageException) e).getErrorMessage();
                } else {
                    Throwable rootCause = ExceptionUtils.getRootCause(e);
                    message = rootCause.getMessage();
                }

                consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Error));
                consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
                consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
                return processAsyncQueryReturn(ExitCode.finish(), queryDTO, ctx);
            }

            // check INVALID_REOPENED
            if (!ctx.getCtxDTO().isRdbAutoCommit()) {
                ctx.resetStatus();
                String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_SESSION_INVALID_REOPENED_ERROR.name(), sessionId);
                consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Warn));
                consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
                consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
                return processAsyncQueryReturn(ExitCode.finish(), queryDTO, ctx);
            }
        }

        // 4.9. execute query
        try {
            if (!ctx.getCtxDTO().isRdbAutoCommit()) {
                ctx.setHasUnCommitted(true);
            }

            ctx.setQueryStatus(QueryStatus.Query);
            ctx.setPrepareCost(System.currentTimeMillis() - ctx.getStartTime());
            consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, false));

            for (QueryRequest request : requests) {
                this.auditService.prepareAudit(ctx.getLevels().dsDO().getId(), curUid, request);
            }
            String batchId = UUID.randomUUID().toString().replace("-", "");
            this.queryService.asyncExecuteQuery(curUid, sessionId, batchId, requests);

            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_STAGE_RESPONSE_MESSAGE.name());
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Info));
            this.queryExecutor.submitTask(() -> asyncQueryWaitResult(queryDTO, consumer, ctx)); // 4.9. wait result.
            return ExitCode.finish();
        } catch (Exception e) {
            try {
                String message = ExceptionUtils.getRootCauseMessage(e);
                for (QueryRequest request : requests) {
                    SqlExecNotifyDTO audit = new SqlExecNotifyDTO();
                    audit.setType(Type.SQL_END);
                    audit.setStatus(SqlStatus.ERROR);
                    audit.setQueryId(request.getQueryId());
                    audit.setSessionId(sessionId);
                    audit.setMessage(message);
                    audit.setTime(new Date());
                    this.auditService.recordAudit(audit, null);
                }
            } catch (Throwable auditError) {
                log.error("Failed to mark prepared SQL audits as error.", auditError);
            }
            String errorKey = HostUtil.getHostIp() + ":" + UUID.randomUUID().toString().replace("-", "");
            log.error("errorKey: " + errorKey + ", error is ", e.getMessage(), e);
            ctx.resetStatus();

            String hintMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_UNEXPECTED_ERROR.name(), errorKey);
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, hintMessage, MessageLevel.Error));

            String consoleMessage = "ErrorKey: " + errorKey + ", " + ExceptionUtils.getRootCauseMessage(e);
            consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, consoleMessage, MessageLevel.Error, true));
            consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return processAsyncQueryReturn(ExitCode.finish(), queryDTO, ctx);
        }
    }

    // 4.6. operate of query on specialCheck
    private boolean specialCheck(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer, QueryCtx ctx,//
                                 SqlParserParameters parameters, List<QueryRequest> requestScripts) {
        // 6.2 at team all statements must be clear
        String curOwnerUid = queryDTO.getPrimaryUserId();
        for (QueryRequest request : requestScripts) {
            Set<SplitQueryType> queryTypes = request.getQueryTypes();
            if (CollectionUtils.isEmpty(queryTypes) || queryTypes.contains(SplitQueryType.UNKNOWN)) {
                String hasSwitchMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NONSUPPORT_QUERY_ERROR.name(), request.getQueryBody());
                consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, hasSwitchMsg, MessageLevel.Error));
                consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
                consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
                return false;
            }

            String enable = this.dmEnvParamService.queryParam(curOwnerUid, ctx.getLevels().dsDO().getDsEnvId(), EnvParamKeys.DM_ALLOW_ALL_STATEMENTS);
            if (StringUtils.equalsIgnoreCase("true", enable)) {
                SqlEngineSpi sqlEngine = this.dmDsConfigService.fetchSqlEngineSpi(ctx.getLevels().dsDO().getId());
                SysObjectRegistrySpi registry = PluginManager.findSpi(SysObjectRegistrySpi.class, sqlEngine.name());
                boolean hasNonReadBehavior = BehaviorRelations.flattenResource(registry, parameters.version(), request.getRelations())
                    .stream()
                    .filter(behavior -> behavior.authKind() != null)
                    .anyMatch(behavior -> behavior.authKind() != SecDataAuthKind.READ);
                if (hasNonReadBehavior) {
                    String authFailedMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_ONLY_QUERY_MESSAGE.name());
                    consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, authFailedMsg, MessageLevel.Error));
                    consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
                    consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
                    return false;
                }
            }
        }

        // 6.3 disallow `use xxx` or `set search_path = xxx` or `alter session set container = xxx`
        for (QueryRequest request : requestScripts) {
            if (request.hasQueryType(SplitQueryType.SWITCH_CATALOG) || request.hasQueryType(SplitQueryType.SWITCH_SCHEMA)) {
                String hasSwitchMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NONSUPPORT_SWITCH_CTX_ERROR.name());
                consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, hasSwitchMsg, MessageLevel.Error));
                consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
                consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
                return false;
            } else if (request.hasQueryType(SplitQueryType.TRANSACTION)) {
                String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NONSUPPORT_TRANSACTION_OPERATE_ERROR.name());
                consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, msg, MessageLevel.Error));
                consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
                consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
                return false;
            }
        }

        String curUserUid = queryDTO.getCurrentUserId();
        QueryRelationAuthResult authResult = this.authCheckService.checkQueryRelationAuth(curOwnerUid, curUserUid, ctx.getLevels(), requestScripts);
        if (!authResult.isPassed()) {
            BehaviorRequest denied = authResult.getDeniedRequests().get(0);
            String authLabel = denied.authKind().getAuthLabel();
            String authLabelI18n = DmI18nUtils.getMessage(authLabel);
            String authFailedMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NO_PERMISSION_MESSAGE.name(), denied.resource().getObjectPath(), authLabelI18n);
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, authFailedMsg, MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return false;
        }

        // 6.4 rules check
        String rulesMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_STAGE_RULES_MESSAGE.name());
        consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, rulesMsg, MessageLevel.Info));
        try {
            SecRulesCheckResult checkResult = rulesCheck(queryDTO, ctx, parameters);
            RuleLevel[] failedLevels = queryDTO.isForce() ? CHECK_LEVELS_FORCE : CHECK_LEVELS_NORMAL;
            if (checkResult.hasAnyTarget(failedLevels)) {
                ctx.resetStatus();
                consumer.accept(BuildResMsgUtils.buildRules(queryDTO, checkResult));
                consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
                consumer.accept(BuildResMsgUtils.buildClearHint(queryDTO));
                consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, e.getMessage(), MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return false;
        }

        return true;
    }

    private ExitCode processAsyncQueryReturn(ExitCode result, WsQueryFO queryDTO, QueryCtx ctx) {
        if (ctx.getCtxDTO().isRdbAutoCommit()) {
            this.queryService.closeSession(queryDTO.getCurrentUserId(), queryDTO.getSessionId());
        }

        return result;
    }

    // ------------------------------------------------------------------------
    //                                                        for ResponseQuery
    // ------------------------------------------------------------------------

    public void offerQueryResponse(ResultList result) {
        //        DmExecSessionDO sessionDO = this.sessionMapper.queryBySessionId(result.getSessionId());
        //        String sessionId = result.getSessionId();
        //        String primaryUid = null;//sessionDO.get.getPrimaryUserId();
        //        String curUid = null;//queryDTO.getCurrentUserId();
        //
        //        // missing session
        //        if (sessionDO == null) {
        //            // 1. close this session.
        //        }
        //
        //        // update session status
        //        StatusDTO status = result.getStatus();
        //        SessionContextDTO rdbCtx = sessionDO.toRdbCtx();
        //        rdbCtx.setSessionId(sessionId);
        //        rdbCtx.setMaxIdleTimeSec(status.getMaxIdleTimeSec());
        //        rdbCtx.setRdbCatalog(status.getCurCatalog());
        //        rdbCtx.setRdbSchema(status.getCurSchema());
        //        rdbCtx.setRdbAutoCommit(status.isAutoCommit());
        //        rdbCtx.setRdbTxIsolation(status.getIsolation());
        //        rdbCtx.setRdbReadOnly(status.isReadOnly());
        //        sessionDO.setConfig(JsonUtils.toJson(rdbCtx));
        //        this.sessionMapper.updateSessionConfig(sessionDO);
        //
        //        // finished
        //        if (status.getWaitQuerySize() == 0 && !status.isExecuting()) {
        //            if (rdbCtx.isRdbAutoCommit()) {
        //                this.queryService.closeSession(curUid, sessionId);
        //            }
        //
        //        }
    }

    private void waitResultDown(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer, QueryCtx ctx) {
        String curUid = queryDTO.getCurrentUserId();
        String sessionId = queryDTO.getSessionId();

        StatusDTO status = this.queryService.getAndUpdateStatus(curUid, sessionId);
        ctx.getCtxDTO().setRdbCatalog(status.getCurCatalog());
        ctx.getCtxDTO().setRdbSchema(status.getCurSchema());
        ctx.getCtxDTO().setRdbAutoCommit(status.isAutoCommit());
        ctx.getCtxDTO().setRdbTxIsolation(status.getIsolation());
        ctx.getCtxDTO().setRdbReadOnly(status.isReadOnly());

        if (ctx.getCtxDTO().isRdbAutoCommit()) {
            this.queryService.closeSession(curUid, sessionId);
        }

        ctx.setQueryStatus(QueryStatus.Finish);
        ctx.setReceiveCost(System.currentTimeMillis() - ctx.getStartTime() - ctx.getPrepareCost() - ctx.getQueryCost());
        consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, true));
        consumer.accept(BuildResMsgUtils.buildClearHint(queryDTO));

        ctx.resetStatus();
        consumer.accept(BuildResMsgUtils.buildStatus(queryDTO, ctx, this.queryEditorService));
        consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
    }

    // 4.11. wait result.
    private ExitCode asyncQueryWaitResult(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer, QueryCtx ctx) {
        String primaryUid = queryDTO.getPrimaryUserId();
        String curUid = queryDTO.getCurrentUserId();
        String sessionId = queryDTO.getSessionId();
        DmExecSessionDO sessionInfo = this.queryService.getSessionInfo(curUid, sessionId);
        if (sessionInfo == null) {
            log.error("session '" + sessionId + "' is closed or not exit.");
            return ExitCode.finish();
        }

        // receive result
        if (ctx.getQueryStatus() == QueryStatus.Query) {
            ctx.setQueryStatus(QueryStatus.Receive);
            ctx.setQueryCost(System.currentTimeMillis() - ctx.getStartTime() - ctx.getPrepareCost());
            consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, false));
        }

        ResultList result;
        do {
            result = this.queryService.fetchQueryResult(curUid, sessionId);

            for (Result r : result.getResultList()) {
                if (!r.isSuccess()) {
                    consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, r.getMessage(), MessageLevel.Error, true));
                    continue;
                }

                switch (r.getResultType()) {
                    case ResultSetMeta: {
                        ResultSetMeta rm = (ResultSetMeta) r;
                        if (StringUtils.isNotBlank(rm.getCacheFileUri())) {
                            DmExecFileDO fileDO = new DmExecFileDO();
                            fileDO.setFileUri(rm.getCacheFileUri());
                            fileDO.setFileFormat(rm.getCacheFileFormat().name());
                            fileDO.setInnerFormat(true);
                            fileDO.setOwnerUid(primaryUid);
                            fileDO.setUserId(curUid);
                            fileDO.setStatus(FileStatus.Pending);
                            fileDO.setQueryId(rm.getQueryId());
                            fileDO.setUniqueId(rm.getResultId());
                            fileDO.setHeartbeat(new Date());
                            this.executionDal.fileMapper().insert(fileDO);
                        }
                        consumer.accept(BuildResMsgUtils.buildResultMeta(queryDTO, ctx, rm));
                        break;
                    }
                    case ResultSetRows: {
                        ResultSetCount rc = (ResultSetCount) r;
                        long fetchCount = rc.getFetchCount();
                        long fetchTimeMs = Math.max(1, rc.getCostTimeMs());

                        this.executionDal.fileMapper().updateAccessTimeByUniqueId(rc.getResultId(), "receive rows " + rc.getFetchCount());
                        String infoMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_SET_INFO_MESSAGE.name(),//
                                fetchCount, ctx.getPrepareCost(), ctx.getQueryCost(), fetchTimeMs);
                        consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, infoMessage, MessageLevel.Info, true));
                        consumer.accept(BuildResMsgUtils.buildResultSetRows(queryDTO, ctx, rc));
                        break;
                    }
                    case ResultSet: {
                        ResultSet rs = (ResultSet) r;
                        long fetchCount = rs.getFetchCount();
                        long fetchTimeMs = Math.max(1, rs.getCostTimeMs());

                        this.executionDal.fileMapper().updateAccessTimeByUniqueId(rs.getResultId(), "receive rows " + rs.getFetchCount());
                        String infoMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_SET_INFO_MESSAGE.name(),//
                                fetchCount, ctx.getPrepareCost(), ctx.getQueryCost(), fetchTimeMs);
                        consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, infoMessage, MessageLevel.Info, true));
                        consumer.accept(BuildResMsgUtils.buildResult(queryDTO, ctx, rs));
                        break;
                    }
                    case ResultCount: {
                        ResultCount rc = (ResultCount) r;
                        long updateCount = ((ResultCount) r).getUpdateCount();
                        long fetchTimeMs = Math.max(1, rc.getCostTimeMs());
                        String infoMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_COUNT_INFO_MESSAGE.name(),//
                                updateCount, ctx.getPrepareCost(), ctx.getQueryCost(), fetchTimeMs);
                        consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, infoMessage, MessageLevel.Info, true));
                        break;
                    }
                    case ResultOut: {
                        ResultOut rm = (ResultOut) r; // TODO procedure output param.
                        break;
                    }
                    case Message: {
                        ResultMessage rm = (ResultMessage) r;
                        String message = rm.getMessage();
                        switch (rm.getLevel()) {
                            case Info:
                                consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Info, true));
                                break;
                            case Warn:
                                consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Warn, true));
                                break;
                            case Error:
                                consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Error, true));
                            default:
                                break;
                        }
                        break;
                    }
                    case Phase: {
                        ResultPhase rp = (ResultPhase) r;
                        switch (rp.getPhaseType()) {
                            case Before: {
                                if (rp instanceof ResultPhaseOfBatch) {
                                    // TODO before all commands.
                                } else {
                                    consumer.accept(BuildResMsgUtils.buildQueryMsg(queryDTO, rp, ctx));
                                }
                                break;
                            }
                            case After: {
                                if (rp instanceof ResultPhaseOfBatch) {
                                    // TODO after all commands.
                                } else {
                                    // TODO after single command.
                                }
                                break;
                            }
                            case BeginReceive:
                                break;
                            case FinishReceive:
                                this.executionDal.fileMapper().updateStatusByQueryId(rp.getQueryId(), FileStatus.Ready, "Finish");
                                break;
                            case Cancel:
                                this.executionDal.fileMapper().updateStatusByQueryId(rp.getQueryId(), FileStatus.Failed, "Cancel");
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

        } while (!CollectionUtils.isEmpty(result.getResultList()));

        // wait next or exit
        StatusDTO status = result.getStatus();
        if (status.getWaitQuerySize() == 0 && !status.isExecuting()) {
            this.waitResultDown(queryDTO, consumer, ctx);
            return ExitCode.finish();
        } else {
            try {
                return ExitCode.delayTimes(ctx.getReceiveTimes().get());
            } finally {
                ctx.incrementReceiveTimes();
            }
        }
    }

    public QueryCtx createQueryCtx(WsQueryFO queryDTO) {
        String curUid = queryDTO.getCurrentUserId();
        String sessionId = queryDTO.getSessionId();
        DsLevels levels = this.dmDsConfigService.parseLevels(queryDTO.getLevels());
        DmDsDO dsDO = levels.dsDO();
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromExists(dsDO.getId());

        Map<String, Object> params = new HashMap<>();
        levels.levelsParam().forEach((umiType, value) -> {
            switch (umiType) {
                case Catalog:
                    params.put(SessionSpi.PARAMS_DEFAULT_DB, value);
                    break;
                case Schema:
                    params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, value);
                    break;
                default:
                    break;
            }
        });

        SessionSpi sessionSpi = PluginManager.findSessionSpi(dsDO.getDataSourceType());
        RdbSupportSpi supportSpi = PluginManager.findRdbSupportSpi(dsDO.getDataSourceType());
        SqlEngineSpi engine = this.dmDsConfigService.fetchSqlEngineSpi(dsDO.getId());

        if (this.queryService.hasSession(curUid, sessionId)) {
            DmExecSessionDO sessionInfo = this.queryService.getSessionInfo(curUid, sessionId);
            SessionContextDTO contextDTO = sessionInfo.toRdbCtx();
            QueryCtx queryCtx = new QueryCtx(levels, dsConfig, contextDTO, params, sessionSpi, engine, supportSpi);

            StatusDTO status;
            if (this.queryService.isExecuting(curUid, sessionId)) {
                status = new StatusDTO();
                status.setExecuting(true);
                status.setCurCatalog(contextDTO.getRdbCatalog());
                status.setCurSchema(contextDTO.getRdbSchema());
                status.setAutoCommit(contextDTO.isRdbAutoCommit());
                status.setReadOnly(contextDTO.isRdbReadOnly());
                status.setIsolation(contextDTO.getRdbTxIsolation());
                status.setHasUnCommitted(true); // at least it is safe.
            } else {
                status = this.queryService.getAndUpdateStatus(curUid, sessionId);
            }

            contextDTO.setRdbCatalog(status.getCurCatalog());
            contextDTO.setRdbSchema(status.getCurSchema());
            contextDTO.setRdbAutoCommit(status.isAutoCommit());
            contextDTO.setRdbTxIsolation(status.getIsolation());
            contextDTO.setRdbReadOnly(status.isReadOnly());
            queryCtx.setHasUnCommitted(status.isHasUnCommitted());
            if (status.isExecuting()) {
                queryCtx.setQueryStatus(QueryStatus.Receive);
                queryCtx.setStartTime(-1);
            } else {
                queryCtx.setQueryStatus(QueryStatus.Free);
                queryCtx.setStartTime(0);
            }
            return queryCtx;
        } else {
            SessionContextDTO contextDTO = sessionSpi.createSessionContext(dsConfig, params);
            contextDTO.setSessionId(sessionId);
            contextDTO.setRdbAutoCommit(queryDTO.isRdbAutoCommit());
            contextDTO.setRdbTxIsolation(queryDTO.getRdbIsolation());
            contextDTO.setRdbReadOnly(queryDTO.isRdbReadOnly());
            return new QueryCtx(levels, dsConfig, contextDTO, params, sessionSpi, engine, supportSpi);
        }
    }

    private SecRulesCheckResult rulesCheck(WsQueryFO fo, QueryCtx ctx, SqlParserParameters parameters) {
        try {
            DmDsDO dsDO = ctx.getLevels().dsDO();

            SecRulesCheckContext ruleCtx = SecRulesCheckContext.builder()//
                .basicCodeLine(fo.getBasicCodeLine())
                .basicCodeColumn(fo.getBasicCodeColumn())
                .dsId(dsDO.getId())
                .currentUID(fo.getCurrentUserId())
                .currentCatalog(ctx.getCtxDTO().getRdbCatalog())
                .currentSchema(ctx.getCtxDTO().getRdbSchema())
                .sqlParameters(parameters)
                .requester(Requester.CONSOLE)
                .unsupportedLevel(WarnLevel.PASS)
                .build();
            SecRulesCheckSession session = this.ruleCheckService.openQueryCheck(fo.getCurrentUserId(), ctx.getDsConfig(), ruleCtx);
            return session.applyCheck(fo.getQueryString(), fo.getBasicCodeLine(), fo.getBasicCodeColumn());
        } catch (Throwable e) {
            SecRulesCheckResult error = new SecRulesCheckResult();
            String unsupportedName = DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_EXCEPTION_NAME_MESSAGE.name());
            String unsupportedMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_EXCEPTION_MSG_MESSAGE.name(), e.getClass().getSimpleName() + ":" + e.getMessage());
            error.addResult(unsupportedName, RuleLevel.FAILURE, null, unsupportedMsg);
            log.error("rulesCheck failed, " + e.getMessage(), e);
            return error;
        }
    }

    // ------------------------------------------------------------------------
    //                                                            for SwitchCtx
    // ------------------------------------------------------------------------

    private void switchCtx(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer) {
        QueryCtx ctx;
        try {
            ctx = this.createQueryCtx(queryDTO);
        } catch (ErrorMessageException e) {
            log.error(e.getErrorMessage(), e);
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, e.getErrorMessage(), MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return;
        }

        String curUid = queryDTO.getCurrentUserId();
        String curSession = queryDTO.getSessionId();
        List<UmiTypes> levelsDef = ctx.getLevels().levelsDef();
        DsLevels newLevels = this.dmDsConfigService.parseLevels(queryDTO.getLevels());

        // test need session but not have
        boolean hasSession = this.queryService.hasSession(curUid, curSession);
        if (!hasSession) {
            if (!queryDTO.isRdbAutoCommit()) {
                Map<UmiTypes, String> changeTo = new HashMap<>();
                if (newLevels.levelsDef().contains(UmiTypes.Catalog)) {
                    String catalog = (String) newLevels.levelsParam().get(UmiTypes.Catalog);
                    ctx.getCtxDTO().setRdbCatalog(catalog);
                    changeTo.put(UmiTypes.Catalog, catalog);
                }
                if (newLevels.levelsDef().contains(UmiTypes.Schema)) {
                    String schema = (String) newLevels.levelsParam().get(UmiTypes.Schema);
                    ctx.getCtxDTO().setRdbSchema(schema);
                    changeTo.put(UmiTypes.Schema, schema);
                }
                this.switchCtxForNewSession(queryDTO, consumer, ctx, levelsDef, changeTo);
            }
            return;
        }

        // check keepSession
        boolean keepSession = true;
        Map<UmiTypes, String> changeTo = new HashMap<>();
        StatusDTO status = this.queryService.getAndUpdateStatus(curUid, curSession);
        for (UmiTypes umiType : levelsDef) {
            switch (umiType) {
                case Catalog: {
                    String oldValue = status.getCurCatalog();
                    String newValue = (String) newLevels.levelsParam().get(UmiTypes.Catalog);
                    if (!StringUtils.equals(oldValue, newValue)) {
                        keepSession = ctx.isSupportSwitchCatalog();
                        changeTo.put(UmiTypes.Catalog, newValue);
                    }
                    break;
                }
                case Schema: {
                    String oldValue = status.getCurSchema();
                    String newValue = (String) newLevels.levelsParam().get(UmiTypes.Schema);
                    if (!StringUtils.equals(oldValue, newValue)) {
                        keepSession = ctx.isSupportSwitchSchema();
                        changeTo.put(UmiTypes.Schema, newValue);
                    }
                    break;
                }
            }
        }

        ctx.setLevels(newLevels);

        // test no change
        if (changeTo.isEmpty()) {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_CHANGE_CTX_DO_NOTHING_MESSAGE.name());
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Info));
            consumer.accept(BuildResMsgUtils.buildStatus(queryDTO, ctx, this.queryEditorService));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return;
        }

        // auto commit
        if (ctx.getCtxDTO().isRdbAutoCommit()) {
            this.switchCtxForAutoSession(queryDTO, consumer, ctx, levelsDef, changeTo);
            return;
        }

        // check uncommitted
        if (!keepSession && ctx.isHasUnCommitted()) {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_UNCOMMITTED_CHANGE_ERROR.name());
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Warn));
            consumer.accept(BuildResMsgUtils.buildStatus(queryDTO, ctx, this.queryEditorService));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return;
        }

        if (keepSession) {
            this.switchCtxForKeepSession(queryDTO, consumer, ctx, levelsDef, changeTo);
        } else {
            this.switchCtxForNewSession(queryDTO, consumer, ctx, levelsDef, changeTo);
        }
    }

    private void switchCtxForAutoSession(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer, QueryCtx ctx, List<UmiTypes> levelsDef, Map<UmiTypes, String> changeTo) {
        String curUid = queryDTO.getCurrentUserId();
        for (UmiTypes umiType : levelsDef) {
            if (!changeTo.containsKey(umiType)) {
                continue;
            }
            switch (umiType) {
                case Catalog:
                    this.queryService.changeCatalog(curUid, queryDTO.getSessionId(), changeTo.get(umiType));
                    ctx.getCtxDTO().setRdbCatalog(changeTo.get(umiType));
                    break;
                case Schema:
                    this.queryService.changeSchema(curUid, queryDTO.getSessionId(), changeTo.get(umiType));
                    ctx.getCtxDTO().setRdbSchema(changeTo.get(umiType));
                    break;
            }
        }

        String changeToMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_CHANGE_NEXT_CTX_MESSAGE.name(), this.changeToMessage(ctx, changeTo));
        consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, changeToMessage, MessageLevel.Info));
        consumer.accept(BuildResMsgUtils.buildStatus(queryDTO, ctx, this.queryEditorService));
        consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
    }

    private void switchCtxForKeepSession(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer, QueryCtx ctx, List<UmiTypes> levelsDef, Map<UmiTypes, String> changeTo) {
        String curUid = queryDTO.getCurrentUserId();
        for (UmiTypes umiType : levelsDef) {
            if (!changeTo.containsKey(umiType)) {
                continue;
            }
            switch (umiType) {
                case Catalog:
                    this.queryService.changeCatalog(curUid, queryDTO.getSessionId(), changeTo.get(umiType));
                    ctx.getCtxDTO().setRdbCatalog(changeTo.get(umiType));
                    break;
                case Schema:
                    this.queryService.changeSchema(curUid, queryDTO.getSessionId(), changeTo.get(umiType));
                    ctx.getCtxDTO().setRdbSchema(changeTo.get(umiType));
                    break;
            }
        }

        String changeToMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_CHANGE_CTX_MESSAGE.name(), this.changeToMessage(ctx, changeTo));
        consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, changeToMessage, MessageLevel.Info, true));
        consumer.accept(BuildResMsgUtils.buildStatus(queryDTO, ctx, this.queryEditorService));
        consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
    }

    private void switchCtxForNewSession(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer, QueryCtx ctx, List<UmiTypes> levelsDef, Map<UmiTypes, String> changeTo) {
        String curUid = queryDTO.getCurrentUserId();
        this.queryService.closeSession(curUid, queryDTO.getSessionId());

        try {
            QueryCtx newCTX = this.createQueryCtx(queryDTO);
            this.queryService.createSession(curUid, newCTX.getLevels(), newCTX.getCtxDTO());

            String changeToMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_CHANGE_RECREATE_MESSAGE.name(), this.changeToMessage(ctx, changeTo));
            consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, changeToMessage, MessageLevel.Warn, true));
            consumer.accept(BuildResMsgUtils.buildStatus(queryDTO, newCTX, this.queryEditorService));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
        } catch (ErrorMessageException e) {
            consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, e.getErrorMessage(), MessageLevel.Error, true));
            consumer.accept(BuildResMsgUtils.buildStatus(queryDTO, ctx, this.queryEditorService));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
        }
    }

    private String changeToMessage(QueryCtx ctx, Map<UmiTypes, String> changeTo) {
        String changeToMessage = "";
        if (changeTo.containsKey(UmiTypes.Catalog)) {
            changeToMessage = DmDsUtils.getDialect(ctx.getDsConfig().getDataSourceType()).fmtName(true, changeTo.get(UmiTypes.Catalog));
        }
        if (changeTo.containsKey(UmiTypes.Schema)) {
            if (StringUtils.isNotBlank(changeToMessage)) {
                changeToMessage = changeToMessage + ".";
            }
            changeToMessage = changeToMessage + DmDsUtils.getDialect(ctx.getDsConfig().getDataSourceType()).fmtName(true, changeTo.get(UmiTypes.Schema));
        }
        return changeToMessage;
    }

    // ------------------------------------------------------------------------
    //                                                          for CancelQuery
    // ------------------------------------------------------------------------

    private void cancelQuery(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer) {
        String curUid = queryDTO.getCurrentUserId();
        String sessionId = queryDTO.getSessionId();
        String hintMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_CANCEL_ING_MESSAGE.name());
        consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, hintMessage, MessageLevel.Info));

        if (this.queryService.hasSession(curUid, sessionId)) {
            if (this.queryService.isExecuting(curUid, sessionId)) {
                this.queryService.cancelQuery(curUid, sessionId);
                String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_CANCEL_MESSAGE.name());
                consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Warn, true));
                consumer.accept(BuildResMsgUtils.buildClearHint(queryDTO));
                consumer.accept(BuildResMsgUtils.buildCancelDone(queryDTO));
                return;
            }
        }

        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_CANCEL_NO_QUERY_MESSAGE.name());
        consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Info, true));
        consumer.accept(BuildResMsgUtils.buildClearHint(queryDTO));
        consumer.accept(BuildResMsgUtils.buildCancelDone(queryDTO));
    }

    // ------------------------------------------------------------------------
    //                                                             for txCommit
    // ------------------------------------------------------------------------

    private void txCommit(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer) {
        String curUid = queryDTO.getCurrentUserId();
        String sessionId = queryDTO.getSessionId();

        if (this.queryService.hasSession(curUid, sessionId)) {
            if (this.queryService.isExecuting(curUid, sessionId)) {
                String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_IN_EXECUTING_ERROR.name(), sessionId);
                consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Error));
            } else {
                String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_COMMIT_MESSAGE.name());
                consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Info, true));
                this.queryService.commitSession(curUid, sessionId);
            }
        }

        consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
    }

    private void txRollback(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer) {
        String curUid = queryDTO.getCurrentUserId();
        String sessionId = queryDTO.getSessionId();

        if (this.queryService.hasSession(curUid, sessionId)) {
            if (this.queryService.isExecuting(curUid, sessionId)) {
                String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_IN_EXECUTING_ERROR.name(), sessionId);
                consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Error));
            } else {
                String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_ROLLBACK_MESSAGE.name());
                consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Info, true));
                this.queryService.rollbackSession(curUid, sessionId);
            }
        }

        consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
    }

    private void txStatus(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer) {
        QueryCtx ctx;
        try {
            ctx = this.createQueryCtx(queryDTO);
        } catch (ErrorMessageException e) {
            log.error(e.getErrorMessage(), e);
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, e.getErrorMessage(), MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return;
        }

        String curUid = queryDTO.getCurrentUserId();
        String sessionId = queryDTO.getSessionId();

        // apply status
        boolean hasSession = this.queryService.hasSession(curUid, sessionId);
        Boolean applyAutoCommit = null;
        RdbIsolation applyIsolation = null;
        Boolean applyReadOnly = null;

        if (hasSession) {
            StatusDTO status = this.queryService.getAndUpdateStatus(curUid, sessionId);
            if (ctx.isSupportChangeAutoCommit() && status.isAutoCommit() != queryDTO.isRdbAutoCommit()) {
                applyAutoCommit = queryDTO.isRdbAutoCommit();
            }
            if (ctx.isSupportSwitchIsolation() && status.getIsolation() != queryDTO.getRdbIsolation()) {
                applyIsolation = queryDTO.getRdbIsolation();
            }
            if (ctx.isSupportChangeReadOnly() && status.isReadOnly() != queryDTO.isRdbReadOnly()) {
                applyReadOnly = queryDTO.isRdbReadOnly();
            }
        } else {
            if (queryDTO.isRdbAutoCommit()) {
                // pass
            } else {
                applyAutoCommit = false;
                applyIsolation = queryDTO.getRdbIsolation();
                applyReadOnly = queryDTO.isRdbReadOnly();
            }
        }

        if (!hasSession) {
            if (!queryDTO.isRdbAutoCommit()) {
                ctx.getCtxDTO().setRdbAutoCommit(false);
                ctx.getCtxDTO().setRdbTxIsolation(queryDTO.getRdbIsolation());
                ctx.getCtxDTO().setRdbReadOnly(queryDTO.isRdbReadOnly());
                this.queryService.createSession(curUid, ctx.getLevels(), ctx.getCtxDTO());

                String message;
                if (ctx.isSupportSwitchIsolation()) {
                    message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_TX_BY_MANUAL_ISOLATION_MESSAGE.name(), queryDTO.getRdbIsolation().name());
                } else {
                    message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_TX_BY_MANUAL_MESSAGE.name());
                }
                consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Info, true));
            }
        } else {
            if (!queryDTO.isRdbAutoCommit()) {
                if (ctx.isSupportSwitchIsolation() && applyIsolation != null) {
                    this.queryService.setIsolation(curUid, sessionId, applyIsolation);

                    String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_SET_ISOLATION_MESSAGE.name(), applyIsolation.name());
                    consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Info, true));
                }
                if (ctx.isSupportChangeReadOnly() && applyReadOnly != null) {
                    this.queryService.setReadOnly(curUid, sessionId, applyReadOnly);

                    String message;
                    if (applyReadOnly) {
                        message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_SET_READ_ONLY_MESSAGE.name());
                    } else {
                        message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_UNSET_READ_ONLY_MESSAGE.name());
                    }
                    consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Info, true));
                }
            } else {
                this.queryService.commitSession(curUid, sessionId);
                this.queryService.closeSession(curUid, sessionId);

                String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_TX_BY_AUTO_MESSAGE.name());
                consumer.accept(BuildResMsgUtils.buildConsoleMsg(queryDTO, message, MessageLevel.Info, true));
            }
        }

        StatusDTO status = this.queryService.getAndUpdateStatus(curUid, sessionId);
        if (status != null) {
            ctx.getCtxDTO().setRdbAutoCommit(status.isAutoCommit());
            ctx.getCtxDTO().setRdbTxIsolation(status.getIsolation());
            ctx.getCtxDTO().setRdbReadOnly(status.isReadOnly());
        } else {
            ctx.getCtxDTO().setRdbAutoCommit(true);
        }
        consumer.accept(BuildResMsgUtils.buildStatus(queryDTO, ctx, this.queryEditorService));
        consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
    }

    private void recoveryStatus(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer) {
        QueryCtx ctx;
        try {
            ctx = this.createQueryCtx(queryDTO);
        } catch (ErrorMessageException e) {
            log.error(e.getErrorMessage(), e);
            consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, e.getErrorMessage(), MessageLevel.Error));
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
            return;
        }

        String curUid = queryDTO.getCurrentUserId();
        String sessionId = queryDTO.getSessionId();

        if (this.queryService.hasSession(curUid, sessionId)) {
            if (!this.queryService.isExecuting(curUid, sessionId)) {
                StatusDTO status = this.queryService.getAndUpdateStatus(curUid, sessionId);
                ctx.getCtxDTO().setRdbCatalog(status.getCurCatalog());
                ctx.getCtxDTO().setRdbSchema(status.getCurSchema());
                ctx.getCtxDTO().setRdbAutoCommit(status.isAutoCommit());
                ctx.getCtxDTO().setRdbTxIsolation(status.getIsolation());
                ctx.getCtxDTO().setRdbReadOnly(status.isReadOnly());
            }
        }
        consumer.accept(BuildResMsgUtils.buildStatus(queryDTO, ctx, this.queryEditorService));

        QueryStatus status = ctx.getQueryStatus();
        if (status == QueryStatus.Free || status == QueryStatus.Finish) {
            consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
        } else {
            consumer.accept(BuildResMsgUtils.buildCost(queryDTO, ctx, false));
        }
    }

    // ------------------------------------------------------------------------
    //                                                                for utils
    // ------------------------------------------------------------------------

    private void executingCheckAndResponseIt(WsQueryFO queryDTO, Consumer<WsQueryResult> consumer) {
        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_IN_EXECUTING_ERROR.name(), queryDTO.getSessionId());
        consumer.accept(BuildResMsgUtils.buildHintMsg(queryDTO, message, MessageLevel.Error));
        consumer.accept(BuildResMsgUtils.buildDone(queryDTO));
    }

    private boolean isUsingCacheResult() {
        Long configValue = this.systemDal.fetchSystemConf(RootUserConfig.Fields.onlineResultCacheCapacityMegaByte, Long.class);
        return configValue == null || configValue > 0;
    }
}

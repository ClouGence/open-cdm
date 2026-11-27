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
package com.clougence.clouddm.console.web.service.sdk;

import static com.clougence.clouddm.sdk.openapi.OpenApiErrorCode.*;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.api.console.sqlaudit.Type;
import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.execute.QueryService;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryFO;
import com.clougence.clouddm.console.web.service.editor.query.ConsoleQueryService;
import com.clougence.clouddm.console.web.service.editor.query.QueryCtx;
import com.clougence.clouddm.console.web.service.security.AuditService;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSessionDO;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.openapi.OpenApiCaller;
import com.clougence.clouddm.sdk.openapi.OpenApiException;
import com.clougence.clouddm.sdk.openapi.data.OpenApiDataService;
import com.clougence.clouddm.sdk.openapi.data.OpenApiOperationResult;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsoleOpenApiDataService implements OpenApiDataService {

    private static final int         MAX_ROWS                  = 1000;
    private static final int         TIMEOUT_SECONDS           = 25;
    private static final int         SESSION_IDLE_SECONDS      = 60;
    private static final int         MAX_CONCURRENT_OPERATIONS = 8;

    // Namespace names are also used by driver connection and session setup.
    private static final Pattern     NAMESPACE_SEGMENT         = Pattern.compile("[\\p{L}\\p{M}\\p{N}_$-]+");

    private final ThreadPoolExecutor executor                  = new ThreadPoolExecutor(0,
        MAX_CONCURRENT_OPERATIONS,
        30,
        TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        ThreadUtils.daemonThreadFactory(ConsoleOpenApiDataService.class.getClassLoader(), "OpenAPI-Query-%s"));

    @Resource
    private DmDsConfigService        configService;
    @Resource
    private DmAuthServiceForBiz      authorization;
    @Resource
    private DmResAuthService         resourceAuth;
    @Resource
    private ConsoleQueryService      consoleQueryService;
    @Resource
    private QueryService             queryService;
    @Resource
    private AuditService             auditService;

    @Override
    public CompletionStage<List<OpenApiOperationResult>> executeOperation(OpenApiCaller caller, long dataSourceId, List<String> namespacePath, String sql, Integer maxRows) {
        int rowLimit = maxRows == null ? MAX_ROWS : Math.min(maxRows, MAX_ROWS);
        if (rowLimit <= 0) {
            throw new OpenApiException(INVALID_ARGUMENT);
        }

        long start = System.nanoTime();
        var result = new CompletableFuture<List<OpenApiOperationResult>>();
        try {
            this.executor.execute(() -> this.execute(caller, dataSourceId, namespacePath, sql, rowLimit, start, result));
        } catch (RejectedExecutionException e) {
            throw new OpenApiException(RATE_LIMITED);
        }

        return result.orTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS).exceptionally(e -> {
            if (e instanceof TimeoutException) {
                throw new OpenApiException(EXECUTION_TIMEOUT);
            }
            throw new CompletionException(e);
        });
    }

    @PreDestroy
    public void stop() {
        this.executor.shutdownNow();
    }

    private void execute(OpenApiCaller caller, long dataSourceId, List<String> path, String sql, int rowLimit, long start, CompletableFuture<List<OpenApiOperationResult>> result) {
        String sessionId = UUID.randomUUID().toString().replace("-", "");
        String uid = caller.uid();
        boolean openingSession = false;
        boolean cancel = false;
        try {
            DsLevels levels = this.authorizeNamespace(caller, dataSourceId, path);
            WsQueryFO query = new WsQueryFO();
            query.setQueryString(sql);
            query.setCurrentUserId(uid);
            query.setPrimaryUserId(caller.puid());
            query.setSessionId(sessionId);
            query.setRdbAutoCommit(true);

            QueryCtx ctx = this.consoleQueryService.createQueryCtx(query, levels);
            SessionContextDTO context = ctx.getCtxDTO();
            int seconds = positiveCap(ctx.getDsConfig().getOnlineMaxQueryTimeoutSec(), TIMEOUT_SECONDS);
            long deadline = start + TimeUnit.SECONDS.toNanos(seconds);
            result.orTimeout(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
            checkDeadline(result, deadline);

            context.setRdbReadOnly(Boolean.TRUE.equals(ctx.getDsConfig().getReadOnly()));
            context.setMaxIdleTimeSec(positiveCap(context.getMaxIdleTimeSec(), SESSION_IDLE_SECONDS));

            // Analysis may need brief metadata sessions to resolve objects and column lineage.
            SqlParserParameters parameters = this.configService.fetchSqlParserParameters(dataSourceId, levels.levelsParam());
            QueryRequest request = this.consoleQueryService.prepareOpenApiQuery(query, ctx, parameters);
            request.getResultConf().setFetchRecordCountLimit(rowLimit + 1L);
            request.getResultConf().setFetchEchoBytesLimit(OpenApiResultCollector.MAX_RESPONSE_BYTES);
            checkDeadline(result, deadline);

            openingSession = true;
            DmExecSessionDO session = this.openSession(uid, ctx, parameters);
            checkDeadline(result, deadline);
            this.auditService.prepareAudit(dataSourceId, uid, request);

            String batchId = UUID.randomUUID().toString().replace("-", "");
            try {
                checkDeadline(result, deadline);
                request.getResultConf().setQueryTimeoutSec((int) (TimeUnit.NANOSECONDS.toSeconds(deadline - System.nanoTime()) + 1));
                cancel = true;
                this.queryService.asyncExecuteQuery(session, batchId, List.of(request), () -> checkDeadline(result, deadline));
            } catch (RuntimeException e) {
                this.recordSubmissionFailure(request, sessionId);
                checkDeadline(result, deadline);
                log.warn("Could not submit SQL for OpenAPI session {}.", sessionId, e);
                throw new OpenApiException(SQL_EXECUTION_FAILED);
            }

            OpenApiResultCollector collector = new OpenApiResultCollector(batchId, rowLimit, request.getResultConf().getFetchRecordCountLimit());
            this.readResults(uid, sessionId, result, deadline, collector);
            cancel = false;
            result.complete(collector.finish());
        } catch (InterruptedException e) {
            result.completeExceptionally(new OpenApiException(SQL_EXECUTION_FAILED));
            Thread.currentThread().interrupt();
        } catch (RuntimeException e) {
            result.completeExceptionally(e);
        } finally {
            if (openingSession) {
                this.releaseSession(uid, sessionId, cancel);
            }
        }
    }

    private DsLevels authorizeNamespace(OpenApiCaller caller, long dataSourceId, List<String> path) {
        if (!this.authorization.checkRoleAuth(caller.puid(), caller.uid(), DM_QUERY_CONSOLE)) {
            throw new OpenApiException(FORBIDDEN);
        }
        if (path.stream().anyMatch(segment -> !NAMESPACE_SEGMENT.matcher(segment).matches())) {
            throw new OpenApiException(INVALID_ARGUMENT);
        }
        DsLevels levels;
        try {
            levels = this.configService.parseLevels(dataSourceId, path);
        } catch (ErrorMessageException e) {
            throw new OpenApiException(NOT_FOUND);
        }
        if (!caller.puid().equals(levels.dsDO().getUid())) {
            throw new OpenApiException(NOT_FOUND);
        }
        if (!this.resourceAuth.listResByUserContainAnyAuth(caller.uid(), AuthKind.DataSource).contains(dataSourceId)) {
            throw new OpenApiException(FORBIDDEN);
        }
        int depth = this.configService.dsConstantSettings(levels.dsDO().getDataSourceType()).getCategories().getLevels().size() - 2;
        if (path.size() != depth || levels.levelsDef().stream().anyMatch(level -> level != UmiTypes.Catalog && level != UmiTypes.Schema)) {
            throw new OpenApiException(INVALID_ARGUMENT);
        }
        return levels;
    }

    private DmExecSessionDO openSession(String uid, QueryCtx ctx, SqlParserParameters parameters) {
        SessionContextDTO context = ctx.getCtxDTO();
        DmExecSessionDO session;
        try {
            this.queryService.createSession(uid, ctx.getLevels(), context);
            session = this.queryService.getSessionInfo(uid, context.getSessionId());
        } catch (RuntimeException e) {
            log.warn("OpenAPI session {} could not be opened.", context.getSessionId(), e);
            throw new OpenApiException(SQL_EXECUTION_FAILED);
        }

        if (session == null) {
            throw new OpenApiException(SQL_EXECUTION_FAILED);
        }

        SessionContextDTO actual = session.toRdbCtx();
        var namespace = ctx.getLevels().levelsParam();
        if ((namespace.containsKey(UmiTypes.Catalog) && !Objects.equals(namespace.get(UmiTypes.Catalog), actual.getRdbCatalog()))
            || (namespace.containsKey(UmiTypes.Schema) && !Objects.equals(namespace.get(UmiTypes.Schema), actual.getRdbSchema()))) {
            throw new OpenApiException(NOT_FOUND);
        }

        if (!actual.isRdbAutoCommit()) {
            throw new OpenApiException(SQL_EXECUTION_FAILED);
        }

        if (!parameters.values().equals(actual.getSqlParameters())) {
            throw new OpenApiException(STATE_CONFLICT);
        }

        return session;
    }

    private void readResults(String uid, String sessionId, CompletableFuture<?> result, long deadline, OpenApiResultCollector collector) throws InterruptedException {
        while (true) {
            checkDeadline(result, deadline);
            ResultList batch;
            try {
                batch = this.queryService.fetchQueryResult(uid, sessionId);
            } catch (RuntimeException e) {
                log.warn("Could not read results from OpenAPI session {}.", sessionId, e);
                throw new OpenApiException(SQL_EXECUTION_FAILED);
            }

            if (batch == null) {
                throw new OpenApiException(SQL_EXECUTION_FAILED);
            }

            if (collector.accept(batch.getResultList())) {
                return;
            }

            if (batch.getResultList().isEmpty()) {
                TimeUnit.NANOSECONDS.sleep(Math.min(TimeUnit.MILLISECONDS.toNanos(100), deadline - System.nanoTime()));
            }
        }
    }

    private void recordSubmissionFailure(QueryRequest request, String sessionId) {
        SqlExecNotifyDTO audit = new SqlExecNotifyDTO();
        audit.setSessionId(sessionId);
        audit.setQueryId(request.getQueryId());
        audit.setTime(new Date());
        audit.setType(Type.SQL_END);
        audit.setStatus(SqlStatus.ERROR);
        try {
            this.auditService.recordAudit(audit, null);
        } catch (RuntimeException e) {
            log.error("Could not record submission failure for OpenAPI SQL query {}.", request.getQueryId(), e);
        }
    }

    private void releaseSession(String uid, String sessionId, boolean cancel) {
        // Cleanup must still issue RPCs when shutdown interrupted the result poll.
        boolean interrupted = Thread.interrupted();
        try {
            if (cancel) {
                try {
                    this.queryService.cancelQuery(uid, sessionId);
                } catch (RuntimeException e) {
                    log.warn("Could not cancel OpenAPI session {}.", sessionId, e);
                }
            }
            this.queryService.closeEphemeralSession(uid, sessionId);
        } catch (RuntimeException e) {
            log.warn("Could not close OpenAPI session {}.", sessionId, e);
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void checkDeadline(CompletableFuture<?> result, long deadline) {
        if (result.isDone() || System.nanoTime() >= deadline) {
            throw new OpenApiException(EXECUTION_TIMEOUT);
        }
    }

    private static int positiveCap(Integer configured, int cap) {
        return configured == null || configured <= 0 ? cap : Math.min(configured, cap);
    }
}

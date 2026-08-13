/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.handler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.analysis.AnalysisQueryOptions;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisFeature;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.approval.model.*;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.execute.QueryService;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlan;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanNode;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmlExplainPreInitHandler extends AbstractPreInitHandler {

    private static final int     DEFAULT_MAX_STATEMENTS          = 100;
    private static final int     DEFAULT_MAX_STATEMENT_MEGABYTES = 1;
    private static final long    BYTES_PER_MEGABYTE              = 1024L * 1024L;
    @Resource
    private ApprovalService      approvalService;
    @Resource
    private QueryAnalysisService queryAnalysisService;
    @Resource
    private QueryService         queryService;
    @Resource
    private DmDsConfigService    dmDsConfigService;
    @Resource
    private SystemDal            systemDal;

    @Override
    protected String analysisType() {
        return ApprovalAnalysisStateMO.TYPE_DML_EXPLAIN;
    }

    @Override
    public int displayOrder() {
        return 3;
    }

    @Override
    protected void doHandle(PreInitContext context) throws IOException {
        DmlExplainStatistics statistics = new DmlExplainStatistics();
        List<DmlExplainResultMO> results = new ArrayList<>();
        context.writeResult(state -> {
            state.setDmlStatementCount(statistics.getDmlCount());
            state.setExecutedExplainCount(statistics.getExecutedCount());
            state.setSkippedBySizeLimit(statistics.getSkippedBySize());
            state.setSkippedByCountLimit(statistics.getSkippedByCount());
            state.setFailedExplainCount(statistics.getFailedCount());
            List<DmlExplainResultMO> sortedResults = new ArrayList<>(results);
            sortedResults.sort(Comparator.comparingLong(DmlExplainResultMO::getIndex));
            state.setExplainResults(sortedResults);
        });

        File requestCache = this.createRequestCache(context);
        try {
            this.buildRequests(context, requestCache, statistics);
            this.executeRequests(context, requestCache, statistics, results);
        } finally {
            try {
                Files.deleteIfExists(requestCache.toPath());
            } catch (IOException e) {
                log.warn("delete DML EXPLAIN request cache failed, path={}", requestCache, e);
            }
        }

        long expectedAffectedRows = results.stream()//
            .map(DmlExplainResultMO::getEstimatedAffectedRows)
            .filter(Objects::nonNull)
            .mapToLong(Long::longValue)
            .sum();
        context.getApprovalDal().approvalMapper().updateExpectedAffectedRows(context.getApproval().getId(), expectedAffectedRows);
    }

    private static ExplainPlanSpi findExplainSpi(DataSourceType dsType) {
        DsPluginInfo dsPlugin = PluginManager.findDsPlugin(dsType);
        List<ExplainPlanSpi> explains = dsPlugin == null ? Collections.emptyList() : dsPlugin.findSpi(ExplainPlanSpi.class);
        return explains.isEmpty() ? null : explains.get(0);
    }

    private File createRequestCache(PreInitContext context) {
        try {
            Path directory = Path.of(GlobalConfUtils.getTempDataHome(), "approval");
            Files.createDirectories(directory);
            return Files.createTempFile(directory, "explain-" + context.getApproval().getId() + "-", ".jsonl").toFile();
        } catch (IOException e) {
            throw new IllegalStateException("create DML EXPLAIN request cache failed", e);
        }
    }

    //
    // buildRequests
    //

    private void buildRequests(PreInitContext context, File requestCache, DmlExplainStatistics statistics) throws IOException {
        context.startPhase(ApprovalAnalysisStateMO.PHASE_PREPARING, null);
        ExplainPlanSpi explainSpi = findExplainSpi(context.getDsConfig().getDataSourceType());
        if (explainSpi == null) {
            return;
        }
        int maxStatements = this.systemDal.fetchSystemConf(RootUserConfig.Fields.approvalDmlExplainMaxStatements, Integer.class, DEFAULT_MAX_STATEMENTS);
        int maxStatementMegaBytes = this.systemDal.fetchSystemConf(RootUserConfig.Fields.approvalDmlExplainMaxStatementMegaByte, Integer.class, DEFAULT_MAX_STATEMENT_MEGABYTES);
        long maxStatementBytes = maxStatementMegaBytes * BYTES_PER_MEGABYTE;
        SqlEngineSpi sqlEngine = this.dmDsConfigService.fetchSqlEngineSpi(context.getDsConfig());
        SqlParserParameters parameters = this.dmDsConfigService.fetchSqlParserParameters(context.getDsConfig(), context.getDsLevels().levelsParam());
        RewriteSpi rewriteSpi = sqlEngine.rewriteSpi(parameters);

        DmApprovalDO approval = context.getApproval();
        try (BufferedWriter writer = Files.newBufferedWriter(requestCache.toPath(), StandardCharsets.UTF_8)) {
            this.approvalService.consumeSqlFile(approval.getId(), sql -> {
                try (Reader reader = context.openReader(sql)) {
                    AnalysisQueryOptions options = AnalysisQueryOptions.builder()
                        .currentUid(approval.getOwnerUid())
                        .dataSourceId(approval.getBindDsId())
                        .levels(context.getDsLevels().levelsParam())
                        .skip(QueryAnalysisFeature.REWRITE, QueryAnalysisFeature.LINEAGE, QueryAnalysisFeature.MASKING)
                        .build();
                    try (Stream<QueryRequest> stream = this.queryAnalysisService.analysisRequestsStream(context.getDsConfig(), reader, Collections.emptyList(), 1, 0, options)) {
                        stream.parallel().forEach(request -> {
                            this.cacheExplainRequest(context, request, statistics, explainSpi, rewriteSpi, parameters, writer, maxStatements, maxStatementBytes);
                        });
                        return null;
                    }
                }
            });
        }
    }

    private void cacheExplainRequest(PreInitContext context, QueryRequest request, DmlExplainStatistics statistics,     //
                                     ExplainPlanSpi explainSpi, RewriteSpi rewriteSpi, SqlParserParameters parameters,  //
                                     BufferedWriter writer, int maxStatements, long maxStatementBytes) {
        try {
            if (!isDml(request.getRelations())) {
                return;
            }
            statistics.incrementDmlCount();
            long statementBytes = request.getQueryBody().getBytes(StandardCharsets.UTF_8).length;
            if (!hasInsertStatement(request.getRelations()) && !explainSpi.supportByQueryType(request.getQueryTypes())) {
                return;
            }
            if (statementBytes > maxStatementBytes) {
                statistics.incrementSkippedBySize();
                return;
            }

            synchronized (writer) {
                if (statistics.getCachedCount() >= maxStatements) {
                    statistics.incrementSkippedByCount();
                    return;
                }
                QueryRequest explainRequest = this.prepareExplainRequest(context, request, rewriteSpi, parameters);
                if (explainRequest == null) {
                    return;
                }
                DmlExplainRequestMO record = new DmlExplainRequestMO();
                record.setIndex(request.getIndex());
                record.setStatementSizeBytes(statementBytes);
                record.setRequest(explainRequest);
                try {
                    writer.write(JsonUtils.toJson(record));
                    writer.newLine();
                    statistics.incrementCachedCount();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        } finally {
            context.itemProcessed(request.getQueryBody());
        }
    }

    private QueryRequest prepareExplainRequest(PreInitContext context, QueryRequest analyzed, RewriteSpi rewriteSpi, SqlParserParameters parameters) {
        SessionSpi sessionSpi = PluginManager.findSessionSpi(context.getDsConfig().getDataSourceType());
        QueryRequest request = sessionSpi.createQueryRequest(context.getDsConfig());
        request.setIndex(analyzed.getIndex());
        request.setQueryId(sessionSpi.newQueryId());
        request.setQueryBody(analyzed.getQueryBody());
        request.setQueryArgs(analyzed.getQueryArgs());
        request.setBodyStartCodeLine(analyzed.getBodyStartCodeLine());
        request.setQueryTypes(analyzed.getQueryTypes());
        request.setRelations(analyzed.getRelations());
        request.setDsType(analyzed.getDsType());
        request.setRequester(Requester.TICKET);
        if (!hasInsertStatement(request.getRelations())) {
            if (rewriteSpi == null) {
                return null;
            }

            RewriteContext rewriteContext = new RewriteContext();
            rewriteContext.setParameters(parameters);
            String explainQuery = rewriteSpi.rewriteToExplain(request.getQueryId(), request.getQueryBody(), rewriteContext);
            if (StringUtils.isBlank(explainQuery)) {
                return null;
            }

            request.setQueryBody(explainQuery);
            request.setUseExplain(true);
        }
        request.getResultConf().setCacheResult(false);
        request.getResultConf().setReceiveMode(ReceiveMode.PAGE_FULL);
        request.getResultConf().setRefreshStatus(true);
        return request;
    }

    private static boolean isDml(List<BehaviorRelation> relations) {
        return relations != null && relations.stream().anyMatch(r -> {
            return r != null && ExplainPlanSpi.AFFECTED_ROW_ACTIONS.contains(r.getAction());
        });
    }

    //
    // execRequests
    //

    private void executeRequests(PreInitContext context, File requestCache, DmlExplainStatistics statistics, List<DmlExplainResultMO> results) {
        context.startPhase(ApprovalAnalysisStateMO.PHASE_ANALYZING, statistics.getCachedCount());
        ExplainPlanSpi explainSpi = findExplainSpi(context.getDsConfig().getDataSourceType());
        if (explainSpi == null) {
            return;
        }

        String sessionId = null;
        try (BufferedReader reader = Files.newBufferedReader(requestCache.toPath(), StandardCharsets.UTF_8)) {
            if (statistics.getCachedCount() > 0) {
                sessionId = this.createExplainSession(context);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                DmlExplainRequestMO request = JsonUtils.toObj(line, DmlExplainRequestMO.class);
                List<DmlExplainResultMO> requestResults = this.executeOne(context, sessionId, request, explainSpi, statistics);
                results.addAll(requestResults);
                context.itemProcessed();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            this.closeExplainSession(context, sessionId);
        }
    }

    private String createExplainSession(PreInitContext context) {
        SessionContextDTO sessionContext = DmDsUtils.createSessionCtx(context.getDsConfig(), context.getDsLevels().levelsParam());
        sessionContext.setSessionId(UUID.randomUUID().toString().replace("-", ""));
        DataSourceType dsType = context.getDsConfig().getDataSourceType();
        boolean usesExplainTable = dsType == DataSourceType.Oracle ||   //
                                   dsType == DataSourceType.Db2 ||      //
                                   dsType == DataSourceType.Db2Fori ||  //
                                   dsType == DataSourceType.Hana;
        sessionContext.setRdbReadOnly(!usesExplainTable);
        return this.queryService.createSession(context.getApproval().getOwnerUid(), context.getDsLevels(), sessionContext);
    }

    private void closeExplainSession(PreInitContext context, String sessionId) {
        if (sessionId == null) {
            return;
        }
        try {
            this.queryService.rollbackSession(context.getApproval().getOwnerUid(), sessionId);
        } catch (RuntimeException e) {
            log.warn("rollback DML EXPLAIN session failed, ticketId={}", context.getApproval().getId(), e);
        }
        try {
            this.queryService.closeSession(context.getApproval().getOwnerUid(), sessionId);
        } catch (RuntimeException e) {
            log.warn("close DML EXPLAIN session failed, ticketId={}", context.getApproval().getId(), e);
        }
    }

    private List<DmlExplainResultMO> executeOne(PreInitContext context, String sessionId, DmlExplainRequestMO record, ExplainPlanSpi explainSpi, DmlExplainStatistics statistics) {
        QueryRequest request = record.getRequest();
        List<DmlExplainResultMO> results = createResultsByAffectedObjects(request, record.getStatementSizeBytes());

        try {
            if (hasInsertStatement(request.getRelations())) {
                return executeOne4Insert(request, explainSpi, results);
            } else {
                return this.executeOne4NativeExplain(context, sessionId, request, explainSpi, statistics, results);
            }
        } catch (RuntimeException e) {
            for (DmlExplainResultMO result : results) {
                result.setStatus(DmlExplainStatus.FAILED);
                result.setMessage(e.getMessage());
            }
            statistics.incrementFailedCount();
            log.warn("DML EXPLAIN failed, ticketId={}, index={}", context.getApproval().getId(), record.getIndex(), e);
            return results;
        }
    }

    private static List<DmlExplainResultMO> executeOne4Insert(QueryRequest request, ExplainPlanSpi explainSpi, List<DmlExplainResultMO> results) {
        ExplainPlan plan = explainSpi.analyze(Collections.emptyList(), request.getRelations());
        for (DmlExplainResultMO result : results) {
            result.setExplainPlan(plan);
            result.setEstimatedAffectedRows(insertAffectedRows(result.getSubjects(), plan));
            result.setStatus(DmlExplainStatus.SUCCESS);
        }
        return results;
    }

    private List<DmlExplainResultMO> executeOne4NativeExplain(PreInitContext context, String sessionId, QueryRequest request, ExplainPlanSpi explainSpi,
                                                              DmlExplainStatistics statistics, List<DmlExplainResultMO> results) {
        statistics.incrementExecutedCount();
        request.setUseExplain(true);
        ResultList resultList = this.queryService.syncExecuteQuery(context.getApproval().getOwnerUid(), sessionId, request);
        List<Result> rawResults = resultList == null ? Collections.emptyList() : resultList.getResultList();
        Result failure = rawResults == null ? null : rawResults.stream().filter(value -> !value.isSuccess()).findFirst().orElse(null);
        if (failure != null) {
            for (DmlExplainResultMO result : results) {
                result.setStatus(DmlExplainStatus.FAILED);
                result.setMessage(failure.getMessage());
            }
            statistics.incrementFailedCount();
            return results;
        }

        ExplainPlan plan = explainSpi.analyze(rawResults, request.getRelations());
        for (DmlExplainResultMO result : results) {
            result.setExplainPlan(plan);
            result.setEstimatedAffectedRows(insertAffectedRows(result.getSubjects(), plan));
            result.setStatus(DmlExplainStatus.SUCCESS);
        }
        return results;
    }

    private static Long insertAffectedRows(List<String> subjects, ExplainPlan plan) {
        if (subjects == null || subjects.isEmpty() || plan == null || plan.getNodes() == null) {
            return null;
        }
        List<Double> estimates = plan.getNodes()
            .stream()
            .filter(node -> subjects.contains(node.getObjectPath()))
            .map(ExplainPlanNode::getEstimatedRows)
            .filter(Objects::nonNull)
            .toList();
        if (estimates.isEmpty()) {
            return null;
        }
        return Math.round(estimates.stream().mapToDouble(Double::doubleValue).sum());
    }

    private static List<DmlExplainResultMO> createResultsByAffectedObjects(QueryRequest request, long statementBytes) {
        Map<String, Set<BehaviorAction>> actionsBySubject = new LinkedHashMap<>();
        if (request.getRelations() != null) {
            for (BehaviorRelation relation : request.getRelations()) {
                if (relation == null || !ExplainPlanSpi.AFFECTED_ROW_ACTIONS.contains(relation.getAction())) {
                    continue;
                }
                String objectPath = relation.getSubject() == null ? null : relation.getSubject().getObjectPath();
                actionsBySubject.computeIfAbsent(objectPath, key -> new LinkedHashSet<>()).add(relation.getAction());
            }
        }

        List<DmlExplainResultMO> results = new ArrayList<>();
        for (Map.Entry<String, Set<BehaviorAction>> entry : actionsBySubject.entrySet()) {
            DmlExplainResultMO result = new DmlExplainResultMO();
            result.setIndex(request.getIndex());
            if (request.getBodyStartCodeLine() > 0) {
                result.setStatementStartLine(request.getBodyStartCodeLine());
            }

            result.setStatementSizeBytes(statementBytes);
            result.setActions(entry.getValue());

            if (entry.getKey() == null) {
                result.setSubjects(Collections.emptyList());
            } else {
                result.setSubjects(Collections.singletonList(entry.getKey()));
            }
            results.add(result);
        }
        return results;
    }

    // Utils

    private static boolean hasInsertStatement(List<BehaviorRelation> relations) {
        if (relations == null || relations.isEmpty()) {
            return false;
        }
        List<BehaviorRelation> writes = relations.stream().filter(r -> {
            return r != null && ExplainPlanSpi.AFFECTED_ROW_ACTIONS.contains(r.getAction());
        }).toList();

        return !writes.isEmpty() && writes.stream().allMatch(r -> {
            return r.getInsertRows() != null;
        });
    }

}

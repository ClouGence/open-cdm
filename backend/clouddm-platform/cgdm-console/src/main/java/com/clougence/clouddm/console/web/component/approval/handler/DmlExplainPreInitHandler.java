/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.handler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
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
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlan;
import com.clougence.clouddm.sdk.execute.explain.ExplainPlanSpi;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmlExplainPreInitHandler extends AbstractPreInitHandler {

    private static final int     EXPLAIN_SHARD_COUNT             = 4;
    private static final int     DEFAULT_MAX_STATEMENTS          = 100;
    private static final int     DEFAULT_MAX_STATEMENT_MEGABYTES = 1;
    private static final long    BYTES_PER_MEGABYTE              = 1024L * 1024L;
    @Resource
    private ApprovalService      approvalService;
    @Resource
    private QueryAnalysisService queryAnalysisService;
    @Resource
    private DmDsConfigService    dmDsConfigService;
    @Resource
    private QueryService         queryService;
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
    protected void doHandle(PreInitContext context) {
        int maxStatements = this.systemDal.fetchSystemConf(//
                RootUserConfig.Fields.approvalDmlExplainMaxStatements, Integer.class, DEFAULT_MAX_STATEMENTS);
        int maxStatementMegaBytes = this.systemDal.fetchSystemConf(//
                RootUserConfig.Fields.approvalDmlExplainMaxStatementMegaByte, Integer.class, DEFAULT_MAX_STATEMENT_MEGABYTES);
        long maxStatementBytes = maxStatementMegaBytes * BYTES_PER_MEGABYTE;
        Path workDirectory = Path.of(GlobalConfUtils.getTempDataHome(), "approval", "explain-" + context.getApproval().getId());

        AtomicLong dmlCount = new AtomicLong();
        AtomicLong cachedCount = new AtomicLong();
        AtomicLong executedCount = new AtomicLong();
        AtomicLong skippedBySize = new AtomicLong();
        AtomicLong skippedByCount = new AtomicLong();
        AtomicLong failedCount = new AtomicLong();
        List<DmlExplainResultMO> results = new ArrayList<>();
        context.writeResult(s -> {
            this.writeState(s, dmlCount, cachedCount, executedCount, skippedBySize, skippedByCount, failedCount, results);
        });

        try {
            Files.createDirectories(workDirectory);
            List<Path> requestFiles = this.buildRequestFiles(context, workDirectory, maxStatements, maxStatementBytes, dmlCount, cachedCount, skippedBySize, skippedByCount);
            List<Path> resultFiles = this.executeRequestFiles(context, workDirectory, requestFiles, executedCount, failedCount);
            results.addAll(this.mergeResults(workDirectory, resultFiles));
        } catch (IOException e) {
            throw new IllegalStateException("DML EXPLAIN local file processing failed", e);
        } finally {
            this.deleteWorkDirectory(workDirectory);
        }
    }

    private List<Path> buildRequestFiles(PreInitContext context, Path workDirectory, int maxStatements, long maxStatementBytes, AtomicLong dmlCount, AtomicLong cachedCount,
                                         AtomicLong skippedBySize, AtomicLong skippedByCount) throws IOException {
        List<Path> stagingFiles = new ArrayList<>();
        List<BufferedWriter> writers = new ArrayList<>();
        for (int shard = 0; shard < EXPLAIN_SHARD_COUNT; shard++) {
            Path staging = workDirectory.resolve("explain-request-" + shard + ".jsonl.tmp");
            stagingFiles.add(staging);
            writers.add(Files.newBufferedWriter(staging, StandardCharsets.UTF_8));
        }
        Path skippedFile = workDirectory.resolve("explain-result-skipped.jsonl");

        DmApprovalDO approval = context.getApproval();
        AnalysisQueryOptions options = AnalysisQueryOptions.builder()
            .currentUid(approval.getOwnerUid())
            .dataSourceId(approval.getBindDsId())
            .levels(context.getDsLevels().levelsParam())
            .skip(QueryAnalysisFeature.REWRITE, QueryAnalysisFeature.LINEAGE, QueryAnalysisFeature.MASKING)
            .build();

        try (BufferedWriter skippedWriter = Files.newBufferedWriter(skippedFile, StandardCharsets.UTF_8)) {
            this.approvalService.consumeSqlFile(approval.getId(), sql -> {
                try (Reader reader = context.openReader(sql);
                        Stream<QueryRequest> requests = this.queryAnalysisService.analysisRequestsStream(context.getDsConfig(), reader, Collections.emptyList(), 1, 0, options)) {
                    requests.forEachOrdered(request -> {
                        context.itemProcessed(request.getQueryBody());
                        if (!isDml(request.getRelations())) {
                            return;
                        }
                        dmlCount.incrementAndGet();
                        if (cachedCount.get() >= maxStatements) {
                            skippedByCount.incrementAndGet();
                            return;
                        }
                        long statementBytes = request.getQueryBody().getBytes(StandardCharsets.UTF_8).length;
                        if (statementBytes > maxStatementBytes) {
                            skippedBySize.incrementAndGet();
                            for (DmlExplainResultMO result : skippedResults(request, statementBytes)) {
                                writeJsonLine(skippedWriter, result);
                            }
                            return;
                        }

                        QueryRequest explainRequest = this.prepareExplainRequest(context, request);
                        DmlExplainRequestMO record = new DmlExplainRequestMO();
                        record.setIndex(request.getIndex());
                        record.setStatementSizeBytes(statementBytes);
                        record.setRequest(explainRequest);
                        int shard = Math.floorMod(request.getIndex(), EXPLAIN_SHARD_COUNT);
                        writeJsonLine(writers.get(shard), record);
                        cachedCount.incrementAndGet();
                    });
                    return null;
                }
            });
        } finally {
            for (BufferedWriter writer : writers) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.warn("close DML EXPLAIN request writer failed", e);
                }
            }
        }

        List<Path> requestFiles = new ArrayList<>();
        for (int shard = 0; shard < EXPLAIN_SHARD_COUNT; shard++) {
            Path target = workDirectory.resolve("explain-request-" + shard + ".jsonl");
            moveCompleted(stagingFiles.get(shard), target);
            requestFiles.add(target);
        }
        return requestFiles;
    }

    private List<Path> executeRequestFiles(PreInitContext context, Path workDirectory, List<Path> requestFiles, AtomicLong executedCount,
                                           AtomicLong failedCount) throws IOException {
        SqlEngineSpi engine = this.dmDsConfigService.fetchSqlEngineSpi(context.getDsConfig());
        ExplainPlanSpi explainSpi = PluginManager.findSpi(ExplainPlanSpi.class, engine.name());
        List<Path> resultFiles = new ArrayList<>();
        String sessionId = null;
        try {
            boolean hasRequests = false;
            for (Path requestFile : requestFiles) {
                if (Files.size(requestFile) > 0) {
                    hasRequests = true;
                    break;
                }
            }
            if (explainSpi != null && hasRequests) {
                SessionContextDTO sessionContext = DmDsUtils.createSessionCtx(context.getDsConfig(), context.getDsLevels().levelsParam());
                sessionContext.setSessionId(UUID.randomUUID().toString().replace("-", ""));
                // Keep the analysis transaction uncommitted if the EXPLAIN marker is lost unexpectedly.
                sessionContext.setRdbAutoCommit(false);
                sessionContext.setRdbReadOnly(true);
                sessionId = this.queryService.createSession(context.getApproval().getOwnerUid(), context.getDsLevels(), sessionContext);
            }

            for (int shard = 0; shard < requestFiles.size(); shard++) {
                Path resultFile = workDirectory.resolve("explain-result-" + shard + ".jsonl");
                resultFiles.add(resultFile);
                try (BufferedReader reader = Files.newBufferedReader(requestFiles.get(shard), StandardCharsets.UTF_8);
                        BufferedWriter writer = Files.newBufferedWriter(resultFile, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        DmlExplainRequestMO record = JsonUtils.toObj(line, DmlExplainRequestMO.class);
                        List<DmlExplainResultMO> results = explainSpi == null ? unsupportedResults(record) : this
                            .executeOne(context, sessionId, record, explainSpi, executedCount, failedCount);
                        for (DmlExplainResultMO result : results) {
                            writeJsonLine(writer, result);
                        }
                    }
                }
            }
        } finally {
            if (sessionId != null) {
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
        }
        return resultFiles;
    }

    private List<DmlExplainResultMO> executeOne(PreInitContext context, String sessionId, DmlExplainRequestMO record, ExplainPlanSpi explainSpi, AtomicLong executedCount,
                                                AtomicLong failedCount) {
        QueryRequest request = record.getRequest();
        List<DmlExplainResultMO> results = baseResults(request, record.getStatementSizeBytes());
        try {
            executedCount.incrementAndGet();
            request.setUseExplain(true);
            ResultList resultList = this.queryService.syncExecuteQuery(context.getApproval().getOwnerUid(), sessionId, request);
            List<Result> rawResults = resultList == null ? Collections.emptyList() : resultList.getResultList();
            Result failure = rawResults == null ? null : rawResults.stream().filter(value -> !value.isSuccess()).findFirst().orElse(null);
            if (failure != null) {
                for (DmlExplainResultMO result : results) {
                    result.setStatus(DmlExplainStatus.FAILED);
                    result.setMessage(failure.getMessage());
                }
                failedCount.incrementAndGet();
                return results;
            }
            ExplainPlan plan = explainSpi.analyze(rawResults, request.getRelations());
            for (DmlExplainResultMO result : results) {
                result.setExplainPlan(plan);
                result.setEstimatedAffectedRows(estimatedAffectedRows(result.getSubjects(), plan));
                result.setStatus(DmlExplainStatus.SUCCESS);
            }
            return results;
        } catch (RuntimeException e) {
            for (DmlExplainResultMO result : results) {
                result.setStatus(DmlExplainStatus.FAILED);
                result.setMessage(e.getMessage());
            }
            failedCount.incrementAndGet();
            log.warn("DML EXPLAIN failed, ticketId={}, index={}", context.getApproval().getId(), record.getIndex(), e);
            return results;
        }
    }

    private QueryRequest prepareExplainRequest(PreInitContext context, QueryRequest analyzed) {
        SessionSpi sessionSpi = PluginManager.findSessionSpi(context.getDsConfig().getDataSourceType());
        QueryRequest request = sessionSpi.createQueryRequest(context.getDsConfig());
        request.setIndex(analyzed.getIndex());
        request.setQueryId(sessionSpi.newQueryId());
        request.setQueryBody(analyzed.getQueryBody());
        request.setQueryArgs(analyzed.getQueryArgs());
        request.setQueryTypes(analyzed.getQueryTypes());
        request.setRelations(analyzed.getRelations());
        request.setDsType(analyzed.getDsType());
        request.setRequester(Requester.TICKET);
        request.setUseExplain(true);
        request.getResultConf().setCacheResult(false);
        request.getResultConf().setReceiveMode(ReceiveMode.PAGE_FULL);
        request.getResultConf().setRefreshStatus(true);
        return request;
    }

    private List<DmlExplainResultMO> mergeResults(Path workDirectory, List<Path> resultFiles) throws IOException {
        List<DmlExplainResultMO> results = new ArrayList<>();
        List<Path> files = new ArrayList<>(resultFiles);
        files.add(workDirectory.resolve("explain-result-skipped.jsonl"));
        for (Path file : files) {
            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    results.add(JsonUtils.toObj(line, DmlExplainResultMO.class));
                }
            }
        }
        results.sort(Comparator.comparingLong(DmlExplainResultMO::getIndex));
        return results;
    }

    private void writeState(ApprovalAnalysisStateMO state, AtomicLong dmlCount, AtomicLong cachedCount, AtomicLong executedCount, AtomicLong skippedBySize,
                            AtomicLong skippedByCount, AtomicLong failedCount, List<DmlExplainResultMO> results) {
        state.setTotalCount(dmlCount.get());
        state.setDmlStatementCount(dmlCount.get());
        state.setCachedExplainCount(cachedCount.get());
        state.setExecutedExplainCount(executedCount.get());
        state.setSkippedBySizeLimit(skippedBySize.get());
        state.setSkippedByCountLimit(skippedByCount.get());
        state.setFailedExplainCount(failedCount.get());
        state.setExplainResults(new ArrayList<>(results));
    }

    private static boolean isDml(List<BehaviorRelation> relations) {
        return relations != null && relations.stream().anyMatch(relation -> relation != null && ExplainPlanSpi.ACTIONS.contains(relation.getAction()));
    }

    private static Long estimatedAffectedRows(List<String> subjects, ExplainPlan plan) {
        if (subjects == null || subjects.isEmpty() || plan == null || plan.getNodes() == null) {
            return null;
        }
        List<Double> estimates = plan.getNodes()
            .stream()
            .filter(node -> subjects.contains(node.getObjectPath()))
            .map(node -> node.getEstimatedRows())
            .filter(Objects::nonNull)
            .toList();
        if (estimates.isEmpty()) {
            return null;
        }
        return Math.round(estimates.stream().mapToDouble(Double::doubleValue).sum());
    }

    private static List<DmlExplainResultMO> skippedResults(QueryRequest request, long statementBytes) {
        List<DmlExplainResultMO> results = baseResults(request, statementBytes);
        for (DmlExplainResultMO result : results) {
            result.setStatus(DmlExplainStatus.SKIPPED);
            result.setSkipReason(DmlExplainSkipReason.STATEMENT_SIZE_LIMIT);
        }
        return results;
    }

    private static List<DmlExplainResultMO> unsupportedResults(DmlExplainRequestMO record) {
        List<DmlExplainResultMO> results = baseResults(record.getRequest(), record.getStatementSizeBytes());
        for (DmlExplainResultMO result : results) {
            result.setStatus(DmlExplainStatus.UNSUPPORTED);
        }
        return results;
    }

    private static List<DmlExplainResultMO> baseResults(QueryRequest request, long statementBytes) {
        Map<String, Set<BehaviorAction>> actionsBySubject = new LinkedHashMap<>();
        if (request.getRelations() != null) {
            for (BehaviorRelation relation : request.getRelations()) {
                if (relation == null || !ExplainPlanSpi.ACTIONS.contains(relation.getAction())) {
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

    private static void writeJsonLine(BufferedWriter writer, Object value) {
        try {
            writer.write(JsonUtils.toJson(value));
            writer.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void moveCompleted(Path staging, Path target) throws IOException {
        try {
            Files.move(staging, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(staging, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void deleteWorkDirectory(Path workDirectory) {
        if (!Files.isDirectory(workDirectory)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(workDirectory)) {
            paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    log.warn("delete DML EXPLAIN temporary file failed, path={}", path, e);
                }
            });
        } catch (IOException e) {
            log.warn("scan DML EXPLAIN temporary directory failed, path={}", workDirectory, e);
        }
    }
}

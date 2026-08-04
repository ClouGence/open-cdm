/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.handler;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.analysis.AnalysisQueryOptions;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisFeature;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;

import jakarta.annotation.Resource;

/**
 * Recognizes SQL statements and records their total count.
 */
@Service
public class SqlCounterPreInitHandler extends AbstractPreInitHandler {

    @Resource
    private ApprovalService      approvalService;
    @Resource
    private QueryAnalysisService queryAnalysisService;

    @Override
    protected String analysisType() {
        return ApprovalAnalysisStateMO.TYPE_SQL_RECOGNITION;
    }

    @Override
    protected void doHandle(DataSourceConfig dsConfig, DsLevels dsLevels, PreInitContext context, Runnable onProcessed) {
        DmApprovalDO approvalDO = context.getApproval();
        AnalysisQueryOptions options = AnalysisQueryOptions.builder()
            .currentUid(approvalDO.getOwnerUid())
            .dataSourceId(approvalDO.getBindDsId())
            .levels(dsLevels.levelsParam())
            .skip(QueryAnalysisFeature.REWRITE, QueryAnalysisFeature.LINEAGE, QueryAnalysisFeature.MASKING)
            .build();

        this.approvalService.consumeSqlFile(approvalDO.getId(), sql -> {
            try (Reader reader = Files.newBufferedReader(sql, StandardCharsets.UTF_8);
                    Stream<QueryRequest> requests = this.queryAnalysisService.analysisRequestsStream(dsConfig, reader, Collections.emptyList(), 1, 0, options)) {
                requests.forEachOrdered(request -> {
                    context.incrementSqlCount();
                    onProcessed.run();
                });
                return null;
            }
        });
    }

    @Override
    protected void fillResult(ApprovalAnalysisStateMO state, PreInitContext context) {
        state.setTotalCount(context.getSqlCounter());
    }
}

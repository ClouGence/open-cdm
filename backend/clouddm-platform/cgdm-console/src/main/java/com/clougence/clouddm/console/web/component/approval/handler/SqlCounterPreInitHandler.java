/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.handler;

import java.io.Reader;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.analysis.AnalysisQueryOptions;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisFeature;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
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
    protected void doHandle(PreInitContext context) {
        DmApprovalDO approvalDO = context.getApproval();
        AtomicLong sqlCounter = new AtomicLong();
        AnalysisQueryOptions options = AnalysisQueryOptions.builder()
            .currentUid(approvalDO.getOwnerUid())
            .dataSourceId(approvalDO.getBindDsId())
            .levels(context.getDsLevels().levelsParam())
            .skip(QueryAnalysisFeature.REWRITE, QueryAnalysisFeature.LINEAGE, QueryAnalysisFeature.MASKING)
            .build();

        this.approvalService.consumeSqlFile(approvalDO.getId(), sql -> {
            try (Reader reader = context.openReader(sql);
                    Stream<QueryRequest> requests = this.queryAnalysisService.analysisRequestsStream(context.getDsConfig(), reader, Collections.emptyList(), 1, 0, options)) {
                requests.forEachOrdered(request -> {
                    sqlCounter.incrementAndGet();
                    context.itemProcessed(request.getQueryBody());
                });
                return null;
            }
        });
        context.writeResult(state -> state.setTotalCount(sqlCounter.get()));
    }
}

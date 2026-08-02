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
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.analysis.QueryRuleAnalysisOptions;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.sdk.service.secrules.Requester;

import jakarta.annotation.Resource;

/**
 * Collects security rule violations during approval pre-initialization.
 */
@Service
public class RuleCheckPreInitHandler {

    @Resource
    private ApprovalService      approvalService;
    @Resource
    private QueryAnalysisService queryAnalysisService;

    public void handle(DataSourceConfig dsConfig, DsLevels dsLevels, PreInitContext context) {
        DmApprovalDO approvalDO = context.getApproval();
        QueryRuleAnalysisOptions ruleOptions = QueryRuleAnalysisOptions.builder()
            .ownerUid(approvalDO.getPrimaryUid())
            .currentUid(approvalDO.getOwnerUid())
            .dataSourceId(approvalDO.getBindDsId())
            .levels(dsLevels.levelsParam())
            .requester(Requester.TICKET)
            .unsupportedLevel(WarnLevel.FAILURE)
            .build();
        this.approvalService.consumeSqlFile(approvalDO.getId(), sql -> {
            try (Reader reader = Files.newBufferedReader(sql, StandardCharsets.UTF_8);
                    Stream<SecRulesCheckResult> results = this.queryAnalysisService.analysisRulesStream(dsConfig, reader, Collections.emptyList(), 1, 0, ruleOptions)) {
                results.forEachOrdered(context::addRuleCheckResult);
                return null;
            }
        });
    }
}

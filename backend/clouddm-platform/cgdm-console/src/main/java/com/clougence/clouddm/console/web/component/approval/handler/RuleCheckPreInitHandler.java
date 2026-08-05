/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.handler;

import java.io.Reader;
import java.util.Collections;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.analysis.AnalysisRuleOptions;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.sdk.service.secrules.Requester;

import jakarta.annotation.Resource;

/**
 * Collects security rule violations during approval pre-initialization.
 */
@Service
public class RuleCheckPreInitHandler extends AbstractPreInitHandler {

    @Resource
    private ApprovalService      approvalService;
    @Resource
    private QueryAnalysisService queryAnalysisService;

    @Override
    protected String analysisType() {
        return ApprovalAnalysisStateMO.TYPE_SECURITY_RULE;
    }

    @Override
    public int displayOrder() {
        return 2;
    }

    @Override
    protected void doHandle(PreInitContext context) {
        DmApprovalDO approvalDO = context.getApproval();
        SecRulesCheckResult ruleCheckResult = new SecRulesCheckResult();
        AnalysisRuleOptions options = AnalysisRuleOptions.builder()
            .currentUid(approvalDO.getOwnerUid())
            .dsId(approvalDO.getBindDsId())
            .levels(context.getDsLevels().levelsParam())
            .requester(Requester.TICKET)
            .unsupportedLevel(WarnLevel.FAILURE)
            .build();

        this.approvalService.consumeSqlFile(approvalDO.getId(), sql -> {
            try (Reader reader = context.openReader(sql);
                    Stream<SecRulesCheckResult> results = this.queryAnalysisService.analysisRulesStream(context.getDsConfig(), reader, Collections.emptyList(), 1, 0, options)) {
                results.forEachOrdered(result -> {
                    ruleCheckResult.merge(result);
                    context.itemProcessed();
                });
                return null;
            }
        });

        context.writeResult(state -> state.setCheckedInfo(DmConvertUtils.convertToTicketRuleCheckResults(ruleCheckResult)));
    }
}

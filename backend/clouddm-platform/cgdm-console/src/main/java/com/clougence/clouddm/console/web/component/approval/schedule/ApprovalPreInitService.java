/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.schedule;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.analysis.AnalysisQueryOptions;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisFeature;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.approval.PreInitHandler;
import com.clougence.clouddm.console.web.component.approval.handler.RuleCheckPreInitHandler;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;

/**
 * Runs all analysis required by the PRE_INIT approval stage.
 */
@Service
public class ApprovalPreInitService {

    @Resource
    private DataSourceDal              dataSourceDal;
    @Resource
    private DmDsConfigService          dmDsConfigService;
    @Resource
    private QueryAnalysisService       queryAnalysisService;
    @Resource
    private ApprovalService            approvalService;
    @Resource
    private RuleCheckPreInitHandler    ruleCheckPreInitHandler;
    private final List<PreInitHandler> preInitHandlers;

    public ApprovalPreInitService(List<PreInitHandler> preInitHandlers){
        this.preInitHandlers = List.copyOf(preInitHandlers);
    }

    public PreInitContext process(DmApprovalDO approvalDO) {
        return this.analyze(approvalDO);
    }

    private PreInitContext analyze(DmApprovalDO approvalDO) {
        DmDsDO dsDO = this.dataSourceDal.dsMapper().selectById(approvalDO.getBindDsId());
        if (dsDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        List<String> levels = new ArrayList<>();
        levels.add(String.valueOf(dsDO.getDsEnvId()));
        levels.add(String.valueOf(dsDO.getId()));
        if (CollectionUtils.isNotEmpty(approvalDO.getLevels())) {
            levels.addAll(approvalDO.getLevels());
        } else if (StringUtils.isNotBlank(approvalDO.getTargetInfo())) {
            levels.addAll(Arrays.stream(approvalDO.getTargetInfo().split("/")).filter(StringUtils::isNotBlank).toList());
        }
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(levels);
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromExists(dsDO.getId());
        PreInitContext context = new PreInitContext(approvalDO);
        this.ruleCheckPreInitHandler.handle(dsConfig, dsLevels, context);

        this.dispatchPreInitHandlers(dsConfig, dsLevels, context);
        return context;
    }

    private void dispatchPreInitHandlers(DataSourceConfig dsConfig, DsLevels dsLevels, PreInitContext context) {
        DmApprovalDO approvalDO = context.getApproval();
        AnalysisQueryOptions options = AnalysisQueryOptions.builder()
            .currentUid(approvalDO.getOwnerUid())
            .dataSourceId(approvalDO.getBindDsId())
            .levels(dsLevels.levelsParam())
            .skip(QueryAnalysisFeature.REWRITE, QueryAnalysisFeature.LINEAGE, QueryAnalysisFeature.MASKING)
            .build();

        List<PreInitHandler> handlers = this.preInitHandlers.stream().filter(handler -> handler.supports(context)).toList();
        this.approvalService.consumeSqlFile(approvalDO.getId(), sql -> {
            try (Reader reader = Files.newBufferedReader(sql, StandardCharsets.UTF_8);
                    Stream<QueryRequest> requests = this.queryAnalysisService.analysisRequestsStream(dsConfig, reader, Collections.emptyList(), 1, 0, options)) {
                requests.forEachOrdered(r -> handlers.forEach(h -> h.handle(r, context)));
                return null;
            }
        });
    }
}

/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.schedule;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.approval.PreInitHandler;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalProcessActivityDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * Resolves shared context and invokes the independent PRE_INIT tasks.
 */
@Service
@Slf4j
public class ApprovalPreInitService {

    private static final int               HEARTBEAT_THREAD_COUNT     = 2;
    private static final long              HEARTBEAT_INTERVAL_SECONDS = 5;
    private final ScheduledExecutorService heartbeatExecutor          = Executors.newScheduledThreadPool(//
            HEARTBEAT_THREAD_COUNT, ThreadUtils.daemonThreadFactory(ApprovalPreInitService.class.getClassLoader(), "Ticket-analysis-heartbeat-%s"));
    @Resource
    private DataSourceDal                  dataSourceDal;
    @Resource
    private DmDsConfigService              dmDsConfigService;
    @Resource
    private ApprovalDal                    approvalDal;
    private final List<PreInitHandler>     preInitHandlers;

    public ApprovalPreInitService(List<PreInitHandler> preInitHandlers){
        this.preInitHandlers = List.copyOf(preInitHandlers);
    }

    @PreDestroy
    public void destroy() {
        this.heartbeatExecutor.shutdownNow();
    }

    public List<ApprovalAnalysisStateMO> initialStates(DmApprovalDO approvalDO) {
        return this.preInitHandlers.stream()//
            .filter(handler -> handler.supports(approvalDO))
            .sorted(Comparator.comparingInt(PreInitHandler::displayOrder))
            .map(handler -> new ApprovalAnalysisStateMO(handler.taskType(), handler.displayOrder()))
            .toList();
    }

    public void process(DmApprovalDO approvalDO, ApprovalTaskSubmitter taskSubmitter, LongConsumer callback) {
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
        Map<String, DmApprovalProcessActivityDO> tasks = this.approvalDal.activityMapper()
            .queryByTicketId(approvalDO.getId())
            .stream()//
            .filter(a -> a.getTaskStatus() != null)
            .collect(Collectors.toMap(DmApprovalProcessActivityDO::getActivityId, a -> a, (left, right) -> left));
        long now = System.currentTimeMillis();
        this.preInitHandlers.stream()//
            .filter(h -> PreInitContext.isClaimable(tasks.get(h.taskType()), now))
            .filter(handler -> handler.supports(approvalDO))
            .forEach(h -> taskSubmitter.submit(() -> this.executeChild(h, dsConfig, dsLevels, approvalDO, callback)));
    }

    private void executeChild(PreInitHandler handler, DataSourceConfig dsConfig, DsLevels dsLevels, DmApprovalDO approvalDO, LongConsumer callback) {
        ScheduledFuture<?> heartbeatTask = null;
        try {
            PreInitContext context = new PreInitContext(approvalDO, dsConfig, dsLevels, handler.taskType(), this.approvalDal);
            if (!context.claim()) {
                return;
            }
            heartbeatTask = this.startHeartbeat(context);
            handler.handle(context);
        } catch (Exception e) {
            log.error("PRE_INIT child task failed, ticketId={}, taskType={}", approvalDO.getId(), handler.taskType(), e);
        } finally {
            if (heartbeatTask != null) {
                heartbeatTask.cancel(false);
            }
            try {
                callback.accept(approvalDO.getId());
            } catch (RuntimeException e) {
                log.error("PRE_INIT parent callback failed, ticketId={}, taskType={}", approvalDO.getId(), handler.taskType(), e);
            }
        }
    }

    private ScheduledFuture<?> startHeartbeat(PreInitContext context) {
        return this.heartbeatExecutor.scheduleAtFixedRate(() -> {
            try {
                this.approvalDal.activityMapper().heartbeatPreInitTask(context.getProcessId(), context.getTaskType(), context.getRunId());
            } catch (RuntimeException e) {
                log.warn("heartbeat analysis task failed, ticketId={}, analysisType={}", context.getApproval().getId(), context.getTaskType(), e);
            }
        }, HEARTBEAT_INTERVAL_SECONDS, HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }
}

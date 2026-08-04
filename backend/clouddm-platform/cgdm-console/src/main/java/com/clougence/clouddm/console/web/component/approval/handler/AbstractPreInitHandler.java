/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.handler;
import java.util.concurrent.TimeUnit;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.approval.PreInitHandler;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBiz;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStage;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalProcessActivityDO;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalProcessDO;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * Owns the lifecycle and persistence of one pre-initialization analysis task.
 */
@Slf4j
public abstract class AbstractPreInitHandler implements PreInitHandler {

    private static final long PROGRESS_SAVE_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(1);

    @Resource
    protected ApprovalDal     approvalDal;

    @Override
    public boolean supports(PreInitContext context) {
        ApprovalBiz approBiz = context.getApproval().getApproBiz();
        return approBiz == ApprovalBiz.DM_QUERY || approBiz == ApprovalBiz.DM_CHANGE;
    }

    @Override
    public final void handle(DataSourceConfig dsConfig, DsLevels dsLevels, PreInitContext context) {
        AnalysisProgress progress = new AnalysisProgress(context.getApproval().getId(), this.analysisType());
        progress.start();
        try {
            this.doHandle(dsConfig, dsLevels, context, progress::increment);
            progress.finish(context);
        } catch (RuntimeException e) {
            progress.fail(e);
            throw e;
        }
    }

    protected abstract String analysisType();

    protected abstract void doHandle(DataSourceConfig dsConfig, DsLevels dsLevels, PreInitContext context, Runnable onProcessed);

    protected abstract void fillResult(ApprovalAnalysisStateMO state, PreInitContext context);

    private DmApprovalProcessDO queryExplainProcess(long ticketId) {
        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXPLAIN);
        if (processDO == null) {
            throw new IllegalStateException("EXPLAIN process not found, ticketId=" + ticketId);
        }
        return processDO;
    }

    private void updateState(long ticketId, String type, java.util.function.Consumer<ApprovalAnalysisStateMO> updater) {
        DmApprovalProcessDO processDO = this.queryExplainProcess(ticketId);
        DmApprovalProcessActivityDO activityDO = this.approvalDal.activityMapper().queryByProcessIdAndActivityId(processDO.getId(), type);
        if (activityDO == null) {
            throw new IllegalStateException("Analysis activity not found, ticketId=" + ticketId + ", analysisType=" + type);
        }
        ApprovalAnalysisStateMO state = StringUtils.isBlank(activityDO.getContext()) ? new ApprovalAnalysisStateMO(type) : JsonUtils
            .toObj(activityDO.getContext(), ApprovalAnalysisStateMO.class);
        updater.accept(state);
        this.approvalDal.activityMapper().updateContext(processDO.getId(), type, JsonUtils.toJson(state));
    }

    private class AnalysisProgress {
        private final long   ticketId;
        private final String type;
        private final long   startedAt   = System.currentTimeMillis();
        private long         processedCount;
        private long         lastSavedAt = System.nanoTime();

        AnalysisProgress(long ticketId, String type){
            this.ticketId = ticketId;
            this.type = type;
        }

        void start() {
            updateState(this.ticketId, this.type, state -> {
                state.setAnalysisStatus(ApprovalAnalysisStateMO.STATUS_RUNNING);
                state.setStartTimeUtc(this.startedAt);
                state.setFinishTimeUtc(null);
                state.setProcessedCount(0L);
                state.setErrorMessage(null);
            });
            log.info("[TicketAnalysis] ticketId={}, analysisType={}, status=STARTED", this.ticketId, this.type);
        }

        void increment() {
            this.processedCount++;
            long now = System.nanoTime();
            if (now - this.lastSavedAt < PROGRESS_SAVE_INTERVAL_NANOS) {
                return;
            }
            this.lastSavedAt = now;
            updateState(this.ticketId, this.type, state -> state.setProcessedCount(this.processedCount));
            log.info("[TicketAnalysis] ticketId={}, analysisType={}, status=RUNNING, processedCount={}, elapsedMs={}", this.ticketId, this.type, this.processedCount, System
                .currentTimeMillis() - this.startedAt);
        }

        void finish(PreInitContext context) {
            updateState(this.ticketId, this.type, state -> {
                state.setAnalysisStatus(ApprovalAnalysisStateMO.STATUS_FINISHED);
                state.setFinishTimeUtc(System.currentTimeMillis());
                state.setProcessedCount(this.processedCount);
                state.setErrorMessage(null);
                fillResult(state, context);
            });
            log.info("[TicketAnalysis] ticketId={}, analysisType={}, status=FINISHED, processedCount={}, elapsedMs={}", this.ticketId, this.type, this.processedCount, System
                .currentTimeMillis() - this.startedAt);
        }

        void fail(RuntimeException error) {
            updateState(this.ticketId, this.type, state -> {
                state.setAnalysisStatus(ApprovalAnalysisStateMO.STATUS_FAILED);
                state.setFinishTimeUtc(System.currentTimeMillis());
                state.setProcessedCount(this.processedCount);
                state.setErrorMessage(error.getMessage());
            });
            log.error("[TicketAnalysis] ticketId={}, analysisType={}, status=FAILED, processedCount={}, elapsedMs={}", this.ticketId, this.type, this.processedCount, System
                .currentTimeMillis() - this.startedAt, error);
        }
    }
}

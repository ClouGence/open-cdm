/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.model;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStage;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalProcessActivityDO;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalProcessDO;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Runtime context for one PRE_INIT handler execution.
 */
@Getter
@Slf4j
public class PreInitContext {

    public static final long                  TASK_LEASE_TIMEOUT_MILLIS    = TimeUnit.SECONDS.toMillis(30);
    private static final long                 PROGRESS_SAVE_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(1);
    private final DmApprovalDO                approval;
    private final DataSourceConfig            dsConfig;
    private final DsLevels                    dsLevels;
    private final String                      taskType;
    private final ApprovalDal                 approvalDal;
    private final String                      runId                        = UUID.randomUUID().toString();
    private final long                        startedAt                    = System.currentTimeMillis();
    private long                              processedCount;
    private long                              processedBytes;
    private long                              totalBytes;
    private long                              lastSavedAt;
    private Consumer<ApprovalAnalysisStateMO> resultWriter                 = state -> {
                                                                           };
    private Long                              processId;

    public PreInitContext(DmApprovalDO approval, DataSourceConfig dsConfig, DsLevels dsLevels, String taskType, ApprovalDal approvalDal){
        this.approval = Objects.requireNonNull(approval, "approval");
        this.dsConfig = Objects.requireNonNull(dsConfig, "dsConfig");
        this.dsLevels = Objects.requireNonNull(dsLevels, "dsLevels");
        this.taskType = Objects.requireNonNull(taskType, "taskType");
        this.approvalDal = Objects.requireNonNull(approvalDal, "approvalDal");
    }

    public boolean claim() {
        DmApprovalProcessDO processDO = this.queryExplainProcess();
        ApprovalAnalysisStateMO state = new ApprovalAnalysisStateMO(this.taskType);
        state.setRunId(this.runId);
        Date expiredBefore = new Date(System.currentTimeMillis() - TASK_LEASE_TIMEOUT_MILLIS);
        boolean claimed = this.approvalDal.activityMapper().claimPreInitTask(processDO.getId(), this.taskType, expiredBefore, JsonUtils.toJson(state)) == 1;
        if (claimed) {
            this.processId = processDO.getId();
        }
        return claimed;
    }

    public static boolean isClaimable(DmApprovalProcessActivityDO activity, long now) {
        if (activity == null) {
            return false;
        }
        if (ApprovalAnalysisStateMO.STATUS_INIT.equals(activity.getTaskStatus())) {
            return true;
        }
        Date modified = activity.getGmtModified();
        return ApprovalAnalysisStateMO.STATUS_RUNNING.equals(activity.getTaskStatus()) && (modified == null || modified.getTime() < now - TASK_LEASE_TIMEOUT_MILLIS);
    }

    public void start() {
        this.updateState(state -> {
            state.setAnalysisStatus(ApprovalAnalysisStateMO.STATUS_RUNNING);
            state.setStartTimeUtc(this.startedAt);
            state.setFinishTimeUtc(null);
            state.setProcessedCount(0L);
            state.setProcessedBytes(0L);
            state.setTotalBytes(null);
            state.setTotalCount(null);
            state.setAnalysisPhase(null);
            state.setErrorMessage(null);
        });
        log.info("[TicketAnalysis] ticketId={}, analysisType={}, status=STARTED", this.approval.getId(), this.taskType);
    }

    public Reader openReader(Path path) throws IOException {
        this.totalBytes = Files.size(path);
        this.updateState(state -> {
            state.setProcessedBytes(0L);
            state.setTotalBytes(this.totalBytes);
        });
        return Files.newBufferedReader(path, StandardCharsets.UTF_8);
    }

    public synchronized void itemProcessed(String processedContent) {
        this.processedCount++;
        if (processedContent != null) {
            this.processedBytes = Math.min(this.totalBytes, this.processedBytes + processedContent.getBytes(StandardCharsets.UTF_8).length);
        }
        this.saveProgressIfDue();
    }

    public synchronized void itemProcessed() {
        this.processedCount++;
        this.saveProgressIfDue();
    }

    public void writeResult(Consumer<ApprovalAnalysisStateMO> writer) {
        this.resultWriter = Objects.requireNonNull(writer, "writer");
    }

    public synchronized void startPhase(String phase, Long totalCount) {
        this.processedCount = 0L;
        this.processedBytes = 0L;
        this.totalBytes = 0L;
        this.lastSavedAt = 0L;
        this.updateState(state -> {
            state.setProcessedCount(0L);
            state.setProcessedBytes(0L);
            state.setTotalBytes(0L);
            state.setTotalCount(totalCount);
            state.setAnalysisPhase(phase);
            this.resultWriter.accept(state);
        });
    }

    public void finish() {
        this.processedBytes = this.totalBytes;
        this.completeState(ApprovalAnalysisStateMO.STATUS_FINISHED, state -> {
            state.setAnalysisStatus(ApprovalAnalysisStateMO.STATUS_FINISHED);
            state.setFinishTimeUtc(System.currentTimeMillis());
            state.setProcessedCount(this.processedCount);
            state.setProcessedBytes(this.processedBytes);
            state.setTotalBytes(this.totalBytes);
            state.setAnalysisPhase(null);
            state.setErrorMessage(null);
            this.resultWriter.accept(state);
        });
        log.info("[TicketAnalysis] ticketId={}, analysisType={}, status=FINISHED, processedCount={}, elapsedMs={}", this.approval
            .getId(), this.taskType, this.processedCount, System.currentTimeMillis() - this.startedAt);
    }

    public void fail(Exception error) {
        this.completeState(ApprovalAnalysisStateMO.STATUS_FAILED, state -> {
            state.setAnalysisStatus(ApprovalAnalysisStateMO.STATUS_FAILED);
            state.setFinishTimeUtc(System.currentTimeMillis());
            state.setProcessedCount(this.processedCount);
            state.setProcessedBytes(this.processedBytes);
            state.setTotalBytes(this.totalBytes);
            state.setAnalysisPhase(null);
            state.setErrorMessage(error.getMessage());
            this.resultWriter.accept(state);
        });
        log.error("[TicketAnalysis] ticketId={}, analysisType={}, status=FAILED, processedCount={}, elapsedMs={}", this.approval
            .getId(), this.taskType, this.processedCount, System.currentTimeMillis() - this.startedAt, error);
    }

    private void saveProgressIfDue() {
        long now = System.nanoTime();
        if (this.lastSavedAt != 0L && now - this.lastSavedAt < PROGRESS_SAVE_INTERVAL_NANOS) {
            return;
        }
        this.lastSavedAt = now;
        this.updateState(state -> {
            state.setProcessedCount(this.processedCount);
            state.setProcessedBytes(this.processedBytes);
            state.setTotalBytes(this.totalBytes);
            this.resultWriter.accept(state);
        });
    }

    private DmApprovalProcessDO queryExplainProcess() {
        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(this.approval.getId(), ApprovalStage.EXPLAIN);
        if (processDO == null) {
            throw new IllegalStateException("EXPLAIN process not found, ticketId=" + this.approval.getId());
        }
        return processDO;
    }

    private void updateState(Consumer<ApprovalAnalysisStateMO> updater) {
        DmApprovalProcessDO processDO = this.queryExplainProcess();
        DmApprovalProcessActivityDO activityDO = this.requireActivity(processDO);
        ApprovalAnalysisStateMO state = this.readState(activityDO);
        updater.accept(state);
        if (this.approvalDal.activityMapper().updatePreInitContext(processDO.getId(), this.taskType, this.runId, JsonUtils.toJson(state)) != 1) {
            throw new IllegalStateException("Analysis task lease lost, ticketId=" + this.approval.getId() + ", analysisType=" + this.taskType);
        }
    }

    private void completeState(String taskStatus, Consumer<ApprovalAnalysisStateMO> updater) {
        DmApprovalProcessDO processDO = this.queryExplainProcess();
        DmApprovalProcessActivityDO activityDO = this.requireActivity(processDO);
        ApprovalAnalysisStateMO state = this.readState(activityDO);
        updater.accept(state);
        if (this.approvalDal.activityMapper().completePreInitTask(processDO.getId(), this.taskType, taskStatus, this.runId, JsonUtils.toJson(state)) != 1) {
            throw new IllegalStateException("Analysis task is not running, ticketId=" + this.approval.getId() + ", analysisType=" + this.taskType);
        }
    }

    private DmApprovalProcessActivityDO requireActivity(DmApprovalProcessDO processDO) {
        DmApprovalProcessActivityDO activityDO = this.approvalDal.activityMapper().queryByProcessIdAndActivityId(processDO.getId(), this.taskType);
        if (activityDO == null) {
            throw new IllegalStateException("Analysis activity not found, ticketId=" + this.approval.getId() + ", analysisType=" + this.taskType);
        }
        return activityDO;
    }

    private ApprovalAnalysisStateMO readState(DmApprovalProcessActivityDO activityDO) {
        return StringUtils.isBlank(activityDO.getContext()) ? new ApprovalAnalysisStateMO(this.taskType) : JsonUtils.toObj(activityDO.getContext(), ApprovalAnalysisStateMO.class);
    }
}

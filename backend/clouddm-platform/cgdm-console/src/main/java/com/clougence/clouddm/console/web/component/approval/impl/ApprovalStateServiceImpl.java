/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.component.approval.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalExecutionStateMO;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;

@Service
public class ApprovalStateServiceImpl implements ApprovalStateService {
    private static final List<String>             TYPES = List.of(//
            ApprovalExecutionStateMO.TYPE_PREPARATION,            //
            ApprovalExecutionStateMO.TYPE_DISPATCH,               //
            ApprovalExecutionStateMO.TYPE_RUNNING);

    @Resource
    private ApprovalDal                           approvalDal;
    @Resource
    private ImSenderService                       imSenderService;
    private final ObjectProvider<ApprovalHandler> approvalHandlers;

    public ApprovalStateServiceImpl(ObjectProvider<ApprovalHandler> approvalHandlers){
        this.approvalHandlers = approvalHandlers;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateApprovalStatus(long ticketId, ApprovalStatus status, String message) {
        this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, status, message);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void finalizeApproval(long ticketId, ApprovalStatus status, String message) {
        if (!ApprovalStatus.isEndStatus(status)) {
            throw new IllegalArgumentException("Approval status is not terminal: " + status);
        }
        DmApprovalDO approval = this.approvalDal.approvalMapper().queryById(ticketId);
        if (approval == null) {
            throw new IllegalStateException("Approval does not exist, ticketId: " + ticketId);
        }
        this.updateApprovalStatus(ticketId, status, message);
        ApprovalHandler handler = this.approvalHandlers.stream()//
            .filter(candidate -> candidate.handleType() == approval.getApproBiz())//
            .findFirst()//
            .orElseThrow(() -> new IllegalStateException("ApprovalHandler about " + approval.getApproBiz() + " does not exist"));
        switch (status) {
            case FINISHED -> handler.approvalCompleted(ticketId, approval.getApproBiz(), this.imSenderService);
            case REJECTED -> handler.approvalRejected(ticketId, approval.getApproBiz(), this.imSenderService);
            case FAILED -> handler.approvalFailed(ticketId, approval.getApproBiz(), this.imSenderService);
            case CLOSED, CANCELED -> handler.approvalCanceled(ticketId, approval.getApproBiz(), this.imSenderService);
            default -> throw new IllegalStateException("Unsupported terminal approval status: " + status);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DmApprovalProcessDO initializeProcess(long ticketId, ApprovalStage stage, ApprovalProcessStatus status, String context) {
        DmApprovalProcessDO process = new DmApprovalProcessDO();
        process.setTicketId(ticketId);
        process.setTicketStage(stage);
        process.setProcessStatus(status);
        process.setStageContext(context);
        this.approvalDal.processMapper().insert(process);
        return process;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateProcessStatus(long ticketId, ApprovalStage stage, ApprovalProcessStatus status, String context) {
        DmApprovalProcessDO process = this.requireProcess(ticketId, stage);
        Date finishTime = process.getFinishTime();
        if (status == ApprovalProcessStatus.INIT) {
            finishTime = null;
        } else if (status == ApprovalProcessStatus.REJECT || status == ApprovalProcessStatus.FINISH || status == ApprovalProcessStatus.FAIL
                   || status == ApprovalProcessStatus.CLOSED) {
            finishTime = new Date();
        }
        this.approvalDal.processMapper().updateProcessStatus(process.getId(), status, context == null ? process.getStageContext() : context, finishTime);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DmApprovalProcessActivityDO initializeActivity(long ticketId, ApprovalStage stage, String activityId, String activityTitle, int orderNumber, String status,
                                                          String context) {
        DmApprovalProcessDO process = this.requireProcess(ticketId, stage);
        DmApprovalProcessActivityDO activity = new DmApprovalProcessActivityDO();
        activity.setTicketId(ticketId);
        activity.setProcessId(process.getId());
        activity.setActivityId(activityId);
        activity.setActivityTitle(activityTitle);
        activity.setOrderNumber(orderNumber);
        activity.setTaskStatus(status);
        activity.setContext(context);
        this.approvalDal.activityMapper().insert(activity);
        return activity;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateActivityStatus(long ticketId, ApprovalStage stage, String activityId, String status, String context) {
        DmApprovalProcessDO process = this.requireProcess(ticketId, stage);
        DmApprovalProcessActivityDO activity = this.approvalDal.activityMapper().queryByProcessIdAndActivityId(process.getId(), activityId);
        if (activity == null) {
            throw new IllegalStateException("Approval activity does not exist, ticketId: " + ticketId + ", stage: " + stage + ", activityId: " + activityId);
        }
        activity.setTaskStatus(status);
        activity.setContext(context);
        this.approvalDal.activityMapper().updateById(activity);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void initializeAnalysisActivities(long ticketId, List<ApprovalAnalysisStateMO> states) {
        DmApprovalProcessDO process = this.requireProcess(ticketId, ApprovalStage.EXPLAIN);
        List<DmApprovalProcessActivityDO> existing = this.approvalDal.activityMapper().queryByTicketId(ticketId);
        if (existing.stream().anyMatch(activity -> process.getId().equals(activity.getProcessId()))) {
            return;
        }
        for (ApprovalAnalysisStateMO state : states) {
            this.initializeActivity(                     //
                    ticketId,                            //
                    ApprovalStage.EXPLAIN,               //
                    state.getAnalysisType(),             //
                    state.getAnalysisType(),             //
                    state.getDisplayOrder(),             //
                    ApprovalAnalysisStateMO.STATUS_INIT, //
                    JsonUtils.toJson(state));
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void initializeExecutionProgress(long ticketId) {
        DmApprovalProcessDO process = this.ensureActivities(ticketId);
        long totalCount = this.resolveStatementCount(ticketId);
        this.update(process.getId(), ApprovalExecutionStateMO.TYPE_PREPARATION, state -> {
            state.setExecutionStatus(ApprovalExecutionStateMO.STATUS_RUNNING);
            state.setStartTimeUtc(System.currentTimeMillis());
            state.setFinishTimeUtc(null);
            state.setProcessedCount(0L);
            state.setTotalCount(totalCount);
            state.setErrorMessage(null);
        });
        this.reset(process.getId(), ApprovalExecutionStateMO.TYPE_DISPATCH);
        this.reset(process.getId(), ApprovalExecutionStateMO.TYPE_RUNNING);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void resetExecutionProgress(long ticketId) {
        DmApprovalProcessDO process = this.ensureActivities(ticketId);
        for (String type : TYPES) {
            this.reset(process.getId(), type);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void reportExecutionPreparationProgress(String approvalBizId, long processedCount, long totalCount) {
        DmApprovalProcessDO process = this.executionProcess(approvalBizId);
        if (process == null) {
            return;
        }
        this.update(process.getId(), ApprovalExecutionStateMO.TYPE_PREPARATION, state -> {
            state.setExecutionStatus(ApprovalExecutionStateMO.STATUS_RUNNING);
            state.setFinishTimeUtc(null);
            state.setErrorMessage(null);
            if (state.getStartTimeUtc() == null) {
                state.setStartTimeUtc(System.currentTimeMillis());
            }
            state.setProcessedCount(processedCount);
            state.setTotalCount(totalCount);
        });
        if (processedCount == 0) {
            this.reset(process.getId(), ApprovalExecutionStateMO.TYPE_DISPATCH);
            this.reset(process.getId(), ApprovalExecutionStateMO.TYPE_RUNNING);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void markExecutionDispatched(String approvalBizId) {
        DmApprovalProcessDO process = this.executionProcess(approvalBizId);
        if (process == null) {
            return;
        }
        this.finish(process.getId(), ApprovalExecutionStateMO.TYPE_PREPARATION);
        this.update(process.getId(), ApprovalExecutionStateMO.TYPE_DISPATCH, state -> {
            state.setExecutionStatus(ApprovalExecutionStateMO.STATUS_RUNNING);
            if (state.getStartTimeUtc() == null) {
                state.setStartTimeUtc(System.currentTimeMillis());
            }
            state.setErrorMessage(null);
        });
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void markExecutionRunning(String approvalBizId) {
        DmApprovalDO approval = this.approval(approvalBizId);
        if (approval == null) {
            return;
        }
        DmApprovalProcessDO process = this.ensureActivities(approval.getId());
        DmApprovalProcessActivityDO runningActivity = this.approvalDal.activityMapper().queryByProcessIdAndActivityId(process.getId(), ApprovalExecutionStateMO.TYPE_RUNNING);
        if (runningActivity != null && StringUtils.isNotBlank(runningActivity.getContext())) {
            ApprovalExecutionStateMO current = JsonUtils.toObj(runningActivity.getContext(), ApprovalExecutionStateMO.class);
            if (ApprovalExecutionStateMO.STATUS_RUNNING.equals(current.getExecutionStatus()) || ApprovalExecutionStateMO.STATUS_FINISHED.equals(current.getExecutionStatus())) {
                return;
            }
        }
        this.finish(process.getId(), ApprovalExecutionStateMO.TYPE_DISPATCH);
        this.update(process.getId(), ApprovalExecutionStateMO.TYPE_RUNNING, state -> {
            state.setExecutionStatus(ApprovalExecutionStateMO.STATUS_RUNNING);
            if (state.getStartTimeUtc() == null) {
                state.setStartTimeUtc(System.currentTimeMillis());
            }
            state.setErrorMessage(null);
        });
        this.updateApprovalStatus(approval.getId(), ApprovalStatus.RUNNING, null);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void completeExecution(String approvalBizId) {
        DmApprovalDO approval = this.approval(approvalBizId);
        if (approval == null) {
            return;
        }
        DmApprovalProcessDO process = this.ensureActivities(approval.getId());
        this.finish(process.getId(), ApprovalExecutionStateMO.TYPE_RUNNING);
        this.updateProcessStatus(approval.getId(), ApprovalStage.EXECUTION, ApprovalProcessStatus.FINISH, null);
        this.finalizeApproval(approval.getId(), ApprovalStatus.FINISHED, null);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void failExecution(String approvalBizId, String errorMessage) {
        DmApprovalDO approval = this.approval(approvalBizId);
        if (approval == null) {
            return;
        }
        DmApprovalProcessDO process = this.ensureActivities(approval.getId());
        for (String type : TYPES) {
            DmApprovalProcessActivityDO activity = this.approvalDal.activityMapper().queryByProcessIdAndActivityId(process.getId(), type);
            if (activity == null || StringUtils.isBlank(activity.getContext())) {
                continue;
            }
            ApprovalExecutionStateMO state = JsonUtils.toObj(activity.getContext(), ApprovalExecutionStateMO.class);
            if (ApprovalExecutionStateMO.STATUS_RUNNING.equals(state.getExecutionStatus())) {
                this.update(process.getId(), type, current -> {
                    current.setExecutionStatus(ApprovalExecutionStateMO.STATUS_FAILED);
                    current.setFinishTimeUtc(System.currentTimeMillis());
                    current.setErrorMessage(errorMessage);
                });
                break;
            }
        }
        this.updateProcessStatus(approval.getId(), ApprovalStage.EXECUTION, ApprovalProcessStatus.FAIL, null);
        this.updateApprovalStatus(approval.getId(), ApprovalStatus.EXEC_FAIL, errorMessage);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void cancelExecution(String approvalBizId) {
        DmApprovalProcessDO process = this.executionProcess(approvalBizId);
        if (process == null) {
            return;
        }
        for (String type : TYPES) {
            DmApprovalProcessActivityDO activity = this.approvalDal.activityMapper().queryByProcessIdAndActivityId(process.getId(), type);
            if (activity == null || StringUtils.isBlank(activity.getContext())) {
                continue;
            }
            ApprovalExecutionStateMO state = JsonUtils.toObj(activity.getContext(), ApprovalExecutionStateMO.class);
            if (ApprovalExecutionStateMO.STATUS_RUNNING.equals(state.getExecutionStatus())) {
                this.update(process.getId(), type, current -> {
                    current.setExecutionStatus(ApprovalExecutionStateMO.STATUS_CANCELED);
                    current.setFinishTimeUtc(System.currentTimeMillis());
                });
                return;
            }
        }
    }

    private DmApprovalProcessDO ensureActivities(long ticketId) {
        DmApprovalProcessDO process = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXECUTION);
        if (process == null) {
            throw new IllegalStateException("Approval execution process does not exist, ticketId: " + ticketId);
        }
        for (int i = 0; i < TYPES.size(); i++) {
            String type = TYPES.get(i);
            if (this.approvalDal.activityMapper().queryByProcessIdAndActivityId(process.getId(), type) != null) {
                continue;
            }
            ApprovalExecutionStateMO state = new ApprovalExecutionStateMO(type, i + 1);
            this.initializeActivity(ticketId, ApprovalStage.EXECUTION, type, type, i + 1, ApprovalExecutionStateMO.STATUS_INIT, JsonUtils.toJson(state));
        }
        return process;
    }

    private long resolveStatementCount(long ticketId) {
        for (DmApprovalProcessActivityDO activity : this.approvalDal.activityMapper().queryByTicketId(ticketId)) {
            if (!ApprovalAnalysisStateMO.TYPE_BEHAVIOR_ANALYSIS.equals(activity.getActivityId()) || StringUtils.isBlank(activity.getContext())) {
                continue;
            }
            Long totalCount = JsonUtils.toObj(activity.getContext(), ApprovalAnalysisStateMO.class).getTotalCount();
            return totalCount == null ? 0L : totalCount;
        }
        return 0L;
    }

    private DmApprovalProcessDO executionProcess(String approvalBizId) {
        DmApprovalDO approval = this.approval(approvalBizId);
        return approval == null ? null : this.ensureActivities(approval.getId());
    }

    private DmApprovalDO approval(String approvalBizId) {
        return this.approvalDal.approvalMapper().queryByBizId(approvalBizId);
    }

    private DmApprovalProcessDO requireProcess(long ticketId, ApprovalStage stage) {
        DmApprovalProcessDO process = this.approvalDal.processMapper().queryByStage(ticketId, stage);
        if (process == null) {
            throw new IllegalStateException(stage + " process does not exist, ticketId: " + ticketId);
        }
        return process;
    }

    private void finish(long processId, String type) {
        this.update(processId, type, state -> {
            state.setExecutionStatus(ApprovalExecutionStateMO.STATUS_FINISHED);
            state.setFinishTimeUtc(System.currentTimeMillis());
            if (ApprovalExecutionStateMO.TYPE_PREPARATION.equals(type)) {
                state.setProcessedCount(state.getTotalCount());
            }
        });
    }

    private void reset(long processId, String type) {
        this.update(processId, type, state -> {
            state.setExecutionStatus(ApprovalExecutionStateMO.STATUS_INIT);
            state.setStartTimeUtc(null);
            state.setFinishTimeUtc(null);
            state.setProcessedCount(null);
            state.setTotalCount(null);
            state.setErrorMessage(null);
        });
    }

    private void update(long processId, String type, java.util.function.Consumer<ApprovalExecutionStateMO> updater) {
        DmApprovalProcessActivityDO activity = this.approvalDal.activityMapper().queryByProcessIdAndActivityId(processId, type);
        if (activity == null) {
            return;
        }
        ApprovalExecutionStateMO state = StringUtils.isBlank(activity.getContext()) ? new ApprovalExecutionStateMO(type, activity.getOrderNumber()) : JsonUtils
            .toObj(activity.getContext(), ApprovalExecutionStateMO.class);
        updater.accept(state);
        activity.setContext(JsonUtils.toJson(state));
        activity.setTaskStatus(state.getExecutionStatus());
        this.approvalDal.activityMapper().updateById(activity);
    }
}

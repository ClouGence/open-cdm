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

import static com.clougence.clouddm.console.web.component.approval.CustomApprovalConstants.*;
import static com.clougence.clouddm.console.web.component.approval.CustomApprovalService.parseStageContext;

import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.approval.CustomApprovalConfigService;
import com.clougence.clouddm.console.web.component.approval.CustomApprovalService;
import com.clougence.clouddm.console.web.component.approval.model.*;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.constants.RdpTicketProcessActivityStatus;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpApprovalFO;
import com.clougence.clouddm.console.web.model.vo.ticket.RdpTicketActivityVO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.format.DateFormatType;

import jakarta.annotation.Resource;

@Service
public class CustomApprovalServiceImpl implements CustomApprovalService {

    @Resource
    private ApprovalDal                 approvalDal;
    @Resource
    private AuthDal                     authDal;
    @Resource
    private CustomApprovalConfigService configService;
    @Resource
    private ApprovalStateService        stateService;
    @Resource
    private ImSenderService             imSenderService;
    @Resource
    private List<ApprovalHandler>       handlers;

    @Override
    public boolean isCustomApproval(DmApprovalDO ticket) {
        DmApprovalProcessDO process = approvalDal.processMapper().queryByStage(ticket.getId(), ApprovalStage.APPROVAL);
        ApprovalStageMO summary = parseStageContext(process);
        CustomApprovalContext custom = summary.getCustomApproval();
        boolean reservedIdentity = ticket.getApproTemplateIdentity() != null && ticket.getApproTemplateIdentity().startsWith(TEMPLATE_ID_PREFIX);
        if (custom == null) {
            if (reservedIdentity) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_INSTANCE_INVALID.name()));
            }
            return false;
        }

        if (ticket.getApproType() != ApprovalType.Internal || !reservedIdentity || !APPROVAL_MODE_CUSTOM.equals(custom.getMode())
            || !Integer.valueOf(APPROVAL_SNAPSHOT_VERSION).equals(custom.getSchemaVersion()) || !AUTHORIZATION_ENGINE.equals(custom.getExecutionAuthorization())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_INSTANCE_INVALID.name()));
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void initializeApproval(DmApprovalDO ticket) {
        DmApprovalTemplateDO template = configService.lockAndValidateTemplate(ticket.getPrimaryUid(), ticket.getApproTemplateIdentity());
        CustomApprovalFlowConfig flowConfig = configService.parseFlowConfig(template);
        ApprovalTeamConfig teams = configService.getTeamConfig(ticket.getPrimaryUid());
        CustomApprovalContext context = new CustomApprovalContext();
        context.setSchemaVersion(APPROVAL_SNAPSHOT_VERSION);
        context.setMode(APPROVAL_MODE_CUSTOM);
        context.setExecutionAuthorization(AUTHORIZATION_ENGINE);
        context.setEngineConfigId(template.getEngineConfigId());
        context.setEngineName(template.getTemplateName());
        context.setFlowId(template.getId());
        context.setFlowVersion(template.getVersion());
        context.setTemplateIdentity(template.getTemplateIdentity());
        ApprovalStageMO approval = new ApprovalStageMO();
        approval.setCustomApproval(context);
        DmApprovalProcessDO process = approvalDal.processMapper().queryByStage(ticket.getId(), ApprovalStage.APPROVAL);
        int order = 0;
        for (CustomApprovalNodeConfig node : flowConfig.getNodes()) {
            order++;
            List<DmAuthUserDO> members = configService
                .resolveParticipants(ticket.getPrimaryUid(), node.getApproverType(), node.getTeamId(), node.getUserUid(), teams, SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE);
            ApprovalNodeContext nodeContext = new ApprovalNodeContext();
            nodeContext.setEngineConfigId(template.getEngineConfigId());
            nodeContext.setEngineName(template.getTemplateName());
            nodeContext.setFlowId(template.getId());
            nodeContext.setFlowVersion(template.getVersion());
            nodeContext.setTemplateIdentity(template.getTemplateIdentity());
            nodeContext.setContextType(NODE_CONTEXT_TYPE);
            nodeContext.setSchemaVersion(NODE_SNAPSHOT_VERSION);
            nodeContext.setNodeId(node.getNodeId());
            nodeContext.setNodeIndex(order);
            nodeContext.setApproverType(node.getApproverType());
            nodeContext.setTeamId(node.getTeamId());
            nodeContext.setTeamName(findTeamName(teams, node.getTeamId()));
            int required = 1;
            if (node.getApproverType() == ApprovalParticipantType.TEAM) {
                required = node.getRequiredCount();
            }

            nodeContext.setEffectiveRequiredCount(required);
            DmApprovalProcessActivityDO activity = new DmApprovalProcessActivityDO();
            activity.setTicketId(ticket.getId());
            activity.setProcessId(process.getId());
            activity.setActivityId(TEMPLATE_ID_PREFIX + node.getNodeId());
            activity.setActivityTitle(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_STAGE.name(), order));
            activity.setOrderNumber(order);
            activity.setTaskStatus(RdpTicketProcessActivityStatus.NEW.name());
            activity.setContext(CustomApprovalConfigService.serializeContext(nodeContext));
            approvalDal.activityMapper().insert(activity);
            for (DmAuthUserDO member : members) {
                DmApprovalNodeTaskDO task = new DmApprovalNodeTaskDO();
                task.setPrimaryUid(ticket.getPrimaryUid());
                task.setProcessActivityId(activity.getId());
                task.setApproverUid(member.getUid());
                task.setApproverName(member.getUsername());
                task.setStatus(ApprovalNodeTaskStatus.WAITING);
                approvalDal.nodeTaskMapper().insert(task);
            }
        }

        approvalDal.processMapper().updateContextById(process.getId(), CustomApprovalConfigService.serializeContext(approval));

        ApprovalExecutionContext execution = new ApprovalExecutionContext();
        execution.setSchemaVersion(EXECUTION_SNAPSHOT_VERSION);
        execution.setAuthorizationMode(AUTHORIZATION_ENGINE);
        execution.setEngineConfigId(template.getEngineConfigId());
        execution.setFlowId(template.getId());
        execution.setFlowVersion(template.getVersion());
        execution.setExecutionMode(flowConfig.getExecutionMode());
        ApprovalStageMO execSummary = new ApprovalStageMO();
        if (flowConfig.getExecutionMode() == ApprovalExecutionMode.AUTO) {
            // System jobs retain their tenant identity without impersonating a human confirmer.
            execution.setParticipantType(ApprovalParticipantType.SYSTEM);
            ApprovalExecutor systemExecutor = new ApprovalExecutor();
            systemExecutor.setUserUid(ticket.getPrimaryUid());
            systemExecutor.setUserName(SYSTEM_EXECUTOR_NAME);
            execution.setParticipants(List.of(systemExecutor));
            execSummary.setExecUserName(List.of(SYSTEM_EXECUTOR_NAME));
        } else {
            ApprovalExecutorConfig executorConfig = flowConfig.getExecutionAssignment();
            List<DmAuthUserDO> executors = configService.resolveParticipants(ticket.getPrimaryUid(), executorConfig.getParticipantType(), executorConfig.getTeamId(), executorConfig
                .getUserUid(), teams, SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE);
            execution.setParticipantType(executorConfig.getParticipantType());
            execution.setTeamId(executorConfig.getTeamId());
            execution.setTeamName(findTeamName(teams, executorConfig.getTeamId()));
            List<ApprovalExecutor> participants = new ArrayList<>();
            List<String> executorNames = new ArrayList<>();
            for (DmAuthUserDO executor : executors) {
                ApprovalExecutor participant = new ApprovalExecutor();
                participant.setUserUid(executor.getUid());
                participant.setUserName(executor.getUsername());
                participants.add(participant);
                executorNames.add(executor.getUsername());
            }

            execution.setParticipants(participants);
            execSummary.setExecUserName(executorNames);
        }

        execSummary.setCustomExecution(execution);
        DmApprovalProcessDO execProcess = approvalDal.processMapper().queryByStage(ticket.getId(), ApprovalStage.EXECUTION);
        approvalDal.processMapper().updateContextById(execProcess.getId(), CustomApprovalConfigService.serializeContext(execSummary));
        DmApprovalProcessDO confirm = approvalDal.processMapper().queryByStage(ticket.getId(), ApprovalStage.CONFIRM);
        if (confirm != null) {
            ApprovalStageMO confirmSummary = new ApprovalStageMO();
            confirmSummary.setExecUserName(execSummary.getExecUserName());
            approvalDal.processMapper().updateContextById(confirm.getId(), CustomApprovalConfigService.serializeContext(confirmSummary));
        }

        if (ticket.getTicketStatus() == ApprovalStatus.WAIT_APPROVAL) {
            startApproval(ticket.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void startApproval(long ticketId) {
        DmApprovalDO ticket = approvalDal.approvalMapper().selectByIdForUpdate(ticketId);
        if (ticket == null || ticket.getTicketStatus() != ApprovalStatus.WAIT_APPROVAL || !isCustomApproval(ticket)) {
            return;
        }

        DmApprovalProcessDO process = approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.APPROVAL);
        ApprovalStageMO summary = parseStageContext(process);
        if (summary.getCustomApproval().getCurrentActivityId() != null) {
            return;
        }

        List<DmApprovalProcessActivityDO> activities = approvalDal.activityMapper().listCustomActivities(ticketId, process.getId());
        if (activities.isEmpty()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_INSTANCE_INVALID.name()));
        }

        for (DmApprovalProcessActivityDO activity : activities) {
            if (!RdpTicketProcessActivityStatus.NEW.name().equals(activity.getTaskStatus())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_INSTANCE_INVALID.name()));
            }
        }

        startApprovalNode(process, summary, activities.get(0));
    }

    private void startApprovalNode(DmApprovalProcessDO process, ApprovalStageMO summary, DmApprovalProcessActivityDO activity) {
        List<DmApprovalNodeTaskDO> tasks = approvalDal.nodeTaskMapper().listByActivityId(activity.getId());
        if (tasks.isEmpty()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_INSTANCE_INVALID.name()));
        }

        List<String> approverNames = new ArrayList<>();
        for (DmApprovalNodeTaskDO task : tasks) {
            if (task.getStatus() != ApprovalNodeTaskStatus.WAITING) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_INSTANCE_INVALID.name()));
            }

            approverNames.add(task.getApproverName());
        }

        Date now = new Date();
        for (DmApprovalNodeTaskDO task : tasks) {
            task.setStatus(ApprovalNodeTaskStatus.PENDING);
            task.setStartTime(now);
            task.setGmtModified(new Date());
            approvalDal.nodeTaskMapper().updateById(task);
        }

        activity.setTaskStatus(RdpTicketProcessActivityStatus.RUNNING.name());
        activity.setGmtModified(new Date());
        approvalDal.activityMapper().updateById(activity);
        summary.getCustomApproval().setCurrentActivityId(activity.getId());
        summary.setExecUserName(approverNames);
        approvalDal.processMapper().updateContextById(process.getId(), CustomApprovalConfigService.serializeContext(summary));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void submitApproval(String puid, String uid, RdpApprovalFO request) {
        DmApprovalDO ticket = approvalDal.approvalMapper().selectByIdForUpdate(request.getTicketId());
        if (ticket == null || ticket.getDeleted() || !puid.equals(ticket.getPrimaryUid()) || !isCustomApproval(ticket)
            || !configService.isEligibleUser(puid, authDal.userMapper().queryByUid(uid), SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_NO_PERMISSION.name()));
        }

        DmApprovalProcessDO process = approvalDal.processMapper().queryByStage(ticket.getId(), ApprovalStage.APPROVAL);
        List<DmApprovalProcessActivityDO> activities = approvalDal.activityMapper().listCustomActivities(ticket.getId(), process.getId());
        DmApprovalProcessActivityDO activity = null;
        for (DmApprovalProcessActivityDO candidate : activities) {
            if (candidate.getId().equals(request.getProcessActivityId())) {
                activity = candidate;
                break;
            }
        }

        if (activity == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_ACTION_CONFLICT.name()));
        }

        List<DmApprovalNodeTaskDO> tasks = approvalDal.nodeTaskMapper().listByActivityIdForUpdate(activity.getId());
        DmApprovalNodeTaskDO task = null;
        for (DmApprovalNodeTaskDO candidate : tasks) {
            if (uid.equals(candidate.getApproverUid())) {
                task = candidate;
                break;
            }
        }

        if (task == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_NO_PERMISSION.name()));
        }

        ApprovalNodeTaskStatus action = ApprovalNodeTaskStatus.APPROVED;
        if (request.isRejected()) {
            action = ApprovalNodeTaskStatus.REJECTED;
        }

        if (action == task.getStatus()) {
            return;
        }

        ApprovalStageMO summary = parseStageContext(process);
        Long currentActivityId = summary.getCustomApproval().getCurrentActivityId();
        if (ticket.getTicketStatus() != ApprovalStatus.WAIT_APPROVAL || process.getProcessStatus() != ApprovalProcessStatus.INIT || currentActivityId == null
            || !currentActivityId.equals(activity.getId()) || !RdpTicketProcessActivityStatus.RUNNING.name().equals(activity.getTaskStatus())
            || task.getStatus() != ApprovalNodeTaskStatus.PENDING) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_ACTION_CONFLICT.name()));
        }

        if (request.getComment() != null && request.getComment().getBytes(StandardCharsets.UTF_8).length > MAX_COMMENT_BYTES) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
        }

        task.setStatus(action);
        task.setComment(request.getComment());
        task.setFinishTime(new Date());
        task.setGmtModified(new Date());
        approvalDal.nodeTaskMapper().updateById(task);
        ApprovalNodeContext context = JsonUtils.toObj(activity.getContext(), ApprovalNodeContext.class);
        int required = context.getEffectiveRequiredCount();
        int approved = 0;
        for (DmApprovalNodeTaskDO candidate : tasks) {
            if (candidate.getStatus() == ApprovalNodeTaskStatus.APPROVED) {
                approved++;
            }
        }

        if (!request.isRejected() && approved < required) {
            return;
        }

        cancelPendingTasks(tasks);
        summary.getCustomApproval().setCurrentActivityId(null);
        summary.setExecMsg(request.getComment());
        summary.setExecUserName(List.of(task.getApproverName()));
        if (request.isRejected()) {
            activity.setTaskStatus(RdpTicketProcessActivityStatus.REFUSE.name());
            activity.setGmtModified(new Date());
            approvalDal.activityMapper().updateById(activity);
            cancelPendingApprovals(ticket.getId());
            stateService.updateProcessStatus(ticket.getId(), ApprovalStage.APPROVAL, ApprovalProcessStatus.REJECT, JsonUtils.toJson(summary));
            for (ApprovalStage stage : List.of(ApprovalStage.CONFIRM, ApprovalStage.EXECUTION)) {
                if (approvalDal.processMapper().queryByStage(ticket.getId(), stage) != null) {
                    stateService.updateProcessStatus(ticket.getId(), stage, ApprovalProcessStatus.CLOSED, null);
                }
            }

            stateService.finalizeApproval(ticket.getId(), ApprovalStatus.REJECTED, request.getComment());
            return;
        }

        activity.setTaskStatus(RdpTicketProcessActivityStatus.COMPLETED.name());
        activity.setGmtModified(new Date());
        approvalDal.activityMapper().updateById(activity);
        DmApprovalProcessActivityDO next = null;
        for (DmApprovalProcessActivityDO candidate : activities) {
            if (candidate.getOrderNumber() > activity.getOrderNumber()) {
                next = candidate;
                break;
            }
        }

        if (next != null) {
            startApprovalNode(process, summary, next);
            return;
        }

        stateService.updateProcessStatus(ticket.getId(), ApprovalStage.APPROVAL, ApprovalProcessStatus.FINISH, JsonUtils.toJson(summary));
        for (ApprovalHandler handler : handlers) {
            if (handler.handleType() == ticket.getApproBiz()) {
                handler.approvalApproved(ticket.getId(), ticket.getApproBiz(), imSenderService);
                return;
            }
        }

        throw new NoSuchElementException("Approval handler not found: " + ticket.getApproBiz());
    }

    @Override
    public boolean canApprove(DmApprovalDO ticket, String uid) {
        if (ticket.getTicketStatus() != ApprovalStatus.WAIT_APPROVAL
            || !configService.isEligibleUser(ticket.getPrimaryUid(), authDal.userMapper().queryByUid(uid), SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE)) {
            return false;
        }

        DmApprovalProcessDO process = approvalDal.processMapper().queryByStage(ticket.getId(), ApprovalStage.APPROVAL);
        Long currentActivityId = parseStageContext(process).getCustomApproval().getCurrentActivityId();
        if (currentActivityId == null) {
            return false;
        }
        return approvalDal.nodeTaskMapper().countPendingByApprover(ticket.getPrimaryUid(), uid, currentActivityId) > 0;
    }

    @Override
    public List<Long> listParticipantTicketIds(String puid, DmAuthUserDO viewer, boolean pendingOnly) {
        if (viewer.isDisable() || (!pendingOnly && puid.equals(viewer.getUid()))) {
            return List.of();
        }

        DmAuthRoleDO role = authDal.roleMapper().selectById(viewer.getRoleId());
        boolean canApprove = role.getRoleAuthLabels().contains(SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE);
        boolean canExecute = role.getRoleAuthLabels().contains(SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE);
        if (!canApprove && !canExecute) {
            return List.of();
        }

        List<DmApprovalDO> tickets = approvalDal.approvalMapper().listCustomTickets(puid, pendingOnly);
        Set<Long> approvalIds = new LinkedHashSet<>();
        Set<Long> executionIds = new LinkedHashSet<>();
        for (DmApprovalDO ticket : tickets) {
            if (!pendingOnly || ticket.getTicketStatus() == ApprovalStatus.WAIT_APPROVAL) {
                approvalIds.add(ticket.getId());
            }

            if (!pendingOnly || ticket.getTicketStatus() == ApprovalStatus.WAIT_CONFIRM) {
                executionIds.add(ticket.getId());
            }
        }

        Set<Long> result = new LinkedHashSet<>();
        if (canApprove && !approvalIds.isEmpty()) {
            result.addAll(listApproverTicketIds(puid, viewer.getUid(), approvalIds, pendingOnly));
        }

        if (canExecute && !executionIds.isEmpty()) {
            result.addAll(listExecutorTicketIds(viewer.getUid(), executionIds));
        }
        return new ArrayList<>(result);
    }

    private List<Long> listApproverTicketIds(String puid, String uid, Set<Long> ticketIds, boolean pendingOnly) {
        List<Long> activityIds = approvalDal.nodeTaskMapper().listActivityIdsByApprover(puid, uid, pendingOnly);
        if (activityIds.isEmpty()) {
            return List.of();
        }

        return approvalDal.activityMapper().listTicketIdsByActivities(activityIds, ticketIds, pendingOnly);
    }

    private List<Long> listExecutorTicketIds(String uid, Set<Long> ticketIds) {
        List<DmApprovalProcessDO> processes = approvalDal.processMapper().listExecutionByTicketIds(ticketIds);
        List<Long> result = new ArrayList<>();
        for (DmApprovalProcessDO process : processes) {
            ApprovalStageMO stageContext = parseStageContext(process);
            ApprovalExecutionContext execution = stageContext.getCustomExecution();
            if (execution == null || !AUTHORIZATION_ENGINE.equals(execution.getAuthorizationMode())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_INSTANCE_INVALID.name()));
            }

            for (ApprovalExecutor participant : execution.getParticipants()) {
                if (uid.equals(participant.getUserUid())) {
                    result.add(process.getTicketId());
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public boolean canRead(DmApprovalDO ticket, String uid) {
        DmAuthUserDO user = authDal.userMapper().queryByUid(uid);
        if (!configService.isEligibleUser(ticket.getPrimaryUid(), user, SecRoleAuthLabel.RDP_WORKER_ORDER_READ)) {
            return false;
        }

        if (uid.equals(ticket.getOwnerUid()) || uid.equals(ticket.getPrimaryUid())) {
            return true;
        }

        if (configService.isEligibleUser(ticket.getPrimaryUid(), user, SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE)
            && !listApproverTicketIds(ticket.getPrimaryUid(), uid, Set.of(ticket.getId()), false).isEmpty()) {
            return true;
        }
        return canExecute(ticket, uid);
    }

    @Override
    public boolean isAutomaticExecution(DmApprovalDO ticket) {
        ApprovalExecutionContext execution = parseStageContext(approvalDal.processMapper().queryByStage(ticket.getId(), ApprovalStage.EXECUTION)).getCustomExecution();
        return execution != null && execution.getExecutionMode() == ApprovalExecutionMode.AUTO;
    }

    @Override
    public boolean canExecute(DmApprovalDO ticket, String uid) {
        if (!isCustomApproval(ticket)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_INSTANCE_INVALID.name()));
        }

        ApprovalStageMO summary = parseStageContext(approvalDal.processMapper().queryByStage(ticket.getId(), ApprovalStage.EXECUTION));
        ApprovalExecutionContext execution = summary.getCustomExecution();
        if (execution == null || !Integer.valueOf(EXECUTION_SNAPSHOT_VERSION).equals(execution.getSchemaVersion()) || !AUTHORIZATION_ENGINE.equals(execution.getAuthorizationMode())
            || execution.getParticipants() == null || execution.getParticipants().isEmpty()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_INSTANCE_INVALID.name()));
        }

        DmAuthUserDO user = authDal.userMapper().queryByUid(uid);
        if (!configService.isEligibleUser(ticket.getPrimaryUid(), user, SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE)) {
            return false;
        }

        for (ApprovalExecutor participant : execution.getParticipants()) {
            if (uid.equals(participant.getUserUid())) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void cancelPendingApprovals(long ticketId) {
        DmApprovalProcessDO process = approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.APPROVAL);
        List<DmApprovalProcessActivityDO> activities = approvalDal.activityMapper().listCustomActivities(ticketId, process.getId());
        for (DmApprovalProcessActivityDO activity : activities) {
            List<DmApprovalNodeTaskDO> tasks = approvalDal.nodeTaskMapper().listByActivityId(activity.getId());
            cancelPendingTasks(tasks);
            if (RdpTicketProcessActivityStatus.NEW.name().equals(activity.getTaskStatus()) || RdpTicketProcessActivityStatus.RUNNING.name().equals(activity.getTaskStatus())) {
                activity.setTaskStatus(RdpTicketProcessActivityStatus.CANCELED.name());
                activity.setGmtModified(new Date());
                approvalDal.activityMapper().updateById(activity);
            }
        }
    }

    private void cancelPendingTasks(List<DmApprovalNodeTaskDO> tasks) {
        for (DmApprovalNodeTaskDO task : tasks) {
            if (task.getStatus() == ApprovalNodeTaskStatus.PENDING || task.getStatus() == ApprovalNodeTaskStatus.WAITING) {
                task.setStatus(ApprovalNodeTaskStatus.CANCELED);
                task.setFinishTime(new Date());
                task.setGmtModified(new Date());
                approvalDal.nodeTaskMapper().updateById(task);
            }
        }
    }

    @Override
    public List<RdpTicketActivityVO> listApprovalNodeDetails(long ticketId, long processId) {
        List<RdpTicketActivityVO> result = new ArrayList<>();
        List<DmApprovalProcessActivityDO> activities = approvalDal.activityMapper().listCustomActivities(ticketId, processId);
        for (DmApprovalProcessActivityDO activity : activities) {
            List<DmApprovalNodeTaskDO> tasks = approvalDal.nodeTaskMapper().listByActivityId(activity.getId());
            RdpTicketActivityVO view = new RdpTicketActivityVO();
            view.setProcessActivityId(activity.getId());
            view.setProcessId(processId);
            view.setTicketId(ticketId);
            view.setActivityTitle(activity.getActivityTitle());
            view.setDisplayOrder(activity.getOrderNumber());
            Date start = null;
            Date finish = null;
            int approved = 0;
            List<String> approverNames = new ArrayList<>();
            for (DmApprovalNodeTaskDO task : tasks) {
                Date taskStart = task.getStartTime();
                if (taskStart != null && (start == null || taskStart.before(start))) {
                    start = taskStart;
                }

                Date taskFinish = task.getFinishTime();
                if (taskFinish != null && (finish == null || taskFinish.after(finish))) {
                    finish = taskFinish;
                }

                if (task.getStatus() == ApprovalNodeTaskStatus.APPROVED) {
                    approved++;
                }

                approverNames.add(task.getApproverName());
            }

            view.setStartTime(DateFormatType.s_yyyyMMdd_HHmmss.format(start));
            if (start != null) {
                view.setStartTimeUtc(start.getTime());
            }

            if (!RdpTicketProcessActivityStatus.NEW.name().equals(activity.getTaskStatus()) && !RdpTicketProcessActivityStatus.RUNNING.name().equals(activity.getTaskStatus())) {
                view.setFinishTime(DateFormatType.s_yyyyMMdd_HHmmss.format(finish));
                if (finish != null) {
                    view.setFinishTimeUtc(finish.getTime());
                }
            }

            view.setActivityStatus(RdpTicketProcessActivityStatus.valueOf(activity.getTaskStatus()));
            view.setApprovalUserList(approverNames);
            view.setApprovalTasks(tasks);
            ApprovalNodeContext context = JsonUtils.toObj(activity.getContext(), ApprovalNodeContext.class);
            view.setApproverType(context.getApproverType());
            view.setTeamName(context.getTeamName());
            view.setRequiredCount(context.getEffectiveRequiredCount());
            view.setApprovedCount(approved);
            result.add(view);
        }
        return result;
    }

    private static String findTeamName(ApprovalTeamConfig config, String teamId) {
        for (ApprovalTeam team : config.getTeams()) {
            if (team.getTeamId().equals(teamId)) {
                return team.getTeamName();
            }
        }
        return null;
    }
}

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

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.ApprovalPersonService;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpApprovalFO;
import com.clougence.clouddm.console.web.model.vo.PrimaryUserVO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Ekko
 * @Date: 2024-05-07 15:48
 */

@Service
@Slf4j
public class ApprovalFlowServiceImpl implements ApprovalFlowService {

    @Resource
    private AuthDal                                 authDal;
    @Resource
    private ApprovalDal                             approvalDal;
    @Resource
    private ApprovalProviderServiceImpl             providerService;
    @Resource
    private ImSenderService                         imSenderService;
    @Resource
    private ApprovalStateService                    approvalStateService;
    @Resource
    private ApprovalPersonService                   approvalPersonService;

    private final Map<ApprovalBiz, ApprovalHandler> approvalHandlers;

    public ApprovalFlowServiceImpl(List<ApprovalHandler> approvalHandlers){
        this.approvalHandlers = new EnumMap<>(ApprovalBiz.class);
        for (ApprovalHandler approvalHandler : approvalHandlers) {
            ApprovalBiz type = approvalHandler.handleType();
            if (this.approvalHandlers.putIfAbsent(type, approvalHandler) != null) {
                throw new IllegalStateException("ApprovalHandler about " + type + " already exists");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void closeTicket(long ticketId, String statusMessage, String puid, String uid) {
        DmApprovalDO ticketDO = checkTicket(ticketId);
        ApprovalStatus ticketStatus = ticketDO.getTicketStatus();
        if (ticketStatus == ApprovalStatus.WAIT_EXEC || ticketStatus == ApprovalStatus.EXEC_FAIL || ticketStatus == ApprovalStatus.EXEC_PAUSE) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_OPERATOR_TYPE_NOT_MATCH_STATUS.name()));
        }
        checkInProgress(ticketDO);
        List<DmApprovalProcessDO> approvalProcessDOS = approvalDal.processMapper().listByTicketId(ticketId);
        DmAuthUserDO rdpUserDO = authDal.userMapper().queryByUid(uid);
        for (DmApprovalProcessDO approvalProcessDO : approvalProcessDOS) {
            if (approvalProcessDO.getProcessStatus() == ApprovalProcessStatus.INIT) {
                ApprovalStageMO execMO = new ApprovalStageMO();
                execMO.setExecUserName(Collections.singletonList(rdpUserDO.getUsername()));
                this.approvalDal.processMapper().updateContextById(approvalProcessDO.getId(), JsonUtils.toJson(execMO));
                break;
            }
        }
        this.cancelAllProcess(ticketId);
        this.transitionTicketToTerminal(ticketDO.getId(), ApprovalStatus.CLOSED, statusMessage);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void failTicket(long ticketId, String statusMessage, String puid) {
        DmApprovalDO ticketDO = checkTicket(ticketId);
        checkInProgress(ticketDO);

        this.failCurrentAndCloseFollowingProcesses(ticketDO);
        this.transitionTicketToTerminal(ticketDO.getId(), ApprovalStatus.FAILED, statusMessage);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void cancelTicket(String puid, long ticketId, String statusMessage) {
        DmApprovalDO ticketDO = checkTicket(ticketId);

        checkInProgress(ticketDO);
        this.cancelAllProcess(ticketId);
        this.transitionTicketToTerminal(ticketDO.getId(), ApprovalStatus.CANCELED, statusMessage);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void approvalTicket(String puid, String uid, RdpApprovalFO fo) {
        DmApprovalDO ticketDO = checkTicket(fo.getTicketId());
        if (ticketDO.getTicketStatus() != ApprovalStatus.WAIT_APPROVAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_OPERATOR_TYPE_NOT_MATCH_STATUS.name()));
        }

        List<DmApprovalPersonDO> persons = this.approvalDal.personMapper().queryByTicketBzId(ticketDO.getBizId());
        List<String> allowUsers = persons.stream().map(DmApprovalPersonDO::getPersonUid).collect(Collectors.toList());

        if (!allowUsers.contains(uid)) {
            throw new RuntimeException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NO_PERMISSION_ERROR.name()));
        }

        ApprovalStageMO execMO = new ApprovalStageMO();
        execMO.setExecUserName(Collections.singletonList(this.authDal.userMapper().queryByUid(uid).getUsername()));
        this.approvalDal.approvalMapper().updateComment(ticketDO.getId(), fo.getComment());
        if (fo.isRejected()) {
            // WAIT_APPROVAL -> REJECTED
            if (StringUtils.isNotBlank(fo.getComment())) {
                execMO.setExecMsg(fo.getComment());
            } else {
                execMO.setExecMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_REJECTED_BY_APPROVAL.name()));
            }
            this.approvalStateService.updateProcessStatus(ticketDO.getId(), ApprovalStage.APPROVAL, ApprovalProcessStatus.REJECT, JsonUtils.toJson(execMO));
            this.transitionTicketToTerminal(ticketDO.getId(), ApprovalStatus.REJECTED, null);
        } else {
            // WAIT_APPROVAL -> WAIT_CONFIRM
            approvalHandler(ticketDO.getApproBiz()).approvalApproved(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
            if (StringUtils.isNotBlank(fo.getComment())) {
                execMO.setExecMsg(fo.getComment());
                ticketDO.setApproComment(fo.getComment());
            } else {
                execMO.setExecMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_ADOPT_BY_APPROVAL.name()));
            }
            this.approvalStateService.updateProcessStatus(ticketDO.getId(), ApprovalStage.APPROVAL, ApprovalProcessStatus.FINISH, JsonUtils.toJson(execMO));
        }

        //  update real approval person
        this.approvalDal.personMapper().deleteByTicketBzId(ticketDO.getBizId());
    }

    private ApprovalHandler approvalHandler(ApprovalBiz approvalBiz) {
        ApprovalHandler approvalHandler = this.approvalHandlers.get(approvalBiz);
        if (approvalHandler == null) {
            throw new IllegalStateException("ApprovalHandler about " + approvalBiz + " does not exist");
        }
        return approvalHandler;
    }

    @Override
    public boolean isFinish(long ticketId) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(ticketId);
        return ticketDO == null || ApprovalStatus.isEndStatus(ticketDO.getTicketStatus());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void createProcess(long ticketId, ApprovalBiz approvalBiz, boolean checkSuccess) {
        long firstStageId = -1;
        DmApprovalProcessDO lastProcessDO = null;
        DmApprovalDO approvalDO = this.approvalDal.approvalMapper().queryById(ticketId);

        List<String> personName = Collections.emptyList();
        if (approvalDO.getApproType() == ApprovalType.Internal) {
            List<PrimaryUserVO> persons = approvalHandler(approvalBiz).queryPerson(ticketId);
            personName = this.approvalPersonService.replacePersons(approvalDO, persons);
        }

        for (ApprovalStage stage : ApprovalStage.values()) {
            if (!stage.checkBiz(approvalBiz)) {
                continue;
            }

            DmApprovalProcessDO processDO = new DmApprovalProcessDO();
            processDO.setTicketId(ticketId);
            processDO.setTicketStage(stage);
            processDO.setProcessStatus(ApprovalProcessStatus.INIT);
            if (stage == ApprovalStage.APPROVAL) {
                ApprovalStageMO mo = new ApprovalStageMO();
                mo.setExecUserName(personName);
                processDO.setStageContext(JsonUtils.toJson(mo));
            } else if (stage == ApprovalStage.EXPLAIN) {
                ApprovalStageMO execMO = new ApprovalStageMO();
                if (checkSuccess) {
                    execMO.setExecMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_RULE_CHECK_EXE.name()));
                } else {
                    execMO.setExecMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_RULE_CHECK_FAIL_EXE.name()));
                }
                execMO.setExecUserName(Collections.singletonList(this.authDal.userMapper().queryByUid(approvalDO.getOwnerUid()).getUsername()));
                processDO.setStageContext(JsonUtils.toJson(execMO));
            }
            processDO = this.approvalStateService.initializeProcess(ticketId, stage, ApprovalProcessStatus.INIT, processDO.getStageContext());

            // need return first process id
            if (firstStageId == -1) {
                firstStageId = processDO.getId();
            } else {
                this.approvalDal.processMapper().updateById(lastProcessDO);
            }

            // refresh last
            lastProcessDO = processDO;
        }

        if (firstStageId == -1) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_CAN_NOT_GET.name(), ticketId));
        }
    }

    @Override
    public List<DmApprovalProcessDO> getProcessList(long ticketId) {
        return this.approvalDal.processMapper().listByTicketId(ticketId);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void cancelProcess(long ticketId, long processId) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(ticketId);
        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryTicketProcessById(ticketId, processId);

        // is completed.
        if (processDO.getProcessStatus() == ApprovalProcessStatus.FINISH) {
            return;
        }

        // do action
        switch (processDO.getTicketStage()) {
            case EXPLAIN:
            case CONFIRM:
            case EXECUTION: {
                // when auto exec, EXECUTION need other code
                break; // do nothing
            }
            case APPROVAL: {
                if (StringUtils.isNotBlank(processDO.getStageContext()) && ticketDO.getApproType() != ApprovalType.Internal) {
                    try {
                        this.providerService.cancelApprovalInst(ticketDO.getId());
                    } catch (ThirdPartyApiException e) {
                        throw new ErrorMessageException(DmI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs()));
                    }
                }
                break;
            }
            default: {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STAGE_CANNOT_CANCEL.name(), processDO.getTicketStage().name()));
            }
        }

        // update status
        processDO.setProcessStatus(ApprovalProcessStatus.CLOSED);
        processDO.setFinishTime(new Date());
        this.approvalDal.processMapper().updateById(processDO);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void cancelAllProcess(long ticketId) {
        List<DmApprovalProcessDO> processList = this.getProcessList(ticketId);
        for (DmApprovalProcessDO processDO : processList) {
            if (processDO.getProcessStatus() != ApprovalProcessStatus.FINISH) {
                this.cancelProcess(ticketId, processDO.getId());
            }
        }
    }

    private void failCurrentAndCloseFollowingProcesses(DmApprovalDO ticketDO) {
        ApprovalStage currentStage = switch (ticketDO.getTicketStatus()) {
            case PRE_INIT_WAIT, PRE_INIT_RUN -> ApprovalStage.EXPLAIN;
            case WAIT_APPROVAL -> ApprovalStage.APPROVAL;
            case WAIT_CONFIRM -> ApprovalStage.CONFIRM;
            case WAIT_EXEC, RUNNING, EXEC_FAIL, EXEC_PAUSE -> ApprovalStage.EXECUTION;
            default -> null;
        };
        if (currentStage == null) {
            return;
        }

        DmApprovalProcessDO currentProcess = this.approvalDal.processMapper().queryByStage(ticketDO.getId(), currentStage);
        if (currentProcess == null || currentProcess.getProcessStatus() == ApprovalProcessStatus.FINISH) {
            return;
        }
        if (currentStage == ApprovalStage.APPROVAL) {
            this.doFailed(ticketDO.getId(), currentProcess);
        }
        currentProcess.setProcessStatus(ApprovalProcessStatus.FAIL);
        currentProcess.setFinishTime(new Date());
        this.approvalDal.processMapper().updateById(currentProcess);

        for (DmApprovalProcessDO processDO : this.getProcessList(ticketDO.getId())) {
            if (processDO.getTicketStage().ordinal() <= currentStage.ordinal() || processDO.getProcessStatus() != ApprovalProcessStatus.INIT) {
                continue;
            }
            processDO.setProcessStatus(ApprovalProcessStatus.CLOSED);
            processDO.setFinishTime(new Date());
            this.approvalDal.processMapper().updateById(processDO);
        }
    }

    @Override
    public boolean checkEnableApproval(String ownerUid, ApprovalProvider type) {
        return this.providerService.checkEnableApproval(ownerUid, type);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void transitionTicketToTerminal(long ticketId, ApprovalStatus terminalStatus, String statusMessage) {
        checkTicket(ticketId);
        this.approvalStateService.finalizeApproval(ticketId, terminalStatus, statusMessage);
    }

    @Override
    public DmApprovalTemplateDO checkApprovalAndReturnTemplate(String ownerUid, ApprovalType type, String templateId, Locale locale) {
        return this.providerService.checkApprovalAndReturnTemplate(ownerUid, type, templateId, locale);
    }

    private void checkInProgress(DmApprovalDO ticketDO) {
        switch (ticketDO.getTicketStatus()) {
            case REJECTED:
            case FINISHED:
            case CLOSED:
            case CANCELED:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_FINAL_ERROR.name()));
            default:
                break;
        }
    }

    private DmApprovalDO checkTicket(long ticketId) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(ticketId);
        if (ticketDO == null || ticketDO.getDeleted()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }

        return ticketDO;
    }

    private void doFailed(long ticketId, DmApprovalProcessDO processDO) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(ticketId);

        boolean isAllowType = StringUtils.isNotBlank(processDO.getStageContext()) && ticketDO.getApproType() != ApprovalType.Internal;
        boolean isEnable = this.providerService.checkEnableApproval(ticketDO.getOwnerUid(), ticketDO.getApproType().getProviderType());

        if (isAllowType && isEnable) {
            try {
                this.providerService.cancelApprovalInst(ticketDO.getId());
            } catch (Exception e) {
                // fail ticket don't care third party anything error
                log.error("cancel approval instance failed", e);
            }
        }
    }
}

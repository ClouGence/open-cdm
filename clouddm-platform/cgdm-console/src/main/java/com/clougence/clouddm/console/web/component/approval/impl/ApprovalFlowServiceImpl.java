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
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpApprovalFO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
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
    private ApprovalProcessServiceImpl              approvalProcessService;
    @Resource
    private ApprovalProviderServiceImpl             approvalProviderService;
    @Resource
    private ImSenderService                         imSenderService;

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
        DmApprovalDO ticketDO = checkTicket(ticketId, puid);
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
        this.approvalProcessService.cancelAllProcess(ticketId);
        this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, ApprovalStatus.CLOSED, statusMessage);
        this.approvalHandler(ticketDO.getApproBiz()).approvalCanceled(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void closeTicket(long ticketId, String statusMessage, String puid) {
        DmApprovalDO ticketDO = checkTicket(ticketId, puid);
        checkInProgress(ticketDO);
        this.approvalProcessService.cancelAllProcess(ticketId);
        this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, ApprovalStatus.CLOSED, statusMessage);
        this.approvalHandler(ticketDO.getApproBiz()).approvalCanceled(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void failTicket(long ticketId, String statusMessage, String puid) {
        DmApprovalDO ticketDO = checkTicket(ticketId, puid);
        checkInProgress(ticketDO);

        this.approvalProcessService.failedAllProcess(ticketId);
        this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, ApprovalStatus.FAILED, statusMessage);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void cancelTicket(String puid, long ticketId, String statusMessage) {
        DmApprovalDO ticketDO = checkTicket(ticketId, puid);

        checkInProgress(ticketDO);
        this.approvalProcessService.cancelAllProcess(ticketId);
        this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, ApprovalStatus.CANCELED, statusMessage);
        this.approvalHandler(ticketDO.getApproBiz()).approvalCanceled(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void approvalTicket(String puid, String uid, RdpApprovalFO fo) {
        DmApprovalDO ticketDO = checkTicket(fo.getTicketId(), puid);
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
        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(fo.getTicketId(), ApprovalStage.APPROVAL);
        this.approvalDal.approvalMapper().updateComment(ticketDO.getId(), fo.getComment());
        if (fo.isRejected()) {
            // WAIT_APPROVAL -> REJECTED
            approvalHandler(ticketDO.getApproBiz()).approvalRefuse(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
            if (StringUtils.isNotBlank(fo.getComment())) {
                execMO.setExecMsg(fo.getComment());
            } else {
                execMO.setExecMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_REJECTED_BY_APPROVAL.name()));
            }
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDO.getId(), ApprovalProcessStatus.REJECT, JsonUtils.toJson(execMO));
            this.approvalDal.approvalMapper().updateStatusByEnum(ticketDO.getId(), ApprovalStatus.REJECTED, null);
        } else {
            // WAIT_APPROVAL -> WAIT_CONFIRM
            approvalHandler(ticketDO.getApproBiz()).approvalCompleted(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
            if (StringUtils.isNotBlank(fo.getComment())) {
                execMO.setExecMsg(fo.getComment());
                ticketDO.setApproComment(fo.getComment());
            } else {
                execMO.setExecMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_ADOPT_BY_APPROVAL.name()));
            }
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDO.getId(), ApprovalProcessStatus.FINISH, JsonUtils.toJson(execMO));
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
    public void retryTicket(String puid, long ticketId) {
        DmApprovalDO ticketDO = checkTicket(ticketId, puid);

        if (ticketDO.getTicketStatus() != ApprovalStatus.EXEC_FAIL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_RETRY_STATUS_DISCONTENT_ERROR.name()));
        }

        this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, ApprovalStatus.WAIT_EXEC, DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_WAIT_EXEC_MESSAGE.name()));
    }

    @Override
    public void createProcess(long ticketId, ApprovalBiz approvalBiz, boolean checkSuccess) {
        this.approvalProcessService.createProcess(ticketId, approvalBiz, checkSuccess);
    }

    @Override
    public List<DmApprovalProcessDO> getProcessList(long ticketId) {
        return this.approvalProcessService.getProcessList(ticketId);
    }

    @Override
    public void cancelProcess(long ticketId, long processId) {
        this.approvalProcessService.cancelProcess(ticketId, processId);
    }

    @Override
    public void cancelAllProcess(long ticketId) {
        this.approvalProcessService.cancelAllProcess(ticketId);
    }

    @Override
    public void failedAllProcess(long ticketId) {
        this.approvalProcessService.failedAllProcess(ticketId);
    }

    @Override
    public void cancelApprovalInst(Long ticketId) {
        this.approvalProviderService.cancelApprovalInst(ticketId);
    }

    @Override
    public boolean checkEnableApproval(String ownerUid, ApprovalProvider type) {
        return this.approvalProviderService.checkEnableApproval(ownerUid, type);
    }

    @Override
    public void refreshApprovalStatus(long ticketId) {
        this.approvalProviderService.refreshApprovalStatus(ticketId);
    }

    @Override
    public DmApprovalTemplateDO checkApprovalAndReturnTemplate(String ownerUid, ApprovalType type, String templateId, Locale locale) {
        return this.approvalProviderService.checkApprovalAndReturnTemplate(ownerUid, type, templateId, locale);
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

    private DmApprovalDO checkTicket(long ticketId, String puid) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(ticketId);
        if (ticketDO == null || ticketDO.getDeleted()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }
        if (!ticketDO.getPrimaryUid().equals(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_BELONG_CURRENT_TEAM.name()));
        }

        return ticketDO;
    }
}

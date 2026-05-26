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

import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.DmApprovalMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmApprovalPersonMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmApprovalProcessMapper;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpApprovalFO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.global.exception.ErrorMessageException;
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
    private ApprovalProcessServiceImpl                 approvalProcessService;
    @Resource
    private ApprovalProviderServiceImpl                approvalProviderService;
    @Resource
    private DmApprovalMapper                           approvalMapper;
    @Resource
    private RdpUserMapper                              userMapper;
    @Resource
    private DmApprovalProcessMapper                    approvalProcessMapper;
    @Resource
    private DmApprovalPersonMapper                     approvalPersonMapper;
    @Resource
    private ImSenderService                            imSenderService;

    private final Map<RdpApprovalBiz, ApprovalHandler> approvalHandlers;

    public ApprovalFlowServiceImpl(List<ApprovalHandler> approvalHandlers){
        this.approvalHandlers = new EnumMap<>(RdpApprovalBiz.class);
        for (ApprovalHandler approvalHandler : approvalHandlers) {
            RdpApprovalBiz type = approvalHandler.handleType();
            if (this.approvalHandlers.putIfAbsent(type, approvalHandler) != null) {
                throw new IllegalStateException("ApprovalHandler about " + type + " already exists");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void closeTicket(long ticketId, String statusMessage, String puid, String uid) {
        DmApprovalDO ticketDO = checkTicket(ticketId, puid);
        RdpTicketStatus ticketStatus = ticketDO.getTicketStatus();
        if (ticketStatus == RdpTicketStatus.WAIT_EXEC || ticketStatus == RdpTicketStatus.EXEC_FAIL || ticketStatus == RdpTicketStatus.EXEC_PAUSE) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_OPERATOR_TYPE_NOT_MATCH_STATUS.name()));
        }
        checkInProgress(ticketDO);
        List<DmApprovalProcessDO> approvalProcessDOS = approvalProcessMapper.listByTicketId(ticketId);
        RdpUserDO rdpUserDO = userMapper.queryByUid(uid);
        for (DmApprovalProcessDO approvalProcessDO : approvalProcessDOS) {
            if (approvalProcessDO.getProcessStatus() == RdpTicketProcessStatus.INIT) {
                ApprovalStageMO execMO = new ApprovalStageMO();
                execMO.setExecUserName(Collections.singletonList(rdpUserDO.getUsername()));
                this.approvalProcessMapper.updateContextById(approvalProcessDO.getId(), JsonUtils.toJson(execMO));
                break;
            }
        }
        this.approvalProcessService.cancelAllProcess(ticketId);
        this.approvalMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.CLOSED, statusMessage);
        this.approvalHandler(ticketDO.getApproBiz()).approvalCanceled(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void closeTicket(long ticketId, String statusMessage, String puid) {
        DmApprovalDO ticketDO = checkTicket(ticketId, puid);
        checkInProgress(ticketDO);
        this.approvalProcessService.cancelAllProcess(ticketId);
        this.approvalMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.CLOSED, statusMessage);
        this.approvalHandler(ticketDO.getApproBiz()).approvalCanceled(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void failTicket(long ticketId, String statusMessage, String puid) {
        DmApprovalDO ticketDO = checkTicket(ticketId, puid);
        checkInProgress(ticketDO);

        this.approvalProcessService.failedAllProcess(ticketId);
        this.approvalMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.FAILED, statusMessage);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void cancelTicket(String puid, long ticketId, String statusMessage) {
        DmApprovalDO ticketDO = checkTicket(ticketId, puid);

        checkInProgress(ticketDO);
        this.approvalProcessService.cancelAllProcess(ticketId);
        this.approvalMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.CANCELED, statusMessage);
        this.approvalHandler(ticketDO.getApproBiz()).approvalCanceled(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void approvalTicket(String puid, String uid, RdpApprovalFO fo) {
        DmApprovalDO ticketDO = checkTicket(fo.getTicketId(), puid);
        if (ticketDO.getTicketStatus() != RdpTicketStatus.WAIT_APPROVAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_OPERATOR_TYPE_NOT_MATCH_STATUS.name()));
        }

        List<DmApprovalPersonDO> persons = this.approvalPersonMapper.queryByTicketBzId(ticketDO.getBizId());
        List<String> allowUsers = persons.stream().map(DmApprovalPersonDO::getPersonUid).collect(Collectors.toList());

        if (!allowUsers.contains(uid)) {
            throw new RuntimeException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NO_PERMISSION_ERROR.name()));
        }

        ApprovalStageMO execMO = new ApprovalStageMO();
        execMO.setExecUserName(Collections.singletonList(this.userMapper.queryByUid(uid).getUsername()));
        DmApprovalProcessDO processDO = this.approvalProcessMapper.queryByStage(fo.getTicketId(), RdpTicketStage.APPROVAL);
        this.approvalMapper.updateComment(ticketDO.getId(), fo.getComment());
        if (fo.isRejected()) {
            // WAIT_APPROVAL -> REJECTED
            approvalHandler(ticketDO.getApproBiz()).approvalRefuse(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
            if (StringUtils.isNotBlank(fo.getComment())) {
                execMO.setExecMsg(fo.getComment());
            } else {
                execMO.setExecMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_REJECTED_BY_APPROVAL.name()));
            }
            this.approvalProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.REJECT, JsonUtils.toJson(execMO));
            this.approvalMapper.updateTicketStatusByEnum(ticketDO.getId(), RdpTicketStatus.REJECTED, null);
        } else {
            // WAIT_APPROVAL -> WAIT_CONFIRM
            approvalHandler(ticketDO.getApproBiz()).approvalCompleted(ticketDO.getId(), ticketDO.getApproBiz(), imSenderService);
            if (StringUtils.isNotBlank(fo.getComment())) {
                execMO.setExecMsg(fo.getComment());
                ticketDO.setApproComment(fo.getComment());
            } else {
                execMO.setExecMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_ADOPT_BY_APPROVAL.name()));
            }
            this.approvalProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.FINISH, JsonUtils.toJson(execMO));
        }

        //  update real approval person
        this.approvalPersonMapper.deleteByTicketBzId(ticketDO.getBizId());
    }

    private ApprovalHandler approvalHandler(RdpApprovalBiz approvalBiz) {
        ApprovalHandler approvalHandler = this.approvalHandlers.get(approvalBiz);
        if (approvalHandler == null) {
            throw new IllegalStateException("ApprovalHandler about " + approvalBiz + " does not exist");
        }
        return approvalHandler;
    }

    @Override
    public boolean isFinish(long ticketId) {
        DmApprovalDO ticketDO = this.approvalMapper.queryById(ticketId);
        return ticketDO == null || RdpTicketStatus.isEndStatus(ticketDO.getTicketStatus());
    }

    @Override
    public void retryTicket(String puid, long ticketId) {
        DmApprovalDO ticketDO = checkTicket(ticketId, puid);

        if (ticketDO.getTicketStatus() != RdpTicketStatus.EXEC_FAIL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_RETRY_STATUS_DISCONTENT_ERROR.name()));
        }

        this.approvalMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.WAIT_EXEC, DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_WAIT_EXEC_MESSAGE.name()));
    }

    @Override
    public void createProcess(long ticketId, RdpApprovalBiz approvalBiz, boolean checkSuccess) {
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
    public DmApprovalCacheTemplateDO checkApprovalAndReturnTemplate(String ownerUid, RdpApprovalType type, String templateId, Locale locale) {
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
        DmApprovalDO ticketDO = this.approvalMapper.queryById(ticketId);
        if (ticketDO == null || ticketDO.getDeleted()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }
        if (!ticketDO.getPrimaryUid().equals(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_BELONG_CURRENT_TEAM.name()));
        }

        return ticketDO;
    }
}

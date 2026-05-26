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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.dal.enumeration.RdpApprovalBiz;
import com.clougence.clouddm.console.web.dal.enumeration.RdpApprovalType;
import com.clougence.clouddm.console.web.dal.enumeration.RdpTicketProcessStatus;
import com.clougence.clouddm.console.web.dal.enumeration.RdpTicketStage;
import com.clougence.clouddm.console.web.dal.mapper.DmApprovalMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmApprovalPersonMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmApprovalProcessMapper;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.model.DmApprovalDO;
import com.clougence.clouddm.console.web.dal.model.DmApprovalPersonDO;
import com.clougence.clouddm.console.web.dal.model.DmApprovalProcessDO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2024/7/4 15:55
*/
@Service
@Slf4j
public class ApprovalProcessServiceImpl {

    @Resource
    private DmApprovalMapper            approvalMapper;
    @Resource
    private DmApprovalProcessMapper     processMapper;
    @Resource
    private ApprovalProviderServiceImpl approService;
    @Resource
    private RdpUserMapper               userMapper;
    @Resource
    private DmApprovalPersonMapper      approvalPersonMapper;
    @Resource
    private ApprovalProviderServiceImpl approvalProviderService;

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void createProcess(long ticketId, RdpApprovalBiz approvalBiz, boolean checkSuccess) {
        long firstStageId = -1;
        DmApprovalProcessDO lastProcessDO = null;
        DmApprovalDO approvalDO = this.approvalMapper.queryById(ticketId);

        // set approval person to APPROVAL process
        List<String> approvalPersonList = new ArrayList<>();
        List<DmApprovalPersonDO> personDOS = this.approvalPersonMapper.queryByTicketBzId(approvalDO.getBizId());
        personDOS.forEach(personDO -> {
            approvalPersonList.add(personDO.getPersonUid());
        });

        List<String> personName = new ArrayList<>();
        approvalPersonList.forEach(uid -> {
            personName.add(this.userMapper.queryByUid(uid).getUsername());
        });

        for (RdpTicketStage ticketStage : RdpTicketStage.values()) {
            if (!ticketStage.checkBiz(approvalBiz)) {
                continue;
            }
            DmApprovalProcessDO approvalProcessDO = new DmApprovalProcessDO();
            approvalProcessDO.setTicketId(ticketId);
            approvalProcessDO.setTicketStage(ticketStage);
            approvalProcessDO.setProcessStatus(RdpTicketProcessStatus.INIT);
            if (ticketStage == RdpTicketStage.APPROVAL) {
                ApprovalStageMO mo = new ApprovalStageMO();
                mo.setExecUserName(personName);
                approvalProcessDO.setStageContext(JsonUtils.toJson(mo));
            } else if (ticketStage == RdpTicketStage.EXPLAIN) {
                ApprovalStageMO execMO = new ApprovalStageMO();
                if (checkSuccess) {
                    execMO.setExecMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_RULE_CHECK_EXE.name()));
                } else {
                    execMO.setExecMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_RULE_CHECK_FAIL_EXE.name()));
                }
                execMO.setExecUserName(Collections.singletonList(this.userMapper.queryByUid(approvalDO.getOwnerUid()).getUsername()));
                approvalProcessDO.setStageContext(JsonUtils.toJson(execMO));
            }
            this.processMapper.insert(approvalProcessDO);

            // need return first process id
            if (firstStageId == -1) {
                firstStageId = approvalProcessDO.getId();
            } else {
                this.processMapper.updateById(lastProcessDO);
            }

            // refresh last
            lastProcessDO = approvalProcessDO;
        }

        if (firstStageId == -1) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_CAN_NOT_GET.name(), ticketId));
        }
    }

    public List<DmApprovalProcessDO> getProcessList(long ticketId) {
        return this.processMapper.listByTicketId(ticketId);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void cancelProcess(long ticketId, long processId) {
        DmApprovalDO ticketDO = this.approvalMapper.queryById(ticketId);
        DmApprovalProcessDO processDO = this.processMapper.queryTicketProcessById(ticketId, processId);

        // is completed.
        if (processDO.getProcessStatus() == RdpTicketProcessStatus.FINISH) {
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
                if (StringUtils.isNotBlank(processDO.getStageContext()) && ticketDO.getApproType() != RdpApprovalType.Internal) {
                    try {
                        this.approService.cancelApprovalInst(ticketDO.getId());
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
        processDO.setProcessStatus(RdpTicketProcessStatus.CLOSED);
        processDO.setFinishTime(new Date());
        this.processMapper.updateById(processDO);
    }

    public void cancelAllProcess(long ticketId) {
        List<DmApprovalProcessDO> processList = this.getProcessList(ticketId);
        for (DmApprovalProcessDO processDO : processList) {
            if (processDO.getProcessStatus() != RdpTicketProcessStatus.FINISH) {
                this.cancelProcess(ticketId, processDO.getId());
            }
        }
    }

    public void failedAllProcess(long ticketId) {
        List<DmApprovalProcessDO> processList = this.getProcessList(ticketId);
        for (DmApprovalProcessDO processDO : processList) {
            // skip finish
            if (processDO.getProcessStatus() == RdpTicketProcessStatus.FINISH) {
                continue;
            }

            // do action
            if (processDO.getTicketStage() == RdpTicketStage.APPROVAL) {
                doFailed(ticketId, processDO);
            }

            // update status
            processDO.setProcessStatus(RdpTicketProcessStatus.FAIL);
            processDO.setFinishTime(new Date());
            this.processMapper.updateById(processDO);
        }
    }

    private void doFailed(long ticketId, DmApprovalProcessDO processDO) {
        DmApprovalDO ticketDO = this.approvalMapper.queryById(ticketId);

        boolean isAllowType = StringUtils.isNotBlank(processDO.getStageContext()) && ticketDO.getApproType() != RdpApprovalType.Internal;
        boolean isEnable = this.approvalProviderService.checkEnableApproval(ticketDO.getOwnerUid(), ticketDO.getApproType().getProviderType());

        if (isAllowType && isEnable) {
            try {
                this.approService.cancelApprovalInst(ticketDO.getId());
            } catch (Exception e) {
                // fail ticket don't care third party anything error
                log.error("cancel approval instance failed", e);
            }
        }
    }
}

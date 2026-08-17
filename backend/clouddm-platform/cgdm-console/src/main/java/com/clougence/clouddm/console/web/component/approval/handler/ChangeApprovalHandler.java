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
package com.clougence.clouddm.console.web.component.approval.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalMO;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfo;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.PrimaryUserVO;
import com.clougence.clouddm.console.web.service.cicd.ChangeCascadeService;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalActivityInfo;
import com.clougence.clouddm.sdk.approval.ApprovalCreateInstanceResult;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.approval.form.ChangeForm;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeApprovalHandler implements ApprovalHandler {
    @Resource
    private ChangeFlowDal        changeFlowDal;
    @Resource
    private ExecutionDal         execDal;
    @Resource
    private AuthDal              authDal;
    @Resource
    private ApprovalDal          approvalDal;
    @Resource
    private ApprovalStateService approvalStateService;
    @Resource
    private ChangeCascadeService changeCascadeService;

    @Override
    public ApprovalBiz handleType() {
        return ApprovalBiz.DM_CHANGE;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void executeTicket(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(approvalId);
        DmExecAutoJobDO jobDO = this.execDal.autoJobMapper().queryByDependOnBizId(ticketDO.getBizId());
        if (jobDO == null) {
            return;
        }

        this.updateExecutionStatus(approvalId, jobDO.getStatus(), sender);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void runningCheck(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(approvalId);
        DmExecAutoJobDO jobDO = this.execDal.autoJobMapper().queryByDependOnBizId(ticketDO.getBizId());
        this.updateExecutionStatus(approvalId, jobDO.getStatus(), sender);
    }

    private void updateExecutionStatus(long approvalId, AutoExecJobStatus status, ImSenderService sender) {
        switch (status) {
            case FINISH -> {
                this.approvalStateService.updateProcessStatus(approvalId, ApprovalStage.EXECUTION, ApprovalProcessStatus.FINISH, null);
                this.approvalStateService.updateApprovalStatus(approvalId, ApprovalStatus.FINISHED, null);
                this.approvalCompleted(approvalId, ApprovalBiz.DM_CHANGE, sender);
            }
            case FAILED -> {
                this.approvalStateService.updateProcessStatus(approvalId, ApprovalStage.EXECUTION, ApprovalProcessStatus.FAIL, null);
                this.approvalStateService.updateApprovalStatus(approvalId, ApprovalStatus.EXEC_FAIL, null);
            }
            case PAUSE -> {
                this.approvalStateService.updateProcessStatus(approvalId, ApprovalStage.EXECUTION, ApprovalProcessStatus.PAUSE, null);
                this.approvalStateService.updateApprovalStatus(approvalId, ApprovalStatus.EXEC_PAUSE, null);
            }
            default -> {
                // Execution progress states are synchronized by execution callbacks.
            }
        }
    }

    @Override
    public List<PrimaryUserVO> queryPerson(long approvalId) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(approvalId);
        List<PrimaryUserVO> userVOS = new ArrayList<>();

        // add primary account
        DmAuthUserDO parentUserDO = this.authDal.userMapper().queryByUid(ticketDO.getPrimaryUid());
        PrimaryUserVO primaryUserVO = new PrimaryUserVO();
        primaryUserVO.setUid(ticketDO.getPrimaryUid());
        primaryUserVO.setUsername(parentUserDO.getUsername());
        userVOS.add(primaryUserVO);

        // add sub account who have auth to approval ticket and manger datasource
        List<RsAuthPersonObj> personDOS = this.authDal.userMapper().queryApproPerson(//
                AccountType.SUB_ACCOUNT, parentUserDO.getId(), ticketDO.getBindDsId(), ticketDO.getLevelPath());
        for (RsAuthPersonObj personDO : personDOS) {
            List<String> roleAuthLabels = personDO.getRoleAuthLabels();
            List<String> resAuthLabel = personDO.getResAuthLabel();
            if (CollectionUtils.isNotEmpty(roleAuthLabels) //
                && CollectionUtils.isNotEmpty(resAuthLabel) //
                && roleAuthLabels.contains(SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE) //
                && resAuthLabel.contains(SecDataAuthLabel.DM_DAUTH_TICKET)) //
            {
                PrimaryUserVO primaryUserVO2 = new PrimaryUserVO();
                primaryUserVO2.setUid(personDO.getUid());
                primaryUserVO2.setUsername(personDO.getUsername());
                userVOS.add(primaryUserVO2);
            }
        }

        return userVOS;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void createApproval(long approvalId, ImSenderService sender) {
        DmApprovalDO ticketDO = approvalDal.approvalMapper().selectByIdForUpdate(approvalId);
        if (ticketDO.getApproType() == ApprovalType.Internal) {
            return; // only external approval need to create approval instance.
        }

        ChangeForm form = convertToChangeForm(ticketDO, ticketDO.getApproTemplateIdentity());

        ApprovalCreateInstanceResult createInstance;
        try {
            ApprovalProviderSpi approvalSdkService = PluginManager.findSpi(ApprovalProviderSpi.class, ticketDO.getApproType().name());
            createInstance = approvalSdkService.createApprovalInstance(ticketDO.getPrimaryUid(), form);
        } catch (ThirdPartyApiException e) {
            this.approvalStateService.updateApprovalStatus(approvalId, ApprovalStatus.FAILED, e.getMessage());
            this.approvalFailed(approvalId, ticketDO.getApproBiz(), sender);
            return;
        }

        for (ApprovalActivityInfo activity : createInstance.getActivityList()) {
            this.approvalStateService
                .initializeActivity(ticketDO.getId(), ApprovalStage.APPROVAL, activity.getActivityId(), activity.getActivityName(), activity.getOrder(), null, null);
        }

        String url = null;
        if (createInstance.getApprovalUrl() != null) {
            url = JsonUtils.toJson(createInstance.getApprovalUrl());
        }
        approvalDal.approvalMapper().updateThirdApprovalInfo(ticketDO.getId(), createInstance.getApprovalIdentity(), url);
    }

    @Override
    public void approvalCompleted(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.updateChange(approvalId, ChangeStep.FINISH, ChangeStatus.READY, sender, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_TICKET_FINISH_MESSAGE.name(), locale, change.getChangeName());
        });
    }

    @Override
    public void approvalApproved(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.approvalStateService.updateApprovalStatus(approvalId, ApprovalStatus.WAIT_CONFIRM, null);
    }

    @Override
    public void approvalRejected(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.updateChange(approvalId, ChangeStep.APPROVAL, ChangeStatus.FAILED, sender, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_TICKET_REFUSE_MESSAGE.name(), locale, change.getChangeName());
        });
    }

    @Override
    public void approvalFailed(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.updateChange(approvalId, ChangeStep.APPROVAL, ChangeStatus.FAILED, sender, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_TICKET_FAILED_MESSAGE.name(), locale, change.getChangeName());
        });
    }

    @Override
    public void approvalCanceled(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.updateChange(approvalId, ChangeStep.APPROVAL, ChangeStatus.FAILED, sender, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_TICKET_CANCELED_MESSAGE.name(), locale, change.getChangeName());
        });
    }

    private void updateChange(long approvalId, ChangeStep changeStep, ChangeStatus changeStatus, ImSenderService sender, ChangeMessageFunction changeMessage) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(approvalId);
        ApprovalMO info = JsonUtils.toObj(ticketDO.getTicketInfo(), ApprovalMO.class);
        if (info == null || info.getChangeOwnerUid() == null || info.getChangeId() == null) {
            return;
        }

        DmChangeDO changeDO = this.changeFlowDal.changeMapper().queryChangeById(info.getChangeId());
        List<DmChangeItemDO> changeItems = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(info.getChangeOwnerUid(), info.getChangeId(), ChangeItemType.TICKET);
        DmChangeItemDO item = changeItems.isEmpty() ? null : changeItems.get(0);
        if (item == null || StringUtils.isBlank(item.getContent())) {
            return;
        }
        ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
        if (ticketInfo == null || approvalId != ticketInfo.getTicketId()) {
            return; // maybe restart flow.
        }

        //
        String language = sender.getFlowLanguage(changeDO.getOwnerUid(), changeDO.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);
        String changeMessageStr = changeMessage.apply(ticketDO, changeDO, locale);

        // send message
        ImMessageType sendMessageAndType = null;
        int version = changeDO.getVersion();
        if (changeDO.getCurrentStep() != changeStep) {
            if (this.changeFlowDal.changeMapper().updateStepTo(changeDO.getId(), version, changeStep, changeMessageStr) != 1) {
                throw new IllegalStateException("change state changed while applying approval step");
            }
            version++;
            sendMessageAndType = ImMessageType.ChangeLife;
        }
        if (changeDO.getCurrentStatus() != changeStatus) {
            if (this.changeFlowDal.changeMapper().updateStatusTo(changeDO.getId(), version, changeStatus, changeMessageStr) != 1) {
                throw new IllegalStateException("change state changed while applying approval status");
            }
            sendMessageAndType = ImMessageType.ChangeNotice;
        }

        if (changeStatus == ChangeStatus.FAILED) {
            DmChangeDO updated = this.changeFlowDal.changeMapper().queryChangeById(changeDO.getId());
            if (this.changeFlowDal.changeMapper().lockChangeById(updated.getId(), updated.getVersion()) != 1) {
                throw new IllegalStateException("change state changed while locking terminal approval change");
            }
            this.changeCascadeService.onChangeTerminal(updated);
        }
        if (sendMessageAndType != null) {
            sender.sendMessage(changeDO.getOwnerUid(), changeDO.getRefFlowId(), sendMessageAndType, changeMessageStr);
        }
    }

    private ChangeForm convertToChangeForm(DmApprovalDO ticketDO, String templateId) {
        ApprovalMO info = JsonUtils.toObj(ticketDO.getTicketInfo(), ApprovalMO.class);
        if (info == null || info.getChangeOwnerUid() == null || info.getChangeId() == null) {
            throw new IllegalArgumentException("ticket info is null");
        }

        DmChangeDO changeDO = this.changeFlowDal.changeMapper().queryChangeById(info.getChangeId());
        DmChangeFlowDO flowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(changeDO.getOwnerUid(), changeDO.getRefFlowId());
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(ticketDO.getOwnerUid());

        ChangeForm form = new ChangeForm();
        form.setTicketUserPhone(userDO.getPhone());
        form.setTicketDesc(ticketDO.getDescription());
        form.setTicketTitle(ticketDO.getTicketTitle());
        form.setTemplateIdentity(templateId);

        form.setTargetDs(ticketDO.getTargetInfo());
        form.setExecuteSql(ticketDO.getRawSql());
        form.setFlowName(flowDO.getFlowName());
        form.setChangeName(changeDO.getChangeName());
        form.setBranch(changeDO.getChangeBranch());
        return form;
    }

    private interface ChangeMessageFunction {

        String apply(DmApprovalDO ticket, DmChangeDO change, Locale locale);
    }
}

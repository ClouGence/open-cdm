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
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalMO;
import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.component.project.model.ChangeTicketInfo;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.PrimaryUserVO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.ProjectDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.clouddm.platform.dal.model.project.*;
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
public class ProjectApprovalHandler implements ApprovalHandler {
    @Resource
    private ProjectDal  projectDal;
    @Resource
    private AuthDal     authDal;
    @Resource
    private ApprovalDal approvalDal;

    @Override
    public ApprovalBiz handleType() {
        return ApprovalBiz.DM_CHANGE;
    }

    @Override
    public void executeTicket(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }

    @Override
    public void runningCheck(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        // do nothing
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
    @Transactional(rollbackFor = Throwable.class)
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
            this.approvalDal.approvalMapper().updateStatusByEnum(approvalId, ApprovalStatus.FAILED, e.getMessage());
            this.approvalFailed(approvalId, ticketDO.getApproBiz(), sender);
            return;
        }

        List<ApprovalActivityInfo> aaList = createInstance.getActivityList();
        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(ticketDO.getId(), ApprovalStage.APPROVAL);
        List<DmApprovalProcessActivityDO> approvalProcessActivityDOS = convertToDmApprovalProcessActivityDO(aaList, processDO.getId(), ticketDO.getId());
        for (DmApprovalProcessActivityDO approvalProcessActivityDO : approvalProcessActivityDOS) {
            approvalDal.activityMapper().insert(approvalProcessActivityDO);
        }

        String url = null;
        if (createInstance.getApprovalUrl() != null) {
            url = JsonUtils.toJson(createInstance.getApprovalUrl());
        }
        approvalDal.approvalMapper().updateThirdApprovalInfo(ticketDO.getId(), createInstance.getApprovalIdentity(), url);
    }

    @Override
    public void approvalCompleted(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.approvalDal.approvalMapper().updateStatusByEnum(approvalId, ApprovalStatus.FINISHED, null);
        this.updateChange(approvalId, ProjectChangeStep.EXECUTE, ProjectChangeStatus.READY, sender, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_TICKET_FINISH_MESSAGE.name(), locale, change.getChangeName());
        });
    }

    @Override
    public void approvalRefuse(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.updateChange(approvalId, ProjectChangeStep.APPROVAL, ProjectChangeStatus.FAILED, sender, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_TICKET_REFUSE_MESSAGE.name(), locale, change.getChangeName());
        });
    }

    @Override
    public void approvalFailed(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.updateChange(approvalId, ProjectChangeStep.APPROVAL, ProjectChangeStatus.FAILED, sender, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_TICKET_FAILED_MESSAGE.name(), locale, change.getChangeName());
        });
    }

    @Override
    public void approvalCanceled(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.updateChange(approvalId, ProjectChangeStep.APPROVAL, ProjectChangeStatus.FAILED, sender, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_TICKET_CANCELED_MESSAGE.name(), locale, change.getChangeName());
        });
    }

    private void updateChange(long approvalId, ProjectChangeStep changeStep, ProjectChangeStatus changeStatus, ImSenderService sender, ChangeMessageFunction changeMessage) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(approvalId);
        ApprovalMO info = JsonUtils.toObj(ticketDO.getTicketInfo(), ApprovalMO.class);
        if (info == null || info.getChangeOwnerUid() == null || info.getChangeId() == null) {
            return;
        }

        DmProjectChangeDO changeDO = this.projectDal.changeMapper().queryChangeById(info.getChangeOwnerUid(), info.getChangeId());
        List<DmProjectChangeItemDO> changeItems = this.projectDal.changeItemMapper().queryChangeItemByChangeId(info.getChangeOwnerUid(), info.getChangeId(), ChangeItemType.TICKET);
        DmProjectChangeItemDO item = changeItems.isEmpty() ? null : changeItems.get(0);
        if (item == null || StringUtils.isBlank(item.getContent())) {
            return;
        }
        ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
        if (ticketInfo == null || approvalId != ticketInfo.getTicketId()) {
            return; // maybe restart flow.
        }

        //
        String language = sender.getProjectLanguage(changeDO.getOwnerUid(), changeDO.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);
        String changeMessageStr = changeMessage.apply(ticketDO, changeDO, locale);

        // send message
        ImMessageType sendMessageAndType = null;
        int version = changeDO.getVersion();
        if (changeDO.getCurrentStep() != changeStep) {
            int res1 = this.projectDal.changeMapper().updateStepTo(changeDO.getId(), version, changeStep, changeMessageStr);
            version++;
            sendMessageAndType = ImMessageType.ChangeLife;
        }
        if (changeDO.getCurrentStatus() != changeStatus) {
            int res2 = this.projectDal.changeMapper().updateStatusTo(changeDO.getId(), version, changeStatus, changeMessageStr);
            sendMessageAndType = ImMessageType.ChangeNotice;
        }

        //
        if (sendMessageAndType != null) {
            sender.sendMessage(changeDO.getOwnerUid(), changeDO.getRefProjectId(), sendMessageAndType, changeMessageStr);
        }
    }

    private List<DmApprovalProcessActivityDO> convertToDmApprovalProcessActivityDO(List<ApprovalActivityInfo> activityDTOList, Long processId, long approvalId) {
        List<DmApprovalProcessActivityDO> result = new ArrayList<>();
        for (ApprovalActivityInfo aaObj : activityDTOList) {
            DmApprovalProcessActivityDO activityDO = new DmApprovalProcessActivityDO();
            activityDO.setActivityId(aaObj.getActivityId());
            // activityDO.setActivityType(approvalActivity.getApprovalMethod());
            // activityDO.setActivityStatus(RdpTicketProcessActivityStatus.NEW);
            activityDO.setActivityTitle(aaObj.getActivityName());
            activityDO.setProcessId(processId);
            activityDO.setTicketId(approvalId);
            activityDO.setOrderNumber(aaObj.getOrder());
            result.add(activityDO);
        }
        return result;
    }

    private ChangeForm convertToChangeForm(DmApprovalDO ticketDO, String templateId) {
        ApprovalMO info = JsonUtils.toObj(ticketDO.getTicketInfo(), ApprovalMO.class);
        if (info == null || info.getChangeOwnerUid() == null || info.getChangeId() == null) {
            throw new IllegalArgumentException("ticket info is null");
        }

        DmProjectChangeDO changeDO = this.projectDal.changeMapper().queryChangeById(info.getChangeOwnerUid(), info.getChangeId());
        DmProjectDO projectDO = this.projectDal.projectMapper().queryByOwnerAndId(changeDO.getOwnerUid(), changeDO.getRefProjectId());
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(ticketDO.getOwnerUid());

        ChangeForm form = new ChangeForm();
        form.setTicketUserPhone(userDO.getPhone());
        form.setTicketDesc(ticketDO.getDescription());
        form.setTicketTitle(ticketDO.getTicketTitle());
        form.setTemplateIdentity(templateId);

        form.setTargetDs(ticketDO.getTargetInfo());
        form.setExecuteSql(ticketDO.getRawSql());
        form.setProjectName(projectDO.getProjectName());
        form.setChangeName(changeDO.getChangeName());
        form.setBranch(changeDO.getChangeBranch());
        return form;
    }

    private interface ChangeMessageFunction {

        String apply(DmApprovalDO ticket, DmProjectChangeDO change, Locale locale);
    }
}

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

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.ticket.ApplyAuth;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpAddAuthTicketFO;
import com.clougence.clouddm.console.web.model.vo.PrimaryUserVO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthApprovalDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalActivityInfo;
import com.clougence.clouddm.sdk.approval.ApprovalCreateInstanceResult;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.approval.form.AuthForm;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthApprovalHandler implements ApprovalHandler {

    @Resource
    private AuthDal                authDal;
    @Resource
    private ApprovalDal            approvalDal;
    @Resource
    private DmAuthServiceForManage authServiceForManage;

    @Override
    public ApprovalBiz handleType() {
        return ApprovalBiz.DATA_SOURCE_AUTH;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void executeTicket(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        DmApprovalDO ticketDO = approvalDal.approvalMapper().selectByIdForUpdate(approvalId);
        // Already executed by other consoles
        if (ticketDO.getTicketStatus() != ApprovalStatus.WAIT_EXEC) {
            log.info("Ticket status is " + ticketDO.getTicketStatus());
            return;
        }

        DmAuthApprovalDO authTicketInfo = authDal.approvalMapper().getAuthTicketInfo(ticketDO.getBizId());
        RdpAddAuthTicketFO fo = JsonUtils.toList(authTicketInfo.getApplyAuthInfo(), new TypeReference<>() {});

        this.authServiceForManage.appendUserAuth(ticketDO.getOwnerUid(), fo);

        DmApprovalProcessDO processExec = this.approvalDal.processMapper().queryByStage(approvalId, ApprovalStage.EXECUTION);

        this.approvalDal.approvalMapper().updateStatusByEnum(approvalId, ApprovalStatus.FINISHED, null);
        ApprovalStageMO mo = new ApprovalStageMO();
        mo.setAutoExecute(true);
        this.approvalDal.processMapper().updateTicketStatusByEnum(processExec.getId(), ApprovalProcessStatus.FINISH, JsonUtils.toJson(mo));
    }

    @Override
    public void runningCheck(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        // do nothing.
    }

    @Override
    public List<PrimaryUserVO> queryPerson(long approvalId) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().selectByIdForUpdate(approvalId);
        List<PrimaryUserVO> userVOS = new ArrayList<>();

        DmAuthApprovalDO authTicketInfo = this.authDal.approvalMapper().getAuthTicketInfo(ticketDO.getBizId());
        RdpAddAuthTicketFO list = JsonUtils.toList(authTicketInfo.getApplyAuthInfo(), new TypeReference<>() {});

        List<Long> idList = list.getApplyAuths().stream().map(ApplyAuth::getResId).toList();

        // add primary account
        DmAuthUserDO parentUserDO = this.authDal.userMapper().queryByUid(ticketDO.getPrimaryUid());
        PrimaryUserVO primaryUserVO = new PrimaryUserVO();
        primaryUserVO.setUid(ticketDO.getPrimaryUid());
        primaryUserVO.setUsername(parentUserDO.getUsername());
        userVOS.add(primaryUserVO);
        List<RsAuthPersonObj> personDOS = authDal.userMapper().queryAuthApproPerson(AccountType.SUB_ACCOUNT, parentUserDO.getId());

        Map<String, Set<Long>> uidToResIds = new HashMap<>();
        Map<String, String> uidToUsername = new HashMap<>();

        for (RsAuthPersonObj personDO : personDOS) {
            List<String> roleAuthLabels = personDO.getRoleAuthLabels();
            List<String> resAuthLabel = personDO.getResAuthLabel();
            if (CollectionUtils.isNotEmpty(roleAuthLabels) //
                && CollectionUtils.isNotEmpty(resAuthLabel) //
                && personDO.getResAuthLabel().contains(SecDataAuthLabel.DM_DAUTH_TICKET)//
                && personDO.getRoleAuthLabels().contains(SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE)) {
                String uid = personDO.getUid();
                uidToResIds.computeIfAbsent(uid, k -> new HashSet<>()).add(personDO.getResId());
                uidToUsername.putIfAbsent(uid, personDO.getUsername());
            }
        }

        Set<Long> targetResIds = new HashSet<>(idList);

        for (Map.Entry<String, Set<Long>> entry : uidToResIds.entrySet()) {
            if (entry.getValue().containsAll(targetResIds)) {
                PrimaryUserVO user = new PrimaryUserVO();
                user.setUid(entry.getKey());
                user.setUsername(uidToUsername.get(entry.getKey()));
                userVOS.add(user);
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

        DmAuthApprovalDO authTicketInfo = authDal.approvalMapper().getAuthTicketInfo(ticketDO.getBizId());
        RdpAddAuthTicketFO ticketFO = JsonUtils.toList(authTicketInfo.getApplyAuthInfo(), new TypeReference<>() {});

        // find plugin
        AuthForm form = getAuthForm(ticketFO, ticketDO, authTicketInfo.getKindType());
        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticketDO.getApproType().name());
        if (approvalService == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), ticketDO.getApproType()));
        }

        // create instance
        ApprovalCreateInstanceResult createInstance;
        try {
            createInstance = approvalService.createApprovalInstance(ticketDO.getPrimaryUid(), form);
        } catch (ThirdPartyApiException e) {
            // wait retry
            if (e.getErrorType() == ThirdPartyApiErrorType.CONNECTION_ERROR) {
                log.error(DmI18nUtils.getMessage(e.getMessageKey()));
                return;
            }
            throw e;
        }

        // store instance data to db.
        List<ApprovalActivityInfo> aaList = createInstance.getActivityList();
        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(ticketDO.getId(), ApprovalStage.APPROVAL);
        List<DmApprovalProcessActivityDO> approvalProcessActivityDOS = convertToDmApprovalProcessActivityDO(aaList, processDO.getId(), ticketDO.getId());
        for (DmApprovalProcessActivityDO approvalProcessActivityDO : approvalProcessActivityDOS) {
            this.approvalDal.activityMapper().insert(approvalProcessActivityDO);
        }

        ticketDO.setApproIdentity(createInstance.getApprovalIdentity());
        String url = null;
        if (createInstance.getApprovalUrl() != null) {
            url = JsonUtils.toJson(createInstance.getApprovalUrl());
        }

        this.approvalDal.approvalMapper().updateThirdApprovalInfo(approvalId, createInstance.getApprovalIdentity(), url);
    }

    private AuthForm getAuthForm(RdpAddAuthTicketFO ticketFO, DmApprovalDO ticketDO, AuthKind authKind) {
        AuthForm form = new AuthForm();
        form.setApplyAuths(new ArrayList<>());
        for (ApplyAuth applyAuth : ticketFO.getApplyAuths()) {
            AuthForm.ApplyAuth auth = new AuthForm.ApplyAuth();
            auth.setEndTime(applyAuth.getEndTime());
            auth.setStartTime(applyAuth.getStartTime());
            auth.setAuthLabels(new ArrayList<>());
            List<AuthInfo> allAuthLabel = this.authServiceForManage.getAllAuthLabel(authKind);
            List<String> authLabels = applyAuth.getAuthLabels();
            Map<String, String> collect = allAuthLabel.stream().collect(Collectors.toMap(AuthInfo::getKey, AuthInfo::getKeyI18n));

            for (String authLabel : authLabels) {
                auth.getAuthLabels().add(DmI18nUtils.getMessage(collect.get(authLabel)));
            }
            auth.setResDesc(applyAuth.getResDesc());
            auth.setResPaths(applyAuth.getResPaths());
            auth.setResInstId(applyAuth.getResInstId());
            form.getApplyAuths().add(auth);
        }
        form.setTicketTitle(ticketDO.getTicketTitle());
        form.setTicketDesc(ticketDO.getDescription());
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(ticketDO.getOwnerUid());
        form.setTicketUserPhone(userDO.getPhone());
        form.setTemplateIdentity(ticketDO.getApproTemplateIdentity());
        return form;
    }

    private List<DmApprovalProcessActivityDO> convertToDmApprovalProcessActivityDO(List<ApprovalActivityInfo> activityDTOList, Long processId, Long approvalId) {
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

    @Override
    public void approvalCompleted(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.approvalDal.approvalMapper().updateStatusByEnum(approvalId, ApprovalStatus.WAIT_EXEC, null);
    }

    @Override
    public void approvalRefuse(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }

    @Override
    public void approvalFailed(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }

    @Override
    public void approvalCanceled(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }
}

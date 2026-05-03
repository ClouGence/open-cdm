package com.clougence.rdp.component.ticket.handler;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.approval.ApprovalCreateInstanceResult;
import com.clougence.clouddm.sdk.approval.form.AuthForm;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.rdp.component.ticket.RdpTicketHelper;
import com.clougence.rdp.component.ticket.RdpTicketLifeCycle;
import com.clougence.rdp.component.ticket.model.RdpExecStageContextMO;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.controller.model.fo.ticket.ApplyAuth;
import com.clougence.rdp.controller.model.fo.ticket.RdpAddAuthTicketFO;
import com.clougence.rdp.controller.model.vo.PrimaryUserVO;
import com.clougence.rdp.dal.enumeration.*;
import com.clougence.rdp.dal.mapper.*;
import com.clougence.rdp.dal.model.*;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.service.RdpAuthServiceForManage;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RdpDsAuthTicketHelper implements RdpTicketHelper {

    @Resource
    private RdpAuthServiceForManage        rdpAuthServiceForManage;
    @Resource
    private RdpTicketMapper                rdpTicketMapper;
    @Resource
    private RdpAuthTicketMapper            rdpAuthTicketMapper;
    @Resource
    private RdpTicketProcessMapper         rdpTicketProcessMapper;
    @Resource
    private RdpTicketProcessActivityMapper activityMapper;
    @Resource
    private RdpUserMapper                  userMapper;
    @Resource
    private List<RdpTicketLifeCycle>       ticketLifeCycle;

    @Override
    public RdpApprovalBiz getHandleType() { return RdpApprovalBiz.DATA_SOURCE_AUTH; }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void executeTicket(long ticketId) {
        RdpTicketDO ticketDO = rdpTicketMapper.selectByIdForUpdate(ticketId);
        // Already executed by other consoles
        if (ticketDO.getTicketStatus() != RdpTicketStatus.WAIT_EXEC) {
            log.info("Ticket status is " + ticketDO.getTicketStatus());
            return;
        }

        RdpAuthTicketDO authTicketInfo = rdpAuthTicketMapper.getAuthTicketInfo(ticketDO.getBizId());
        RdpAddAuthTicketFO fo = JsonUtils.toList(authTicketInfo.getApplyAuthInfo(), new TypeReference<RdpAddAuthTicketFO>() {
        });

        this.rdpAuthServiceForManage.appendUserAuth(ticketDO.getOwnerUid(), fo);

        RdpTicketProcessDO processExec = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.EXECUTION);

        this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.FINISHED, null);
        RdpExecStageContextMO mo = new RdpExecStageContextMO();
        mo.setAutoExecute(true);
        this.rdpTicketProcessMapper.updateTicketStatusByEnum(processExec.getId(), RdpTicketProcessStatus.FINISH, JsonUtils.toJson(mo));
    }

    @Override
    public void runningCheck(long ticketId) {
        // do nothing.
    }

    @Override
    public List<PrimaryUserVO> queryPerson(long ticketId) {
        RdpTicketDO ticketDO = rdpTicketMapper.selectByIdForUpdate(ticketId);
        List<PrimaryUserVO> userVOS = new ArrayList<>();

        RdpAuthTicketDO authTicketInfo = this.rdpAuthTicketMapper.getAuthTicketInfo(ticketDO.getBizId());
        RdpAddAuthTicketFO list = JsonUtils.toList(authTicketInfo.getApplyAuthInfo(), new TypeReference<RdpAddAuthTicketFO>() {
        });

        List<Long> idList = list.getApplyAuths().stream().map(ApplyAuth::getResId).collect(Collectors.toList());

        // add primary account
        RdpUserDO parentUserDO = this.userMapper.queryByUid(ticketDO.getPrimaryUid());
        PrimaryUserVO primaryUserVO = new PrimaryUserVO();
        primaryUserVO.setUid(ticketDO.getPrimaryUid());
        primaryUserVO.setUsername(parentUserDO.getUsername());
        userVOS.add(primaryUserVO);
        List<RdpTicketApproPersonDO> personDOS = userMapper.queryAuthApproPerson(AccountType.SUB_ACCOUNT, parentUserDO.getId());

        Map<String, Set<Long>> uidToResIds = new HashMap<>();
        Map<String, String> uidToUsername = new HashMap<>();

        for (RdpTicketApproPersonDO personDO : personDOS) {
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
    public void createApproval(long ticketId) {
        RdpTicketDO ticketDO = rdpTicketMapper.selectByIdForUpdate(ticketId);
        if (ticketDO.getApproType() == RdpApprovalType.Internal) {
            return; // only external approval need to create approval instance.
        }

        RdpAuthTicketDO authTicketInfo = rdpAuthTicketMapper.getAuthTicketInfo(ticketDO.getBizId());
        RdpAddAuthTicketFO ticketFO = JsonUtils.toList(authTicketInfo.getApplyAuthInfo(), new TypeReference<RdpAddAuthTicketFO>() {
        });

        // find plugin
        AuthForm form = getAuthForm(ticketFO, ticketDO, authTicketInfo.getKindType());
        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticketDO.getApproType().name());
        if (approvalService == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), ticketDO.getApproType()));
        }

        // create instance
        ApprovalCreateInstanceResult createInstance;
        try {
            createInstance = approvalService.createApprovalInstance(ticketDO.getPrimaryUid(), form);
        } catch (ThirdPartyApiException e) {
            // wait retry
            if (e.getErrorType() == ThirdPartyApiErrorType.CONNECTION_ERROR) {
                log.error(RdpI18nUtils.getMessage(e.getMessageKey()));
                return;
            }
            throw e;
        }

        // store instance data to db.
        List<ApprovalActivity> approvalActivityList = createInstance.getActivityList();
        RdpTicketProcessDO processDO = this.rdpTicketProcessMapper.queryByStage(ticketDO.getId(), RdpTicketStage.APPROVAL);
        List<RdpTicketProcessActivityDO> rdpTicketProcessActivityDOS = convertToRdpTicketProcessActivityDO(approvalActivityList, processDO.getId(), ticketDO.getId());
        for (RdpTicketProcessActivityDO rdpTicketProcessActivityDO : rdpTicketProcessActivityDOS) {
            this.activityMapper.insert(rdpTicketProcessActivityDO);
        }

        ticketDO.setApproIdentity(createInstance.getApprovalIdentity());
        String url = null;
        if (createInstance.getApprovalUrl() != null) {
            url = JsonUtils.toJson(createInstance.getApprovalUrl());
        }

        this.rdpTicketMapper.updateThirdApprovalInfo(ticketId, createInstance.getApprovalIdentity(), url);
    }

    private AuthForm getAuthForm(RdpAddAuthTicketFO ticketFO, RdpTicketDO ticketDO, AuthKind authKind) {
        AuthForm form = new AuthForm();
        form.setApplyAuths(new ArrayList<>());
        for (ApplyAuth applyAuth : ticketFO.getApplyAuths()) {
            AuthForm.ApplyAuth auth = new AuthForm.ApplyAuth();
            auth.setEndTime(applyAuth.getEndTime());
            auth.setStartTime(applyAuth.getStartTime());
            auth.setAuthLabels(new ArrayList<>());
            List<AuthInfo> allAuthLabel = this.rdpAuthServiceForManage.getAllAuthLabel(authKind);
            List<String> authLabels = applyAuth.getAuthLabels();
            Map<String, String> collect = allAuthLabel.stream().collect(Collectors.toMap(AuthInfo::getKey, AuthInfo::getKeyI18n));

            for (String authLabel : authLabels) {
                auth.getAuthLabels().add(RdpI18nUtils.getMessage(collect.get(authLabel)));
            }
            auth.setResDesc(applyAuth.getResDesc());
            auth.setResPaths(applyAuth.getResPaths());
            auth.setResInstId(applyAuth.getResInstId());
            form.getApplyAuths().add(auth);
        }
        form.setTicketTitle(ticketDO.getTicketTitle());
        form.setTicketDesc(ticketDO.getDescription());
        RdpUserDO userDO = this.userMapper.queryByUid(ticketDO.getOwnerUid());
        form.setTicketUserPhone(userDO.getPhone());
        form.setTemplateIdentity(ticketDO.getApproTemplateIdentity());
        return form;
    }

    private List<RdpTicketProcessActivityDO> convertToRdpTicketProcessActivityDO(List<ApprovalActivity> activityDTOList, Long processId, Long ticketId) {
        List<RdpTicketProcessActivityDO> result = new ArrayList<>();
        for (ApprovalActivity approvalActivity : activityDTOList) {
            RdpTicketProcessActivityDO activityDO = new RdpTicketProcessActivityDO();
            activityDO.setActivityId(approvalActivity.getActivityId());
            //            activityDO.setActivityType(approvalActivity.getApprovalMethod());
            //            activityDO.setActivityStatus(RdpTicketProcessActivityStatus.NEW);
            activityDO.setActivityTitle(approvalActivity.getActivityName());
            activityDO.setProcessId(processId);
            activityDO.setTicketId(ticketId);
            activityDO.setOrderNumber(approvalActivity.getOrder());
            result.add(activityDO);
        }
        return result;
    }

    @Override
    public void approvalCompleted(long ticketId) {
        this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.WAIT_EXEC, null);

        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalCompleted(RdpApprovalBiz.DATA_SOURCE_AUTH, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for completed. failed: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void approvalRefuse(long ticketId) {
        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalRefuse(RdpApprovalBiz.DATA_SOURCE_AUTH, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for refuse. failed: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void approvalFailed(long ticketId) {
        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalFailed(RdpApprovalBiz.DATA_SOURCE_AUTH, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for failed. failed: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void approvalCanceled(long ticketId) {
        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalCanceled(RdpApprovalBiz.DATA_SOURCE_AUTH, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for canceled. failed: " + e.getMessage(), e);
            }
        }
    }
}

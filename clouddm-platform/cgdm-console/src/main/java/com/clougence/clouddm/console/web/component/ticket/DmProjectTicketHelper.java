package com.clougence.clouddm.console.web.component.ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.component.project.model.ChangeTicketInfo;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeItemType;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStatus;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectChangeStep;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectChangeItemMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectChangeMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmTicketMapper;
import com.clougence.clouddm.console.web.dal.model.DmProjectChangeDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectChangeItemDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectDO;
import com.clougence.clouddm.console.web.dal.model.DmTicketDO;
import org.springframework.transaction.annotation.Transactional;
import com.clougence.clouddm.console.web.service.ticket.model.TicketInfo;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.approval.ApprovalCreateInstanceResult;
import com.clougence.clouddm.sdk.approval.form.ChangeForm;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.rdp.component.ticket.RdpTicketHelper;
import com.clougence.rdp.component.ticket.RdpTicketLifeCycle;
import com.clougence.rdp.controller.model.vo.PrimaryUserVO;
import com.clougence.rdp.dal.enumeration.*;
import com.clougence.rdp.dal.mapper.RdpTicketMapper;
import com.clougence.rdp.dal.mapper.RdpTicketProcessActivityMapper;
import com.clougence.rdp.dal.mapper.RdpTicketProcessMapper;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DmProjectTicketHelper implements RdpTicketHelper {

    @Resource
    private RdpTicketMapper                rdpTicketMapper;
    @Resource
    private RdpTicketProcessMapper         rdpTicketProcessMapper;
    @Resource
    private RdpUserMapper                  userMapper;
    @Resource
    private RdpTicketProcessActivityMapper activityMapper;
    @Resource
    private DmTicketMapper                 dmTicketMapper;
    @Resource
    private DmProjectMapper                dmProjectMapper;
    @Resource
    private DmProjectChangeMapper          dmProjectChangeMapper;
    @Resource
    private DmProjectChangeItemMapper      dmProjectChangeItemMapper;
    @Resource
    private ImSenderService                imSenderService;
    @Resource
    private List<RdpTicketLifeCycle>       ticketLifeCycle;

    @Override
    public RdpApprovalBiz getHandleType() { return RdpApprovalBiz.DM_CHANGE; }

    @Override
    public void executeTicket(long ticketId) {
        // do nothing
    }

    @Override
    public void runningCheck(long ticketId) {
        // do nothing
    }

    @Override
    public List<PrimaryUserVO> queryPerson(long ticketId) {
        RdpTicketDO ticketDO = this.rdpTicketMapper.queryById(ticketId);
        List<PrimaryUserVO> userVOS = new ArrayList<>();
        DmTicketDO dmTicketInfo = dmTicketMapper.getDmTicketInfo(ticketDO.getBizId());

        // add primary account
        RdpUserDO parentUserDO = this.userMapper.queryByUid(ticketDO.getPrimaryUid());
        PrimaryUserVO primaryUserVO = new PrimaryUserVO();
        primaryUserVO.setUid(ticketDO.getPrimaryUid());
        primaryUserVO.setUsername(parentUserDO.getUsername());
        userVOS.add(primaryUserVO);

        // add sub account who have auth to approval ticket and manger datasource
        List<RdpTicketApproPersonDO> personDOS = this.userMapper
            .queryApproPerson(AccountType.SUB_ACCOUNT, parentUserDO.getId(), ticketDO.getBindDsId(), dmTicketInfo.getLevelPath());
        for (RdpTicketApproPersonDO personDO : personDOS) {
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
    public void createApproval(long ticketId) {
        RdpTicketDO ticketDO = rdpTicketMapper.selectByIdForUpdate(ticketId);
        if (ticketDO.getApproType() == RdpApprovalType.Internal) {
            return; // only external approval need to create approval instance.
        }

        DmTicketDO dmTicketInfo = this.dmTicketMapper.getDmTicketInfo(ticketDO.getBizId());
        ChangeForm form = convertToChangeForm(dmTicketInfo, ticketDO, ticketDO.getApproTemplateIdentity());

        ApprovalCreateInstanceResult createInstance;
        try {
            ApprovalProviderSpi approvalSdkService = PluginManager.findSpi(ApprovalProviderSpi.class, ticketDO.getApproType().name());
            createInstance = approvalSdkService.createApprovalInstance(ticketDO.getPrimaryUid(), form);
        } catch (ThirdPartyApiException e) {
            this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.FAILED, e.getMessage());
            this.approvalFailed(ticketId);
            return;
        }

        List<ApprovalActivity> approvalActivityList = createInstance.getActivityList();
        RdpTicketProcessDO processDO = this.rdpTicketProcessMapper.queryByStage(ticketDO.getId(), RdpTicketStage.APPROVAL);
        List<RdpTicketProcessActivityDO> rdpTicketProcessActivityDOS = convertToRdpTicketProcessActivityDO(approvalActivityList, processDO.getId(), ticketDO.getId());
        for (RdpTicketProcessActivityDO rdpTicketProcessActivityDO : rdpTicketProcessActivityDOS) {
            activityMapper.insert(rdpTicketProcessActivityDO);
        }

        String url = null;
        if (createInstance.getApprovalUrl() != null) {
            url = JsonUtils.toJson(createInstance.getApprovalUrl());
        }
        rdpTicketMapper.updateThirdApprovalInfo(ticketDO.getId(), createInstance.getApprovalIdentity(), url);
    }

    @Override
    public void approvalCompleted(long ticketId) {
        this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.FINISHED, null);
        this.updateChange(ticketId, ProjectChangeStep.EXECUTE, ProjectChangeStatus.READY, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_TICKET_FINISH_MESSAGE.name(), locale, change.getChangeName());
        });

        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalCompleted(RdpApprovalBiz.DM_CHANGE, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for failed. failed: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void approvalRefuse(long ticketId) {
        this.updateChange(ticketId, ProjectChangeStep.APPROVAL, ProjectChangeStatus.FAILED, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_TICKET_REFUSE_MESSAGE.name(), locale, change.getChangeName());
        });

        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalRefuse(RdpApprovalBiz.DM_CHANGE, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for failed. failed: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void approvalFailed(long ticketId) {
        this.updateChange(ticketId, ProjectChangeStep.APPROVAL, ProjectChangeStatus.FAILED, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_TICKET_FAILED_MESSAGE.name(), locale, change.getChangeName());
        });

        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalFailed(RdpApprovalBiz.DM_CHANGE, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for failed. failed: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void approvalCanceled(long ticketId) {
        this.updateChange(ticketId, ProjectChangeStep.APPROVAL, ProjectChangeStatus.FAILED, (ticket, change, locale) -> {
            return DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_TICKET_CANCELED_MESSAGE.name(), locale, change.getChangeName());
        });

        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalCanceled(RdpApprovalBiz.DM_CHANGE, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for failed. failed: " + e.getMessage(), e);
            }
        }
    }

    private void updateChange(long ticketId, ProjectChangeStep changeStep, ProjectChangeStatus changeStatus, ChangeMessageFunction changeMessage) {
        RdpTicketDO ticketDO = this.rdpTicketMapper.queryById(ticketId);
        DmTicketDO dmTicketDO = this.dmTicketMapper.getDmTicketInfo(ticketDO.getBizId());
        TicketInfo info = JsonUtils.toObj(dmTicketDO.getTicketInfo(), TicketInfo.class);
        if (info == null || info.getChangeOwnerUid() == null || info.getChangeId() == null) {
            return;
        }

        DmProjectChangeDO changeDO = this.dmProjectChangeMapper.queryChangeById(info.getChangeOwnerUid(), info.getChangeId());
        List<DmProjectChangeItemDO> changeItems = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(info.getChangeOwnerUid(), info.getChangeId(), DmChangeItemType.TICKET);
        DmProjectChangeItemDO item = changeItems.isEmpty() ? null : changeItems.get(0);
        if (item == null || StringUtils.isBlank(item.getContent())) {
            return;
        }
        ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
        if (ticketInfo == null || ticketId != ticketInfo.getTicketId()) {
            return; // maybe restart flow.
        }

        //
        String language = this.imSenderService.getProjectLanguage(changeDO.getOwnerUid(), changeDO.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);
        String changeMessageStr = changeMessage.apply(ticketDO, changeDO, locale);

        // send message
        ImMessageType sendMessageAndType = null;
        int version = changeDO.getVersion();
        if (changeDO.getCurrentStep() != changeStep) {
            int res1 = this.dmProjectChangeMapper.updateStepTo(changeDO.getId(), version, changeStep, changeMessageStr);
            version++;
            sendMessageAndType = ImMessageType.ChangeLife;
        }
        if (changeDO.getCurrentStatus() != changeStatus) {
            int res2 = this.dmProjectChangeMapper.updateStatusTo(changeDO.getId(), version, changeStatus, changeMessageStr);
            sendMessageAndType = ImMessageType.ChangeNotice;
        }

        //
        if (sendMessageAndType != null) {
            this.imSenderService.sendMessage(changeDO.getOwnerUid(), changeDO.getRefProjectId(), sendMessageAndType, changeMessageStr);
        }
    }

    private List<RdpTicketProcessActivityDO> convertToRdpTicketProcessActivityDO(List<ApprovalActivity> activityDTOList, Long processId, long ticketId) {
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

    private ChangeForm convertToChangeForm(DmTicketDO dmTicketDO, RdpTicketDO ticketDO, String templateId) {
        TicketInfo info = JsonUtils.toObj(dmTicketDO.getTicketInfo(), TicketInfo.class);
        if (info == null || info.getChangeOwnerUid() == null || info.getChangeId() == null) {
            throw new IllegalArgumentException("ticket info is null");
        }

        DmProjectChangeDO changeDO = this.dmProjectChangeMapper.queryChangeById(info.getChangeOwnerUid(), info.getChangeId());
        DmProjectDO projectDO = this.dmProjectMapper.queryByOwnerAndId(changeDO.getOwnerUid(), changeDO.getRefProjectId());
        RdpUserDO userDO = this.userMapper.queryByUid(ticketDO.getOwnerUid());

        ChangeForm form = new ChangeForm();
        form.setTicketUserPhone(userDO.getPhone());
        form.setTicketDesc(ticketDO.getDescription());
        form.setTicketTitle(ticketDO.getTicketTitle());
        form.setTemplateIdentity(templateId);

        form.setTargetDs(ticketDO.getTargetInfo());
        form.setExecuteSql(dmTicketDO.getRawSql());
        form.setProjectName(projectDO.getProjectName());
        form.setChangeName(changeDO.getChangeName());
        form.setBranch(changeDO.getChangeBranch());
        return form;
    }

    private interface ChangeMessageFunction {

        String apply(RdpTicketDO ticket, DmProjectChangeDO change, Locale locale);
    }
}

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
package com.clougence.clouddm.console.web.component.ticket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.dal.enumeration.AutoExecJobStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecJobMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmTicketMapper;
import com.clougence.clouddm.console.web.dal.model.DmTicketDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;
import com.clougence.clouddm.console.web.model.vo.PrimaryUserVO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.approval.ApprovalCreateInstanceResult;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.approval.form.QueryForm;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.rdp.component.ticket.RdpTicketHelper;
import com.clougence.rdp.component.ticket.RdpTicketLifeCycle;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.RdpTicketMapper;
import com.clougence.clouddm.console.web.dal.mapper.RdpTicketProcessActivityMapper;
import com.clougence.clouddm.console.web.dal.mapper.RdpTicketProcessMapper;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DmQueryTicketHelper implements RdpTicketHelper {

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
    private DmAutoExecJobMapper            dmAutoExecJobMapper;
    @Resource
    private List<RdpTicketLifeCycle>       ticketLifeCycle;

    @Override
    public RdpApprovalBiz getHandleType() { return RdpApprovalBiz.DM_QUERY; }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void executeTicket(long ticketId) {
        RdpTicketDO ticketDO = this.rdpTicketMapper.queryById(ticketId);
        DmAutoExecJobDO jobDO = this.dmAutoExecJobMapper.queryByDependOnBizId(ticketDO.getBizId());
        if (jobDO == null) {
            return;
        }

        AutoExecJobStatus status = jobDO.getStatus();
        if (status == AutoExecJobStatus.EXECUTING) {
            rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.RUNNING, null);
        } else if (status == AutoExecJobStatus.WAIT_EXEC || status == AutoExecJobStatus.INIT) {
        } else {
            runningCheck(ticketId, status);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void runningCheck(long ticketId) {
        RdpTicketDO ticketDO = this.rdpTicketMapper.queryById(ticketId);
        DmAutoExecJobDO jobDO = this.dmAutoExecJobMapper.queryByDependOnBizId(ticketDO.getBizId());
        AutoExecJobStatus status = jobDO.getStatus();
        runningCheck(ticketId, status);
    }

    private void runningCheck(long ticketId, AutoExecJobStatus status) {
        if (status == AutoExecJobStatus.FINISH) {
            rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.FINISHED, null);
            rdpTicketProcessMapper.updateProcessStatusByTicketIdAndStage(ticketId, RdpTicketStage.EXECUTION, RdpTicketProcessStatus.FINISH);
        } else if (status == AutoExecJobStatus.FAILED) {
            rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.EXEC_FAIL, null);
            rdpTicketProcessMapper.updateProcessStatusByTicketIdAndStage(ticketId, RdpTicketStage.EXECUTION, RdpTicketProcessStatus.FAIL);
        } else if (status == AutoExecJobStatus.PAUSE) {
            rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.EXEC_PAUSE, null);
            rdpTicketProcessMapper.updateProcessStatusByTicketIdAndStage(ticketId, RdpTicketStage.EXECUTION, RdpTicketProcessStatus.PAUSE);
        }
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
        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticketDO.getApproType().name());
        QueryForm form = convertToQueryForm(dmTicketInfo, ticketDO, ticketDO.getApproTemplateIdentity());

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
        rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.WAIT_CONFIRM, null);

        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalCompleted(RdpApprovalBiz.DM_QUERY, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for completed. failed: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void approvalRefuse(long ticketId) {
        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalRefuse(RdpApprovalBiz.DM_QUERY, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for refuse. failed: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void approvalFailed(long ticketId) {
        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalFailed(RdpApprovalBiz.DM_QUERY, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for failed. failed: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void approvalCanceled(long ticketId) {
        for (RdpTicketLifeCycle lifeCycle : this.ticketLifeCycle) {
            try {
                lifeCycle.approvalCanceled(RdpApprovalBiz.DM_QUERY, ticketId);
            } catch (Exception e) {
                log.error("notifyApproval for canceled. failed: " + e.getMessage(), e);
            }
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

    private QueryForm convertToQueryForm(DmTicketDO dmTicketDO, RdpTicketDO rdpTicketDO, String templateId) {
        QueryForm form = new QueryForm();

        RdpUserDO userDO = this.userMapper.queryByUid(rdpTicketDO.getOwnerUid());
        form.setTicketUserPhone(userDO.getPhone());
        form.setExecuteSql(dmTicketDO.getRawSql());
        form.setRollBackSql(dmTicketDO.getRollBackSql());
        form.setAffectCount(dmTicketDO.getExpectedAffectedRows());
        form.setTargetDs(rdpTicketDO.getTargetInfo());
        form.setTicketDesc(rdpTicketDO.getDescription());
        form.setTicketTitle(rdpTicketDO.getTicketTitle());
        form.setTemplateIdentity(templateId);

        return form;
    }
}

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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.*;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;
import com.clougence.clouddm.console.web.model.vo.PrimaryUserVO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalActivityInfo;
import com.clougence.clouddm.sdk.approval.ApprovalCreateInstanceResult;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.approval.form.QueryForm;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QueryApprovalHandler implements ApprovalHandler {

    @Resource
    private DmApprovalMapper                approvalMapper;
    @Resource
    private DmApprovalProcessMapper         approvalProcessMapper;
    @Resource
    private RdpUserMapper                   userMapper;
    @Resource
    private DmApprovalProcessActivityMapper activityMapper;
    @Resource
    private DmAutoExecJobMapper             dmAutoExecJobMapper;

    @Override
    public RdpApprovalBiz handleType() {
        return RdpApprovalBiz.DM_QUERY;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void executeTicket(long approvalId, RdpApprovalBiz bizType, ImSenderService sender) {
        DmApprovalDO ticketDO = this.approvalMapper.queryById(approvalId);
        DmAutoExecJobDO jobDO = this.dmAutoExecJobMapper.queryByDependOnBizId(ticketDO.getBizId());
        if (jobDO == null) {
            return;
        }

        AutoExecJobStatus status = jobDO.getStatus();
        if (status == AutoExecJobStatus.EXECUTING) {
            approvalMapper.updateTicketStatusByEnum(approvalId, RdpTicketStatus.RUNNING, null);
        } else if (status == AutoExecJobStatus.WAIT_EXEC || status == AutoExecJobStatus.INIT) {
        } else {
            runningCheck(approvalId, status);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void runningCheck(long approvalId, RdpApprovalBiz bizType, ImSenderService sender) {
        DmApprovalDO ticketDO = this.approvalMapper.queryById(approvalId);
        DmAutoExecJobDO jobDO = this.dmAutoExecJobMapper.queryByDependOnBizId(ticketDO.getBizId());
        AutoExecJobStatus status = jobDO.getStatus();
        runningCheck(approvalId, status);
    }

    private void runningCheck(long ticketId, AutoExecJobStatus status) {
        if (status == AutoExecJobStatus.FINISH) {
            approvalMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.FINISHED, null);
            approvalProcessMapper.updateProcessStatusByTicketIdAndStage(ticketId, RdpTicketStage.EXECUTION, RdpTicketProcessStatus.FINISH);
        } else if (status == AutoExecJobStatus.FAILED) {
            approvalMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.EXEC_FAIL, null);
            approvalProcessMapper.updateProcessStatusByTicketIdAndStage(ticketId, RdpTicketStage.EXECUTION, RdpTicketProcessStatus.FAIL);
        } else if (status == AutoExecJobStatus.PAUSE) {
            approvalMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.EXEC_PAUSE, null);
            approvalProcessMapper.updateProcessStatusByTicketIdAndStage(ticketId, RdpTicketStage.EXECUTION, RdpTicketProcessStatus.PAUSE);
        }
    }

    @Override
    public List<PrimaryUserVO> queryPerson(long approvalId) {
        DmApprovalDO ticketDO = this.approvalMapper.queryById(approvalId);
        List<PrimaryUserVO> userVOS = new ArrayList<>();

        // add primary account
        RdpUserDO parentUserDO = this.userMapper.queryByUid(ticketDO.getPrimaryUid());
        PrimaryUserVO primaryUserVO = new PrimaryUserVO();
        primaryUserVO.setUid(ticketDO.getPrimaryUid());
        primaryUserVO.setUsername(parentUserDO.getUsername());
        userVOS.add(primaryUserVO);

        // add sub account who have auth to approval ticket and manger datasource
        List<RdpTicketApproPersonDO> personDOS = this.userMapper.queryApproPerson(AccountType.SUB_ACCOUNT, parentUserDO.getId(), ticketDO.getBindDsId(), ticketDO.getLevelPath());
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
    public void createApproval(long approvalId, ImSenderService sender) {
        DmApprovalDO ticketDO = approvalMapper.selectByIdForUpdate(approvalId);
        if (ticketDO.getApproType() == RdpApprovalType.Internal) {
            return; // only external approval need to create approval instance.
        }

        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticketDO.getApproType().name());
        QueryForm form = convertToQueryForm(ticketDO, ticketDO.getApproTemplateIdentity());

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

        List<ApprovalActivityInfo> aaObj = createInstance.getActivityList();
        DmApprovalProcessDO processDO = this.approvalProcessMapper.queryByStage(ticketDO.getId(), RdpTicketStage.APPROVAL);
        List<DmApprovalProcessActivityDO> approvalProcessActivityDOS = convertToDmApprovalProcessActivityDO(aaObj, processDO.getId(), ticketDO.getId());
        for (DmApprovalProcessActivityDO approvalProcessActivityDO : approvalProcessActivityDOS) {
            activityMapper.insert(approvalProcessActivityDO);
        }
        String url = null;
        if (createInstance.getApprovalUrl() != null) {
            url = JsonUtils.toJson(createInstance.getApprovalUrl());
        }
        approvalMapper.updateThirdApprovalInfo(ticketDO.getId(), createInstance.getApprovalIdentity(), url);
    }

    @Override
    public void approvalCompleted(long approvalId, RdpApprovalBiz bizType, ImSenderService sender) {
        approvalMapper.updateTicketStatusByEnum(approvalId, RdpTicketStatus.WAIT_CONFIRM, null);
    }

    @Override
    public void approvalRefuse(long approvalId, RdpApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }

    @Override
    public void approvalFailed(long approvalId, RdpApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }

    @Override
    public void approvalCanceled(long approvalId, RdpApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }

    private List<DmApprovalProcessActivityDO> convertToDmApprovalProcessActivityDO(List<ApprovalActivityInfo> activityDTOList, Long processId, long approvalId) {
        List<DmApprovalProcessActivityDO> result = new ArrayList<>();
        for (ApprovalActivityInfo aaObj : activityDTOList) {
            DmApprovalProcessActivityDO activityDO = new DmApprovalProcessActivityDO();
            activityDO.setActivityId(aaObj.getActivityId());
            //            activityDO.setActivityType(approvalActivity.getApprovalMethod());
            //            activityDO.setActivityStatus(RdpTicketProcessActivityStatus.NEW);
            activityDO.setActivityTitle(aaObj.getActivityName());
            activityDO.setProcessId(processId);
            activityDO.setTicketId(approvalId);
            activityDO.setOrderNumber(aaObj.getOrder());
            result.add(activityDO);
        }
        return result;
    }

    private QueryForm convertToQueryForm(DmApprovalDO approvalDO, String templateId) {
        QueryForm form = new QueryForm();

        RdpUserDO userDO = this.userMapper.queryByUid(approvalDO.getOwnerUid());
        form.setTicketUserPhone(userDO.getPhone());
        form.setExecuteSql(approvalDO.getRawSql());
        form.setRollBackSql(approvalDO.getRollBackSql());
        form.setAffectCount(approvalDO.getExpectedAffectedRows());
        form.setTargetDs(approvalDO.getTargetInfo());
        form.setTicketDesc(approvalDO.getDescription());
        form.setTicketTitle(approvalDO.getTicketTitle());
        form.setTemplateIdentity(templateId);

        return form;
    }
}

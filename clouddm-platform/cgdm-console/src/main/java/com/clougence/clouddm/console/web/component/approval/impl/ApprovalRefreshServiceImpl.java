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
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.dal.enumeration.RdpTicketStage;
import com.clougence.clouddm.console.web.dal.mapper.DmApprovalMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmApprovalProcessActivityMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmApprovalProcessMapper;
import com.clougence.clouddm.console.web.dal.model.DmApprovalDO;
import com.clougence.clouddm.console.web.dal.model.DmApprovalProcessActivityDO;
import com.clougence.clouddm.console.web.dal.model.DmApprovalProcessDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.approval.ApprovalUserInfo;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivityStatus;
import com.clougence.clouddm.sdk.service.approval.ApprovalIdentity;
import com.clougence.clouddm.sdk.service.approval.ApprovalRefreshService;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApprovalRefreshServiceImpl implements ApprovalRefreshService {

    @Resource
    private DmApprovalMapper                approvalMapper;
    @Resource
    private DmApprovalProcessMapper         approvalProcessMapper;
    @Resource
    private DmApprovalProcessActivityMapper activityMapper;
    @Resource
    private ApprovalProviderServiceImpl     approvalProviderService;

    @Override
    @SneakyThrows
    public void refreshTicket(ApprovalIdentity callback) {
        DmApprovalDO approvalDO = approvalMapper.queryByApproIdentity(callback.getApproIdentity(), callback.getProviderType(), callback.getOwnerUid());
        if (approvalDO == null) {
            log.error("Callback event not find ticket for approval instance: {} and type: {} and puid: {}", //
                    callback.getApproIdentity(), callback.getProviderType(), callback.getOwnerUid());
            return;
        }
        approvalProviderService.refreshApprovalStatus(approvalDO.getId());
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void updateActivity(ApprovalActivity activity) {
        DmApprovalDO approvalDO = approvalMapper.queryByApproIdentity(activity.getApprovalIdentity(), activity.getPlatform(), activity.getPuid());
        // avoid receive another callback
        if (approvalDO == null) {
            log.error("Callback event not find ticket for approval instance: {} and type: {} and puid: {}", //
                    activity.getApprovalIdentity(), activity.getPlatform(), activity.getPuid());
            return;
        }
        DmApprovalProcessDO processDO = this.approvalProcessMapper.queryByStage(approvalDO.getId(), RdpTicketStage.APPROVAL);
        DmApprovalProcessActivityDO activityDO = activityMapper.queryByProcessIdAndActivityIdForUpdate(processDO.getId(), activity.getActivityId());

        String context = activityDO.getContext();
        List<ApprovalActivity> list;
        if (StringUtils.isEmpty(context)) {
            list = new ArrayList<>();
        } else {
            list = JsonUtils.toList(context, new TypeReference<>() {});
        }

        ApprovalActivity originTask = null;
        for (ApprovalActivity approvalTask : list) {
            if (approvalTask.getTaskId().equals(activity.getTaskId())) {
                originTask = approvalTask;
                break;
            }
        }
        if (originTask == null && activity.getStatus() != ApprovalActivityStatus.CLOSE) {
            if (StringUtils.isEmpty(activity.getUserName())) {
                ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, approvalDO.getApproType().name());
                ApprovalUserInfo userInfo = service.getUserDetailByUid(approvalDO.getPrimaryUid(), activity.getUserId());
                activity.setUserName(userInfo.getUsername());
            }
            list.add(activity);
        } else if (ApprovalActivityStatus.canUpdate(originTask.getStatus(), activity.getStatus())) {
            if (activity.getStatus() == ApprovalActivityStatus.CLOSE) {
                list.remove(originTask);
            } else {
                originTask.setStatus(activity.getStatus());
                originTask.setRemark(activity.getRemark());
                originTask.setFinishTime(activity.getFinishTime());
            }
        }

        String json = JsonUtils.toJson(list);

        activityMapper.updateContext(processDO.getId(), activityDO.getActivityId(), json);

    }
}

package com.clougence.rdp.component.ticket.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.UserDetail;
import com.clougence.rdp.component.ticket.RdpApprovalService;
import com.clougence.rdp.dal.enumeration.RdpTicketStage;
import com.clougence.rdp.dal.mapper.RdpTicketMapper;
import com.clougence.rdp.dal.mapper.RdpTicketProcessActivityMapper;
import com.clougence.rdp.dal.mapper.RdpTicketProcessMapper;
import com.clougence.rdp.dal.model.RdpTicketDO;
import com.clougence.rdp.dal.model.RdpTicketProcessActivityDO;
import com.clougence.rdp.dal.model.RdpTicketProcessDO;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalActivityInfo;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalActivityStatus;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalConsoleService;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalTicketInfo;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RdpApprovalConsoleServiceImpl implements RdpApprovalConsoleService {

    @Resource
    private RdpTicketMapper                rdpTicketMapper;
    @Resource
    private RdpTicketProcessMapper         rdpTicketProcessMapper;
    @Resource
    private RdpTicketProcessActivityMapper activityMapper;
    @Resource
    private RdpApprovalService             rdpApproService;

    @Override
    @SneakyThrows
    public void refreshTicket(RdpApprovalTicketInfo callback) {
        RdpTicketDO rdpTicketDO = rdpTicketMapper.queryByApproIdentity(callback.getApproIdentity(), callback.getProviderType(), callback.getOwnerUid());
        if (rdpTicketDO == null) {
            log.error("Callback event not find ticket for approval instance: {} and type: {} and puid: {}", callback.getApproIdentity(), callback.getProviderType(), callback
                .getOwnerUid());
            return;
        }
        rdpApproService.refreshApprovalStatus(rdpTicketDO.getId());
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void updateActivity(RdpApprovalActivityInfo activity) {
        RdpTicketDO rdpTicketDO = rdpTicketMapper.queryByApproIdentity(activity.getApprovalIdentity(), activity.getPlatform(), activity.getPuid());
        // avoid receive another callback
        if (rdpTicketDO == null) {
            log.error("Callback event not find ticket for approval instance: {} and type: {} and puid: {}", activity.getApprovalIdentity(), activity.getPlatform(), activity
                .getPuid());
            return;
        }
        RdpTicketProcessDO processDO = this.rdpTicketProcessMapper.queryByStage(rdpTicketDO.getId(), RdpTicketStage.APPROVAL);
        RdpTicketProcessActivityDO activityDO = activityMapper.queryByProcessIdAndActivityIdForUpdate(processDO.getId(), activity.getActivityId());

        String context = activityDO.getContext();
        List<RdpApprovalActivityInfo> list;
        if (StringUtils.isEmpty(context)) {
            list = new ArrayList<>();
        } else {
            list = JsonUtils.toList(context, new TypeReference<List<RdpApprovalActivityInfo>>() {
            });
        }

        RdpApprovalActivityInfo originTask = null;
        for (RdpApprovalActivityInfo approvalTask : list) {
            if (approvalTask.getTaskId().equals(activity.getTaskId())) {
                originTask = approvalTask;
                break;
            }
        }
        if (originTask == null && activity.getStatus() != RdpApprovalActivityStatus.CLOSE) {
            if (StringUtils.isEmpty(activity.getUserName())) {
                ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, rdpTicketDO.getApproType().name());
                UserDetail userDetail = service.getUserDetailByUid(rdpTicketDO.getPrimaryUid(), activity.getUserId());
                activity.setUserName(userDetail.getUsername());
            }
            list.add(activity);
        } else if (RdpApprovalActivityStatus.canUpdate(originTask.getStatus(), activity.getStatus())) {
            if (activity.getStatus() == RdpApprovalActivityStatus.CLOSE) {
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

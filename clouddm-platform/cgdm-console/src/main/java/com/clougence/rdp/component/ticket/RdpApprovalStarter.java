package com.clougence.rdp.component.ticket;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RdpApprovalStarter implements UnifiedPostConstruct {

    @Resource
    private RdpUserMapper           rdpUserMapper;
    @Resource
    private RdpApprovalService      approvalService;
    @Resource
    private RdpApprovalTaskSchedule scheduleService;

    @Override
    public void init() throws Exception {
        ThreadUtils.runDaemonThread(this::initApprovalProvider);
    }

    @Override
    public void stop() {

    }

    private void initApprovalProvider() {
        List<String> serviceNames;
        try {
            serviceNames = PluginManager.getSpiNamesByType(ApprovalProviderSpi.class);
            if (CollectionUtils.isNotEmpty(serviceNames)) {
                log.info("[RdpApprovalStarter] ScheduleService found " + serviceNames.size() + " provider is " + StringUtils.join(serviceNames.toArray(), ","));
            } else {
                String msg = "[RdpApprovalStarter] ScheduleService not found any provider.";
                log.error(msg);
                return;
            }
        } catch (Exception e) {
            String msg = "[RdpApprovalStarter] ScheduleService found provider error " + e.getMessage();
            log.error(msg, e);
            return;
        }

        // start plugin for user.
        List<RdpUserDO> users = this.rdpUserMapper.listPrimaryAccount();
        for (RdpUserDO rdpUserDO : users) {
            for (String serviceName : serviceNames) {
                ApprovalProvider providerType = ApprovalProvider.valueOf(serviceName);

                if (!this.approvalService.checkEnableApproval(rdpUserDO.getUid(), providerType)) {
                    continue;
                }

                ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, serviceName);
                try {
                    service.start(rdpUserDO.getUid(), new LifeSpiRequest());
                } catch (Exception e) {
                    log.error("[RdpApprovalStarter] Switch " + serviceName + "client was failed", e);
                }
            }
        }

        // scheduleService start
        try {
            this.scheduleService.start();
            log.info("[RdpApprovalStarter] ScheduleService started.");
        } catch (Exception e) {
            String msg = "[RdpApprovalStarter] ScheduleService started failed, but will ignore exception, msg: " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
        }
    }
}

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
package com.clougence.clouddm.console.web.component.approval.schedule;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.model.UserConfigMO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApprovalStarter implements UnifiedPostConstruct, RdpNotifyService {
    @Resource
    private AuthDal                     authDal;
    @Resource
    private ApprovalProviderServiceImpl approvalService;
    @Resource
    private ApprovalTaskSchedule        scheduleService;

    @Override
    public void init() throws Exception {
        ThreadUtils.runDaemonThread(this::initApprovalProvider);
    }

    @Override
    public void stop() {

    }

    private void initApprovalProvider() {
        List<DmAuthUserDO> users = this.authDal.userMapper().listPrimaryAccount();
        for (DmAuthUserDO rdpUserDO : users) {
            for (ApprovalProvider provider : ApprovalFlowService.SUPPORT_LIST) {
                startProviderIfEnabled(rdpUserDO.getUid(), provider);
            }
        }

        // scheduleService start
        try {
            this.scheduleService.start();
            log.info("[ApprovalStarter] ScheduleService started.");
        } catch (Exception e) {
            String msg = "[ApprovalStarter] ScheduleService started failed, but will ignore exception, msg: " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
        }
    }

    @SneakyThrows
    @Override
    public void notifyUserConfig(String ownerUid, List<UserConfigMO> configList) {
        if (configList == null || configList.isEmpty()) {
            return;
        }

        restartModifiedProviders(ownerUid, configList);
    }

    private void restartModifiedProviders(String ownerUid, List<UserConfigMO> configList) throws Exception {
        Set<UserConfigTagType> filtered = configList.stream()
            .filter(config -> config != null && config.getTagType() != null && (config.isInsert() || config.isUpdate() || config.isDelete()))
            .map(UserConfigMO::getTagType)
            .collect(Collectors.toSet());

        for (ApprovalProvider provider : ApprovalFlowService.SUPPORT_LIST) {
            UserConfigTagType tagType = convertToConfigTagType(provider);
            if (tagType == null || !filtered.contains(tagType)) {
                continue;
            }
            restartProvider(ownerUid, provider);
        }
    }

    private void restartProvider(String ownerUid, ApprovalProvider provider) throws Exception {
        stopProvider(ownerUid, provider);
        startProviderIfEnabled(ownerUid, provider);
    }

    private void stopProvider(String ownerUid, ApprovalProvider provider) throws Exception {
        ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, provider.name());
        if (service != null) {
            service.stop(ownerUid, new LifeSpiRequest());
        }
    }

    private void startProviderIfEnabled(String ownerUid, ApprovalProvider provider) {
        ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, provider.name());
        if (service == null) {
            return;
        }
        if (!this.approvalService.checkEnableApproval(ownerUid, provider)) {
            return;
        }

        try {
            service.start(ownerUid, new LifeSpiRequest());
        } catch (Exception e) {
            log.error("[ApprovalStarter] Switch " + provider.name() + " client was failed", e);
        }
    }

    private static UserConfigTagType convertToConfigTagType(ApprovalProvider provider) {
        return switch (provider) {
            case Feishu -> UserConfigTagType.FEISHU;
            case Wechat -> UserConfigTagType.WECHAT;
            case DingTalk -> UserConfigTagType.DINGTALK;
            default -> null;
        };
    }
}

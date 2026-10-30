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
package com.clougence.clouddm.console.web.component.approval;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.console.web.component.approval.schedule.ApprovalTaskScheduler;
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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApprovalStarter implements UnifiedPostConstruct, RdpNotifyService {
    @Resource
    private AuthDal                     authDal;
    @Resource
    private ApprovalProviderServiceImpl approvalService;
    @Resource
    private ApprovalTaskScheduler       taskScheduler;

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
                startProviderIfConfigured(rdpUserDO.getUid(), provider);
            }
        }

        // task scheduler start
        try {
            this.taskScheduler.start();
            log.info("[ApprovalStarter] ApprovalTaskScheduler started.");
        } catch (Exception e) {
            String msg = "[ApprovalStarter] ApprovalTaskScheduler started failed, but will ignore exception, msg: " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
        }
    }

    @Override
    public void notifyUserConfig(String ownerUid, List<UserConfigMO> configList) {
        if (configList == null || configList.isEmpty()) {
            return;
        }

        try {
            restartModifiedProviders(ownerUid, configList);
        } catch (Exception e) {
            String msg = "Restart approval providers failed, ownerUid=" + ownerUid;
            log.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
    }

    private void restartModifiedProviders(String ownerUid, List<UserConfigMO> configList) throws Exception {
        Set<UserConfigTagType> modifiedTypes = new HashSet<>();
        for (UserConfigMO config : configList) {
            if (config.isInsert() || config.isUpdate() || config.isDelete()) {
                modifiedTypes.add(config.getTagType());
            }
        }

        for (ApprovalProvider provider : ApprovalFlowService.SUPPORT_LIST) {
            UserConfigTagType tagType = convertToConfigTagType(provider);
            if (tagType == null || !modifiedTypes.contains(tagType)) {
                continue;
            }

            ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, provider.name());
            if (service != null) {
                service.stop(ownerUid, new LifeSpiRequest());
                startProviderIfConfigured(ownerUid, provider);
            }
        }
    }

    private void startProviderIfConfigured(String ownerUid, ApprovalProvider provider) {
        ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, provider.name());
        if (service == null) {
            return;
        }

        if (!this.approvalService.isApprovalConfigured(ownerUid, provider)) {
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

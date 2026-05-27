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

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApprovalStarter implements UnifiedPostConstruct {
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
        List<String> serviceNames;
        try {
            serviceNames = PluginManager.getSpiNamesByType(ApprovalProviderSpi.class);
            if (CollectionUtils.isNotEmpty(serviceNames)) {
                log.info("[ApprovalStarter] ScheduleService found " + serviceNames.size() + " provider is " + StringUtils.join(serviceNames.toArray(), ","));
            } else {
                String msg = "[ApprovalStarter] ScheduleService not found any provider.";
                log.error(msg);
                return;
            }
        } catch (Exception e) {
            String msg = "[ApprovalStarter] ScheduleService found provider error " + e.getMessage();
            log.error(msg, e);
            return;
        }

        // start plugin for user.
        List<DmAuthUserDO> users = this.authDal.userMapper().listPrimaryAccount();
        for (DmAuthUserDO rdpUserDO : users) {
            for (String serviceName : serviceNames) {
                ApprovalProvider providerType = ApprovalProvider.valueOf(serviceName);

                if (!this.approvalService.checkEnableApproval(rdpUserDO.getUid(), providerType)) {
                    continue;
                }

                ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, serviceName);
                try {
                    service.start(rdpUserDO.getUid(), new LifeSpiRequest());
                } catch (Exception e) {
                    log.error("[ApprovalStarter] Switch " + serviceName + "client was failed", e);
                }
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
}

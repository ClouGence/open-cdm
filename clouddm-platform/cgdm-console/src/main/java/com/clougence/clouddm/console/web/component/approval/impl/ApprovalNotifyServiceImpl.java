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

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.model.UserConfigMO;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApprovalNotifyServiceImpl implements RdpNotifyService {

    @Resource
    private ApprovalProviderServiceImpl approvalService;

    @SneakyThrows
    @Override
    public void notifyUserConfig(String ownerUid, List<UserConfigMO> configList) {

        List<UserConfigTagType> tagTypes = configList.stream().map(UserConfigMO::getTagType).distinct().toList();
        for (UserConfigTagType tagType : tagTypes) {
            ApprovalProvider providerType = convertToProviderType(tagType);
            if (providerType == null) {
                continue;
            }

            ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, providerType.name());
            if (service == null) {
                continue;
            }

            service.stop(ownerUid, new LifeSpiRequest());
            if (this.approvalService.checkEnableApproval(ownerUid, providerType)) {
                service.start(ownerUid, new LifeSpiRequest());
            }
        }
    }

    private static ApprovalProvider convertToProviderType(UserConfigTagType type) {
        switch (type) {
            case FEISHU:
                return ApprovalProvider.Feishu;
            case WECHAT:
                return ApprovalProvider.Wechat;
            case DINGTALK:
                return ApprovalProvider.DingTalk;
            default:
                return null;
        }
    }
}

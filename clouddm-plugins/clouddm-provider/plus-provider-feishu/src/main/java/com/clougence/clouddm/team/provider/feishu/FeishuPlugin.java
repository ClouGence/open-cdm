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
package com.clougence.clouddm.team.provider.feishu;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.service.approval.ApprovalRefreshService;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.team.provider.feishu.approval.FeishuApprovalProviderSpi;
import com.clougence.clouddm.team.provider.feishu.auth.FeishuLoginProviderSpi;
import com.clougence.clouddm.team.provider.feishu.constants.FeishuI18nKeys2;
import com.clougence.clouddm.team.provider.feishu.im.FeishuMsgSendSpi;

@Plugin
public class FeishuPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        ApprovalRefreshService approvalService = dsPlugin.findGlobalService(ApprovalRefreshService.class);
        ConsoleConfigService configService = dsPlugin.findGlobalService(ConsoleConfigService.class);

        // i18n
        dsPlugin.bindGlobalI18n(FeishuI18nKeys2.class);

        // spi
        dsPlugin.addGlobalSpi(ApprovalProviderSpi.class, ApprovalProvider.Feishu.name(), new FeishuApprovalProviderSpi(configService, approvalService));
        dsPlugin.addGlobalSpi(LoginProviderSpi.class, LoginProvider.Feishu.name(), new FeishuLoginProviderSpi(configService));
        dsPlugin.addGlobalSpi(new FeishuMsgSendSpi());
    }
}

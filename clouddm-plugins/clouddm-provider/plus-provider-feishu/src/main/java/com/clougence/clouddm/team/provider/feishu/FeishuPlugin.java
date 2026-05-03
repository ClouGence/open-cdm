package com.clougence.clouddm.team.provider.feishu;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.team.provider.feishu.approval.FeishuApprovalProviderSpi;
import com.clougence.clouddm.team.provider.feishu.auth.FeishuLoginProviderSpi;
import com.clougence.clouddm.team.provider.feishu.constants.FeishuI18nKeys2;
import com.clougence.clouddm.team.provider.feishu.im.FeishuMsgSendSpi;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalConsoleService;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;

@Plugin
public class FeishuPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        RdpApprovalConsoleService approvalService = dsPlugin.findGlobalService(RdpApprovalConsoleService.class);
        ConsoleConfigService configService = dsPlugin.findGlobalService(ConsoleConfigService.class);

        // i18n
        dsPlugin.bindGlobalI18n(FeishuI18nKeys2.class);

        // spi
        dsPlugin.addGlobalSpi(ApprovalProviderSpi.class, ApprovalProvider.Feishu.name(), new FeishuApprovalProviderSpi(configService, approvalService));
        dsPlugin.addGlobalSpi(LoginProviderSpi.class, LoginProvider.Feishu.name(), new FeishuLoginProviderSpi(configService));
        dsPlugin.addGlobalSpi(new FeishuMsgSendSpi());
    }
}

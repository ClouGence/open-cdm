package com.clougence.clouddm.team.provider.dingtalk;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.team.provider.dingtalk.approval.DingApprovalProviderSpi;
import com.clougence.clouddm.team.provider.dingtalk.auth.DingLoginProviderSpi;
import com.clougence.clouddm.team.provider.dingtalk.constants.DingI18nKeys;
import com.clougence.clouddm.team.provider.dingtalk.im.DingTalkMsgSendSpi;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalConsoleService;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;

@Plugin
public class DingPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        ClassLoader pluginLoader = dsPlugin.getPluginClassLoader();
        RdpApprovalConsoleService approvalService = dsPlugin.findGlobalService(RdpApprovalConsoleService.class);
        ConsoleConfigService configService = dsPlugin.findGlobalService(ConsoleConfigService.class);

        // i18n
        dsPlugin.bindGlobalI18n(DingI18nKeys.class);

        // spi
        dsPlugin.addGlobalSpi(ApprovalProviderSpi.class, ApprovalProvider.DingTalk.name(), new DingApprovalProviderSpi(configService, approvalService, pluginLoader));
        dsPlugin.addGlobalSpi(LoginProviderSpi.class, LoginProvider.DingTalk.name(), new DingLoginProviderSpi(configService));
        dsPlugin.addGlobalSpi(new DingTalkMsgSendSpi());
    }
}

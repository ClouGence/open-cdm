package com.clougence.clouddm.team.provider.wechat;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.approval.ApprovalCallbackSpi;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.team.provider.wechat.approval.WechatApprovalCallbackSpi;
import com.clougence.clouddm.team.provider.wechat.approval.WechatApprovalProviderSpi;
import com.clougence.clouddm.team.provider.wechat.auth.WechatLoginProviderSpi;
import com.clougence.clouddm.team.provider.wechat.constants.WechatI18nKey2;
import com.clougence.clouddm.team.provider.wechat.im.WechatMsgSendSpi;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalConsoleService;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;

@Plugin
public class WechatPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        RdpApprovalConsoleService approvalService = dsPlugin.findGlobalService(RdpApprovalConsoleService.class);
        ConsoleConfigService configService = dsPlugin.findGlobalService(ConsoleConfigService.class);

        // i18n
        dsPlugin.bindGlobalI18n(WechatI18nKey2.class);

        // spi
        WechatApprovalProviderSpi service = new WechatApprovalProviderSpi(configService);
        dsPlugin.addGlobalSpi(ApprovalProviderSpi.class, ApprovalProvider.Wechat.name(), service);
        dsPlugin.addGlobalSpi(ApprovalCallbackSpi.class, ApprovalProvider.Wechat.name(), new WechatApprovalCallbackSpi(service, approvalService));
        dsPlugin.addGlobalSpi(LoginProviderSpi.class, LoginProvider.Wechat.name(), new WechatLoginProviderSpi(configService));
        dsPlugin.addGlobalSpi(new WechatMsgSendSpi());
    }
}

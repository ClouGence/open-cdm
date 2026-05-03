package com.clougence.clouddm.console.web.global.session;

import java.util.Set;

import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.rdp.component.jwtsession.RdpJwtManager;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;

public class DmJwtManager extends RdpJwtManager {

    @Override
    protected void configUrl(Set<String> includeVerifyStartWith, Set<String> ignoreEndWithUrl) {
        // RDP
        includeVerifyStartWith.add(RdpControllerUrlPrefix.CONSOLE_PREFIX);
        includeVerifyStartWith.add("/logout");
        includeVerifyStartWith.add("/switchSaasMode");

        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/user/resetpasswd");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/verify/sendcode");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/constant/sso_type");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/onlinecheckauthcode");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/checkpayresult");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/stripePayResult");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/user/recordandreturndownloadurlbydm");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/marketplace/notifications/handle");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/alipayLicenseCallback");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/alipayPrepayCallback");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/stripeLicenseCallback");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/stripePrepayCallback");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/autoapplylicense");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/saas/queryPriceMeta");

        // DM
        includeVerifyStartWith.add(DmControllerUrlPrefix.CONSOLE_PREFIX);
        ignoreEndWithUrl.add(DmControllerUrlPrefix.CONSOLE_PREFIX + "/version");
        ignoreEndWithUrl.add(DmControllerUrlPrefix.CONSOLE_PREFIX + "/dm_global_settings");
    }
}

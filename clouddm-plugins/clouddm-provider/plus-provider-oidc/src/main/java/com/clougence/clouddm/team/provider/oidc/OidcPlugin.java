package com.clougence.clouddm.team.provider.oidc;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.team.provider.oidc.auth.OidcLoginProviderSpi;
import com.clougence.clouddm.team.provider.oidc.constants.OidcI18nKey;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;

@Plugin(includePackages = { "com.clougence.clouddm.team.provider.oidc.*" })
public class OidcPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        ConsoleConfigService configService = dsPlugin.findGlobalService(ConsoleConfigService.class);

        // i18n
        dsPlugin.bindGlobalI18n(OidcI18nKey.class);

        // spi
        dsPlugin.addGlobalSpi(LoginProviderSpi.class, LoginProvider.OIDC.name(), new OidcLoginProviderSpi(configService));
    }
}

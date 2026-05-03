package com.clougence.clouddm.team.provider.ldap;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.team.provider.ldap.auth.LoginProviderSpiForAd;
import com.clougence.clouddm.team.provider.ldap.auth.LoginProviderSpiForLdap;
import com.clougence.clouddm.team.provider.ldap.constants.LdapI18nKey;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;

@Plugin(includePackages = { "com.clougence.clouddm.team.provider.ldap.*" },//
        excludePackages = { "org.springframework.ldap" })
public class LdapPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        ConsoleConfigService configService = dsPlugin.findGlobalService(ConsoleConfigService.class);

        // i18n
        dsPlugin.bindGlobalI18n(LdapI18nKey.class);

        // spi
        dsPlugin.addGlobalSpi(LoginProviderSpi.class, LoginProvider.AD.name(), new LoginProviderSpiForAd(configService));
        dsPlugin.addGlobalSpi(LoginProviderSpi.class, LoginProvider.LDAP.name(), new LoginProviderSpiForLdap(configService));
    }
}

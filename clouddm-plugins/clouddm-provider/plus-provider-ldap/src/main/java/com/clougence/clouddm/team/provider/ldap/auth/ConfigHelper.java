package com.clougence.clouddm.team.provider.ldap.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.team.provider.ldap.constants.LdapConfigKey;
import com.clougence.clouddm.team.provider.ldap.constants.LdapI18nKey;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode
 * @version 2020-01-17 15:29
 */
@Slf4j
public class ConfigHelper {

    public static BaseConfig fetchConfig(ConsoleConfigService configService, final String primaryUID) {
        List<ConfigData> configList = configService.fetchSettings(primaryUID, Arrays.asList(//
                LdapConfigKey.AuthType.getConfigKey(),           //
                LdapConfigKey.LdapHost.getConfigKey(),           //
                LdapConfigKey.LdapPort.getConfigKey(),           //
                LdapConfigKey.LdapNetBIOSRoute.getConfigKey(),   //
                LdapConfigKey.LdapSoTimeout.getConfigKey(),      //
                LdapConfigKey.LdapBase.getConfigKey(),           //
                LdapConfigKey.LdapDomain.getConfigKey(),         //
                LdapConfigKey.LdapUser.getConfigKey(),           //
                LdapConfigKey.LdapPassword.getConfigKey(),       //
                LdapConfigKey.LdapRoleMap.getConfigKey(),        //
                LdapConfigKey.LdapUserObjectClass.getConfigKey(),//
                LdapConfigKey.LdapFieldLogin.getConfigKey(),     //
                LdapConfigKey.LdapFieldUser.getConfigKey(),      //
                LdapConfigKey.LdapFieldEmail.getConfigKey(),     //
                LdapConfigKey.LdapFieldPhone.getConfigKey()      //
        ));

        Map<String, ConfigData> configMap = new HashMap<>();
        for (ConfigData config : configList) {
            configMap.put(config.getConfigName(), config);
        }

        ConfigData authType = configMap.get(LdapConfigKey.AuthType.getConfigKey());
        ConfigData ldapHost = configMap.get(LdapConfigKey.LdapHost.getConfigKey());
        ConfigData ldapPort = configMap.get(LdapConfigKey.LdapPort.getConfigKey());
        ConfigData ldapNetBIOSRoute = configMap.get(LdapConfigKey.LdapNetBIOSRoute.getConfigKey());
        ConfigData ldapSoTimeout = configMap.get(LdapConfigKey.LdapSoTimeout.getConfigKey());
        ConfigData ldapBase = configMap.get(LdapConfigKey.LdapBase.getConfigKey());
        ConfigData ldapDomain = configMap.get(LdapConfigKey.LdapDomain.getConfigKey());
        ConfigData ldapUser = configMap.get(LdapConfigKey.LdapUser.getConfigKey());
        ConfigData ldapPassword = configMap.get(LdapConfigKey.LdapPassword.getConfigKey());
        ConfigData ldapRoleMap = configMap.get(LdapConfigKey.LdapRoleMap.getConfigKey());
        ConfigData ldapUserObjectClass = configMap.get(LdapConfigKey.LdapUserObjectClass.getConfigKey());
        ConfigData ldapFieldLogin = configMap.get(LdapConfigKey.LdapFieldLogin.getConfigKey());
        ConfigData ldapFieldUser = configMap.get(LdapConfigKey.LdapFieldUser.getConfigKey());
        ConfigData ldapFieldEmail = configMap.get(LdapConfigKey.LdapFieldEmail.getConfigKey());
        ConfigData ldapFieldPhone = configMap.get(LdapConfigKey.LdapFieldPhone.getConfigKey());

        BaseConfig config = new BaseConfig();
        config.setAuthType(authType == null ? "" : authType.getConfigValue());
        config.setLdapHost(ldapHost == null ? "" : ldapHost.getConfigValue());
        config.setLdapPort(ldapPort == null ? "389" : ldapPort.getConfigValue());
        config.setLdapNetBIOSRoute(ldapNetBIOSRoute == null ? "" : ldapNetBIOSRoute.getConfigValue());
        config.setLdapSoTimeout(ldapSoTimeout == null ? "3000" : ldapSoTimeout.getConfigValue());
        config.setLdapBase(ldapBase == null ? "" : ldapBase.getConfigValue());
        config.setLdapUser(ldapUser == null ? "" : ldapUser.getConfigValue());
        config.setLdapPassword(ldapPassword == null ? "" : ldapPassword.getConfigValue());
        config.setLdapDomain(ldapDomain == null ? "" : ldapDomain.getConfigValue());
        config.setLdapRoleMap(ldapRoleMap == null ? "" : ldapRoleMap.getConfigValue());
        config.setLdapUserObjectClass(ldapUserObjectClass == null ? "" : ldapUserObjectClass.getConfigValue());
        config.setLdapFieldLogin(ldapFieldLogin == null ? "" : ldapFieldLogin.getConfigValue());
        config.setLdapFieldUser(ldapFieldUser == null ? "" : ldapFieldUser.getConfigValue());
        config.setLdapFieldEmail(ldapFieldEmail == null ? "" : ldapFieldEmail.getConfigValue());
        config.setLdapFieldPhone(ldapFieldPhone == null ? "" : ldapFieldPhone.getConfigValue());
        return config;
    }

    public static BaseConfig checkAdConfig(BaseConfig cfg) {
        if (StringUtils.isBlank(cfg.getLdapHost())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapHost");
        }
        if (StringUtils.isBlank(cfg.getLdapPort())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapPort");
        }
        if (StringUtils.isBlank(cfg.getLdapBase())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapBase");
        }
        if (StringUtils.isBlank(cfg.getLdapUser())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapUser");
        }
        if (StringUtils.isBlank(cfg.getLdapPassword())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapPassword");
        }
        if (StringUtils.isBlank(cfg.getLdapFieldLogin())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapFieldLogin");
        }
        if (StringUtils.isBlank(cfg.getLdapRoleMap())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapRoleMap");
        }
        if (StringUtils.isBlank(cfg.getLdapDomain())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapDomain");
        }
        return cfg;
    }

    public static BaseConfig checkLdapConfig(BaseConfig conf) {
        if (StringUtils.isBlank(conf.getLdapHost())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapHost");
        }
        if (StringUtils.isBlank(conf.getLdapPort())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapPort");
        }
        if (StringUtils.isBlank(conf.getLdapBase())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapBase");
        }
        if (StringUtils.isBlank(conf.getLdapUser())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapUser");
        }
        if (StringUtils.isBlank(conf.getLdapPassword())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapPassword");
        }
        if (StringUtils.isBlank(conf.getLdapFieldLogin())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapFieldLogin");
        }
        if (StringUtils.isBlank(conf.getLdapRoleMap())) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapRoleMap");
        }

        return conf;
    }

}

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
package com.clougence.clouddm.team.provider.ldap.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.team.provider.ldap.constants.LdapConfigKey;
import com.clougence.clouddm.team.provider.ldap.constants.LdapI18nKey;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode
 * @version 2020-01-17 15:29
 */
@Slf4j
public class ConfigHelper {

    public static BaseConfig fetchLdapConfig(ConsoleConfigService configService) {
        List<LdapConfigKey> configKeys = List.of( //
                LdapConfigKey.AuthType,           //
                LdapConfigKey.LdapHost,           //
                LdapConfigKey.LdapPort,           //
                LdapConfigKey.LdapNetBIOSRoute,   //
                LdapConfigKey.LdapSoTimeout,      //
                LdapConfigKey.LdapBase,           //
                LdapConfigKey.LdapDomain,         //
                LdapConfigKey.LdapUser,           //
                LdapConfigKey.LdapPassword,       //
                LdapConfigKey.LdapRoleMap,        //
                LdapConfigKey.LdapUserObjectClass,//
                LdapConfigKey.LdapFieldLogin,     //
                LdapConfigKey.LdapFieldUser,      //
                LdapConfigKey.LdapFieldEmail,     //
                LdapConfigKey.LdapFieldPhone      //
        );
        List<ConfigData> configList = configService.fetchSettings(configKeys(configKeys));
        return buildConfig(configList, configKeys);
    }

    public static BaseConfig fetchAdConfig(ConsoleConfigService configService) {
        List<LdapConfigKey> configKeys = List.of(//
                LdapConfigKey.AuthType,          //
                LdapConfigKey.AdHost,            //
                LdapConfigKey.AdPort,            //
                LdapConfigKey.AdNetBIOSRoute,    //
                LdapConfigKey.AdSoTimeout,       //
                LdapConfigKey.AdBase,            //
                LdapConfigKey.AdDomain,          //
                LdapConfigKey.AdUser,            //
                LdapConfigKey.AdPassword,        //
                LdapConfigKey.AdRoleMap,         //
                LdapConfigKey.AdUserObjectClass, //
                LdapConfigKey.AdFieldLogin,      //
                LdapConfigKey.AdFieldUser,       //
                LdapConfigKey.AdFieldEmail,      //
                LdapConfigKey.AdFieldPhone       //
        );
        List<ConfigData> configList = configService.fetchSettings(configKeys(configKeys));
        return buildConfig(configList, configKeys);
    }

    private static List<String> configKeys(List<LdapConfigKey> configKeys) {
        return configKeys.stream().map(LdapConfigKey::getConfigKey).collect(Collectors.toList());
    }

    private static BaseConfig buildConfig(List<ConfigData> configList, List<LdapConfigKey> configKeys) {
        Map<String, ConfigData> configMap = new HashMap<>();
        for (ConfigData config : configList) {
            configMap.put(config.getConfigName(), config);
        }

        ConfigData authType = configData(configMap, configKeys, 0);
        ConfigData ldapHost = configData(configMap, configKeys, 1);
        ConfigData ldapPort = configData(configMap, configKeys, 2);
        ConfigData ldapNetBIOSRoute = configData(configMap, configKeys, 3);
        ConfigData ldapSoTimeout = configData(configMap, configKeys, 4);
        ConfigData ldapBase = configData(configMap, configKeys, 5);
        ConfigData ldapDomain = configData(configMap, configKeys, 6);
        ConfigData ldapUser = configData(configMap, configKeys, 7);
        ConfigData ldapPassword = configData(configMap, configKeys, 8);
        ConfigData ldapRoleMap = configData(configMap, configKeys, 9);
        ConfigData ldapUserObjectClass = configData(configMap, configKeys, 10);
        ConfigData ldapFieldLogin = configData(configMap, configKeys, 11);
        ConfigData ldapFieldUser = configData(configMap, configKeys, 12);
        ConfigData ldapFieldEmail = configData(configMap, configKeys, 13);
        ConfigData ldapFieldPhone = configData(configMap, configKeys, 14);

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

    private static ConfigData configData(Map<String, ConfigData> configMap, List<LdapConfigKey> configKeys, int index) {
        return configMap.get(configKeys.get(index).getConfigKey());
    }

    public static BaseConfig checkAdConfig(BaseConfig cfg) {
        if (StringUtils.isBlank(cfg.getLdapHost())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "adHost");
        }
        if (StringUtils.isBlank(cfg.getLdapPort())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "adPort");
        }
        if (StringUtils.isBlank(cfg.getLdapBase())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "adBase");
        }
        if (StringUtils.isBlank(cfg.getLdapUser())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "adUser");
        }
        if (StringUtils.isBlank(cfg.getLdapPassword())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "adPassword");
        }
        if (StringUtils.isBlank(cfg.getLdapFieldLogin())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "adFieldLogin");
        }
        if (StringUtils.isBlank(cfg.getLdapRoleMap())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "adRoleMap");
        }
        if (StringUtils.isBlank(cfg.getLdapDomain())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "adDomain");
        }
        return cfg;
    }

    public static BaseConfig checkLdapConfig(BaseConfig conf) {
        if (StringUtils.isBlank(conf.getLdapHost())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "ldapHost");
        }
        if (StringUtils.isBlank(conf.getLdapPort())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "ldapPort");
        }
        if (StringUtils.isBlank(conf.getLdapBase())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "ldapBase");
        }
        if (StringUtils.isBlank(conf.getLdapUser())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "ldapUser");
        }
        if (StringUtils.isBlank(conf.getLdapPassword())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "ldapPassword");
        }
        if (StringUtils.isBlank(conf.getLdapFieldLogin())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "ldapFieldLogin");
        }
        if (StringUtils.isBlank(conf.getLdapRoleMap())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR, "ldapRoleMap");
        }

        return conf;
    }

}

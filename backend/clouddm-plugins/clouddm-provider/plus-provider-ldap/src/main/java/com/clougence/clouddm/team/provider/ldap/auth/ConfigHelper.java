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

    public static BaseConfig fetchLdapConfig(ConsoleConfigService configService, final String primaryUID) {
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

        return buildConfig(configList, LdapConfigKey.AuthType, LdapConfigKey.LdapHost, LdapConfigKey.LdapPort, LdapConfigKey.LdapNetBIOSRoute,
            LdapConfigKey.LdapSoTimeout, LdapConfigKey.LdapBase, LdapConfigKey.LdapDomain, LdapConfigKey.LdapUser, LdapConfigKey.LdapPassword,
            LdapConfigKey.LdapRoleMap, LdapConfigKey.LdapUserObjectClass, LdapConfigKey.LdapFieldLogin, LdapConfigKey.LdapFieldUser,
            LdapConfigKey.LdapFieldEmail, LdapConfigKey.LdapFieldPhone);
    }

    public static BaseConfig fetchAdConfig(ConsoleConfigService configService, final String primaryUID) {
        List<ConfigData> configList = configService.fetchSettings(primaryUID, Arrays.asList(//
                LdapConfigKey.AuthType.getConfigKey(),       //
                LdapConfigKey.AdHost.getConfigKey(),         //
                LdapConfigKey.AdPort.getConfigKey(),         //
                LdapConfigKey.AdNetBIOSRoute.getConfigKey(), //
                LdapConfigKey.AdSoTimeout.getConfigKey(),    //
                LdapConfigKey.AdBase.getConfigKey(),         //
                LdapConfigKey.AdDomain.getConfigKey(),       //
                LdapConfigKey.AdUser.getConfigKey(),         //
                LdapConfigKey.AdPassword.getConfigKey(),     //
                LdapConfigKey.AdRoleMap.getConfigKey(),      //
                LdapConfigKey.AdUserObjectClass.getConfigKey(),//
                LdapConfigKey.AdFieldLogin.getConfigKey(),   //
                LdapConfigKey.AdFieldUser.getConfigKey(),    //
                LdapConfigKey.AdFieldEmail.getConfigKey(),   //
                LdapConfigKey.AdFieldPhone.getConfigKey()    //
        ));

        return buildConfig(configList, LdapConfigKey.AuthType, LdapConfigKey.AdHost, LdapConfigKey.AdPort, LdapConfigKey.AdNetBIOSRoute,
            LdapConfigKey.AdSoTimeout, LdapConfigKey.AdBase, LdapConfigKey.AdDomain, LdapConfigKey.AdUser, LdapConfigKey.AdPassword,
            LdapConfigKey.AdRoleMap, LdapConfigKey.AdUserObjectClass, LdapConfigKey.AdFieldLogin, LdapConfigKey.AdFieldUser,
            LdapConfigKey.AdFieldEmail, LdapConfigKey.AdFieldPhone);
    }

    private static BaseConfig buildConfig(List<ConfigData> configList, LdapConfigKey authTypeKey, LdapConfigKey hostKey, LdapConfigKey portKey,
                                          LdapConfigKey netBIOSRouteKey, LdapConfigKey soTimeoutKey, LdapConfigKey baseKey, LdapConfigKey domainKey,
                                          LdapConfigKey userKey, LdapConfigKey passwordKey, LdapConfigKey roleMapKey, LdapConfigKey userObjectClassKey,
                                          LdapConfigKey fieldLoginKey, LdapConfigKey fieldUserKey, LdapConfigKey fieldEmailKey, LdapConfigKey fieldPhoneKey) {
        Map<String, ConfigData> configMap = new HashMap<>();
        for (ConfigData config : configList) {
            configMap.put(config.getConfigName(), config);
        }

        ConfigData authType = configMap.get(authTypeKey.getConfigKey());
        ConfigData ldapHost = configMap.get(hostKey.getConfigKey());
        ConfigData ldapPort = configMap.get(portKey.getConfigKey());
        ConfigData ldapNetBIOSRoute = configMap.get(netBIOSRouteKey.getConfigKey());
        ConfigData ldapSoTimeout = configMap.get(soTimeoutKey.getConfigKey());
        ConfigData ldapBase = configMap.get(baseKey.getConfigKey());
        ConfigData ldapDomain = configMap.get(domainKey.getConfigKey());
        ConfigData ldapUser = configMap.get(userKey.getConfigKey());
        ConfigData ldapPassword = configMap.get(passwordKey.getConfigKey());
        ConfigData ldapRoleMap = configMap.get(roleMapKey.getConfigKey());
        ConfigData ldapUserObjectClass = configMap.get(userObjectClassKey.getConfigKey());
        ConfigData ldapFieldLogin = configMap.get(fieldLoginKey.getConfigKey());
        ConfigData ldapFieldUser = configMap.get(fieldUserKey.getConfigKey());
        ConfigData ldapFieldEmail = configMap.get(fieldEmailKey.getConfigKey());
        ConfigData ldapFieldPhone = configMap.get(fieldPhoneKey.getConfigKey());

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
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "adHost");
        }
        if (StringUtils.isBlank(cfg.getLdapPort())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "adPort");
        }
        if (StringUtils.isBlank(cfg.getLdapBase())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "adBase");
        }
        if (StringUtils.isBlank(cfg.getLdapUser())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "adUser");
        }
        if (StringUtils.isBlank(cfg.getLdapPassword())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "adPassword");
        }
        if (StringUtils.isBlank(cfg.getLdapFieldLogin())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "adFieldLogin");
        }
        if (StringUtils.isBlank(cfg.getLdapRoleMap())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "adRoleMap");
        }
        if (StringUtils.isBlank(cfg.getLdapDomain())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "adDomain");
        }
        return cfg;
    }

    public static BaseConfig checkLdapConfig(BaseConfig conf) {
        if (StringUtils.isBlank(conf.getLdapHost())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapHost");
        }
        if (StringUtils.isBlank(conf.getLdapPort())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapPort");
        }
        if (StringUtils.isBlank(conf.getLdapBase())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapBase");
        }
        if (StringUtils.isBlank(conf.getLdapUser())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapUser");
        }
        if (StringUtils.isBlank(conf.getLdapPassword())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapPassword");
        }
        if (StringUtils.isBlank(conf.getLdapFieldLogin())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapFieldLogin");
        }
        if (StringUtils.isBlank(conf.getLdapRoleMap())) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_CONFIG_ERROR.name(), "ldapRoleMap");
        }

        return conf;
    }

}

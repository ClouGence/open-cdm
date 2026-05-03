package com.clougence.clouddm.team.provider.oidc.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.team.provider.oidc.constants.OidcConfigKey;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.ConfigData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OidcCfg {

    private boolean enable;
    private String  wellKnownURL;
    private String  clientId;
    private String  clientSecret;
    private String  scope;
    private String  roleMap;

    public static OidcCfg fetchConfig(ConsoleConfigService configService, String ownerUid) {
        List<ConfigData> configList = configService.fetchSettings(ownerUid, Arrays.asList(//
                OidcConfigKey.AuthType.getConfigKey(),    //
                OidcConfigKey.WellKnownUrl.getConfigKey(),//
                OidcConfigKey.ClientId.getConfigKey(),    //
                OidcConfigKey.ClientSecret.getConfigKey(),//
                OidcConfigKey.Scope.getConfigKey(),       //
                OidcConfigKey.RoleMap.getConfigKey()));
        Map<String, String> configMap = new HashMap<>();
        for (ConfigData c : configList) {
            configMap.put(c.getConfigName(), c.getConfigValue());
        }

        OidcCfg cfg = new OidcCfg();
        cfg.enable = configMap.get(OidcConfigKey.AuthType.getConfigKey()).equalsIgnoreCase(LoginProvider.OIDC.name());
        cfg.wellKnownURL = configMap.get(OidcConfigKey.WellKnownUrl.getConfigKey());
        cfg.clientId = configMap.get(OidcConfigKey.ClientId.getConfigKey());
        cfg.clientSecret = configMap.get(OidcConfigKey.ClientSecret.getConfigKey());
        cfg.scope = configMap.get(OidcConfigKey.Scope.getConfigKey());
        cfg.roleMap = configMap.get(OidcConfigKey.RoleMap.getConfigKey());
        return cfg;
    }
}

package com.clougence.clouddm.team.provider.oidc.constants;

import lombok.Getter;

/**
 * @author mode create time is 2025/02/13
 **/
@Getter
public enum OidcConfigKey {

    AuthType("subAccountAuthType"),
    WellKnownUrl("oidcLoginWellKnownUrl"),
    ClientId("oidcLoginClientId"),
    ClientSecret("oidcLoginClientSecret"),
    Scope("oidcLoginScope"),
    RoleMap("oidcLoginRoleMap"),;

    private final String configKey;

    OidcConfigKey(String configKey){
        this.configKey = configKey;
    }
}

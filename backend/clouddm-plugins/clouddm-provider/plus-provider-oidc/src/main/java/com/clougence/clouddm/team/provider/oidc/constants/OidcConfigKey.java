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
package com.clougence.clouddm.team.provider.oidc.constants;

import lombok.Getter;

/**
 * @author mode create time is 2025/02/13
 **/
@Getter
public enum OidcConfigKey {

    AuthType("accountAuthType"),
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

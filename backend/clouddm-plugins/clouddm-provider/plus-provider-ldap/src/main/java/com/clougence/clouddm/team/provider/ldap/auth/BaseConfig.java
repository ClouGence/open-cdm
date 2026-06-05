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

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-04 09:44
 * @since 1.1.3
 */
@Getter
@Setter
public class BaseConfig {

    private String authType;

    //
    private String ldapHost;
    private String ldapPort;
    private String ldapNetBIOSRoute;
    private String ldapSoTimeout;
    private String ldapBase;
    private String ldapUser;
    private String ldapPassword;
    private String ldapDomain;

    //
    private String ldapRoleMap;
    private String ldapUserObjectClass;

    //
    private String ldapFieldLogin;
    private String ldapFieldUser;
    private String ldapFieldEmail;
    private String ldapFieldPhone;

}

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
package com.clougence.clouddm.team.provider.ldap.constants;

import com.clougence.utils.i18n.I18nResource;

/**
 * @author wanshao create time is 2021/3/1
 **/
@I18nResource("/META-INF/clougence/i18n/ldap")
public interface LdapI18nKey {

    String PLUGIN_NAME_LDAP                         = "PLUGIN_NAME_LDAP";


    // -- for Login
    String LDAP_LOGIN_FAIL_PASSWORD_ERROR           = "LDAP_LOGIN_FAIL_PASSWORD_ERROR";
    String LDAP_LOGIN_FAIL_PASSWORD_EXPIRED         = "LDAP_LOGIN_FAIL_PASSWORD_EXPIRED";
    String LDAP_LOGIN_FAIL_PRIMARY_MISSING_ARGS     = "LDAP_LOGIN_FAIL_PRIMARY_MISSING_ARGS";

    // -- for User
    String LDAP_USER_IS_DISABLED_ERROR              = "LDAP_USER_IS_DISABLED_ERROR";
    String LDAP_USER_ROLE_MAPPING_FAILED            = "LDAP_USER_ROLE_MAPPING_FAILED";

    // -- for LDAP
    String LDAP_LOGIN_SERVICES_NAME                 = "LDAP_LOGIN_SERVICES_NAME";
    String LDAP_CONFIG_ERROR                        = "LDAP_CONFIG_ERROR";
    String LDAP_SERVICE_ERROR                       = "LDAP_SERVICE_ERROR";
    String LDAP_MATCH_MULTIPLE_ACCOUNT              = "LDAP_MATCH_MULTIPLE_ACCOUNT";
    String LDAP_OBJECTCLASS_NOT_ALLOWED_ERROR       = "LDAP_OBJECTCLASS_NOT_ALLOWED_ERROR";

    // -- for AD
    String AD_LOGIN_SERVICES_NAME                   = "AD_LOGIN_SERVICES_NAME";
    String AD_LOGIN_FAIL_ACCOUNT_NOT_EXIST          = "AD_LOGIN_FAIL_ACCOUNT_NOT_EXIST";
    String AD_LOGIN_FAIL_PASSWORD_ERROR             = "AD_LOGIN_FAIL_PASSWORD_ERROR";
    String AD_LOGIN_FAIL_LOGIN_NOT_ALLOWED_THIS_TIME = "AD_LOGIN_FAIL_LOGIN_NOT_ALLOWED_THIS_TIME";
    String AD_LOGIN_FAIL_LOGIN_NOT_ALLOWED_THIS_PC   = "AD_LOGIN_FAIL_LOGIN_NOT_ALLOWED_THIS_PC";
    String AD_LOGIN_FAIL_PASSWORD_EXPIRED           = "AD_LOGIN_FAIL_PASSWORD_EXPIRED";
    String AD_LOGIN_FAIL_USER_DISABLED              = "AD_LOGIN_FAIL_USER_DISABLED";
    String AD_LOGIN_FAIL_ACCOUNT_EXPIRED            = "AD_LOGIN_FAIL_ACCOUNT_EXPIRED";
    String AD_LOGIN_FAIL_NEED_RESET_PASSWORD        = "AD_LOGIN_FAIL_NEED_RESET_PASSWORD";
    String AD_LOGIN_FAIL_ACCOUNT_LOCKED             = "AD_LOGIN_FAIL_ACCOUNT_LOCKED";
    String AD_NET_BIOS_IP_MAP                       = "AD_NET_BIOS_IP_MAP";
}

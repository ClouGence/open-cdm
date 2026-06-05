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

import com.clougence.utils.i18n.I18nResource;

/**
 * @author wanshao create time is 2021/3/1
 **/
@I18nResource("/META-INF/rdp/sdk/i18n/oidc")
public interface OidcI18nKey {

    String OIDC_LOGIN_SERVICES_NAME                  = "OIDC_LOGIN_SERVICES_NAME";
    String OIDC_UNKNOWN_CALL_API_ERROR               = "OIDC_UNKNOWN_CALL_API_ERROR";
    String OIDC_LOGIN_FAIL_PRIMARY_MISSING_ARGS      = "OIDC_LOGIN_FAIL_PRIMARY_MISSING_ARGS";
    String OIDC_ROLE_MAPPING_FAILED                  = "OIDC_ROLE_MAPPING_FAILED";
    String OIDC_API_WELLKNOWN_ERROR                  = "OIDC_API_WELLKNOWN_ERROR";
    String OIDC_VERIFY_TOKEN_ERROR                   = "OIDC_VERIFY_TOKEN_ERROR";
    String OIDC_VERIFY_TOKEN_CLOCK_ERROR             = "OIDC_VERIFY_TOKEN_CLOCK_ERROR";
    String OIDC_VERIFY_AUTH_METHOD_ERROR             = "OIDC_VERIFY_AUTH_METHOD_ERROR";
    String OIDC_API_TOKEN_ERROR                      = "OIDC_API_TOKEN_ERROR";
    String OIDC_API_WELLKNOWN_MISSING_JWKS_URI_ERROR = "OIDC_API_WELLKNOWN_MISSING_JWKS_URI_ERROR";
    String OIDC_API_ALGORITHM_ERROR                  = "OIDC_API_ALGORITHM_ERROR";
}

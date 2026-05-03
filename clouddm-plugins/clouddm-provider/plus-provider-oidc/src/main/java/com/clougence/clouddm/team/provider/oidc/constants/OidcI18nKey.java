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

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
package com.clougence.clouddm.ds.es.i18n;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/es-plugin")
public interface EsDsI18nKeys {
    String PLUGIN_NAME_ES                     = "PLUGIN_NAME_ES";
    String ES_CONFIG_DECODE_ERROR             = "ES_CONFIG_DECODE_ERROR";
    String ES_ENDPOINTS_INVALID               = "ES_ENDPOINTS_INVALID";
    String ES_ENDPOINTS_LABEL                 = "ES_ENDPOINTS_LABEL";
    String ES_ENDPOINTS_DESC                  = "ES_ENDPOINTS_DESC";
    String ES_LIMITS_INVALID                  = "ES_LIMITS_INVALID";
    String ES_SSH_UNSUPPORTED                 = "ES_SSH_UNSUPPORTED";
    String ES_TLS_INVALID                     = "ES_TLS_INVALID";
    String ES_TLS_HTTPS_REQUIRED              = "ES_TLS_HTTPS_REQUIRED";
    String ES_AUTH_INVALID                    = "ES_AUTH_INVALID";
    String ES_VERSION_INVALID                 = "ES_VERSION_INVALID";
    String ES_REQUEST_CANCELLED               = "ES_REQUEST_CANCELLED";
    String ES_REQUEST_TIMEOUT                 = "ES_REQUEST_TIMEOUT";
    String ES_HTTP_ERROR                      = "ES_HTTP_ERROR";
    String ES_CONNECT_FAILED                  = "ES_CONNECT_FAILED";
    String ES_CLIENT_CLOSED                   = "ES_CLIENT_CLOSED";
    String ES_CLOSE_FAILED                    = "ES_CLOSE_FAILED";
    String ES_OPERATION_UNSUPPORTED           = "ES_OPERATION_UNSUPPORTED";
    String ES_CONNECT_TIMEOUT_LABEL           = "ES_CONNECT_TIMEOUT_LABEL";
    String ES_CONNECT_TIMEOUT_DESC            = "ES_CONNECT_TIMEOUT_DESC";
    String ES_SOCKET_TIMEOUT_LABEL            = "ES_SOCKET_TIMEOUT_LABEL";
    String ES_SOCKET_TIMEOUT_DESC             = "ES_SOCKET_TIMEOUT_DESC";
    String ES_REQUEST_TIMEOUT_LABEL           = "ES_REQUEST_TIMEOUT_LABEL";
    String ES_REQUEST_TIMEOUT_DESC            = "ES_REQUEST_TIMEOUT_DESC";
    String ES_POOL_TIMEOUT_LABEL              = "ES_POOL_TIMEOUT_LABEL";
    String ES_POOL_TIMEOUT_DESC               = "ES_POOL_TIMEOUT_DESC";
    String ES_MAX_CONNECTIONS_LABEL           = "ES_MAX_CONNECTIONS_LABEL";
    String ES_MAX_CONNECTIONS_DESC            = "ES_MAX_CONNECTIONS_DESC";
    String ES_MAX_PER_ROUTE_LABEL             = "ES_MAX_PER_ROUTE_LABEL";
    String ES_MAX_PER_ROUTE_DESC              = "ES_MAX_PER_ROUTE_DESC";
    String ES_METADATA_FAILED                 = "ES_METADATA_FAILED";
    String ES_METADATA_INVALID                = "ES_METADATA_INVALID";
    String ES_IMPLICIT_RESOURCE_UNSUPPORTED   = "ES_IMPLICIT_RESOURCE_UNSUPPORTED";
    String ES_READ_ONLY                       = "ES_READ_ONLY";
    String ES_RESPONSE_SIZE_LIMIT             = "ES_RESPONSE_SIZE_LIMIT";
    String ES_RESULT_SIZE_LIMIT               = "ES_RESULT_SIZE_LIMIT";
    String ES_QUERY_FAILED                    = "ES_QUERY_FAILED";
    String ES_SESSION_BUSY                    = "ES_SESSION_BUSY";
    String ES_RESPONSE_INVALID                = "ES_RESPONSE_INVALID";
    String ES_PARTIAL_RESPONSE                = "ES_PARTIAL_RESPONSE";
    String ES_EXISTS_RESULT                   = "ES_EXISTS_RESULT";
    String ES_SEARCH_RESULT                   = "ES_SEARCH_RESULT";
    String ES_EXECUTION_RESULT                = "ES_EXECUTION_RESULT";
    String ES_RESULT_TRUNCATED                = "ES_RESULT_TRUNCATED";
    String ES_INDICES                         = "ES_INDICES";
    String ES_EXAMPLE_SEARCH                  = "ES_EXAMPLE_SEARCH";
    String ES_EXAMPLE_PUT                     = "ES_EXAMPLE_PUT";
    String ES_EXAMPLE_DELETE                  = "ES_EXAMPLE_DELETE";
    String ES_EXAMPLE_COUNT                   = "ES_EXAMPLE_COUNT";
    String ES_EXAMPLE_MAPPING                 = "ES_EXAMPLE_MAPPING";
    String ES_EXAMPLE_SETTINGS                = "ES_EXAMPLE_SETTINGS";
    String ES_EXAMPLE_ALIASES                 = "ES_EXAMPLE_ALIASES";

    String ES_CURSOR_NOT_ADVANCING            = "ES_CURSOR_NOT_ADVANCING";
    String ES_PIT_CLOSE_FAILED                = "ES_PIT_CLOSE_FAILED";
    String ES_MASKING_UNSUPPORTED             = "ES_MASKING_UNSUPPORTED";
    String ES_CONTINUOUS_RESPONSE_UNSUPPORTED = "ES_CONTINUOUS_RESPONSE_UNSUPPORTED";
    String ES_CONTINUOUS_RESULT               = "ES_CONTINUOUS_RESULT";
    String ES_EXAMPLE_FILTER                  = "ES_EXAMPLE_FILTER";
    String ES_EXAMPLE_AGGREGATION             = "ES_EXAMPLE_AGGREGATION";
}

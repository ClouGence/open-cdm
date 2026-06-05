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
package com.clougence.clouddm.team.provider.dingtalk.constants;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/rdp/sdk/i18n/dingtalk")
public interface DingI18nKeys {

    String DINGTALK_APPROVAL_SERVICES_NAME          = "DINGTALK_APPROVAL_SERVICES_NAME";
    String DINGTALK_LOGIN_SERVICES_NAME             = "DINGTALK_LOGIN_SERVICES_NAME";

    String DINGTALK_APPROVAL_TEMPLATE_NOT_EXISTS    = "DINGTALK_APPROVAL_TEMPLATE_NOT_EXISTS";
    String DINGTALK_APPROVAL_INSTANCE_NOT_EXISTS    = "DINGTALK_APPROVAL_INSTANCE_NOT_EXISTS";
    String DINGTALK_APPROVAL_INSTANCE_IS_EMPTY      = "DINGTALK_APPROVAL_INSTANCE_IS_EMPTY";
    String DINGTALK_APPROVAL_CLOSE_FAILED_TIME      = "DINGTALK_APPROVAL_CLOSE_FAILED_TIME";
    String DINGTALK_CONNECTION_ERROR                = "DINGTALK_CONNECTION_ERROR";
    String DINGTALK_USER_NOT_FIND                   = "DINGTALK_USER_NOT_FIND";
    String DINGTALK_UNKNOWN_ERROR                   = "DINGTALK_UNKNOWN_ERROR";
    String DINGTALK_PAID_API_EXHAUSTION             = "DINGTALK_PAID_API_EXHAUSTION";
    String DINGTALK_QPS_EXHAUSTION                  = "DINGTALK_QPS_EXHAUSTION";
    String DINGTALK_CONFIG_ERROR                    = "DINGTALK_CONFIG_ERROR";
    String DINGTALK_SERVER_INNER_ERROR              = "DINGTALK_SERVER_INNER_ERROR";
    String DINGTALK_ACCESS_PERMISSION_DENY          = "DINGTALK_ACCESS_PERMISSION_DENY";

    String DINGTALK_LOGIN_FAIL_PRIMARY_MISSING_ARGS = "DINGTALK_LOGIN_FAIL_PRIMARY_MISSING_ARGS";
    String DINGTALK_API_TOKEN_ERROR                 = "DINGTALK_API_TOKEN_ERROR";
    String DINGTALK_FETCH_USER_ERROR                = "DINGTALK_FETCH_USER_ERROR";
    String DINGTALK_ROLE_MAPPING_FAILED             = "DINGTALK_ROLE_MAPPING_FAILED";
}

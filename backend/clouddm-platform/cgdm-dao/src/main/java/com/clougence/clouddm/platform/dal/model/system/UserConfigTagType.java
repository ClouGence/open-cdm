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
package com.clougence.clouddm.platform.dal.model.system;

/**
 * @author bucketli 2022/1/10 19:31:09
 */
public enum UserConfigTagType {

    COMMON,

    SECURITY,

    SYS_ALERT,

    USER_INFO,

    USER_PREFER,

    EMAIL_CONFIG,

    LDAP_CONFIG,

    DINGTALK,

    WECHAT,

    FEISHU,

    OIDC,

    CC_FUNCTION,

    CC_SMS_ALERT,

    CC_MOBILE_ALERT,

    CC_IM_ALERT,

    PROJECT,

    QUERY_RESULT,

    @Deprecated
    DATA_EXPORT,
    @Deprecated
    GH_OST
}

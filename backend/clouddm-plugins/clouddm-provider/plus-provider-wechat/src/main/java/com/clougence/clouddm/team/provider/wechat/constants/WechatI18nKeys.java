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
package com.clougence.clouddm.team.provider.wechat.constants;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/dm/sdk/i18n/wechat")
public interface WechatI18nKeys {

    String WECHAT_IM_CONFIG_MISSING_WEBHOOK_URL = "WECHAT_IM_CONFIG_MISSING_WEBHOOK_URL";
    String WECHAT_IM_CONFIG_BAD_WEBHOOK_URL     = "WECHAT_IM_CONFIG_BAD_WEBHOOK_URL";
    String WECHAT_IM_CONFIG_ERROR               = "WECHAT_IM_CONFIG_ERROR";
    String WECHAT_IM_UNSUPPORT_TYPE             = "WECHAT_IM_UNSUPPORT_TYPE";
    String WECHAT_IM_SEND_ERROR                 = "WECHAT_IM_SEND_ERROR";
}

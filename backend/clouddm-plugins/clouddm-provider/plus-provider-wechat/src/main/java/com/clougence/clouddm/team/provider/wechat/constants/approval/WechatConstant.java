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
package com.clougence.clouddm.team.provider.wechat.constants.approval;

public class WechatConstant {

    public static final String WECHAT_CORP_ID          = "wechatApprovalCorpId";
    public static final String WECHAT_TOKEN            = "wechatApprovalToken";
    public static final String WECHAT_ENCODING_AES_KEY = "wechatApprovalEncodingAesKey";

    //callback
    public static final String PUID                    = "puid";
    public static final String CALLBACK_ECHOSTR        = "echostr";
    public static final String CALLBACK_MSG_SIGNATURE  = "msg_signature";
    public static final String CALLBACK_TIMESTAMP      = "timestamp";
    public static final String CALLBACK_NONCE          = "nonce";
    public static final String CALLBACK_REQUEST_BODY   = "requestBody";

    //
    public static final String DEFAULT_ACTIVITY_ID     = "defaultActivityId";
    public static final String DEFAULT_ACTIVITY_TITLE  = "审批人";
    public static final String DEFAULT_TASK_ID         = "00000000";

    public static final String TICKET_TITLE            = "标题：";
    public static final String TICKET_DESCRIPTION      = "需求描述：";
}

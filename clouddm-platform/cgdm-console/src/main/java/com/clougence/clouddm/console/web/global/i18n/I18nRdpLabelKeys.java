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
package com.clougence.clouddm.console.web.global.i18n;

import com.clougence.utils.i18n.I18nResource;

/**
 * @author wanshao create time is 2021/3/1
 **/
@I18nResource("/rdp/static/i18n/label")
public enum I18nRdpLabelKeys {

    // -- for ENV
    DEFAULT_ENV,
    DEFAULT_ENV_DESC,

    // -- for com.clougence.clouddm.platform.dal.model.approval.ApprovalType
    APPROVAL_INTERNAL,
    APPROVAL_DINGTALK,
    APPROVAL_WECHAT,
    APPROVAL_FEISHU,
    APPROVAL_CUSTOM,

    // -- for com.clougence.rdp.controller.model.enumeration.LoginAuthType
    LOGIN_VERIFY,
    LOGIN_PASSWORD,
    LOGIN_LDAP,
    LOGIN_AD,
    LOGIN_DINGTALK,
    LOGIN_FEISHU,
    LOGIN_WECHAT,
    LOGIN_GOOGLE,
    LOGIN_OAUTH2,
    LOGIN_OIDC,
    LOGIN_TAB_PASSWORD,
    LOGIN_TAB_LDAP,
    LOGIN_TAB_AD,
    LOGIN_TAB_DINGTALK,
    LOGIN_TAB_FEISHU,
    LOGIN_TAB_WECHAT,
    LOGIN_TAB_OIDC,

    // -- for com.clougence.clouddm.platform.dal.model.approval.ApprovalStage
    TICKET_STAGE_EXPLAIN,
    TICKET_STAGE_APPROVAL,
    TICKET_STAGE_CONFIRM,
    TICKET_STAGE_EXECUTION,

    // auth
    AUTH_TICKET_TARGET,
}

package com.clougence.rdp.constant;

import com.clougence.utils.i18n.I18nResource;

/**
 * @author wanshao create time is 2021/3/1
 **/
@I18nResource("/rdp/static/i18n/label")
public enum I18nRdpLabelKeys {

    // -- for ENV
    DEFAULT_ENV,
    DEFAULT_ENV_DESC,

    // -- for com.clougence.rdp.dal.enumeration.RdpApprovalType
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

    // -- for com.clougence.rdp.dal.enumeration.RdpTicketStage
    TICKET_STAGE_EXPLAIN,
    TICKET_STAGE_APPROVAL,
    TICKET_STAGE_CONFIRM,
    TICKET_STAGE_EXECUTION,

    // auth
    AUTH_TICKET_TARGET,
}

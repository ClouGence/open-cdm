package com.clougence.rdp.global.config.user;

import com.clougence.clouddm.base.metadata.rdp.enumeration.SchemaLessValueFormat;
import com.clougence.rdp.constant.I18nUserConfigMsgKeys;
import com.clougence.rdp.constant.KvConfValType;
import com.clougence.rdp.constant.UserConfigDef;
import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.rdp.controller.model.enumeration.LoginAuthType;
import com.clougence.rdp.dal.enumeration.ConfBelong;
import com.clougence.rdp.service.enumeration.AlertImType;
import com.clougence.rdp.service.enumeration.AlertSmsType;
import com.clougence.rdp.service.enumeration.AlertVoiceType;
import com.clougence.rdp.service.enumeration.LogExportStorageType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author bucketli 2022/1/10 18:34:57
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDefinedConfig {

    /**
     * RDP subAccount config
     */
    @UserConfigDef(name = "forbidDelSubAccount", defaultValue = "false", descKey = I18nUserConfigMsgKeys.USER_RDP_FORBID_DELETE_SUB_ACCOUNT, configTagType = UserConfigTagType.SECURITY, confBelong = ConfBelong.Common, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean               forbidDelSubAccount;

    @UserConfigDef(name = "subAccountPwdExpireDays", descKey = I18nUserConfigMsgKeys.USER_RDP_SUB_ACCOUNT_PWD_EXPIRED_DAYS, configTagType = UserConfigTagType.SECURITY, confBelong = ConfBelong.Common)
    private Integer               subAccountPwdExpireDays;

    @UserConfigDef(name = "subAccountPwdVerifyExpr", descKey = I18nUserConfigMsgKeys.USER_RDP_SUB_ACCOUNT_PWD_VERIFY_EXPR, configTagType = UserConfigTagType.SECURITY, confBelong = ConfBelong.Common)
    private String                subAccountPwdVerifyExpr;

    @UserConfigDef(name = "subAccountPwdVerifyTips", descKey = I18nUserConfigMsgKeys.USER_RDP_SUB_ACCOUNT_PWD_VERIFY_TIPS, configTagType = UserConfigTagType.SECURITY, confBelong = ConfBelong.Common)
    private String                subAccountPwdVerifyTips;

    @UserConfigDef(name = "subAccountAuthType", defaultValue = "PASSWORD", descKey = I18nUserConfigMsgKeys.USER_RDP_SUB_ACCOUNT_AUTH_TYPE, valueRange = "PASSWORD / LDAP / AD / DingTalk / Feishu / Wechat / OIDC", configTagType = UserConfigTagType.SECURITY, confBelong = ConfBelong.Common)
    private LoginAuthType         subAccountAuthType;

    @UserConfigDef(name = "defaultLanguage", defaultValue = "zh_CN", descKey = I18nUserConfigMsgKeys.USER_RDP_DEFAULT_LANGUAGE, valueRange = "en_US / zh_CN", configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.Common)
    private String                defaultLanguage;

    /**
     * RDP Sub Account Login
     */

    @UserConfigDef(name = "ldapHost", descKey = I18nUserConfigMsgKeys.LDAP_CONFIG_HOST, configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapHost;

    @UserConfigDef(name = "ldapPort", descKey = I18nUserConfigMsgKeys.LDAP_CONFIG_PORT, valueRange = "LDAP:389 /AD:3268", configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapPort;

    @UserConfigDef(name = "ldapNetBIOSRoute", descKey = I18nUserConfigMsgKeys.LDAP_CONFIG_NET_BIOS_ROUTE, valueRange = "Name=IP;Name=IP;", configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapNetBIOSRoute;

    @UserConfigDef(name = "ldapSoTimeout", defaultValue = "3000", descKey = I18nUserConfigMsgKeys.LDAP_CONFIG_SOCKET_TIMEOUT, configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapSoTimeout;

    @UserConfigDef(name = "ldapBase", descKey = I18nUserConfigMsgKeys.LDAP_CONFIG_BASE, configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapBase;

    @UserConfigDef(name = "ldapUser", descKey = I18nUserConfigMsgKeys.LDAP_CONFIG_USER, configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapUser;

    @UserConfigDef(name = "ldapPassword", descKey = I18nUserConfigMsgKeys.LDAP_CONFIG_PASSWORD, configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapPassword;

    @UserConfigDef(name = "ldapDomain", descKey = I18nUserConfigMsgKeys.LDAP_CONFIG_DOMAIN, configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapDomain;

    @UserConfigDef(name = "ldapRoleMap", defaultValue = "Developers", descKey = I18nUserConfigMsgKeys.LDAP_CONFIG_ROLE_MAP, configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapRoleMap;

    @UserConfigDef(name = "ldapUserObjectClass", defaultValue = "posixAccount,sambaSamAccount", descKey = I18nUserConfigMsgKeys.LDAP_CONFIG_USER_OBJECT_CLASS, valueRange = "posixAccount / sambaSamAccount / ", configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapUserObjectClass;

    @UserConfigDef(name = "ldapFieldLogin", defaultValue = "cn", descKey = I18nUserConfigMsgKeys.LDAP_FIELD_LOGIN, valueRange = "cn / userPrincipalName / ...", configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapFieldLogin;

    @UserConfigDef(name = "ldapFieldUser", defaultValue = "sn", descKey = I18nUserConfigMsgKeys.LDAP_FIELD_USER, valueRange = "sn / displayName / ...", configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapFieldUser;

    @UserConfigDef(name = "ldapFieldEmail", defaultValue = "mail", descKey = I18nUserConfigMsgKeys.LDAP_FIELD_EMAIL, valueRange = "mail / ...", configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapFieldEmail;

    @UserConfigDef(name = "ldapFieldPhone", defaultValue = "mobile", descKey = I18nUserConfigMsgKeys.LDAP_FIELD_PHONE, valueRange = "mobile / telephoneNumber / ...", configTagType = UserConfigTagType.LDAP_CONFIG, confBelong = ConfBelong.Common)
    private String                ldapFieldPhone;

    @UserConfigDef(name = "dingLoginConfigAk", descKey = I18nUserConfigMsgKeys.DING_CONFIG_LOGIN_AK, configTagType = UserConfigTagType.DINGTALK, confBelong = ConfBelong.Common)
    private String                dingLoginConfigAk;

    @UserConfigDef(name = "dingLoginConfigSk", descKey = I18nUserConfigMsgKeys.DING_CONFIG_LOGIN_SK, configTagType = UserConfigTagType.DINGTALK, confBelong = ConfBelong.Common)
    private String                dingLoginConfigSk;

    @UserConfigDef(name = "dingLoginRoleMap", defaultValue = "Developers", descKey = I18nUserConfigMsgKeys.DING_CONFIG_LOGIN_ROLE_MAP, configTagType = UserConfigTagType.DINGTALK, confBelong = ConfBelong.Common)
    private String                dingLoginRoleMap;

    @UserConfigDef(name = "feishuLoginAppID", descKey = I18nUserConfigMsgKeys.FEISHU_CONFIG_LOGIN_APP_ID, configTagType = UserConfigTagType.FEISHU, confBelong = ConfBelong.Common)
    private String                feishuLoginAppID;

    @UserConfigDef(name = "feishuLoginAppSecret", descKey = I18nUserConfigMsgKeys.FEISHU_CONFIG_LOGIN_APP_SECRET, configTagType = UserConfigTagType.FEISHU, confBelong = ConfBelong.Common)
    private String                feishuLoginAppSecret;

    @UserConfigDef(name = "feishuLoginApiTimeoutSec", defaultValue = "5", descKey = I18nUserConfigMsgKeys.FEISHU_CONFIG_LOGIN_API_TIMEOUTSEC, configTagType = UserConfigTagType.FEISHU, confBelong = ConfBelong.Common)
    private Integer               feishuLoginApiTimeoutSec;

    @UserConfigDef(name = "feishuLoginRoleMap", defaultValue = "Developers", descKey = I18nUserConfigMsgKeys.FEISHU_CONFIG_LOGIN_ROLE_MAP, configTagType = UserConfigTagType.FEISHU, confBelong = ConfBelong.Common)
    private String                feishuLoginRoleMap;

    @UserConfigDef(name = "wechatLoginCorpId", descKey = I18nUserConfigMsgKeys.WECHAT_CONFIG_LOGIN_CORP_ID, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatLoginCorpId;

    @UserConfigDef(name = "wechatLoginSecret", descKey = I18nUserConfigMsgKeys.WECHAT_CONFIG_LOGIN_SECRET, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatLoginSecret;

    @UserConfigDef(name = "wechatLoginAgentId", descKey = I18nUserConfigMsgKeys.WECHAT_CONFIG_LOGIN_AGENT_ID, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatLoginAgentId;

    @UserConfigDef(name = "wechatLoginRoleMap", defaultValue = "Developers", descKey = I18nUserConfigMsgKeys.WECHAT_CONFIG_LOGIN_ROLE_MAP, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatLoginRoleMap;

    @UserConfigDef(name = "oidcLoginWellKnownUrl", descKey = I18nUserConfigMsgKeys.OIDC_CONFIG_LOGIN_WELL_KNOWN_URL, configTagType = UserConfigTagType.OIDC, confBelong = ConfBelong.Common)
    private String                oidcLoginWellKnownUrl;

    @UserConfigDef(name = "oidcLoginClientId", descKey = I18nUserConfigMsgKeys.OIDC_CONFIG_LOGIN_CLIENT_ID, configTagType = UserConfigTagType.OIDC, confBelong = ConfBelong.Common)
    private String                oidcLoginClientId;

    @UserConfigDef(name = "oidcLoginClientSecret", descKey = I18nUserConfigMsgKeys.OIDC_CONFIG_LOGIN_CLIENT_SECRET, configTagType = UserConfigTagType.OIDC, confBelong = ConfBelong.Common)
    private String                oidcLoginClientSecret;

    @UserConfigDef(name = "oidcLoginScope", defaultValue = "openid email phone roles", descKey = I18nUserConfigMsgKeys.OIDC_CONFIG_LOGIN_SCOPE, configTagType = UserConfigTagType.OIDC, confBelong = ConfBelong.Common)
    private String                oidcLoginScope;

    @UserConfigDef(name = "oidcLoginRoleMap", defaultValue = "Developers", descKey = I18nUserConfigMsgKeys.OIDC_CONFIG_LOGIN_ROLE_MAP, configTagType = UserConfigTagType.OIDC, confBelong = ConfBelong.Common)
    private String                oidcLoginRoleMap;

    /**
     * RDP Email config
     */

    @UserConfigDef(name = "emailHost", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_SMTP_HOST, valueRange = "smtp.gmail.com / smtp.office365.com / ...", configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common)
    private String                emailHost;

    @UserConfigDef(name = "emailPort", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_SMTP_PORT, valueRange = "25 / 465 / 587", configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common)
    private Integer               emailPort;

    @UserConfigDef(name = "emailUserName", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_SENDER_USERNAME, configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common)
    private String                emailUserName;

    @UserConfigDef(name = "emailPwd", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_SENDER_PASSWORD, configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common)
    private String                emailPwd;

    @UserConfigDef(name = "emailDisplay", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_SENDER_DISPLAY, configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common)
    private String                emailDisplay;

    @UserConfigDef(name = "emailFrom", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_SENDER_FROM, configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common)
    private String                emailFrom;

    @UserConfigDef(name = "emailSmtpAuth", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_SMTP_AUTH, valueRange = "true / false", configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean               emailSmtpAuth;

    @UserConfigDef(name = "emailEnableTls", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_SMTP_ENABLE_TLS, valueRange = "true / false", configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean               emailEnableTls;

    @UserConfigDef(name = "emailRequiredTls", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_SMTP_REQUIRED_TLS, valueRange = "true / false", configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean               emailRequiredTls;

    @UserConfigDef(name = "emailEnableSsl", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_SMTP_ENABLE_SSL, valueRange = "true / false", configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean               emailEnableSsl;

    @UserConfigDef(name = "emailProtocol", descKey = I18nUserConfigMsgKeys.EMAIL_CONFIG_TRANSFER_PROTOCOL, valueRange = "smtps / smtp", configTagType = UserConfigTagType.EMAIL_CONFIG, confBelong = ConfBelong.Common)
    private String                emailProtocol;

    /**
     * RDP Approval
     */

    @UserConfigDef(name = "updateApprovalStatusIntervalTime", descKey = I18nUserConfigMsgKeys.APPROVAL_UPDATE_INTERVAL_TIME, defaultValue = "86400", configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.Common, valueRange = "0 - 2592000")
    private String                updateApprovalStatusIntervalTime;

    @UserConfigDef(name = "dingEnableApprovalService", defaultValue = "false", descKey = I18nUserConfigMsgKeys.DING_ENABLE_APPROVAL_SERVICE, configTagType = UserConfigTagType.DINGTALK, confBelong = ConfBelong.Common, kvConfWebOp = KvConfValType.BOOLEAN)
    private String                dingEnableApprovalService;

    @UserConfigDef(name = "dingApprovalConfigAk", descKey = I18nUserConfigMsgKeys.DING_CONFIG_APPROVAL_AK, configTagType = UserConfigTagType.DINGTALK, confBelong = ConfBelong.Common)
    private String                dingApprovalConfigAk;

    @UserConfigDef(name = "dingApprovalConfigSk", descKey = I18nUserConfigMsgKeys.DING_CONFIG_APPROVAL_SK, configTagType = UserConfigTagType.DINGTALK, confBelong = ConfBelong.Common)
    private String                dingApprovalConfigSk;

    @UserConfigDef(name = "feishuEnableApprovalService", defaultValue = "false", descKey = I18nUserConfigMsgKeys.FEISHU_ENABLE_APPROVAL_SERVICE, configTagType = UserConfigTagType.FEISHU, confBelong = ConfBelong.Common, kvConfWebOp = KvConfValType.BOOLEAN)
    private String                feishuEnableApprovalService;

    @UserConfigDef(name = "feishuApprovalAppID", descKey = I18nUserConfigMsgKeys.FEISHU_CONFIG_APPROVAL_APP_ID, configTagType = UserConfigTagType.FEISHU, confBelong = ConfBelong.Common)
    private String                feishuApprovalAppID;

    @UserConfigDef(name = "feishuApprovalAppSecret", descKey = I18nUserConfigMsgKeys.FEISHU_CONFIG_APPROVAL_APP_SECRET, configTagType = UserConfigTagType.FEISHU, confBelong = ConfBelong.Common)
    private String                feishuApprovalAppSecret;

    @UserConfigDef(name = "feishuApprovalApiTimeoutSec", defaultValue = "5", descKey = I18nUserConfigMsgKeys.FEISHU_CONFIG_APPROVAL_API_TIMEOUTSEC, configTagType = UserConfigTagType.FEISHU, confBelong = ConfBelong.Common)
    private Integer               feishuApprovalApiTimeoutSec;

    @UserConfigDef(name = "feishuApprovalTemplateList", descKey = I18nUserConfigMsgKeys.FEISHU_CONFIG_APPROVAL_TEMPLATE_CODE, configTagType = UserConfigTagType.FEISHU, confBelong = ConfBelong.Common)
    private String                feishuApprovalTemplateList;

    @UserConfigDef(name = "wechatEnableApprovalService", defaultValue = "false", descKey = I18nUserConfigMsgKeys.WECHAT_ENABLE_APPROVAL_SERVICE, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common, kvConfWebOp = KvConfValType.BOOLEAN)
    private String                wechatEnableApprovalService;

    @UserConfigDef(name = "wechatApprovalCorpId", descKey = I18nUserConfigMsgKeys.WECHAT_APPROVAL_CORP_ID, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatApprovalCorpId;

    @UserConfigDef(name = "wechatApprovalSecret", descKey = I18nUserConfigMsgKeys.WECHAT_APPROVAL_SECRET, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatApprovalSecret;

    @UserConfigDef(name = "wechatApprovalAgentId", descKey = I18nUserConfigMsgKeys.WECHAT_APPROVAL_AGENT_ID, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatApprovalAgentId;

    @UserConfigDef(name = "wechatApprovalToken", descKey = I18nUserConfigMsgKeys.WECHAT_APPROVAL_TOKEN, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatApprovalToken;

    @UserConfigDef(name = "wechatApprovalEncodingAesKey", descKey = I18nUserConfigMsgKeys.WECHAT_APPROVAL_ENCODING_AES_KEY, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatApprovalEncodingAesKey;

    @UserConfigDef(name = "wechatApprovalTemplateList", descKey = I18nUserConfigMsgKeys.WECHAT_APPROVAL_TEMPLATE_LIST, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatApprovalTemplateList;

    @UserConfigDef(name = "wechatApprovalTemplateLang", descKey = I18nUserConfigMsgKeys.WECHAT_APPROVAL_TEMPLATE_LANG, configTagType = UserConfigTagType.WECHAT, confBelong = ConfBelong.Common)
    private String                wechatApprovalTemplateLang;

    // for CloudDM

    /**
     * CloudDM project
     */

    // for CI/CD
    @UserConfigDef(name = "defaultProjectSpace", defaultValue = "default", descKey = I18nUserConfigMsgKeys.PROJECT_DEFAULT_PROJECT_SPACE, valueRange = "absolute path or relative path (APP_HOME)", configTagType = UserConfigTagType.PROJECT, confBelong = ConfBelong.CloudDM)
    private String                defaultProjectSpace;
    @UserConfigDef(name = "defaultTempSpace", defaultValue = "temporary", descKey = I18nUserConfigMsgKeys.PROJECT_DEFAULT_TEMP_SPACE, valueRange = "absolute path or relative path (APP_HOME)", configTagType = UserConfigTagType.PROJECT, confBelong = ConfBelong.CloudDM)
    private String                defaultTempSpace;
    @UserConfigDef(name = "scmMaxFailedTimes", defaultValue = "3", descKey = I18nUserConfigMsgKeys.PROJECT_SCM_MAX_FAILED_TIMES, valueRange = "max retry times.", configTagType = UserConfigTagType.PROJECT, confBelong = ConfBelong.CloudDM)
    private Integer               scmMaxFailedTimes;

    // for SQL Console
    @UserConfigDef(name = "dmEnableMCP", defaultValue = "false", descKey = I18nUserConfigMsgKeys.AI_DM_ENABLE_MCP, valueRange = "true / false", configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.CloudDM)
    private Boolean               dmEnableMCP;
    @UserConfigDef(name = "sqlAuditRetentionDays", defaultValue = "30", descKey = I18nUserConfigMsgKeys.SQL_AUDIT_RETENTION_DAYS, valueRange = "1 - 60", configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.CloudDM)
    private String                sqlAuditRetentionDays;
    @UserConfigDef(name = "defaultColumnDisplayChars", defaultValue = "250", valueRange = "10~500, default 250", descKey = I18nUserConfigMsgKeys.SQL_RESULT_COL_MAXIMUM_DISPLAY_SIZE, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Integer               defaultColumnDisplayChars;
    @UserConfigDef(name = "consoleMetadataCache", defaultValue = "false", valueRange = "true, false", descKey = I18nUserConfigMsgKeys.SQL_RESULT_METADATA_CACHE, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Boolean               consoleMetadataCache;

    // for Console Query Result
    @UserConfigDef(name = "onlineSelectRewriteDisable", defaultValue = "false", valueRange = "true, false", descKey = I18nUserConfigMsgKeys.SQL_RESULT_SELECT_REWRITE_DISABLE, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Boolean               onlineSelectRewriteDisable;
    @UserConfigDef(name = "onlineResultCacheTimeoutSec", defaultValue = "300", valueRange = "0 ~ 43200", descKey = I18nUserConfigMsgKeys.SQL_RESULT_CACHE_TIMEOUT_SEC, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Integer               onlineResultCacheTimeoutSec;
    @UserConfigDef(name = "onlineMaxRecordCount", defaultValue = "10000", valueRange = "1 ~ 1000000, default 10000 records.", descKey = I18nUserConfigMsgKeys.SQL_RESULT_ONLINE_MAX_RECORD_COUNT, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Integer               onlineMaxRecordCount;
    @UserConfigDef(name = "onlineMaxResultSetMegaByte", defaultValue = "60", valueRange = "4~1024 MB, default 60MB", descKey = I18nUserConfigMsgKeys.SQL_RESULT_ONLINE_MAX_RESULT_SET_MB, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Integer               onlineMaxResultSetMegaByte;
    @UserConfigDef(name = "onlineMaxColumnMegaByte", defaultValue = "1", valueRange = "1~16 MB, default 1MB", descKey = I18nUserConfigMsgKeys.SQL_RESULT_ONLINE_MAX_COLUMN_MB, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Integer               onlineMaxColumnMegaByte;
    @UserConfigDef(name = "onlineMaxElementMegaByte", defaultValue = "1", valueRange = "1~16 MB, 'default 1MB", descKey = I18nUserConfigMsgKeys.SQL_RESULT_ONLINE_MAX_ELEMENT_MB, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Integer               onlineMaxElementMegaByte;

    // for other Query Result
    @UserConfigDef(name = "taskMaxRecordCount", defaultValue = "-1", valueRange = "'-1' wont limit, default no limit.", descKey = I18nUserConfigMsgKeys.SQL_RESULT_TASK_MAX_RECORD_COUNT, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Integer               taskMaxRecordCount;
    @UserConfigDef(name = "taskMaxResultSetMegaByte", defaultValue = "1024", valueRange = "-1 ~ 2147483647 MB, '-1' wont limit, default 1GB", descKey = I18nUserConfigMsgKeys.SQL_RESULT_TASK_MAX_RESULT_SET_MB, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Integer               taskMaxResultSetMegaByte;
    @UserConfigDef(name = "taskMaxColumnMegaByte", defaultValue = "4", valueRange = "-1 ~ 2147483647 MB, '-1' wont limit, default 4MB", descKey = I18nUserConfigMsgKeys.SQL_RESULT_TASK_MAX_COLUMN_MB, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Integer               taskMaxColumnMegaByte;
    @UserConfigDef(name = "taskMaxElementMegaByte", defaultValue = "1", valueRange = "-1 ~ 2147483647 MB, '-1' wont limit, default 1MB", descKey = I18nUserConfigMsgKeys.SQL_RESULT_TASK_MAX_ELEMENT_MB, configTagType = UserConfigTagType.QUERY_RESULT, confBelong = ConfBelong.CloudDM)
    private Integer               taskMaxElementMegaByte;

    // from CloudCanal

    /**
     * CloudCanal IM config
     */

    @UserConfigDef(name = "alertImType", descKey = I18nUserConfigMsgKeys.USER_CONFIG_ALERT_IM_TYPE, valueRange = "dingtalk / weixin / feishu / slack / discord / custom", configTagType = UserConfigTagType.CC_IM_ALERT, confBelong = ConfBelong.CloudCanal)
    private AlertImType           alertImType;

    @UserConfigDef(name = "defaultImAlertUrl", descKey = I18nUserConfigMsgKeys.USER_CONFIG_DEFAULT_IM_ALERT_URL, configTagType = UserConfigTagType.CC_IM_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                defaultImAlertUrl;

    @UserConfigDef(name = "criticalImAlertUrl", descKey = I18nUserConfigMsgKeys.USER_CONFIG_CRITICAL_IM_ALERT_URL, configTagType = UserConfigTagType.CC_IM_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                criticalImAlertUrl;

    @UserConfigDef(name = "webHookProxyHost", descKey = I18nUserConfigMsgKeys.USER_CONFIG_WEBHOOK_PROXY_HOST, configTagType = UserConfigTagType.CC_IM_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                webHookProxyHost;

    @UserConfigDef(name = "imAlertAtAll", defaultValue = "false", descKey = I18nUserConfigMsgKeys.USER_CONFIG_IM_ALERT_AT_ALL, configTagType = UserConfigTagType.CC_IM_ALERT, confBelong = ConfBelong.CloudCanal, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean               imAlertAtAll;

    /**
     * CloudCanal SMS config
     */

    @UserConfigDef(name = "alertSmsType", defaultValue = "alibaba_cloud", descKey = I18nUserConfigMsgKeys.USER_CONFIG_ALERT_SMS_TYPE, valueRange = "alibaba_cloud", configTagType = UserConfigTagType.CC_SMS_ALERT, confBelong = ConfBelong.CloudCanal)
    private AlertSmsType          alertSmsType;

    @UserConfigDef(name = "smsSignName", descKey = I18nUserConfigMsgKeys.USER_CONFIG_SMS_SIGN_NAME, valueRange = "Certified sms sender name", configTagType = UserConfigTagType.CC_SMS_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                smsSignName;

    @UserConfigDef(name = "taskErrorSmsTc", descKey = I18nUserConfigMsgKeys.USER_CONFIG_TASK_ERROR_SMS_TC, valueRange = "SMS template code, content e.g.,CloudCanal DataJob error,message:${errMsg}", configTagType = UserConfigTagType.CC_SMS_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                taskErrorSmsTc;

    @UserConfigDef(name = "taskRecoverSmsTc", descKey = I18nUserConfigMsgKeys.USER_CONFIG_TASK_RECOVER_SMS_TC, valueRange = "SMS template code, content e.g.,CloudCanal DataJob recover,message:${msg}", configTagType = UserConfigTagType.CC_SMS_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                taskRecoverSmsTc;

    @UserConfigDef(name = "smsAccessKey", descKey = I18nUserConfigMsgKeys.USER_CONFIG_SMS_ACCESS_KEY, valueRange = "Access key for SMS service", configTagType = UserConfigTagType.CC_SMS_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                smsAccessKey;

    @UserConfigDef(name = "smsSecretKey", descKey = I18nUserConfigMsgKeys.USER_CONFIG_SMS_SECRET_KEY, valueRange = "Secret key for SMS service", configTagType = UserConfigTagType.CC_SMS_ALERT, confBelong = ConfBelong.CloudCanal, isSecret = true)
    private String                smsSecretKey;

    /**
     * CloudCanal Voice config
     */

    @UserConfigDef(name = "alertMobileType", defaultValue = "alibaba_cloud", descKey = I18nUserConfigMsgKeys.USER_CONFIG_ALERT_MOBILE_TYPE, valueRange = "alibaba_cloud", configTagType = UserConfigTagType.CC_MOBILE_ALERT, confBelong = ConfBelong.CloudCanal)
    private AlertVoiceType        alertMobileType;

    @UserConfigDef(name = "taskErrorMobileTc", descKey = I18nUserConfigMsgKeys.USER_CONFIG_TASK_ERROR_MOBILE_TC, valueRange = "Mobile template code, content e.g.,CloudCanal DataJob delay ${delaySecond} seconds,numberic ID:${jobId}", configTagType = UserConfigTagType.CC_MOBILE_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                taskErrorMobileTc;

    @UserConfigDef(name = "taskRecoverMobileTc", descKey = I18nUserConfigMsgKeys.USER_CONFIG_TASK_RECOVER_MOBILE_TC, valueRange = "Mobile template code, content e.g.,CloudCanal DataJob recovery from delay,numberic ID:${jobId}", configTagType = UserConfigTagType.CC_MOBILE_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                taskRecoverMobileTc;

    @UserConfigDef(name = "mobileAccessKey", descKey = I18nUserConfigMsgKeys.USER_CONFIG_MOBILE_ACCESS_KEY, valueRange = "Access key for mobile service", configTagType = UserConfigTagType.CC_MOBILE_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                mobileAccessKey;

    @UserConfigDef(name = "mobileSecretKey", descKey = I18nUserConfigMsgKeys.USER_CONFIG_MOBILE_SECRET_KEY, valueRange = "Secret key for mobile service", configTagType = UserConfigTagType.CC_MOBILE_ALERT, confBelong = ConfBelong.CloudCanal, isSecret = true)
    private String                mobileSecretKey;

    @UserConfigDef(name = "mobileProxyAddr", descKey = I18nUserConfigMsgKeys.USER_CONFIG_MOBILE_PROXY_ADDR, valueRange = "http://ip:port or https://ip:port", configTagType = UserConfigTagType.CC_MOBILE_ALERT, confBelong = ConfBelong.CloudCanal)
    private String                mobileProxyAddr;

    /**
     * CloudCanal function config
     */

    @UserConfigDef(name = "taskAlertInhibitMin", defaultValue = "5", descKey = I18nUserConfigMsgKeys.USER_CONFIG_TASK_ALERT_INHIBIT_MIN, valueRange = "1 ~ n", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private Integer               taskAlertInhibitMin;

    @Deprecated // see jobKvConfTempl instead
    @UserConfigDef(name = "jobKvConfTemplate", descKey = I18nUserConfigMsgKeys.USER_CONFIG_DATA_JOB_KV_CONFIG_TEMPLATE, valueRange = "LE_256MB_TASK / GE_3GB_TASK / SR_DORIS_TARGET_HIGH_PERF_LV1 / SR_DORIS_TARGET_HIGH_PERF_LV2 / FULL_GC_OPTIMIZE_LV1 / FULL_GC_OPTIMIZE_LV2", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                jobKvConfTemplate;

    @UserConfigDef(name = "defaultMsgFormat", defaultValue = "CLOUDCANAL_JSON_FOR_MQ", descKey = I18nUserConfigMsgKeys.USER_CONFIG_DEFAULT_MSG_FORMAT, valueRange = "CLOUDCANAL_JSON_FOR_MQ / CANAL_JSON_FOR_MQ / ALIYUN_DTS_AVRO_FOR_MQ / DEBEZIUM_ENVELOP_JSON_FOR_MQ / ORIGIN_MSG_FOR_MQ / DSG_JSON_FOR_MQ", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                defaultMsgFormat;

    @UserConfigDef(name = "targetExtendMsgFormat", descKey = I18nUserConfigMsgKeys.USER_CONFIG_TARGET_EXTEND_MSG_FORMAT, valueRange = "Example: 'ALIYUN_DTS_AVRO_FOR_MQ_V1,ALIYUN_DTS_AVRO_FOR_MQ_V2' (Multiple separated by ',')", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                targetExtendMsgFormat;

    @UserConfigDef(name = "keyConflictStrategy", descKey = I18nUserConfigMsgKeys.USER_CONFIG_KEY_CONFLICT_STRATEGY, valueRange = "IGNORE / REPLACE", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                keyConflictStrategy;

    @UserConfigDef(name = "increParallelApplyStrategy", descKey = I18nUserConfigMsgKeys.USER_CONFIG_KEY_CONFLICT_STRATEGY, valueRange = "KEY / TABLE / KEY_UPGRADE_TABLE", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                increParallelApplyStrategy;

    @UserConfigDef(name = "originalDecodeFormat", descKey = I18nUserConfigMsgKeys.USER_CONFIG_ORIGIN_DECODE_MSG_FORMAT_DESCRIPTION, valueRange = "CLOUDCANAL_JSON_FOR_MQ / CANAL_JSON_FOR_MQ", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private SchemaLessValueFormat originalDecodeFormat;

    @UserConfigDef(name = "extraDDL", descKey = I18nUserConfigMsgKeys.USER_CONFIG_MYSQL_INCR_EXTRA_DDL_MODE_DESCRIPTION, valueRange = "NONE / PT / GHOST / ALI_DMS / PT_GHOST", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                extraDDL;

    @UserConfigDef(name = "dbHeartbeatEnable", defaultValue = "false", descKey = I18nUserConfigMsgKeys.USER_CONFIG_DB_HEART_BEAT_ENABLE_DESCRIPTION, valueRange = "true / false", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean               dbHeartbeatEnable;

    @UserConfigDef(name = "pkSeparator", descKey = I18nUserConfigMsgKeys.USER_CONFIG_ES_PK_SEPARATOR_DESCRIPTION, valueRange = "$ / _ / - / ...", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                pkSeparator;

    @UserConfigDef(name = "workerAutoFetchExternalIp", defaultValue = "false", descKey = I18nUserConfigMsgKeys.USER_CONFIG_WORKER_AUTO_FETCH_EXTERNAL_IP, valueRange = "true / false", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean               workerAutoFetchExternalIp;

    //    @UserConfigDef(name = "jobTableDefaultSelectAll", defaultValue = "false", descKey = I18nUserConfigMsgKeys.CREATE_JOB_TABLE_DEFAULT_SELECT_ALL, valueRange = "true / false", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal, kvConfWebOp = KvConfValType.BOOLEAN)
    //    private Boolean               jobTableDefaultSelectAll;

    @UserConfigDef(name = "tableSelectMode", defaultValue = "LIST_ALL_AND_NOT_SELECT", descKey = I18nUserConfigMsgKeys.CREATE_JOB_TABLE_SELECT_MODE, valueRange = "LIST_ALL_AND_SELECT / LIST_ALL_AND_NOT_SELECT / SEARCH_AND_NOT_SELECT", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal, kvConfWebOp = KvConfValType.TEXT)
    private String                tableSelectMode;

    @UserConfigDef(name = "queryUksWhenTableSelect", defaultValue = "false", descKey = I18nUserConfigMsgKeys.CREATE_JOB_QUERY_TABLE_UK, valueRange = "true / false", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean               queryUksWhenTableSelect;

    @UserConfigDef(name = "parallelFetchMetaBatchSize", defaultValue = "5000", descKey = I18nUserConfigMsgKeys.PARALLEL_FETCH_META_BATCH_SIZE, valueRange = "1 ~ n", configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.CloudCanal)
    private Boolean               parallelFetchMetaBatchSize;

    @UserConfigDef(name = "migrationBucketNumber", defaultValue = "4", descKey = I18nUserConfigMsgKeys.MIGRATION_BUCKET_NUMBER, valueRange = "1 ~ n", configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.CloudCanal)
    private String                migrationBucketNumber;

    @UserConfigDef(name = "migrationPropertiesConfig", defaultValue = "PROPERTIES(\"replication_num\" = \"1\")", descKey = I18nUserConfigMsgKeys.MIGRATION_PROPERTIES_CONFIG, configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.CloudCanal)
    private String                migrationPropertiesConfig;

    @UserConfigDef(name = "icebergMigrationPropertiesConfig", defaultValue = "{\"format-version\": \"2\"}", descKey = I18nUserConfigMsgKeys.MIGRATION_PROPERTIES_CONFIG, configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.CloudCanal)
    private String                icebergMigrationPropertiesConfig;

    @UserConfigDef(name = "paimonMigrationPropertiesConfig", defaultValue = "{}", descKey = I18nUserConfigMsgKeys.MIGRATION_PROPERTIES_CONFIG, configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.CloudCanal)
    private String                paimonMigrationPropertiesConfig;

    @UserConfigDef(name = "deltaLakeMigrationProps", defaultValue = "{}", descKey = I18nUserConfigMsgKeys.MIGRATION_PROPERTIES_CONFIG, configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.CloudCanal)
    private String                deltaLakeMigrationProps;

    /**
     * Cloudcanal system config
     */

    @UserConfigDef(name = "defaultClusterName", descKey = I18nUserConfigMsgKeys.USER_CONFIG_DEFAULT_CLUSTER_NAME, configTagType = UserConfigTagType.COMMON, confBelong = ConfBelong.CloudCanal)
    private String                defaultClusterName;

    @UserConfigDef(name = "logExportStorageType", descKey = I18nUserConfigMsgKeys.USER_CONFIG_LOG_EXPORT_STORAGE_TYPE, valueRange = "OSS / S3", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private LogExportStorageType  logExportStorageType;

    @UserConfigDef(name = "logExportOssBucket", descKey = I18nUserConfigMsgKeys.USER_CONFIG_LOG_EXPORT_OSS_BUCKET, configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                logExportOssBucket;

    @UserConfigDef(name = "logExportOssEndPoint", descKey = I18nUserConfigMsgKeys.USER_CONFIG_LOG_EXPORT_OSS_ENDPOINT, configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                logExportOssEndPoint;

    @UserConfigDef(name = "logExportOssAk", descKey = I18nUserConfigMsgKeys.USER_CONFIG_LOG_EXPORT_OSS_AK, configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                logExportOssAk;

    @UserConfigDef(name = "logExportOssSk", descKey = I18nUserConfigMsgKeys.USER_CONFIG_LOG_EXPORT_OSS_SK, isSecret = true, configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                logExportOssSk;

    @UserConfigDef(name = "logExportS3Bucket", descKey = I18nUserConfigMsgKeys.USER_CONFIG_LOG_EXPORT_S3_BUCKET, configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                logExportS3Bucket;

    @UserConfigDef(name = "logExportS3Region", descKey = I18nUserConfigMsgKeys.USER_CONFIG_LOG_EXPORT_S3_REGION, configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                logExportS3Region;

    @UserConfigDef(name = "logExportS3Ak", descKey = I18nUserConfigMsgKeys.USER_CONFIG_LOG_EXPORT_S3_AK, configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                logExportS3Ak;

    @UserConfigDef(name = "logExportS3Sk", descKey = I18nUserConfigMsgKeys.USER_CONFIG_LOG_EXPORT_S3_SK, isSecret = true, configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private String                logExportS3Sk;

    @UserConfigDef(name = "logExportTimeoutSec", defaultValue = "300", descKey = I18nUserConfigMsgKeys.USER_CONFIG_LOG_EXPORT_TIMEOUT_SEC, valueRange = "100 ~ 86400", configTagType = UserConfigTagType.CC_FUNCTION, confBelong = ConfBelong.CloudCanal)
    private Long                  logExportTimeoutSec;
}

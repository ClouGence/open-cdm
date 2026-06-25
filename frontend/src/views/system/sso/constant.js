// 每种 SSO 提供者类型对应的 KV 配置项定义
// type: LoginAuthType 名称（与后端 LoginDefService.splitLoginTypes 解析一致）
// fields: 该提供者管理的 user config key 列表
// primaryField: 在列表中作为基本信息展示的关键字段（与 host/clientId 对应）

export const SSO_PROVIDERS = [
  {
    type: 'LDAP',
    labelKey: 'sso-provider-ldap',
    iconResource: 'webside/LDAP@login-icon',
    primaryField: 'ldapHost',
    primaryLabelKey: 'sso-field-ldap-host',
    conflictsWith: ['AD'],
    helpUrl: 'https://www.cdmgr.com/docs/integrations/sso/sso_ldap',
    fields: [
      { key: 'ldapHost', labelKey: 'sso-field-ldap-host', required: true },
      { key: 'ldapPort', labelKey: 'sso-field-ldap-port', required: true },
      { key: 'ldapBase', labelKey: 'sso-field-ldap-base', required: true },
      { key: 'ldapUser', labelKey: 'sso-field-ldap-user', required: true },
      { key: 'ldapPassword', labelKey: 'sso-field-ldap-password', required: true, password: true },
      { key: 'ldapFieldLogin', labelKey: 'sso-field-ldap-field-login', required: true },
      { key: 'ldapRoleMap', labelKey: 'sso-field-role-map', widget: 'roleSelect', required: true },
      { key: 'ldapDomain', labelKey: 'sso-field-ldap-domain' },
      { key: 'ldapNetBIOSRoute', labelKey: 'sso-field-ldap-netbios' },
      { key: 'ldapSoTimeout', labelKey: 'sso-field-ldap-so-timeout' },
      { key: 'ldapUserObjectClass', labelKey: 'sso-field-ldap-user-object-class' },
      { key: 'ldapFieldUser', labelKey: 'sso-field-ldap-field-user' },
      { key: 'ldapFieldEmail', labelKey: 'sso-field-ldap-field-email' },
      { key: 'ldapFieldPhone', labelKey: 'sso-field-ldap-field-phone' }
    ]
  },
  {
    type: 'AD',
    labelKey: 'sso-provider-ad',
    iconResource: 'webside/AD@login-icon',
    primaryField: 'ldapHost',
    primaryLabelKey: 'sso-field-ldap-host',
    conflictsWith: ['LDAP'],
    helpUrl: 'https://www.cdmgr.com/docs/integrations/sso/sso_ad',
    fields: [
      { key: 'ldapHost', labelKey: 'sso-field-ldap-host', required: true },
      { key: 'ldapPort', labelKey: 'sso-field-ldap-port', required: true },
      { key: 'ldapBase', labelKey: 'sso-field-ldap-base', required: true },
      { key: 'ldapUser', labelKey: 'sso-field-ldap-user', required: true },
      { key: 'ldapPassword', labelKey: 'sso-field-ldap-password', required: true, password: true },
      { key: 'ldapDomain', labelKey: 'sso-field-ldap-domain', required: true },
      { key: 'ldapFieldLogin', labelKey: 'sso-field-ldap-field-login', required: true },
      { key: 'ldapRoleMap', labelKey: 'sso-field-role-map', widget: 'roleSelect', required: true },
      { key: 'ldapNetBIOSRoute', labelKey: 'sso-field-ldap-netbios' },
      { key: 'ldapSoTimeout', labelKey: 'sso-field-ldap-so-timeout' },
      { key: 'ldapUserObjectClass', labelKey: 'sso-field-ldap-user-object-class' },
      { key: 'ldapFieldUser', labelKey: 'sso-field-ldap-field-user' },
      { key: 'ldapFieldEmail', labelKey: 'sso-field-ldap-field-email' },
      { key: 'ldapFieldPhone', labelKey: 'sso-field-ldap-field-phone' }
    ]
  },
  {
    type: 'OIDC',
    labelKey: 'sso-provider-oidc',
    iconResource: 'webside/OIDC@login-icon',
    primaryField: 'oidcLoginClientId',
    primaryLabelKey: 'sso-field-oidc-client-id',
    helpUrl: 'https://www.cdmgr.com/docs/integrations/sso/sso_oidc',
    fields: [
      { key: 'oidcLoginWellKnownUrl', labelKey: 'sso-field-oidc-well-known-url', required: true },
      { key: 'oidcLoginClientId', labelKey: 'sso-field-oidc-client-id', required: true },
      { key: 'oidcLoginClientSecret', labelKey: 'sso-field-oidc-client-secret', required: true, password: true },
      { key: 'oidcLoginScope', labelKey: 'sso-field-oidc-scope', required: true },
      { key: 'oidcLoginRoleMap', labelKey: 'sso-field-role-map', widget: 'roleSelect' }
    ]
  },
  {
    type: 'DingTalk',
    labelKey: 'sso-provider-dingtalk',
    iconResource: 'webside/DingTalk@login-icon',
    primaryField: 'dingLoginConfigAk',
    primaryLabelKey: 'sso-field-dingtalk-ak',
    helpUrl: 'https://www.cdmgr.com/docs/integrations/sso/sso_dingtalk',
    fields: [
      { key: 'dingLoginConfigAk', labelKey: 'sso-field-dingtalk-ak', required: true },
      { key: 'dingLoginConfigSk', labelKey: 'sso-field-dingtalk-sk', required: true, password: true },
      { key: 'dingLoginRoleMap', labelKey: 'sso-field-role-map', widget: 'roleSelect' }
    ]
  },
  {
    type: 'Feishu',
    labelKey: 'sso-provider-feishu',
    iconResource: 'webside/Feishu@login-icon',
    primaryField: 'feishuLoginAppID',
    primaryLabelKey: 'sso-field-feishu-app-id',
    helpUrl: 'https://www.cdmgr.com/docs/integrations/sso/sso_feishu',
    fields: [
      { key: 'feishuLoginAppID', labelKey: 'sso-field-feishu-app-id', required: true },
      { key: 'feishuLoginAppSecret', labelKey: 'sso-field-feishu-app-secret', required: true, password: true },
      { key: 'feishuLoginApiTimeoutSec', labelKey: 'sso-field-feishu-api-timeout' },
      { key: 'feishuLoginRoleMap', labelKey: 'sso-field-role-map', widget: 'roleSelect' }
    ]
  },
  {
    type: 'Wechat',
    labelKey: 'sso-provider-wechat',
    iconResource: 'webside/Wechat@login-icon',
    primaryField: 'wechatLoginCorpId',
    primaryLabelKey: 'sso-field-wechat-corp-id',
    helpUrl: 'https://www.cdmgr.com/docs/integrations/sso/sso_wechat',
    fields: [
      { key: 'wechatLoginCorpId', labelKey: 'sso-field-wechat-corp-id', required: true },
      { key: 'wechatLoginAgentId', labelKey: 'sso-field-wechat-agent-id', required: true },
      { key: 'wechatLoginSecret', labelKey: 'sso-field-wechat-secret', required: true, password: true },
      { key: 'wechatLoginRoleMap', labelKey: 'sso-field-role-map', widget: 'roleSelect' }
    ]
  }
];

export const ACCOUNT_AUTH_TYPE_KEY = 'accountAuthType';
export const PASSWORD_TYPE = 'PASSWORD';
export const AUTH_TYPE_SEPARATOR = ',';

export function getProviderByType(type) {
  return SSO_PROVIDERS.find((p) => p.type === type);
}

export function parseAuthTypes(value) {
  if (!value) return [];
  return value
    .split(/[,，;；]/)
    .map((s) => s.trim())
    .filter((s) => s.length > 0);
}

export function buildAuthTypeValue(types) {
  const unique = [];
  types.forEach((t) => {
    if (t && !unique.includes(t)) unique.push(t);
  });
  if (!unique.includes(PASSWORD_TYPE)) unique.unshift(PASSWORD_TYPE);
  return unique.join(AUTH_TYPE_SEPARATOR);
}

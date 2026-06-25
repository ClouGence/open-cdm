// 审批引擎 provider 配置定义
// type: ApprovalProvider 枚举名（DingTalk / Feishu / Wechat）
// enableField: 该 provider 的启用标志 KV，存字符串 'true' / 'false'
// fields: 该 provider 在 form 里展示的 KV 字段列表
//   widget=switch  → boolean 开关，KV 存字符串
//   password=true  → 密码输入框，编辑模式下"留空保留旧值"

export const APPROVAL_PROVIDERS = [
  {
    type: 'DingTalk',
    labelKey: 'ding-ding-shen-pi',
    iconResource: 'webside/DingTalk@login-icon',
    primaryField: 'dingApprovalConfigAk',
    primaryLabelKey: 'approval-field-dingtalk-ak',
    enableField: 'dingEnableApprovalService',
    helpUrl: 'https://www.cdmgr.com/docs/integrations/approval/dingtalk_approval',
    fields: [
      { key: 'dingEnableApprovalService', labelKey: 'approval-enable-service', widget: 'switch' },
      { key: 'dingApprovalConfigAk', labelKey: 'approval-field-dingtalk-ak', required: true },
      { key: 'dingApprovalConfigSk', labelKey: 'approval-field-dingtalk-sk', required: true, password: true }
    ]
  },
  {
    type: 'Feishu',
    labelKey: 'fei-shu-shen-pi',
    iconResource: 'webside/Feishu@login-icon',
    primaryField: 'feishuApprovalAppID',
    primaryLabelKey: 'approval-field-feishu-app-id',
    enableField: 'feishuEnableApprovalService',
    helpUrl: 'https://www.cdmgr.com/docs/integrations/approval/feishu_approval',
    fields: [
      { key: 'feishuEnableApprovalService', labelKey: 'approval-enable-service', widget: 'switch' },
      { key: 'feishuApprovalAppID', labelKey: 'approval-field-feishu-app-id', required: true },
      { key: 'feishuApprovalAppSecret', labelKey: 'approval-field-feishu-app-secret', required: true, password: true },
      { key: 'feishuApprovalApiTimeoutSec', labelKey: 'approval-field-feishu-api-timeout', required: true }
    ]
  },
  {
    type: 'Wechat',
    labelKey: 'wei-xin-shen-pi',
    iconResource: 'webside/Wechat@login-icon',
    primaryField: 'wechatApprovalCorpId',
    primaryLabelKey: 'approval-field-wechat-corp-id',
    enableField: 'wechatEnableApprovalService',
    helpUrl: 'https://www.cdmgr.com/docs/integrations/approval/wechat_approval',
    fields: [
      { key: 'wechatEnableApprovalService', labelKey: 'approval-enable-service', widget: 'switch' },
      { key: 'wechatApprovalCorpId', labelKey: 'approval-field-wechat-corp-id', required: true },
      { key: 'wechatApprovalAgentId', labelKey: 'approval-field-wechat-agent-id', required: true },
      { key: 'wechatApprovalSecret', labelKey: 'approval-field-wechat-secret', required: true, password: true },
      { key: 'wechatApprovalToken', labelKey: 'approval-field-wechat-token', required: true, password: true },
      { key: 'wechatApprovalEncodingAesKey', labelKey: 'approval-field-wechat-encoding-aes-key', required: true, password: true },
      { key: 'wechatApprovalTemplateLang', labelKey: 'approval-field-wechat-template-lang' }
    ]
  }
];

export const APPROVAL_MANAGED_FIELDS = ['feishuApprovalTemplateList', 'wechatApprovalTemplateList'];

export function getProviderByType(type) {
  if (!type) return undefined;
  const lower = String(type).toLowerCase();
  return APPROVAL_PROVIDERS.find((p) => p.type.toLowerCase() === lower);
}

function readConfig(configMap, key) {
  const c = configMap[key];
  return c?.currentCount ?? c?.configValue ?? '';
}

export function isEnabled(configMap, def) {
  return readConfig(configMap, def.enableField) === 'true';
}

export function isConfigured(configMap, def) {
  return String(readConfig(configMap, def.primaryField)).length > 0;
}

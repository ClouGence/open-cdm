// 审批引擎 provider 配置定义
// type: ApprovalProvider 枚举名（DingTalk / Feishu / Wechat）
// fields: 该 provider 在 form 里展示的 KV 字段列表
//   password=true  → 密码输入框，编辑模式下"留空保留旧值"

export const APPROVAL_FLOW_CONTEXT_TYPE = 'CUSTOM_APPROVAL_FLOW';
export const APPROVAL_FLOW_SCHEMA_VERSION = 2;
export const APPROVAL_PARTICIPANT_TYPES = { TEAM: 'TEAM', PERSON: 'PERSON' };
export const APPROVAL_EXECUTION_MODES = { MANUAL: 'MANUAL', AUTO: 'AUTO' };
export const APPROVAL_TASK_STATUSES = {
  WAITING: 'WAITING',
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
  CANCELED: 'CANCELED'
};

export const APPROVAL_PROVIDERS = [
  {
    type: 'Internal',
    labelKey: 'approval-internal-engine',
    primaryField: 'engineName',
    fields: [{ key: 'engineName', labelKey: 'approval-engine-name', required: true }]
  },
  {
    type: 'DingTalk',
    labelKey: 'ding-ding-shen-pi',
    iconResource: 'webside/DingTalk@login-icon',
    primaryField: 'dingApprovalConfigAk',
    primaryLabelKey: 'approval-field-dingtalk-ak',
    helpUrl: 'https://www.cdmgr.com/docs/integrations/approval/dingtalk_approval',
    fields: [
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
    helpUrl: 'https://www.cdmgr.com/docs/integrations/approval/feishu_approval',
    fields: [
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
    helpUrl: 'https://www.cdmgr.com/docs/integrations/approval/wechat_approval',
    fields: [
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
  return c?.configValue ?? '';
}

export function isConfigured(configMap, def) {
  return String(readConfig(configMap, def.primaryField)).length > 0;
}

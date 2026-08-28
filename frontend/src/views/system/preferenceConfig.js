import { ACCOUNT_AUTH_TYPE_KEY, SSO_PROVIDERS } from '@/views/system/sso/constant';
import { APPROVAL_MANAGED_FIELDS, APPROVAL_PROVIDERS } from '@/views/system/approval/constant';

const INTEGER_MAX = 2147483647;

function assertUniqueConfigKeys(registryName, configKeys) {
  const duplicateKeys = configKeys.filter((key, index) => configKeys.indexOf(key) !== index);
  if (duplicateKeys.length) {
    throw new Error(`${registryName} contains duplicate config keys: ${[...new Set(duplicateKeys)].join(', ')}.`);
  }
}

export const PREFERENCE_TABS = [
  {
    name: 'account',
    labelKey: 'preference-tab-account',
    sections: [
      {
        name: 'password-policy',
        titleKey: 'preference-section-password-policy',
        fields: [
          {
            key: 'accountPwdExpireDays',
            labelKey: 'preference-account-pwd-expire-days',
            helpKey: 'preference-account-pwd-expire-days-help',
            widget: 'number',
            defaultValue: 0,
            min: 0,
            max: INTEGER_MAX,
            unitKey: 'preference-unit-days',
            specialValue: 0,
            specialValueKey: 'preference-special-zero-unlimited'
          },
          {
            key: 'accountPwdStrongPolicy',
            labelKey: 'preference-account-pwd-strong-policy',
            helpKey: 'preference-account-pwd-strong-policy-help',
            widget: 'switch',
            defaultValue: false
          },
          {
            key: 'accountPwdMinLength',
            labelKey: 'preference-account-pwd-min-length',
            helpKey: 'preference-account-pwd-min-length-help',
            widget: 'number',
            defaultValue: 8,
            min: 1,
            max: 128,
            unitKey: 'preference-unit-characters'
          }
        ]
      }
    ]
  },
  {
    name: 'cicd',
    labelKey: 'preference-tab-cicd',
    sections: [
      {
        name: 'cicd-defaults',
        titleKey: 'preference-section-cicd-defaults',
        fields: [
          {
            key: 'defaultLanguage',
            labelKey: 'preference-cicd-notification-language',
            helpKey: 'preference-cicd-notification-language-help',
            widget: 'select',
            defaultValue: 'zh_CN',
            required: true,
            options: [
              { value: 'zh_CN', labelKey: 'preference-language-zh-cn' },
              { value: 'en_US', labelKey: 'preference-language-en-us' }
            ]
          },
          {
            key: 'defaultCicdWorkspace',
            labelKey: 'preference-cicd-workspace',
            helpKey: 'preference-cicd-workspace-help',
            placeholderKey: 'preference-cicd-workspace-placeholder',
            widget: 'text',
            defaultValue: 'default',
            required: true
          },
          {
            key: 'defaultCicdTempSpace',
            labelKey: 'preference-cicd-temp-space',
            helpKey: 'preference-cicd-temp-space-help',
            placeholderKey: 'preference-cicd-temp-space-placeholder',
            widget: 'text',
            defaultValue: 'temporary',
            required: true
          },
          {
            key: 'cicdMaxFailedTimes',
            labelKey: 'preference-cicd-max-failed-times',
            helpKey: 'preference-cicd-max-failed-times-help',
            widget: 'number',
            defaultValue: 3,
            min: 1,
            max: INTEGER_MAX,
            unitKey: 'preference-unit-times'
          }
        ]
      }
    ]
  },
  {
    name: 'query',
    labelKey: 'preference-tab-query',
    sections: [
      {
        name: 'editor-behavior',
        titleKey: 'preference-section-query-editor',
        fields: [
          {
            key: 'defaultColumnDisplayChars',
            labelKey: 'preference-query-column-display-chars',
            helpKey: 'preference-query-column-display-chars-help',
            widget: 'number',
            defaultValue: 250,
            min: 10,
            max: 500,
            unitKey: 'preference-unit-characters'
          },
          {
            key: 'consoleMetadataCache',
            labelKey: 'preference-query-metadata-cache',
            helpKey: 'preference-query-metadata-cache-help',
            widget: 'switch',
            defaultValue: true
          },
          {
            key: 'onlineSelectRewriteDisable',
            labelKey: 'preference-query-disable-select-rewrite',
            helpKey: 'preference-query-disable-select-rewrite-help',
            widget: 'switch',
            defaultValue: false
          },
          {
            key: 'onlineResultCacheTimeoutSec',
            labelKey: 'preference-query-result-cache-timeout',
            helpKey: 'preference-query-result-cache-timeout-help',
            widget: 'number',
            defaultValue: 300,
            min: 0,
            max: 43200,
            unitKey: 'preference-unit-seconds',
            specialValue: 0,
            specialValueKey: 'preference-special-zero-no-cache'
          }
        ]
      },
      {
        name: 'language-service',
        titleKey: 'preference-section-language-service',
        fields: [
          {
            key: 'languageMaxRequests',
            labelKey: 'preference-language-max-requests',
            helpKey: 'preference-language-max-requests-help',
            widget: 'number',
            defaultValue: 50,
            min: 50,
            max: 200,
            unitKey: 'preference-unit-requests'
          },
          {
            key: 'languageMaxRequestsByUser',
            labelKey: 'preference-language-max-requests-by-user',
            helpKey: 'preference-language-max-requests-by-user-help',
            widget: 'number',
            defaultValue: 2,
            min: 1,
            max: 3,
            unitKey: 'preference-unit-requests'
          },
          {
            key: 'languageMaxRequestKiloByte',
            labelKey: 'preference-language-max-request-kb',
            helpKey: 'preference-language-max-request-kb-help',
            widget: 'number',
            defaultValue: 1024,
            min: 64,
            max: 16384,
            unitKey: 'preference-unit-kib'
          }
        ]
      },
      {
        name: 'online-result-limits',
        titleKey: 'preference-section-online-result-limits',
        fields: [
          {
            key: 'onlineMaxRecordCount',
            labelKey: 'preference-online-max-record-count',
            helpKey: 'preference-online-max-record-count-help',
            widget: 'number',
            defaultValue: 1000,
            min: 1,
            max: 1000000,
            unitKey: 'preference-unit-records'
          },
          {
            key: 'onlineMaxResultSetMegaByte',
            labelKey: 'preference-online-max-result-set-mb',
            helpKey: 'preference-online-max-result-set-mb-help',
            widget: 'number',
            defaultValue: 60,
            min: 4,
            max: 1024,
            unitKey: 'preference-unit-mb'
          },
          {
            key: 'onlineMaxColumnMegaByte',
            labelKey: 'preference-online-max-column-mb',
            helpKey: 'preference-online-max-column-mb-help',
            widget: 'number',
            defaultValue: 1,
            min: 1,
            max: 16,
            unitKey: 'preference-unit-mb'
          },
          {
            key: 'onlineMaxElementMegaByte',
            labelKey: 'preference-online-max-element-mb',
            helpKey: 'preference-online-max-element-mb-help',
            widget: 'number',
            defaultValue: 1,
            min: 1,
            max: 16,
            unitKey: 'preference-unit-mb'
          }
        ]
      },
      {
        name: 'task-result-limits',
        titleKey: 'preference-section-task-result-limits',
        fields: [
          {
            key: 'taskMaxRecordCount',
            labelKey: 'preference-task-max-record-count',
            helpKey: 'preference-task-max-record-count-help',
            widget: 'number',
            defaultValue: -1,
            min: -1,
            max: INTEGER_MAX,
            unitKey: 'preference-unit-records',
            specialValue: -1,
            specialValueKey: 'preference-special-minus-one-unlimited'
          },
          {
            key: 'taskMaxResultSetMegaByte',
            labelKey: 'preference-task-max-result-set-mb',
            helpKey: 'preference-task-max-result-set-mb-help',
            widget: 'number',
            defaultValue: 1024,
            min: -1,
            max: INTEGER_MAX,
            unitKey: 'preference-unit-mb',
            specialValue: -1,
            specialValueKey: 'preference-special-minus-one-unlimited'
          },
          {
            key: 'taskMaxColumnMegaByte',
            labelKey: 'preference-task-max-column-mb',
            helpKey: 'preference-task-max-column-mb-help',
            widget: 'number',
            defaultValue: 4,
            min: -1,
            max: INTEGER_MAX,
            unitKey: 'preference-unit-mb',
            specialValue: -1,
            specialValueKey: 'preference-special-minus-one-unlimited'
          },
          {
            key: 'taskMaxElementMegaByte',
            labelKey: 'preference-task-max-element-mb',
            helpKey: 'preference-task-max-element-mb-help',
            widget: 'number',
            defaultValue: 1,
            min: -1,
            max: INTEGER_MAX,
            unitKey: 'preference-unit-mb',
            specialValue: -1,
            specialValueKey: 'preference-special-minus-one-unlimited'
          }
        ]
      }
    ]
  },
  {
    name: 'approval',
    labelKey: 'preference-tab-approval',
    sections: [
      {
        name: 'approval-sql-file',
        titleKey: 'preference-section-approval-sql-file',
        fields: [
          {
            key: 'approvalSqlFileMaxMegaByte',
            labelKey: 'preference-approval-sql-file-max-mb',
            helpKey: 'preference-approval-sql-file-max-mb-help',
            widget: 'number',
            defaultValue: 20,
            min: 1,
            max: 20,
            unitKey: 'preference-unit-mb'
          }
        ]
      },
      {
        name: 'approval-analysis',
        titleKey: 'preference-section-approval-analysis',
        fields: [
          {
            key: 'approvalDmlExplainMaxStatements',
            labelKey: 'preference-approval-dml-explain-max-statements',
            helpKey: 'preference-approval-dml-explain-max-statements-help',
            widget: 'number',
            defaultValue: 100,
            min: 1,
            max: 10000
          },
          {
            key: 'approvalDmlExplainMaxStatementMegaByte',
            labelKey: 'preference-approval-dml-explain-max-statement-mb',
            helpKey: 'preference-approval-dml-explain-max-statement-mb-help',
            widget: 'number',
            defaultValue: 1,
            min: 1,
            max: 20,
            unitKey: 'preference-unit-mb'
          }
        ]
      },
      {
        name: 'approval-sync',
        titleKey: 'preference-section-approval-sync',
        fields: [
          {
            key: 'updateApprovalStatusIntervalTime',
            labelKey: 'preference-approval-sync-interval',
            helpKey: 'preference-approval-sync-interval-help',
            widget: 'number',
            defaultValue: 86400,
            min: 0,
            max: 2592000,
            unitKey: 'preference-unit-seconds',
            specialValue: 0,
            specialValueKey: 'preference-special-zero-disabled'
          }
        ]
      }
    ]
  },
  {
    name: 'mcp',
    hidden: true,
    labelKey: 'preference-tab-mcp',
    sections: [
      {
        name: 'mcp-service',
        titleKey: 'preference-section-mcp-service',
        fields: [
          {
            key: 'dmEnableMCP',
            labelKey: 'preference-mcp-enabled',
            helpKey: 'preference-mcp-enabled-help',
            widget: 'switch',
            defaultValue: false
          }
        ]
      }
    ]
  }
];

export const PREFERENCE_CONFIG_KEYS = PREFERENCE_TABS.flatMap((tab) => tab.sections.flatMap((section) => section.fields.map((field) => field.key)));

assertUniqueConfigKeys('Preference config registry', PREFERENCE_CONFIG_KEYS);

const preferenceFeatures = PREFERENCE_TABS.flatMap((tab, tabIndex) =>
  tab.sections.map((section, sectionIndex) => ({
    featureId: `preference-${tab.name}-${section.name}`,
    tabId: tab.name,
    sectionId: section.name,
    order: tabIndex * 100 + sectionIndex,
    titleKey: section.titleKey,
    descriptionKey: section.descriptionKey,
    configKeys: section.fields.map((field) => field.key),
    requiredAuth: 'RDP_PRI_USER_KV_CONF_R',
    availableModes: ['all'],
    fields: section.fields
  }))
);

const existingFeatureConfigs = [
  {
    featureId: 'sso-login-method',
    tabId: 'sso',
    sectionId: 'login-method',
    order: 1000,
    configKeys: [ACCOUNT_AUTH_TYPE_KEY]
  },
  ...SSO_PROVIDERS.map((provider, index) => ({
    featureId: `sso-${provider.type.toLowerCase()}`,
    tabId: 'sso',
    sectionId: provider.type.toLowerCase(),
    order: 1010 + index,
    configKeys: provider.fields.map((field) => field.key),
    fields: provider.fields
  })),
  ...APPROVAL_PROVIDERS.map((provider, index) => ({
    featureId: `approval-provider-${provider.type.toLowerCase()}`,
    tabId: 'approval-provider',
    sectionId: provider.type.toLowerCase(),
    order: 1100 + index,
    configKeys: provider.fields.map((field) => field.key),
    fields: provider.fields
  })),
  {
    featureId: 'approval-provider-templates',
    tabId: 'approval-provider',
    sectionId: 'templates',
    order: 1110,
    configKeys: APPROVAL_MANAGED_FIELDS
  },
  {
    featureId: 'sql-audit-retention',
    tabId: 'sql-log',
    sectionId: 'retention',
    order: 1200,
    configKeys: ['sqlAuditRetentionDays']
  }
].map((feature) => ({
  titleKey: '',
  descriptionKey: '',
  requiredAuth: 'RDP_PRI_USER_KV_CONF_R',
  availableModes: ['all'],
  fields: [],
  ...feature
}));

export const USER_CONFIG_FEATURE_REGISTRY = [...preferenceFeatures, ...existingFeatureConfigs];
export const REGISTERED_USER_CONFIG_KEYS = USER_CONFIG_FEATURE_REGISTRY.flatMap((feature) => feature.configKeys);

assertUniqueConfigKeys('User config feature registry', REGISTERED_USER_CONFIG_KEYS);

export function getPreferenceTab(tabName) {
  return PREFERENCE_TABS.find((tab) => tab.name === tabName) || PREFERENCE_TABS[0];
}

export function getPreferenceTabFields(tabName) {
  return getPreferenceTab(tabName).sections.flatMap((section) => section.fields);
}

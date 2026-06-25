import { ACCOUNT_AUTH_TYPE_KEY, SSO_PROVIDERS } from '@/views/system/sso/constant';
import { APPROVAL_MANAGED_FIELDS, APPROVAL_PROVIDERS } from '@/views/system/approval/constant';

const SQL_AUDIT_RETENTION_DAYS_KEY = 'sqlAuditRetentionDays';

const managedUserConfigNames = new Set([
  ACCOUNT_AUTH_TYPE_KEY,
  ...SSO_PROVIDERS.flatMap((provider) => provider.fields.map((field) => field.key)),
  ...APPROVAL_PROVIDERS.flatMap((provider) => provider.fields.map((field) => field.key)),
  ...APPROVAL_MANAGED_FIELDS,
  SQL_AUDIT_RETENTION_DAYS_KEY
]);

export function isManagedUserConfig(configName) {
  return managedUserConfigNames.has(configName);
}

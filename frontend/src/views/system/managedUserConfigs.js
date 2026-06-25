import { ACCOUNT_AUTH_TYPE_KEY, SSO_PROVIDERS } from '@/views/system/sso/constant';
import { APPROVAL_MANAGED_FIELDS, APPROVAL_PROVIDERS } from '@/views/system/approval/constant';

const managedUserConfigNames = new Set([
  ACCOUNT_AUTH_TYPE_KEY,
  ...SSO_PROVIDERS.flatMap((provider) => provider.fields.map((field) => field.key)),
  ...APPROVAL_PROVIDERS.flatMap((provider) => provider.fields.map((field) => field.key)),
  ...APPROVAL_MANAGED_FIELDS
]);

export function isManagedUserConfig(configName) {
  return managedUserConfigNames.has(configName);
}

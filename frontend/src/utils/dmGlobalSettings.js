let dmBootstrapStatus = null;

export function getDmSystemStatus(dmGlobalSettingsRes) {
  return dmGlobalSettingsRes?.data?.systemStatus || {};
}

export function isDmSystemReady(dmGlobalSettingsRes) {
  return getDmSystemStatus(dmGlobalSettingsRes).status === 'Ready';
}

export function isDmSystemInitial(dmGlobalSettingsRes) {
  return getDmSystemStatus(dmGlobalSettingsRes).status === 'Initial';
}

export function isDmSystemUpgrade(dmGlobalSettingsRes) {
  return getDmSystemStatus(dmGlobalSettingsRes).status === 'Upgrade';
}

export function isDmSystemStarting(dmGlobalSettingsRes) {
  return getDmSystemStatus(dmGlobalSettingsRes).status === 'Starting';
}

export function isDmSystemBootstrapRequired(dmGlobalSettingsRes) {
  const status = getDmSystemStatus(dmGlobalSettingsRes).status;
  return status === 'Initial' || status === 'Upgrade';
}

export function cacheDmBootstrapStatus(dmGlobalSettingsRes) {
  dmBootstrapStatus = dmGlobalSettingsRes || null;
}

export function consumeDmBootstrapStatus() {
  const status = dmBootstrapStatus;
  dmBootstrapStatus = null;
  return status;
}

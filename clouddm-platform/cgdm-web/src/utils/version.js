export function formatDisplayVersion(version) {
  const rawVersion = `${version || ''}`.trim();
  if (!rawVersion) {
    return '';
  }
  if (rawVersion.toLowerCase() === 'unknow') {
    return rawVersion;
  }

  const normalizedVersion = rawVersion.replace(/\(.*/, '').trim();
  if (!normalizedVersion) {
    return '';
  }

  return /^v/i.test(normalizedVersion) ? normalizedVersion : `v${normalizedVersion}`;
}

export function resolveDisplayVersion(settings = {}) {
  return formatDisplayVersion(settings.version);
}

export function resolveDeploymentModeText(settings = {}) {
  return settings.aloneMode ? '独立' : '集群';
}

export function resolveVersionBadgeText(settings = {}) {
  const displayVersion = resolveDisplayVersion(settings) || 'unknow';
  return `${resolveDeploymentModeText(settings)} ${displayVersion}`;
}

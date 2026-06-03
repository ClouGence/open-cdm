const PLUGIN_RESOURCE_PREFIX = '/clouddm/console/api/v1/resource/fetch';

export const getPluginResourceUrl = (resource, params = {}) => {
  const query = new URLSearchParams({ resource, ...params });
  return `${PLUGIN_RESOURCE_PREFIX}?${query.toString()}`;
};

export const setPageIcon = (iconUrl, type = 'image/x-icon') => {
  if (!iconUrl) {
    return;
  }

  document.querySelectorAll("link[rel~='icon'], link[rel='shortcut icon']").forEach((link) => {
    link.parentNode.removeChild(link);
  });

  ['icon', 'shortcut icon'].forEach((rel) => {
    const link = document.createElement('link');
    link.rel = rel;
    link.type = type;
    link.href = iconUrl;
    document.head.appendChild(link);
  });
};

export const WEBSIDE_FAVICON = getPluginResourceUrl('webside/favicon.ico', { format: 'ico' });
export const WEBSIDE_LOGO_LOGIN = getPluginResourceUrl('webside/logo_login');
export const WEBSIDE_LOGO_HEADER = getPluginResourceUrl('webside/logo_header');

const STORAGE_KEY = 'dm:lastWorkbenchRoute';

function getStorageKey(uid) {
  if (!uid) {
    return STORAGE_KEY;
  }
  return `${STORAGE_KEY}:${uid}`;
}

function isSqlRoute(path) {
  return path === '/sql' || path.startsWith('/sql/');
}

function isValidWorkbenchPath(path) {
  if (!path || typeof path !== 'string') {
    return false;
  }
  if (path === '/' || isSqlRoute(path)) {
    return false;
  }
  return path.startsWith('/');
}

function menuKeyToPath(key) {
  if (!key || typeof key !== 'string') {
    return '';
  }
  if (key.startsWith('/')) {
    return key;
  }
  return `/${key}`;
}

export function resolveWorkbenchFallbackPath(menuItems = []) {
  for (const item of menuItems) {
    const path = menuKeyToPath(item?.key);
    if (isValidWorkbenchPath(path)) {
      return path;
    }
  }

  return '/settings/profile';
}

export function isAccessibleWorkbenchPath(path, menuItems = []) {
  if (!isValidWorkbenchPath(path)) {
    return false;
  }

  const menuPaths = menuItems.map((item) => menuKeyToPath(item?.key)).filter(Boolean);
  return menuPaths.some((menuPath) => path === menuPath || path.startsWith(`${menuPath}/`));
}

export function saveLastWorkbenchRoute(route, uid) {
  if (!route || !isValidWorkbenchPath(route.path)) {
    return;
  }

  try {
    localStorage.setItem(
      getStorageKey(uid),
      JSON.stringify({
        path: route.path,
        query: route.query || {},
        hash: route.hash || ''
      })
    );
  } catch (e) {
    console.warn('saveLastWorkbenchRoute failed', e);
  }
}

export function resolveWorkbenchRoute(fallbackPath, uid, menuItems = []) {
  const fallback = isValidWorkbenchPath(fallbackPath) ? fallbackPath : resolveWorkbenchFallbackPath(menuItems);

  try {
    const raw = localStorage.getItem(getStorageKey(uid));
    if (!raw) {
      return { path: fallback };
    }

    const saved = JSON.parse(raw);
    if (!isValidWorkbenchPath(saved?.path) || !isAccessibleWorkbenchPath(saved.path, menuItems)) {
      return { path: fallback };
    }

    return {
      path: saved.path,
      query: saved.query || {},
      hash: saved.hash || ''
    };
  } catch (e) {
    return { path: fallback };
  }
}

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

export function resolveWorkbenchRoute(fallbackPath = '/datasource', uid) {
  const fallback = isValidWorkbenchPath(fallbackPath) ? fallbackPath : '/datasource';

  try {
    const raw = localStorage.getItem(getStorageKey(uid));
    if (!raw) {
      return { path: fallback };
    }

    const saved = JSON.parse(raw);
    if (!isValidWorkbenchPath(saved?.path)) {
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

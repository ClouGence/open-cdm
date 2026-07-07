export const DS_SUPPORT_GROUP_METAS = [
  { labelKey: 'guan-xi-xing-shu-ju-ku', icon: 'ios-server-outline' },
  { labelKey: 'fen-xi-xing-shu-ju-ku', icon: 'ios-stats-outline' },
  { labelKey: 'fei-guan-xi-xing-shu-ju-ku', icon: 'ios-cube-outline' },
  { labelKey: 'yun-shu-ju-ku', icon: 'ios-cloud-outline' }
];

export const getDsSupportGroupKey = (groupIndex) => `display-group-${groupIndex}`;

export const getDsSupportGroupMeta = (groupIndex) => {
  const fallback = {
    labelKey: 'qi-ta',
    icon: 'ios-apps-outline'
  };
  return {
    key: getDsSupportGroupKey(groupIndex),
    ...(DS_SUPPORT_GROUP_METAS[groupIndex] || fallback)
  };
};

export const normalizeDsSupportName = (type, groupIndex = -1, order = 0) => {
  if (!type) {
    return null;
  }

  const normalizedType =
    typeof type === 'string'
      ? {
          dsKey: type,
          displayName: type
        }
      : {
          dsKey: type.dsKey,
          displayName: type.displayName || type.dsKey
        };

  if (!normalizedType.dsKey) {
    return null;
  }

  return {
    ...normalizedType,
    displayGroupIndex: groupIndex,
    displayGroupKey: getDsSupportGroupKey(groupIndex),
    displayOrder: order
  };
};

export const normalizeDsSupportNameGroups = (supportNames) => {
  if (!Array.isArray(supportNames)) {
    return [];
  }

  return supportNames
    .map((group, groupIndex) =>
      (Array.isArray(group) ? group : [group]).map((type, order) => normalizeDsSupportName(type, groupIndex, order)).filter(Boolean)
    )
    .filter((group) => group.length > 0);
};

export const flattenDsSupportNameGroups = (groups) => {
  const typeMap = new Map();
  (Array.isArray(groups) ? groups : []).forEach((group) => {
    (Array.isArray(group) ? group : [group]).forEach((type) => {
      const normalizedType = normalizeDsSupportName(type, type?.displayGroupIndex, type?.displayOrder);
      if (normalizedType?.dsKey && !typeMap.has(normalizedType.dsKey)) {
        typeMap.set(normalizedType.dsKey, normalizedType);
      }
    });
  });
  return [...typeMap.values()];
};

export const findDsSupportName = (dsType, supportNames) =>
  flattenDsSupportNameGroups(normalizeDsSupportNameGroups(supportNames)).find((type) => type.dsKey === dsType) || null;

export const getDsSupportOrderMap = (groups) => {
  const orderMap = new Map();
  flattenDsSupportNameGroups(groups).forEach((type, index) => {
    orderMap.set(type.dsKey, index);
  });
  return orderMap;
};

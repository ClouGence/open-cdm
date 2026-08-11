import appLogger from '@/utils/logger';
export const arrToObj = (arr, keyName) => {
  if (Array.isArray(arr) && arr.length) {
    return Object.fromEntries(arr.map((item) => [item[keyName], item]));
  }
  return {};
};

export const getLanguage = (type) => {
  appLogger.debug('getLanguage', type);
  switch (type) {
    case 'MySQL':
      return 'mysql';
    case 'PostgreSQL':
      return 'pgsql';
    case 'Redis':
      return 'redis';
    case 'Valkey':
      return 'redis';
    default:
      return 'sql';
  }
};

// 'true', 'false'
export const formatToCleanBoolean = (flag) => String(flag).toLowerCase() === 'true';

// Display scientific notation.
export const transformScientific = (data) => {
  if (data && data.toString().includes('e')) {
    const tofix = data.toString().split('e-')[1];
    return Number(data).toFixed(Number(tofix));
  }
  return data;
};

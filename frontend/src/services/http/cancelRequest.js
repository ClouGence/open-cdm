import appLogger from '@/utils/logger';
import axios from 'axios';
import qs from 'qs';

const CancelToken = axios.CancelToken;

const pending = new Map();

const generateCancelReqKey = (type, config) => {
  const { url, method, params, data } = config;
  let dataPart = '';

  if (data) {
    if (typeof data === 'string') {
      // If already a string, too long, only abridged feature
      dataPart = data.length > 1000 ? `len_${data.length}_${data.substring(0, 100)}` : data;
    } else {
      try {
        // If big data objects, avoid full-text sequencing
        if (Array.isArray(data) && data.length > 1000) {
          dataPart = `array_len_${data.length}`;
        } else if (typeof data === 'object') {
          const keys = Object.keys(data);
          if (keys.length > 100) {
            dataPart = `obj_keys_${keys.length}`;
          } else {
            dataPart = JSON.stringify(data);
          }
        } else {
          dataPart = String(data);
        }
      } catch (e) {
        dataPart = 'complex_data';
      }
    }
  }

  return [url, method, qs.stringify(params), dataPart].join('&');
};

export const addPending = (config) => {
  const url = generateCancelReqKey('add', config);
  config.cancelToken =
    config.cancelToken ||
    new CancelToken((c) => {
      if (!pending.has(url)) {
        pending.set(url, c);
      }
    });
};

export const cancelPending = (config) => {
  const url = generateCancelReqKey('remove', config);
  if (pending.has(url)) {
    appLogger.debug('cancel pending');
    const cancel = pending.get(url);
    cancel(url);
    pending.delete(url);
  }
};

export const removePending = (config) => {
  const url = generateCancelReqKey('remove', config);
  if (pending.has(url)) {
    pending.delete(url);
  }
};

export const clearAllPending = () => {
  appLogger.debug(pending);
  for (const [url, cancel] of pending) {
    cancel(url);
  }
  pending.clear();
};

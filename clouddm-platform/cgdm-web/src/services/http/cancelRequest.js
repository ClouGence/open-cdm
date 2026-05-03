import axios from 'axios';
import qs from 'qs';

const CancelToken = axios.CancelToken;

const pending = new Map();

const generateCancelReqKey = (type, config) => {
  const { url, method, params, data } = config;
  let dataPart = '';

  if (data) {
    if (typeof data === 'string') {
      // 如果已经是字符串，且长度过大，只取简略特征
      dataPart = data.length > 1000 ? `len_${data.length}_${data.substring(0, 100)}` : data;
    } else {
      try {
        // 如果是大数据对象，避免全文序列化
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
    console.log('cancel pending');
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
  console.log(pending);
  for (const [url, cancel] of pending) {
    cancel(url);
  }
  pending.clear();
};

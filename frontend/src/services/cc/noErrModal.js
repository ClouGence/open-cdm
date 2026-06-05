import axios from 'axios';
import { Modal, Spin } from 'view-ui-plus';
import i18n from '@/i18n';
import formatError from '../formatError';

// eslint-disable-next-line no-restricted-globals
// const baseURL = `${location.protocol}//${location.host}/cloudcanal/console/api/v1/inner`;
let baseURL = `${window.location.protocol}//${window.location.host}/cloudcanal/console/api/v1/inner`;
if (process.env.VUE_APP_BASE_URL) {
  baseURL = `${process.env.VUE_APP_BASE_URL}/cloudcanal/console/api/v1/inner`;
}
const timeout = 60000;
const trimObj = (obj) => {
  if (typeof obj === 'string') {
    return obj.trim();
  }
  if (obj === null) {
    return obj;
  }
  if (Array.isArray(obj) || typeof obj === 'object') {
    return Object.keys(obj).reduce(
      (acc, key) => {
        acc[key.trim()] = trimObj(obj[key]);
        return acc;
      },
      Array.isArray(obj) ? [] : {}
    );
  }
  return obj;
};
const instance = axios.create({
  baseURL,
  timeout,
  transformRequest: [
    (data) => {
      if (!data) {
        return;
      }
      Object.keys(data).map((key) => {
        if (!data[key] && data[key] !== false && data[key] !== 0) {
          data[key] = null;
        } else {
          try {
            data[key] = trimObj(data[key]);
          } catch (e) {
            console.error(e);
          }
        }
        return null;
      });
      return JSON.stringify(data);
    }
  ],
  headers: {
    'Accept-Language': i18n?.global?.locale?.value,
    Accept: 'application/json',
    'Content-Type': 'application/json'
    // 'Access-Control-Allow-Origin': '*'
  },
  withCredentials: true,
  credentials: 'include'
});

export { instance };

// 返回结果拦截器,处理默认的错误
instance.interceptors.response.use(
  (response) =>
    // 正常的请求前拦截,在这里处理
    // eslint-disable-next-line implicit-arrow-linebreak
    response,

  (error) => {
    // 非200请求时的错误处理'
    Spin.hide();
    if (error.response) {
      const res = error.response.data; // 请求data
      const status = error.response.status; // 请求状态吗

      if (status === 499) {
        window.location.href = res.url;
      } else if (status === 401) {
        window.location.href = `${window.location.protocol}//${window.location.host}/#/login`;
        // Router.push({ name: 'Login' });
        // window.location.reload();
      } else if (res && !res.errors) {
        Modal.error({
          title: 'ERROR',
          content: `${res.message}`
        });
      } else {
        Modal.error({
          title: 'ERROR',
          content: `${res.errors[0].message}`
        });
      }
    } else {
      Modal.error({
        title: 'ERROR',
        content: i18n.global.t('yi-chao-shi')
      });
    }
    return Promise.reject(error);
  }
);

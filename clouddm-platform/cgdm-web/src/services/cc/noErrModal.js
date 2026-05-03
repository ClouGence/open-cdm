import axios from 'axios';
import { Modal, Spin } from 'view-ui-plus';
import i18n from '@/i18n';
import store from '@/store';
import Cookies from 'js-cookie';
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
    'Content-Type': 'application/json',
    'X-Product-Code': store?.state?.selectCcProductCluster
    // 'Access-Control-Allow-Origin': '*'
  },
  withCredentials: true,
  credentials: 'include'
});

export { instance };

instance.interceptors.request.use((request) => {
  if (request.headers['x-is-redirect']) {
    request.headers['X-Product-Code'] = null;
  } else {
    request.headers['X-Product-Code'] = store.state.selectCcProductCluster;
  }
  if (request.customeHeaders && request.customeHeaders['X-Product-Code']) {
    request.headers['X-Product-Code'] = request.customeHeaders['X-Product-Code'];
  }
  return request;
});

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
      } else if (status === 307) {
        if (error.response.headers['x-redirect-addr']) {
          const cookies = Cookies.get();
          const newUrl = `${error.response.headers['x-redirect-addr']}/cloudcanal/console/api/v1/inner/${error.response.config.url}`;
          const headers = {
            'x-is-redirect': true
          };
          if (cookies.jwt_token) {
            headers.jwt_token = cookies.jwt_token;
          }
          return instance.post(newUrl, error.response.config.data ? JSON.parse(error.response.config.data) : null, {
            headers,
            withCredentials: true,
            credentials: 'include'
          });
        } else {
          // instance.post('').then((response) => response);
        }
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

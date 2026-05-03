import axios from 'axios';
import { Modal, Spin } from 'view-ui-plus';
import i18n from '@/i18n';
import store from '@/store';
import Cookies from 'js-cookie';
import formatError from '../formatError';
import { resolveComponent } from 'vue';

axios.defaults.withCredentials = true;
// const baseURL = `${window.location.protocol}//${window.location.host}/cloudcanal/console/api/v1/inner`;
let baseURL = `${window.location.protocol}//${window.location.host}/cloudcanal/console/api/v1/inner`;
if (process.env.VUE_APP_BASE_URL) {
  baseURL = `${process.env.VUE_APP_BASE_URL}/cloudcanal/console/api/v1/inner`;
}
const timeout = 600000;

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
  },
  responseType: 'blob',
  noStatus: true,
  withCredentials: true,
  credentials: 'include'
});

export { instance };

instance.interceptors.request.use((request) => {
  if (request.headers['x-is-redirect']) {
    request.headers['X-Product-Code'] = null;
  } else {
    request.headers['X-Product-Code'] = store?.state?.selectCcProductCluster;
  }
  if (request.customeHeaders && request.customeHeaders['X-Product-Code']) {
    request.headers['X-Product-Code'] = request.customeHeaders['X-Product-Code'];
  }
  return request;
});
// 返回结果拦截器,处理默认的错误
instance.interceptors.response.use(
  (response) => {
    let parseData = {};

    if (response.config.data) {
      parseData = JSON.parse(response.config.data);
    }

    if (response.data.code !== '1') {
      if (response.data.code !== '6028' && response.data.code !== '2011' && response.data.code !== '0014') {
        if (response.data.msg && !parseData.noModal) {
          Modal.info({
            render: (h) =>
              h('div', [
                h(
                  'div',
                  {
                    class: 'operation-error-title'
                  },
                  [
                    h(resolveComponent('Icon'), {
                      type: 'md-close-circle',
                      style: {
                        fontSize: '24px',
                        float: 'left'
                      }
                    }),
                    h(
                      'p',
                      {
                        class: 'preCheck-title-desc',
                        style: {
                          marginLeft: '30px',
                          wordBreak: 'break-all'
                        }
                      },
                      i18n.global.t('cao-zuo-shi-bai')
                    ),
                    h('p', {
                      style: {
                        marginLeft: '30px'
                      },
                      innerHTML: formatError(response.data.msg)
                    })
                  ]
                )
              ]),
            width: 600,
            okText: i18n.global.t('zhi-dao-le'),
            closable: true
          });
        }
      }
    }
    // 正常的请求前拦截,在这里处理
    return response;
  },
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
        console.log('response', error.response);
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
      } else if (status === 406) {
        Modal.error({
          title: i18n.global.t('quan-xian-yi-chang'),
          content: formatError(res.message) || i18n.global.t('nin-mei-you-gai-quan-xian-de-cao-zuo-qing-lian-xi-zhu-zhang-hao-huo-guan-li-yuan')
        });
      } else if (status === 500) {
        // if (window.location.href.split('#')[1] !== '/permission/denied' && window.location.href.split('#')[1] !== '/permission/exception') {
        //   localStorage.setItem('console_last_url', window.location.href.split('#')[1]);
        // }
        // window.location.href = `${window.location.protocol}//${window.location.host}/#/permission/exception`;
      } else if (res && !res.errors) {
        Modal.error({
          title: 'ERROR',
          content: `${formatError(res.message)}`
        });
      } else {
        Modal.error({
          title: 'ERROR',
          content: `${formatError(res.errors[0].message)}`
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

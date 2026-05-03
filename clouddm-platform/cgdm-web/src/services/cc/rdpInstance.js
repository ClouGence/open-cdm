import axios from 'axios';
import { Modal, Spin } from 'view-ui-plus';
import { showActiveLicense } from '@/utils';
import i18n from '@/i18n';
import eventBus from '@/utils/eventBus';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import formatError from '../formatError';
import CustomIcon from '@/components/function/CustomIcon.vue';

axios.defaults.withCredentials = true;
// const baseURL = `${window.location.protocol}//${window.location.host}/rdp/console/api/v1`;
let baseURL = `${window.location.protocol}//${window.location.host}/rdp/console/api/v1`;
if (process.env.VUE_APP_BASE_URL) {
  baseURL = `${process.env.VUE_APP_BASE_URL}/rdp/console/api/v1`;
}
const timeout = 1800000;

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
  (response) => {
    // console.log('response', response, response.data);
    let parseData = {};

    if (response.config.data) {
      parseData = JSON.parse(response.config.data);
    }

    if (response.data.code !== '1') {
      if (response.data.code !== '2011' && response.data.code !== '0014' && response.data.code !== '0032') {
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
                    h(CustomIcon, {
                      type: 'icon-v2-error'
                    }),
                    h(
                      'span',
                      {
                        class: 'preCheck-title-desc',
                        style: {
                          display: 'inline-block',
                          wordBreak: 'break-all',
                          marginLeft: '14px',
                          marginBottom: '10px'
                        }
                      },
                      i18n.global.t('cao-zuo-shi-bai')
                    ),
                    h('p', {
                      class: 'preCheck-info',
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
      if (response.data.code === '2011') {
        // console.log('hello tao');
        // eventBus.emit('setCloudAKSKModal');
      }
    } else if (!response.data.permission) {
      response.data.success = false;
      response.data.code = 403;
      Modal.error({
        title: i18n.global.t('quan-xian-yi-chang'),
        content: i18n.global.t('que-shao-quan-xian') + response.data.needAuthKey
      });
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
      } else if (status === 406) {
        Modal.error({
          title: i18n.global.t('quan-xian-yi-chang'),
          content: res.message || i18n.global.t('nin-mei-you-gai-quan-xian-de-cao-zuo-qing-lian-xi-zhu-zhang-hao-huo-guan-li-yuan')
        });
      } else if (status === 500) {
        if (window.location.href.split('#')[1] !== '/permission/denied' && window.location.href.split('#')[1] !== '/permission/exception') {
          localStorage.setItem('console_last_url', window.location.href.split('#')[1]);
        }
        window.location.href = `${window.location.protocol}//${window.location.host}/#/permission/exception`;
      } else if (res && !res.errors) {
        if (showActiveLicense(status)) {
          eventBus.emit(EVENT_BUS_NAME_LIST.SHOW_INACTIVE_MODAL, res.message || error.response.data);
        } else {
          setTimeout(() => {
            Modal.error({
              title: 'ERROR',
              render: (h) =>
                h('div', {
                  innerHTML: res.message || error.response.data
                })
            });
          }, 300);
        }
      } else {
        setTimeout(() => {
          Modal.error({
            title: 'ERROR',
            render: (h) =>
              h('div', {
                innerHTML: res.errors[0].message
              })
          });
        }, 300);
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

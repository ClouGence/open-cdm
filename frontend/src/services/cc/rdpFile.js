import axios from 'axios';
import { Modal, Spin } from 'view-ui-plus';
import i18n from '@/i18n';
import { showActiveLicense } from '@/utils';
import eventBus from '@/utils/eventBus';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import formatError from '../formatError';
import { resolveComponent } from 'vue';

// const baseURL = `${window.location.protocol}//${window.location.host}/rdp/console/api/v1`;
let baseURL = `${window.location.protocol}//${window.location.host}/rdp/console/api/v1`;
if (process.env.VUE_APP_BASE_URL) {
  baseURL = `${process.env.VUE_APP_BASE_URL}/rdp/console/api/v1`;
}
const timeout = 60000;
const instance = axios.create({
  baseURL,
  timeout,
  headers: {
    'Accept-Language': i18n?.global?.locale?.value,
    Accept: 'application/json',
    'Content-Type': 'application/x-www-form-urlencoded'
    // 'Access-Control-Allow-Origin': '*'
  },
  withCredentials: true,
  credentials: 'include'
});

export { instance };

// 返回结果拦截器,处理默认的错误
// 返回结果拦截器,处理默认的错误
instance.interceptors.response.use(
  (response) => {
    if (response.data.code !== '1') {
      if (response.data.code !== '6028' && response.data.code !== '2011' && response.data.code !== '0014') {
        if (response.data.msg) {
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
      if (response.data.code === '2011') {
        console.log('hello tao');
        window.$bus.emit('setCloudAKSKModal');
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
          window.$bus.emit(EVENT_BUS_NAME_LIST.SHOW_INACTIVE_MODAL, res.message || res);
        } else {
          Modal.error({
            title: 'ERROR',
            content: `${res.message || res}`
          });
        }
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

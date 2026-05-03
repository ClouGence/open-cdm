import axios from 'axios';
import { Modal, Spin } from 'view-ui-plus';
import { showActiveLicense } from '@/utils';
import i18n from '@/i18n';
import store from '@/store';
import Cookies from 'js-cookie';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import eventBus from '@/utils/eventBus';
// import CCMI from '@/utils/modalInstance';
import { debounce } from 'lodash';
import MarkdownIt from 'markdown-it';
import DOMPurify from 'dompurify';
import { DOMPURIFY_CONFIG } from '../constants';
import formatError from '../formatError';

const debouncedErrorModal = debounce((title, content, type = 'error') => {
  // CCMI.confirm({
  //   title,
  //   content,
  //   type
  // });
  Modal.error({
    title,
    content,
    width: 520
  });
}, 600);

axios.defaults.withCredentials = true;
let baseURL = `${window.location.protocol}//${window.location.host}/cloudcanal/console/api/v1/inner`;
if (process.env.VUE_APP_BASE_URL) {
  baseURL = `${process.env.VUE_APP_BASE_URL}/cloudcanal/console/api/v1/inner`;
}
console.log('baseURL', baseURL);
// const baseURL = '/' + '/api/v1/';
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

let requestData = null;

const instance = axios.create({
  baseURL,
  timeout,
  transformRequest: [
    (data) => {
      if (!data) {
        return;
      }
      requestData = data;
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
  (response) => {
    let parseData = {};

    if (response.config.data) {
      parseData = JSON.parse(response.config.data);
    }

    if (response.data.code !== '1') {
      const code = response.data.code;
      // if (code === '9001' || code === '9002' || code === '9003') {
      //   let errInfo1 = '';
      //   if (code === '9001') {
      //     errInfo1 = i18n.global.t('nin-de-ka-wei-ding-yue-bladepipe-fu-wu-qing-zhi');
      //   } else if (code === '9002') {
      //     errInfo1 = i18n.global.t('nin-de-zhang-hao-wei-bang-ding-fu-kuan-fang-shi-qing-zhi');
      //   } else if (code === '9003') {
      //     errInfo1 = i18n.global.t('nin-de-ka-pian-ding-yue-shi-bai-qing-zhi');
      //   }
      //   Modal.info({
      //     render: (h) =>
      //       h('div', [
      //         h(
      //           'div',
      //           {
      //             class: 'operation-error-title'
      //           },
      //           [
      //             h('Icon', {
      //               props: {
      //                 type: 'md-close-circle'
      //               },
      //               style: {
      //                 fontSize: '24px',
      //                 float: 'left'
      //               }
      //             }),
      //             h(
      //               'p',
      //               {
      //                 class: 'preCheck-title-desc',
      //                 style: {
      //                   marginLeft: '30px',
      //                   wordBreak: 'break-all'
      //                 }
      //               },
      //               i18n.global.t('cao-zuo-shi-bai')
      //             ),
      //             h(
      //               'p',
      //               {
      //                 class: 'preCheck-info',
      //                 style: {
      //                   marginLeft: '30px'
      //                 }
      //               },
      //               [
      //                 h('span', {}, errInfo1),
      //                 h(
      //                   'a',
      //                   {
      //                     class: 'text-cc-primary',
      //                     attrs: {
      //                       href: '/#/system/payment'
      //                     },
      //                     on: {
      //                       click: () => {
      //                         Modal.remove();
      //                       }
      //                     }
      //                   },
      //                   i18n.global.t('zhi-fu-ye-mian')
      //                 ),
      //                 h('span', {}, i18n.global.t('jin-xing-cao-zuo'))
      //               ]
      //             )
      //           ]
      //         )
      //       ]),
      //     width: 600,
      //     okText: i18n.global.t('zhi-dao-le'),
      //     closable: true
      //   });
      // } else if (code === '9000') {
      //   Modal.info({
      //     render: (h) =>
      //       h('div', [
      //         h(
      //           'div',
      //           {
      //             class: 'operation-error-title'
      //           },
      //           [
      //             h('Icon', {
      //               props: {
      //                 type: 'md-close-circle'
      //               },
      //               style: {
      //                 fontSize: '24px',
      //                 float: 'left'
      //               }
      //             }),
      //             h(
      //               'p',
      //               {
      //                 class: 'preCheck-title-desc',
      //                 style: {
      //                   marginLeft: '30px',
      //                   wordBreak: 'break-all'
      //                 }
      //               },
      //               i18n.global.t('cao-zuo-shi-bai')
      //             ),
      //             h(
      //               'p',
      //               {
      //                 class: 'preCheck-info',
      //                 style: {
      //                   marginLeft: '30px'
      //                 }
      //               },
      //               [
      //                 h('span', {}, i18n.global.t('nin-de-zhang-hao-yi-bei-suo-ding-qing-zhi')),
      //                 h(
      //                   'a',
      //                   {
      //                     class: 'text-cc-primary',
      //                     attrs: {
      //                       href: '/#/system/invoice'
      //                     }
      //                   },
      //                   i18n.global.t('fa-piao-lie-biao-ye-mian')
      //                 ),
      //                 h('span', {}, i18n.global.t('cha-kan-kou-kuan-shi-bai-de-zhang-dan-bing-jin-xing-fu-kuan'))
      //               ]
      //             )
      //           ]
      //         )
      //       ]),
      //     width: 600,
      //     okText: i18n.global.t('zhi-dao-le'),
      //     closable: true
      //   });
      // } else
      if (response.data.code !== '6028' && response.data.code !== '2011' && response.data.code !== '0014' && response.data.code !== '0032') {
        if (response.data.msg && !parseData.noModal) {
          debouncedErrorModal(i18n.global.t('cao-zuo-shi-bai'), formatError(response.data.msg), 'error');
        }
      }
    } else if (!response.data.permission) {
      response.data.success = false;
      response.data.code = 403;
      // CCMI.confirm({
      //   title: i18n.global.t('quan-xian-yi-chang'),
      //   content: i18n.global.t('que-shao-quan-xian') + response.data.needAuthKey,
      //   type: 'error'
      // });
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
      } else if (status === 406) {
        // CCMI.confirm({
        //   title: i18n.global.t('quan-xian-yi-chang'),
        //   content: res.message || res.data.message || i18n.global.t('nin-mei-you-gai-quan-xian-de-cao-zuo-qing-lian-xi-zhu-zhang-hao-huo-guan-li-yuan'),
        //   type: 'error'
        // });
        Modal.error({
          title: i18n.global.t('quan-xian-yi-chang'),
          content:
            res.message || res.data.message || i18n.global.t('nin-mei-you-gai-quan-xian-de-cao-zuo-qing-lian-xi-zhu-zhang-hao-huo-guan-li-yuan')
        });
      } else if (status === 500) {
        // if (window.location.href.split('#')[1] !== '/permission/denied' && window.location.href.split('#')[1] !== '/permission/exception') {
        //   localStorage.setItem('console_last_url', window.location.href.split('#')[1]);
        // }
        // window.location.href = `${window.location.protocol}//${window.location.host}/#/permission/exception`;
      } else if (res && !res.errors) {
        if (showActiveLicense(status)) {
          // window.$bus.emit(EVENT_BUS_NAME_LIST.SHOW_INACTIVE_MODAL, res.message || error.response.data);
          eventBus.emit(EVENT_BUS_NAME_LIST.SHOW_INACTIVE_MODAL, res.message || error.response.data);
        } else {
          setTimeout(() => {
            // CCMI.confirm({
            //   title: 'ERROR',
            //   content: `${res.message || error.response.data}`,
            //   type: 'error'
            // });
            Modal.error({
              title: 'ERROR',
              content: `${res.message || error.response.data}`
            });
          }, 300);
        }
      } else {
        setTimeout(() => {
          // CCMI.confirm({
          //   title: 'ERROR',
          //   content: `${res.errors[0].message}`,
          //   type: 'error'
          // });
          Modal.error({
            title: 'ERROR',
            content: `${res.errors[0].message}`
          });
        }, 300);
      }
    } else {
      const url = new URL(error.config.url);
      const pathList = url.pathname.split('/');
      if (pathList.length > 1 && pathList[1] === 'cloudcanal') {
        if (url.pathname !== '/cloudcanal/console/api/v1/inner/cc_global_settings') {
          // CCMI.confirm({
          //   title: 'ERROR',
          //   content: i18n.global.t('chan-pin-ji-qun-wu-fa-fang-wen')
          // });
          Modal.error({
            title: 'ERROR',
            content: i18n.global.t('chan-pin-ji-qun-wu-fa-fang-wen')
          });
        }
      } else {
        // CCMI.confirm({
        //   title: 'ERROR',
        //   content: i18n.global.t('yi-chao-shi')
        // });
        Modal.error({
          title: 'ERROR',
          content: i18n.global.t('yi-chao-shi')
        });
      }
    }
    return Promise.reject(error);
  }
);

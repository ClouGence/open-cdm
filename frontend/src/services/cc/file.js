import axios from 'axios';
import { Modal, Spin } from 'view-ui-plus';
import i18n from '@/i18n';
import CustomIcon from '@/components/function/CustomIcon.vue';
import formatError from '../formatError';

// const baseURL = `${window.location.protocol}//${window.location.host}/cloudcanal/console/api/v1/inner`;
// const baseURL = '/' + '/api/v1/';
let baseURL = `${window.location.protocol}//${window.location.host}/cloudcanal/console/api/v1/inner`;
if (process.env.VUE_APP_BASE_URL) {
  baseURL = `${process.env.VUE_APP_BASE_URL}/cloudcanal/console/api/v1/inner`;
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
      //                     },
      //                     on: {
      //                       click: () => {
      //                         Modal.remove();
      //                       }
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
      if (response.data.code !== '6028' && response.data.code !== '2-11' && response.data.code !== '0014') {
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
                    h('Icon', {
                      props: {
                        type: 'md-close-circle'
                      },
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

import axios from 'axios';
import { Message, Modal } from 'view-ui-plus';
import { showActiveLicense } from '@/utils';
import { addPending, cancelPending, removePending } from '@/services/http/cancelRequest';
import store from '@/store';
import Cookies from 'js-cookie';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import Router from '@/router';
import eventBus from '@/utils/eventBus';
import i18n from '@/i18n';
import { checkStatus, trimObj } from './utils';
import formatError from '../formatError';
import errorQueue from '@/utils/errorQueue';

const UPDATE_DATA_SOURCE_STATUS_LIST = [
  '/clouddm/console/api/v1/browse/actions/doAction',
  '/clouddm/console/api/v1/browse/listLevels',
  '/clouddm/console/api/v1/browse/rdbObjectDetail',
  '/clouddm/console/api/v1/browse/listLeaf',
  '/clouddm/console/api/v1/browse/actions/loadObject',
  '/clouddm/console/api/v1/editor/data/fetchData',
  '/clouddm/console/api/v1/editor/data/saveData',
  '/clouddm/console/api/v1/editor/data/fetchCount',
  '/clouddm/console/api/v1/editor/table/editorDef',
  '/clouddm/console/api/v1/editor/table/initEditor',
  '/clouddm/console/api/v1/editor/table/generateScript',
  '/clouddm/console/api/v1/query/createsession',
  '/clouddm/console/api/v1/browse/actions/requestScript',
  '/clouddm/console/api/v1/browse/actions/generateScript',
  '/clouddm/console/api/v1/editor/table/scriptExecute',
  '/clouddm/console/api/v1/datasource/testConnect'
];

let baseURL = '';
if (process.env.VUE_APP_BASE_URL) {
  baseURL = process.env.VUE_APP_BASE_URL;
}
console.log('request url', baseURL);

const instance = axios.create({
  baseURL,
  timeout: 6000000,
  headers: {
    // 'Accept-Language': i18n.locale
    // 'Access-Control-Allow-Origin': '*'
  },
  transformRequest: [
    (data) => {
      if (!data) {
        return {};
      }
      if (Object.prototype.toString.call(data) !== '[object FormData]') {
        Object.keys(data).forEach((key) => {
          if (!data[key] && data[key] !== false && data[key] !== 0) {
            data[key] = null;
          } else {
            try {
              // 性能优化：对于超大数据量，跳过递归 trim
              const value = data[key];
              if (value && typeof value === 'object' && ((Array.isArray(value) && value.length > 5000) || Object.keys(value).length > 5000)) {
                // 大数据量不进行递归 trim
              } else {
                data[key] = trimObj(value);
              }
            } catch (e) {
              console.error(e);
            }
          }
        });
        return JSON.stringify(data);
      }
      return data;
    }
  ],
  withCredentials: true,
  credentials: 'include'
});

instance.interceptors.request.use(
  (config) => {
    config.headers = {
      Accept: 'application/json',
      'Content-Type': 'application/json; charset=UTF-8',
      'Accept-Language': i18n?.global?.locale?.value,
      ...config.headers
    };

    if (config.url.startsWith('/cloudcanal/console') && store?.state?.selectCcProductCluster) {
      config.headers = {
        'X-Product-Code': store?.state?.selectCcProductCluster,
        ...config.headers
      };
    }

    console.log(i18n?.global?.locale?.value);

    if (config.data && config.data.cancelPending) {
      cancelPending(config);
    }

    addPending(config);
    return Promise.resolve(config);
  },
  (error) => {
    Promise.reject(error);
  }
);

instance.interceptors.response.use(
  (res) => {
    removePending(res.config);
    // if (!res.data.permission) {
    //   res.data.success = false;
    //   res.data.code = 403;
    //   Modal.error({
    //     title: i18n.global.t('quan-xian-yi-chang'),
    //     content: i18n.global.t('que-shao-quan-xian') + res.data.needAuthKey
    //   });
    // }
    return Promise.resolve(checkStatus(res));
  },
  (error) => {
    if (error.response) {
      if (error.response.status === 307) {
        if (error.response.headers['x-redirect-addr']) {
          const cookies = Cookies.get();
          const newUrl = `${error.response.headers['x-redirect-addr']}${error.response.config.url}`;
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
      }
      return Promise.reject(checkStatus(error.response));
    }
    if (error.code === 'ECONNABORTED' && error.message.indexOf('timeout') !== -1) {
      return Promise.reject(new Error(i18n.global.t('qing-qiu-chao-shi')));
    }
    return Promise.reject(error instanceof Error ? error : new Error(error));
  }
);

const request = async (opt) => {
  const options = {
    method: 'post',
    data: {},
    ...opt
  };

  const { url: requestUrl, msg, modal = true, page } = options;

  try {
    // if (!['/login', '/register', '/logout', '/global_settings', '/list_org', '/login_supplement', '/check_supplement', '/load_supplement_info'].includes(options.url)) {
    //   options.baseURL = process.env.VUE_APP_BASE_URL;
    // }
    const res = await instance(options);

    if (UPDATE_DATA_SOURCE_STATUS_LIST.includes(requestUrl) && options?.data?.levels) {
      if (options.data.levels.length && options.data.levels.length > 1) {
        const instanceId = options.data.levels[1];
        if (res.code === '10103' || res.code === '10201') {
          console.log(res);
          eventBus.emit(EVENT_BUS_NAME_LIST.SET_DATA_SOURCE_STATUS, {
            instanceId,
            connected: false,
            msg: res.msg,
            code: res.code
          });
        } else if (res.success) {
          eventBus.emit(EVENT_BUS_NAME_LIST.SET_DATA_SOURCE_STATUS, {
            instanceId,
            connected: true,
            code: res.code
          });
        }
      }
    }

    if (!opt.noStatus) {
      if (!res.success) {
        switch (res.code) {
          case '10103':
          case '20001':
          case '6028':
          case '0014':
          case '2011':
            // eventBus.emit('setCloudAKSKModal');
            break;
          case '6001':
            eventBus.emit('dingDingSettingModal');
            break;
          case '10005':
            if (modal) {
              Modal.error({
                title: i18n.global.t('cuo-wu'),
                class: 'limit-height',
                width: 500,
                okText: i18n.global.t('zhong-xin-deng-lu'),
                zIndex: 9999,
                content: formatError(res.msg) || i18n.global.t('xi-tong-yi-chang-qing-lian-xi-guan-li-yuan'),
                onOk: () => {
                  Router.push({ name: 'Login' });
                }
              });
              return res;
            }
            break;
          default:
            if (['/login', '/datasource/connectds', '/rdp/console/api/v1/mfa/resetMfaSetting'].includes(requestUrl)) {
              return res;
            }
            if (['/datasource/schema/rightclickschema'].includes(requestUrl) && res.data && res.data.next) {
              return res;
            }
            if (modal) {
              // 将错误添加到队列中
              let contentStr = formatError(res.msg) || i18n.global.t('xi-tong-yi-chang-qing-lian-xi-guan-li-yuan');
              errorQueue.addError({
                title: i18n.global.t('cuo-wu'),
                content: contentStr,
                type: 'error',
                url: requestUrl
              });
              return res;
            } else {
              if (res.msg) {
                res.msg = formatError(res.msg);
              }
            }
        }
      } else if (res.success && msg) {
        Message.success(options.msg);
      }
    }
    return res;
  } catch (err) {
    if (typeof err === 'object' && err.toString().includes('Cancel')) {
      return err;
    }
    switch (err.status) {
      case 401:
        console.log('router', Router);
        if (Router.history?.current && Router.history?.current.name === 'Login') {
          console.log('No Redirect');
        } else {
          await Router.push({ name: 'Login' });
        }
        break;
      case 498:
        eventBus.emit('setOpPasswordModal');
        break;
      case 499:
        eventBus.emit('showEnterOpPwdModal');
        break;
      case 406:
        let errmsg = i18n.global.t('nin-mei-you-gai-quan-xian-de-cao-zuo-qing-lian-xi-zhu-zhang-hao-huo-guan-li-yuan');
        if (err.msg && typeof err.msg === 'string') {
          errmsg = err.msg;
        }
        // 权限错误添加到队列
        errorQueue.addError({
          title: i18n.global.t('quan-xian-yi-chang'),
          content: errmsg,
          type: 'error',
          url: err.config?.url || ''
        });
        break;
      case 307:
        break;
      default:
        if (showActiveLicense(err.status)) {
          eventBus.emit(EVENT_BUS_NAME_LIST.SHOW_INACTIVE_MODAL, formatError(err.msg));
        } else if (err.config && err.config.url) {
          const url = new URL(err.config.url);
          const pathList = url.pathname.split('/');
          if (pathList.length > 1 && pathList[1] === 'clouddm') {
            if (url.pathname !== '/clouddm/console/api/v1/dm_global_setting') {
              errorQueue.addError({
                title: 'ERROR',
                content: i18n.global.t('chan-pin-ji-qun-wu-fa-fang-wen'),
                type: 'error',
                url: err.config?.url
              });
            }
          } else {
            errorQueue.addError({
              title: 'ERROR',
              content: err.msg || i18n.global.t('xi-tong-yi-chang'),
              type: 'error',
              url: err.config?.url
            });
          }
        } else {
          errorQueue.addError({
            title: 'ERROR',
            content: err.msg || i18n.global.t('xi-tong-yi-chang'),
            type: 'error',
            url: ''
          });
        }
    }
    return err;
  }
};

export default request;

import ReconnectingWebSocket from 'reconnecting-websocket';
import { UPDATE_SOCKET_STATUS } from '@/store/mutationTypes';
import store from '@/store';
import { WS_TYPE } from '@/utils';
import i18n from '@/i18n';
import eventBus from '@/utils/eventBus';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import Cookies from 'js-cookie';

let rws = null;
let globalCallback = {
  open: null,
  message: null,
  error: null,
  close: null
};

const hasWebSocketInstance = () => !!rws;

const createWebSocket = (url) => {
  console.log('create socket', i18n);

  const jwtToken = Cookies.get('jwt_token');

  let fullUrl = url;
  const params = new URLSearchParams();

  // 添加 locale 参数
  params.append('locale', i18n.global.locale.value);

  if (process.env.NODE_ENV === 'development' && jwtToken) {
    // 将 jwt_token 添加到查询参数中（服务器可能从 query string 读取）
    params.append('jwt_token', jwtToken);
  }

  // 如果 URL 中已经有查询参数，使用 & 连接，否则使用 ?
  const separator = url.includes('?') ? '&' : '?';
  fullUrl = `${url}${separator}${params.toString()}`;

  // ws连接不支持直接塞入headers、这里通过qeury string，让后端优先读取，解决代理ws连接 401的问题
  rws = new ReconnectingWebSocket(fullUrl, null, {
    debug: false,
    reconnectInterval: 3000
  });
  rws.addEventListener('open', () => {
    if (!rws) {
      return;
    }

    if (rws.readyState === rws.OPEN) {
      store?.commit(UPDATE_SOCKET_STATUS, { connected: true, msg: i18n.global.t('lian-jie-yi-jian-li') });
      if (globalCallback.open) {
        globalCallback.open();
      }
    }
  });

  rws.addEventListener('message', (e) => {
    try {
      const data = JSON.parse(e.data);
      if (data && data.type === 'WS_SYS_STATUS' && data?.object?.serviceReady) {
        eventBus.emit(EVENT_BUS_NAME_LIST.SOCKET_CONNECTION_OPEN);
      }

      if (data && data.type === WS_TYPE.WS_RES_ASYNC_EVENT) {
        eventBus.emit(EVENT_BUS_NAME_LIST.WS_RES_ASYNC_EVENT, data);
      }

      if (data && data.type === WS_TYPE.WS_RES_DRIVER_DOWNLOAD_EVENT) {
        eventBus.emit(EVENT_BUS_NAME_LIST.WS_RES_DRIVER_DOWNLOAD_EVENT, data.object || data);
      }
    } catch (error) {
      console.error(error);
    }
    if (globalCallback.message) {
      globalCallback.message(e.data);
    }
  });

  rws.addEventListener('error', (e) => {
    if (globalCallback.error) {
      globalCallback.error(e);
    }
  });

  rws.addEventListener('close', () => {
    store?.commit(UPDATE_SOCKET_STATUS, { connected: false, msg: i18n.global.t('lian-jie-yi-duan-kai') });
    eventBus.emit(EVENT_BUS_NAME_LIST.SOCKET_CONNECTION_CLOSE);
    // rws.close();
    if (globalCallback.close) {
      globalCallback.close();
    }
  });
};

// const webSocketOpen = () => {
//   console.log('ws open');
//   sendWebSocket({}, () => {});
// };

const webSocketSend = (data) => {
  rws.send(JSON.stringify(data));
};

// const webSocketOnMessage = (data) => {
//   globalCallback(data);
// };

const webSocketClose = () => {
  rws.close();
  rws = null;
};

const sendWebSocket = (data, callback = {}) => {
  globalCallback = callback;
  switch (rws.readyState) {
    case rws.OPEN:
      console.log('OPEN');
      webSocketSend(data);
      break;
    case rws.CONNECTING:
      console.log('CONNECTING');
      setTimeout(() => {
        webSocketSend(data, callback);
      }, 1000);
      break;
    case rws.CLOSING:
      console.log('CLOSING');
      setTimeout(() => {
        webSocketSend(data, callback);
      });
      break;
    case rws.CLOSED:
      console.log('CLOSED');
      break;
    default:
      break;
  }
};

export { createWebSocket, webSocketClose, sendWebSocket, hasWebSocketInstance };

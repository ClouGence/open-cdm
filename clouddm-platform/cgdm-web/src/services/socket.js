import ReconnectingWebSocket from 'reconnecting-websocket';
import { UPDATE_SOCKET_STATUS } from '@/store/mutationTypes';
import store from '@/store';
import { WS_TYPE } from '@/utils';
import i18n from '@/i18n';
import eventBus from '@/utils/eventBus';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import Cookies from 'js-cookie';

let rws = null;
let creatingWebSocket = false;
let globalCallback = {
  open: null,
  message: null,
  error: null,
  close: null
};
const pendingRequests = new Map();
const DEFAULT_REQUEST_TIMEOUT = 5000;

const hasWebSocketInstance = () => !!rws;

const buildFullUrl = (url) => {
  const jwtToken = Cookies.get('jwt_token');

  const params = new URLSearchParams();

  // 添加 locale 参数
  params.append('locale', i18n.global.locale.value);

  if (process.env.NODE_ENV === 'development' && jwtToken) {
    // 将 jwt_token 添加到查询参数中（服务器可能从 query string 读取）
    params.append('jwt_token', jwtToken);
  }

  // 如果 URL 中已经有查询参数，使用 & 连接，否则使用 ?
  const separator = url.includes('?') ? '&' : '?';
  return `${url}${separator}${params.toString()}`;
};

const buildHttpUrl = (path) => `${(process.env.VUE_APP_BASE_URL || '').replace(/\/$/, '')}${path}`;

const checkLoginStatus = async () => {
  try {
    const res = await fetch(buildHttpUrl('/rdp/console/api/v1/user/queryLoginUser'), {
      method: 'POST',
      credentials: 'include',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json; charset=UTF-8',
        'Accept-Language': i18n?.global?.locale?.value
      },
      body: JSON.stringify({})
    });
    if (!res.ok) {
      return false;
    }
    const data = await res.json();
    return !!data?.success;
  } catch (error) {
    return false;
  }
};

const createWebSocket = async (url) => {
  console.log('create socket', i18n);

  if (rws || creatingWebSocket) {
    return;
  }

  creatingWebSocket = true;
  const loggedIn = await checkLoginStatus();
  creatingWebSocket = false;
  if (!loggedIn || rws) {
    return;
  }

  // ws连接不支持直接塞入headers、这里通过qeury string，让后端优先读取，解决代理ws连接 401的问题
  rws = new ReconnectingWebSocket(
    async () => {
      const reconnectLoggedIn = await checkLoginStatus();
      if (!reconnectLoggedIn) {
        webSocketClose();
      }
      return buildFullUrl(url);
    },
    null,
    {
      debug: false,
      minReconnectionDelay: 3000,
      maxReconnectionDelay: 3000
    }
  );
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
      resolvePendingRequest(data);
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
    rejectPendingRequests(new Error('websocket closed'));
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
  if (!rws || rws.readyState !== rws.OPEN) {
    return;
  }

  rws.send(JSON.stringify(data));
};

const pendingKey = (type, requestId) => `${type}:${requestId}`;

const resolvePendingRequest = (data) => {
  const requestId = data?.object?.requestId;
  if (!data?.type || !requestId) {
    return;
  }

  const key = pendingKey(data.type, requestId);
  const pending = pendingRequests.get(key);
  if (!pending) {
    return;
  }

  clearTimeout(pending.timer);
  pendingRequests.delete(key);
  pending.resolve(data.object);
};

const rejectPendingRequests = (error) => {
  pendingRequests.forEach((pending) => {
    clearTimeout(pending.timer);
    pending.reject(error);
  });
  pendingRequests.clear();
};

// const webSocketOnMessage = (data) => {
//   globalCallback(data);
// };

const webSocketClose = () => {
  if (!rws) {
    return;
  }

  rws.close();
  rws = null;
  rejectPendingRequests(new Error('websocket closed'));
};

const sendWebSocket = (data, callback = {}) => {
  if (!rws) {
    return;
  }

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

const requestWebSocket = ({ type, responseType, object, timeout = DEFAULT_REQUEST_TIMEOUT }) =>
  new Promise((resolve, reject) => {
    if (!type || !responseType) {
      reject(new Error('missing websocket type'));
      return;
    }

    const requestId = object?.requestId;
    if (!requestId) {
      reject(new Error('missing websocket requestId'));
      return;
    }

    if (!rws || rws.readyState !== rws.OPEN) {
      reject(new Error('websocket is not connected'));
      return;
    }

    const key = pendingKey(responseType, requestId);
    const timer = setTimeout(() => {
      pendingRequests.delete(key);
      reject(new Error('websocket request timeout'));
    }, timeout);
    pendingRequests.set(key, { resolve, reject, timer });
    webSocketSend({ type, object });
  });

export { createWebSocket, webSocketClose, sendWebSocket, requestWebSocket, hasWebSocketInstance };

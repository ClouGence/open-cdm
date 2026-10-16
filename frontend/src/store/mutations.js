import appLogger from '@/utils/logger';
import {
  SET_MENU_ITEMS,
  SET_THEME,
  UPDATE_DM_GLOBAL_SETTING,
  UPDATE_EDITOR_SET,
  UPDATE_GLOBAL_SETTING,
  UPDATE_MY_AUTH,
  UPDATE_MY_CATALOG,
  UPDATE_PUBLIC_KEY,
  UPDATE_RULE_SETTING,
  UPDATE_SOCKET_STATUS,
  UPDATE_USERINFO
} from '@/store/mutationTypes';
import router from '@/router';
import { buildSidebarMenu, flattenSidebarMenu } from '@/utils/buildSidebarMenu';

function applyMenuItems(state, myCatLog = state.myCatLog, myAuth = state.myAuth) {
  const isDesktop = !!state.dmGlobalSetting.personal;
  const sidebarMenu = buildSidebarMenu({
    myCatLog,
    myAuth,
    isDesktop,
    accountType: state.userInfo?.accountType
  });

  state.sidebarMenu = sidebarMenu;
  state.mySystemMenuItems = flattenSidebarMenu(sidebarMenu);
}

export default {
  [UPDATE_RULE_SETTING](state, data) {
    state.ruleSetting = data.ruleSetting;
  },
  [UPDATE_PUBLIC_KEY](state, publicKey) {
    state.publicKey = publicKey;
  },
  [UPDATE_USERINFO](state, userInfo) {
    appLogger.debug('UPDATE_USERINFO', userInfo);
    if (userInfo) {
      state.userInfo = { ...state.userInfo, ...userInfo };
    } else {
      state.userInfo = {};
    }
    applyMenuItems(state);
  },
  [UPDATE_MY_CATALOG](state, data) {
    state.myCatLog = data;
    applyMenuItems(state, data);
  },
  [SET_MENU_ITEMS](state, { myCatLog, myAuth }) {
    applyMenuItems(state, myCatLog, myAuth || state.myAuth);
  },
  [UPDATE_GLOBAL_SETTING](state, globalSetting) {
    appLogger.warn(UPDATE_GLOBAL_SETTING);
    state.globalSetting = globalSetting;
    applyMenuItems(state);
    // Set menu entry after initialization of globalSetting
    let url = '';
    if (state.mySystemMenuItems.length) {
      url = state.mySystemMenuItems[0].key;
    }
    state.contactUsUrl = 'https://www.cdmgr.com/';
    state.dmDocUrlPrefix = 'https://www.clougence.com/dm-doc';
    if (state.myCatLog.includes('CAT_DM_CONSOLE')) {
      url = '/sql';
    } else if (state.myCatLog.includes('CAT_RDP_WORKER_ORDER')) {
      url = '/ticket';
    } else if (state.myCatLog.includes('CAT_DM_SYS')) {
      if (state.myCatLog.includes('CAT_DM_WORKER')) {
        url = '/data-access/cluster';
      } else if (state.myCatLog.includes('CAT_DM_SECRULES')) {
        url = '/data-access/rules';
      }
    } else if (state.myCatLog.includes('CAT_DM_CICD_FLOW')) {
      url = '/cicd';
    }

    if (!url) {
      url = '/sql';
    }

    appLogger.debug(url);
    state.defaultRedirectUrl = url;

    if (window.location.hash === '#/') {
      router.push(url);
    }
  },
  [UPDATE_DM_GLOBAL_SETTING](state, dmGlobalSetting = {}) {
    state.dmGlobalSetting = dmGlobalSetting;
    state.globalDsSetting = dmGlobalSetting.dsSettingDef;
  },
  [UPDATE_EDITOR_SET](state, data) {
    const { id, model, state: mState } = data;
    state.editorSet[id] = { model, state: mState };
  },
  [UPDATE_MY_AUTH](state, data) {
    state.myAuth = data;
    applyMenuItems(state);
  },
  [UPDATE_SOCKET_STATUS](state, socket) {
    appLogger.debug(socket);
    state.socket = socket;
  },
  [SET_THEME](state, theme) {
    state.theme = theme;
    //RequestAnimationFrame: Ensure DOM changes are synchronized with browser rendering to avoid flashing and Carton
    requestAnimationFrame(() => {
      document.documentElement.setAttribute('data-theme', theme);
    });
    // Endurance of the walk to avoid blocking the main course
    try {
      requestIdleCallback
        ? requestIdleCallback(() => localStorage.setItem('app-theme', theme))
        : setTimeout(() => localStorage.setItem('app-theme', theme), 0);
    } catch (err) {
      appLogger.debug(err);
      localStorage.setItem('app-theme', theme);
    }
  }
};

import {
  UPDATE_CLUSTER_LIST,
  UPDATE_DEPLOY_ENV_LIST_MAP,
  UPDATE_DS_TYPE_LIST,
  UPDATE_GLOBAL_SETTING,
  UPDATE_USERINFO,
  UPDATE_DM_GLOBAL_SETTING,
  UPDATE_MY_AUTH,
  UPDATE_MY_CATALOG,
  UPDATE_RULE_SETTING,
  REMAIN_TRIAL_DAY,
  SET_MENU_ITEMS,
  SET_THEME
} from '@/store/mutationTypes';
import i18n from '@/i18n';
import { createWebSocket, hasWebSocketInstance } from '@/services/socket';
import { services } from '@/services/http';
import { setPageIcon, WEBSIDE_FAVICON } from '@/utils/pluginResource';

const initWebsocket = (globalSetting, loggedIn) => {
  if (hasWebSocketInstance()) {
    return;
  }

  if (!loggedIn) {
    return;
  }

  if (window.location.hash.startsWith('#/initialization')) {
    return;
  }

  if (globalSetting?.systemStatus?.status && globalSetting.systemStatus.status !== 'Ready') {
    return;
  }

  const wsProtocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
  const host = process.env.NODE_ENV === 'development' && process.env.VUE_APP_DM_HOST ? process.env.VUE_APP_DM_HOST : window.location.host;

  createWebSocket(`${wsProtocol}://${host}/api/entry/ws/channel`);
};

export default {
  async getRuleSetting({ commit }) {
    const res = await services.dmSecurityRulesRuleSettingDef();
    if (res.success) {
      commit(UPDATE_RULE_SETTING, { ruleSetting: res.data });
    }
  },
  async getUserInfo({ commit }) {
    const userInfoRes = await services.rdpUserQueryLoginUser();
    if (userInfoRes.success) {
      // if (!process.env.VUE_APP_IS_SAAS || process.env.VUE_APP_IS_TEST) {
      // const userConfigRes = await services.rdpUserConfigGetCurrUserConfigs();
      // if (userConfigRes.success) {
      //   userInfoRes.data.userConfig = userConfigRes.data;
      // }
      // }
      commit(UPDATE_USERINFO, userInfoRes.data);
      const userAuthRes = await services.rdpUserListMyAuth();
      if (userAuthRes.success) {
        commit(UPDATE_MY_AUTH, userAuthRes.data);
      }
      const catRes = await services.rdpUserListMyAuthCategoryForMenu();
      if (catRes.success) {
        commit(UPDATE_MY_CATALOG, catRes.data);
      }
    }

    const globalSettingRes = await services.getGlobalSettings();
    if (globalSettingRes.success) {
      commit('SET_MENU_ITEMS', {
        myCatLog: this.state.myCatLog,
        globalSetting: globalSettingRes.data,
        userInfo: this.state.userInfo,
        myAuth: this.state.myAuth
      });

      commit(UPDATE_GLOBAL_SETTING, globalSettingRes.data);
      initWebsocket(globalSettingRes.data, userInfoRes.success);
      commit(SET_THEME, 'light');
      setPageIcon(WEBSIDE_FAVICON);
      document.title = 'CloudDM';
    }
  },
  async getDmGlobalConfig({ commit, state }) {
    const consoleSettingRes = await services.dmConsoleSettings();
    if (consoleSettingRes.success) {
      const dmSetting = consoleSettingRes.data;
      dmSetting.dsSupportNames = consoleSettingRes.data?.dsSupportNames || [];
      dmSetting.fmtConvertDef = consoleSettingRes.data?.fmtConvertDef;

      if (!dmSetting.version && state.dmGlobalSetting?.version) {
        dmSetting.version = state.dmGlobalSetting.version;
      }

      if (dmSetting.personal) {
        i18n.locale = 'zh-CN';
      }

      commit(UPDATE_DM_GLOBAL_SETTING, dmSetting);
    }
  },
  async getDeployEnvList({ commit }) {
    const res = await services.dmConstantListDeployEnv();
    if (res.success) {
      commit(UPDATE_DEPLOY_ENV_LIST_MAP, res.data);
    }
  },
  async getRegionList({ commit }) {
    // const res = await request({
    //   url: api.getRegionList,
    //   data: { deployEnvType: CLUSTER_ENV.ALIBABA_CLOUD_HOSTED }
    // });
    // const res2 = await request({
    //   url: api.getRegionList,
    //   data: { deployEnvType: CLUSTER_ENV.SELF_MAINTENANCE }
    // });
    //
    // if (res.success && res2.success) {
    //   const data = {
    //     aliyun: res.data,
    //     self: res2.data
    //   };
    //   commit(UPDATE_REGION_LIST_MAP, data);
    // }
  },
  async getClusterList({ commit }) {
    const res = await services.dmClusterListByCondition({ data: {} });
    if (res.success) {
      commit(UPDATE_CLUSTER_LIST, res.data);
    }
  },
  async getDsTypeList({ commit }, deployEnvType) {
    const res = await services.dmConstantListDsTypes({ data: { deployEnvType } });

    if (res.success) {
      commit(UPDATE_DS_TYPE_LIST, res.data);
    }
  },
  // Theme-related actions
  toggleTheme({ commit, state }) {
    const newTheme = state.theme === 'light' ? 'dark' : 'light';
    commit(SET_THEME, newTheme);
  },
  setTheme({ commit }, theme) {
    commit(SET_THEME, theme);
  },
  initTheme({ commit }) {
    // Follow the system theme by default
    let theme = matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';

    // Prefer the user preference
    if (localStorage.getItem('app-theme')) {
      theme = localStorage.getItem('app-theme');
    }

    if (theme) {
      commit(SET_THEME, theme);
    } else {
      commit(SET_THEME, 'light');
    }
  }
};

export const ACTIONS_TYPE = {
  GET_USER_INFO: 'getUserInfo',
  GET_DEPLOY_ENV_LIST: 'getDeployEnvList',
  GET_REGION_LIST: 'getRegionList',
  GET_CLUSTER_LIST: 'getClusterList',
  GET_DS_TYPE_LIST: 'getDsTypeList',
  GET_DS_LIST: 'getDsList'
};

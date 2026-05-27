import {
  UPDATE_CLUSTER_LIST,
  UPDATE_DEPLOY_ENV_LIST_MAP,
  UPDATE_DS_TYPE_LIST,
  UPDATE_GLOBAL_SETTING,
  UPDATE_USERINFO,
  UPDATE_DM_GLOBAL_SETTING,
  UPDATE_CC_GLOBAL_SETTING,
  UPDATE_MY_AUTH,
  UPDATE_MY_CATALOG,
  UPDATE_RULE_SETTING,
  UPDATE_PUBLIC_KEY,
  REMAIN_TRIAL_DAY,
  SET_MENU_ITEMS,
  SET_THEME
} from '@/store/mutationTypes';
import i18n from '@/i18n';
import { createWebSocket, hasWebSocketInstance } from '@/services/socket';
import { services } from '@/services/http';
import { filterGlobalSettingByBuild, supportsCloudCanalBuild, supportsCloudDMBuild } from '@/utils/product';

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

  if (!globalSetting?.features?.PRODUCT_CLOUD_DM) {
    return;
  }

  if (globalSetting?.systemStatus?.status && globalSetting.systemStatus.status !== 'Ready') {
    return;
  }

  const wsProtocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
  const host = process.env.NODE_ENV === 'development' && process.env.VUE_APP_DM_HOST ? process.env.VUE_APP_DM_HOST : window.location.host;

  createWebSocket(`${wsProtocol}://${host}/clouddm/console/api/v1/ws/channel`);
};

export default {
  async getRuleSetting({ commit }) {
    const res = await services.dmSecurityRulesRuleSettingDef();
    if (res.success) {
      commit(UPDATE_RULE_SETTING, { ruleSetting: res.data });
    }
  },
  async getUserInfo({ commit }) {
    const publicKeyRes = await services.getPublicKey();
    if (publicKeyRes.success) {
      commit(UPDATE_PUBLIC_KEY, publicKeyRes.data);
    }
    const userInfoRes = await services.rdpUserQueryLoginUser();
    if (userInfoRes.success) {
      // if (!process.env.VUE_APP_IS_SAAS || process.env.VUE_APP_IS_TEST) {
      // const userConfigRes = await services.rdpUserConfigGetCurrUserConfigs();
      // if (userConfigRes.success) {
      //   userInfoRes.data.userConfig = userConfigRes.data;
      // }
      // }
      commit(UPDATE_USERINFO, userInfoRes.data);
      // const remainingTrialDay = await request({ url: api.ccSaasQueryRemainingTrialDay });
      // if (remainingTrialDay.success) {
      //   commit(REMAIN_TRIAL_DAY, remainingTrialDay.data.data.trailEndTimeMs);
      // }
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
      const filteredGlobalSetting = filterGlobalSettingByBuild(globalSettingRes.data);
      commit('SET_MENU_ITEMS', {
        myCatLog: this.state.myCatLog,
        globalSetting: filteredGlobalSetting,
        userInfo: this.state.userInfo,
        myAuth: this.state.myAuth
      });

      commit(UPDATE_GLOBAL_SETTING, filteredGlobalSetting);
      initWebsocket(filteredGlobalSetting, userInfoRes.success);

      const { PRODUCT_CLOUD_DM, PRODUCT_CLOUD_CANAL } = filteredGlobalSetting.features;

      if (PRODUCT_CLOUD_DM && !PRODUCT_CLOUD_CANAL) {
        commit(SET_THEME, 'light');
      }

      let icon_url = '';
      let title = '';
      if (PRODUCT_CLOUD_DM && !PRODUCT_CLOUD_CANAL) {
        icon_url = '/dm.ico';
        title = 'CloudDM';
      }
      if (PRODUCT_CLOUD_DM && PRODUCT_CLOUD_CANAL) {
        icon_url = '/rdp.ico';
        title = 'ClouGence RDP';
      }
      if (!PRODUCT_CLOUD_DM && PRODUCT_CLOUD_CANAL) {
        icon_url = '/cc.ico';
        title = 'CloudCanal';
      }
      const link = document.querySelector("link[rel*='icon']") || document.createElement('link');
      link.type = 'image/x-icon';
      link.rel = 'shortcut icon';
      link.href = icon_url;
      document.getElementsByTagName('head')[0].appendChild(link);
      document.title = title;
    }
  },
  async getCcGlobalConfig({ commit }) {
    if (!supportsCloudCanalBuild) {
      return;
    }

    const ccGlobalSettingRes = await services.ccCcGlobalConfig();
    if (ccGlobalSettingRes.success) {
      commit(UPDATE_CC_GLOBAL_SETTING, ccGlobalSettingRes.data);
    }
  },
  async getDmGlobalConfig({ commit }) {
    if (!supportsCloudDMBuild) {
      return;
    }

    const globalSettingRes = await services.dmGlobalSettings();
    const consoleSettingRes = await services.dmConsoleSettings();
    if (globalSettingRes.success && consoleSettingRes.success) {
      const dmSetting = globalSettingRes.data;
      dmSetting.menus = consoleSettingRes.data.menus;
      dmSetting.dsSettingDef = consoleSettingRes.data.dsSettingDef;
      dmSetting.fmtConvertDef = consoleSettingRes.data?.fmtConvertDef;

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
  async getClusterList({ commit }, deployEnvType) {
    const res = await services.dmConstantListCluster({ data: { deployEnvType } });
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
  // 主题相关 actions
  toggleTheme({ commit, state }) {
    const newTheme = state.theme === 'light' ? 'dark' : 'light';
    commit(SET_THEME, newTheme);
  },
  setTheme({ commit }, theme) {
    commit(SET_THEME, theme);
  },
  initTheme({ commit }) {
    // 默认跟随系统主题
    let theme = matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';

    // 优先使用用户偏好
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

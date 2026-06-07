import {
  UPDATE_CLUSTER_LIST,
  UPDATE_DEPLOY_ENV_LIST_MAP,
  UPDATE_DS_TYPE_LIST,
  UPDATE_EDITOR_SET,
  UPDATE_GLOBAL_SETTING,
  UPDATE_REGION_LIST_MAP,
  UPDATE_USERINFO,
  UPDATE_PRODUCT_CLUSTER,
  UPDATE_SELECT_PRODUCT_CLUSTER,
  UPDATE_CC_GLOBAL_SETTING,
  UPDATE_DM_GLOBAL_SETTING,
  UPDATE_MY_CATALOG,
  UPDATE_MY_AUTH,
  UPDATE_SOCKET_STATUS,
  UPDATE_RULE_SETTING,
  UPDATE_PUBLIC_KEY,
  REMAIN_TRIAL_DAY,
  UPDATE_TASK_INFO_HISTORY,
  UPDATE_TASK_INFO_DB_MAP_HISTORY,
  SET_MENU_ITEMS,
  SET_THEME
} from '@/store/mutationTypes';
import i18n from '@/i18n';
import router from '@/router';
import { supportsCloudCanalBuild, supportsCloudDMBuild } from '@/utils/product';

const URL_AUTH_MAPPING = {};

function applyMenuItems(state, myCatLog = state.myCatLog, globalSetting = state.globalSetting, myAuth = state.myAuth) {
  const systemMenuItems = [];
  if (myCatLog.includes('CAT_RDP_USER')) {
    systemMenuItems.push({
      key: '/system/account',
      href: '/#/system/account',
      label: i18n.global.t('zi-zhang-hao-guan-li'),
      iconName: 'icon-v2-sub_account'
    });
  }
  if (myCatLog.includes('CAT_RDP_ROLE')) {
    systemMenuItems.push({
      key: '/system/role',
      href: '/#/system/role',
      label: i18n.global.t('jiao-se-guan-li'),
      iconName: 'icon-v2-role'
    });
  }
  if (myCatLog.includes('CAT_RDP_ENV')) {
    systemMenuItems.push({
      key: '/system/env',
      href: '/#/system/env',
      label: i18n.global.t('huan-jing-guan-li'),
      iconName: 'icon-v2-env'
    });
  }
  if (myCatLog.includes('CAT_RDP_DS')) {
    systemMenuItems.push({
      key: '/system/ccdatasource',
      href: '/#/system/ccdatasource',
      label: i18n.global.t('shu-ju-yuan-guan-li'),
      iconName: 'icon-v2-peizhishujuyuan'
    });
  }
  if (myCatLog.includes('CAT_DM_SYS') && myCatLog.includes('CAT_DM_WORKER')) {
    systemMenuItems.push({
      key: '/system/dmmachine',
      href: '/#/system/dmmachine',
      label: i18n.global.t('cha-xun-ji-qi'),
      iconName: 'icon-v2-cluster'
    });
  }
  if (myCatLog.includes('CAT_DM_SYS') && myCatLog.includes('CAT_DM_SECRULES')) {
    systemMenuItems.push({
      key: '/system/dmrulelist',
      href: '/#/system/dmrulelist',
      label: i18n.global.t('an-quan-gui-ze'),
      iconName: 'icon-v2-audit'
    });
    systemMenuItems.push({
      key: '/system/dmspeclist',
      href: '/#/system/dmspeclist',
      label: i18n.global.t('an-quan-gui-fan'),
      iconName: 'icon-v2-role'
    });
  }
  if (myCatLog.includes('CAT_RDP_OP_AUDIT')) {
    systemMenuItems.push({
      key: '/system/operation_log',
      href: '/#/system/operation_log',
      label: i18n.global.t('cao-zuo-shen-ji'),
      iconName: 'icon-v2-audit'
    });
  }
  if (myCatLog.includes('CAT_DM_SQL_AUDIT')) {
    systemMenuItems.push({
      key: '/system/sql_log',
      href: '/#/system/sql_log',
      label: i18n.global.t('sql-shen-ji'),
      iconName: 'icon-v2-SqlLog'
    });
  }
  if (myCatLog.includes('CAT_RDP_PRI_PREFERENCE_CONF') && myAuth.includes('RDP_PRI_USER_KV_CONF_R')) {
    systemMenuItems.push({
      key: '/system/preference',
      href: '/#/system/preference',
      label: i18n.global.t('ge-ren-pian-hao'),
      iconName: 'icon-v2-preference'
    });
  }

  if (myCatLog.includes('CAT_DM_CICD')) {
    systemMenuItems.push({
      key: '/system/devops',
      href: '/#/system/devops',
      label: 'CI/CD',
      iconName: 'icon-v2-sub_account'
    });
  }

  if (myCatLog.includes('CAT_DM_IM')) {
    systemMenuItems.push({
      key: '/system/im',
      href: '/#/system/im',
      label: i18n.global.t('im'),
      iconName: 'icon-v2-sub_account'
    });
  }

  state.mySystemMenuItems = systemMenuItems;
}

export default {
  [UPDATE_RULE_SETTING](state, data) {
    state.ruleSetting = data.ruleSetting;
  },
  [UPDATE_PUBLIC_KEY](state, publicKey) {
    state.publicKey = publicKey;
  },
  [UPDATE_USERINFO](state, userInfo) {
    console.log('UPDATE_USERINFO', userInfo);
    if (userInfo) {
      state.userInfo = { ...state.userInfo, ...userInfo };
    } else {
      state.userInfo = {};
    }
  },
  [UPDATE_CLUSTER_LIST](state, list) {
    const temp = {};
    let workersNum = 0;
    let runningWorkersNum = 0;
    list.forEach((cluster) => {
      workersNum += cluster.workerCount;
      runningWorkersNum += cluster.runningCount;
      temp[cluster.id] = {
        name: cluster.clusterName,
        desc: cluster.clusterDesc,
        value: cluster.id,
        ...cluster
      };
    });

    state.clusterListMap = temp;
    state.allClusterWorkers = workersNum;
    state.allClusterRunningWorkers = runningWorkersNum;
  },
  [UPDATE_DEPLOY_ENV_LIST_MAP](state, list) {
    const temp = {};
    list.forEach((env) => {
      temp[env.name] = {
        name: env.nameI18n,
        value: env.name
      };
    });

    state.deployEnvListMap = temp;
  },
  [UPDATE_REGION_LIST_MAP](state, list) {
    const temp = {};
    const temp2 = {};
    const temp3 = {};
    list.aliyun.forEach((region) => {
      const { regionName, regionNameI18n, regionKindI18n } = region;
      temp3[regionName] = regionNameI18n;
      if (temp[regionKindI18n]) {
        temp[regionKindI18n].children.push(region);
      } else {
        temp[regionKindI18n] = {
          name: regionKindI18n,
          children: [region]
        };
      }
    });
    list.self.forEach((region) => {
      const { regionKindI18n } = region;
      if (temp2[regionKindI18n]) {
        temp2[regionKindI18n].children.push(region);
      } else {
        temp2[regionKindI18n] = {
          name: regionKindI18n,
          children: [region]
        };
      }
    });
    state.aliyunRegionListMap = temp;
    state.selfRegionListMap = temp2;
    state.regionList = temp3;
  },
  [UPDATE_DS_TYPE_LIST](state, list) {
    state.dsTypeList = list;
  },
  [UPDATE_PRODUCT_CLUSTER](state, list) {
    state.productClusterList = list;
  },
  [UPDATE_SELECT_PRODUCT_CLUSTER](state, cluster) {
    console.log('UPDATE_SELECT_PRODUCT_CLUSTER', cluster);
    state.selectCcProductCluster = cluster;
  },
  [UPDATE_MY_CATALOG](state, data) {
    state.myCatLog = data;
    applyMenuItems(state, data);
  },
  [SET_MENU_ITEMS](state, { myCatLog, globalSetting, userInfo, myAuth }) {
    applyMenuItems(state, myCatLog, globalSetting, myAuth || state.myAuth);
  },
  updateMetaCenterSearchParam(state, params) {
    state.metaCenterSearchParams = params;
  },
  updateSelectedWorker(state, worker) {
    state.selectedWorker = worker;
  },
  updateSetPkAndIdFlags(state, flags) {
    state.setPkAndIdFlags = flags;
  },
  getGlobalConfig(state, config) {
    state.globalConfig = config;
  },
  getUserRole(state, role) {
    state.userRole = role;
  },
  getLicenseStatus(state, data) {
    state.licenseStatus = data;
  },
  getBlackUri(state, blackUri) {
    state.blackUri = blackUri;
  },
  getUrlLabels(state, list) {
    state.urlLabels = list;
  },
  updateSelectProductCluster(state, cluster) {
    state.selectCcProductCluster = cluster;
  },
  updateUserInfo(state, userInfo) {
    state.userInfo = userInfo;
  },
  changeConnection(state, changeState) {
    if (changeState.type === 'source') {
      state.sourceConnection = changeState.ifConnection;
    } else {
      state.sinkConnection = changeState.ifConnection;
    }
  },
  getDataSourceDeployTypes(state, list) {
    state.dataSourceDeployTypes = list;
  },
  getSourceDataSourceTypes(state, list) {
    state.sourceDataSourceTypes = list;
  },
  getSinkDataSourceTypes(state, list) {
    state.sinkDataSourceTypes = list;
  },
  getSourceInstance(state, list) {
    state.sourceInstanceList = list;
  },
  getSinkInstance(state, list) {
    state.sinkInstanceList = list;
  },
  getSourceDbList(state, list) {
    state.sourceDbList = list;
  },
  getSinkDbList(state, list) {
    state.sinkDbList = list;
  },
  changeTest1(state, ifTest) {
    state.showTest1 = ifTest;
  },
  changeTest2(state, ifTest) {
    state.showTest2 = ifTest;
  },
  getTableDatas(state, data) {
    state.tableDatas[data.db][data.index][data.type] = data.data;
  },
  updateCleanDataData(state, data) {
    state.cleanDataData[`${data.table}|${data.db}`][data.index][data.type] = data.data;
  },
  getSelectedTables(state, data) {
    state.selectedTables = data;
  },
  getSelectedColumns(state, data) {
    state.selectedColumns = data;
  },
  updateSelectedTables(state, data) {
    state.selectedTables[data.db][data.index][data.type] = data.data;
  },
  updateTableFilter(state) {
    state.firstToTableFilter = true;
  },
  updateCleanData(state, firstToCleanData = true) {
    state.firstToCleanData = firstToCleanData;
  },
  getSinkTableList(state, data) {
    state.sinkTableList[data.db] = data.data;
  },
  getTableInfo(state, list) {
    state.tableInfo = list;
  },
  getSinkColumns(state, data) {
    state.sinkColumns[data.key] = data.data;
  },
  getCleanDataData(state, data) {
    state.cleanDataData[`${data.table}|${data.db}`] = data.data;
  },
  clearCleanData(state) {
    state.cleanDataData = {};
  },
  getCleanDataSinkTables(state, list) {
    state.cleanDataSinkTables = list;
  },
  updateCompareSelectedTables(state, obj) {
    state.compareSelectedTables = obj;
  },
  updateCompareSelectedDbs(state, obj) {
    state.compareSelectedDbs = obj;
  },
  getJobDataForSimilarJob(state, data) {
    state.jobData = data;
  },
  clearJobDataForSimilarJob(state) {
    state.jobData = null;
  },
  [UPDATE_GLOBAL_SETTING](state, globalSetting) {
    console.warn(UPDATE_GLOBAL_SETTING);
    state.globalSetting = globalSetting;
    const includesCC = supportsCloudCanalBuild;
    const includesDM = supportsCloudDMBuild;
    applyMenuItems(state, state.myCatLog, globalSetting);
    // 在 globalSetting 初始化完成后设置菜单项
    let url = '';
    if (state.mySystemMenuItems.length) {
      url = state.mySystemMenuItems[0].key;
    }
    state.docUrlPrefix = 'https://www.clougence.com/cc-doc';
    state.contactUsUrl = 'https://www.clougence.com/about';
    state.dmDocUrlPrefix = 'https://www.clougence.com/dm-doc';
    state.bladePipeApply = 'https://www.clougence.com/dm-doc/clouddm';
    if (state.myCatLog.includes('CAT_DM_CONSOLE')) {
      url = '/sql';
    } else if (state.myCatLog.includes('CAT_RDP_WORKER_ORDER')) {
      url = '/ticket';
    } else if (state.myCatLog.includes('CAT_DM_SYS')) {
      if (state.myCatLog.includes('CAT_DM_WORKER')) {
        url = '/system/dmmachine';
      } else if (state.myCatLog.includes('CAT_DM_SECRULES')) {
        url = '/system/dmrulelist';
      }
    } else if (state.myCatLog.includes('CAT_DM_PROJECT')) {
      url = '/project';
    }

    if (!url) {
      url = includesDM ? '/sql' : '/system';
    }

    console.log(url);
    state.defaultRedirectUrl = url;

    if (window.location.hash === '#/') {
      router.push(url);
    }
  },
  [UPDATE_CC_GLOBAL_SETTING](state, ccGlobalSetting) {
    if (!supportsCloudCanalBuild) {
      return;
    }

    state.ccGlobalSetting = ccGlobalSetting;
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
    console.log(socket);
    state.socket = socket;
  },
  [REMAIN_TRIAL_DAY](state, data) {
    state.remainTrialDay = data;
  },
  updateLastChildTableSubOpts(state, data) {
    if (!data) return (state.lastChildTableSubOptions = {});
    state.lastChildTableSubOptions[data.key] = data.value;
  },
  [UPDATE_TASK_INFO_HISTORY](state, data) {
    state.taskInfoHistory = {
      ...state.taskInfoHistory,
      ...data
    };
  },
  [UPDATE_TASK_INFO_DB_MAP_HISTORY](state, data) {
    state.taskInfoDbMapHistory = {
      ...state.taskInfoDbMapHistory,
      ...data
    };
  },
  [SET_THEME](state, theme) {
    state.theme = theme;
    //requestAnimationFrame：确保 DOM 修改与浏览器渲染同步，避免闪烁和卡顿
    requestAnimationFrame(() => {
      document.documentElement.setAttribute('data-theme', theme);
    });
    // 异步持久化避免阻塞主线程
    try {
      requestIdleCallback
        ? requestIdleCallback(() => localStorage.setItem('app-theme', theme))
        : setTimeout(() => localStorage.setItem('app-theme', theme), 0);
    } catch (err) {
      console.log(err);
      localStorage.setItem('app-theme', theme);
    }
  }
};

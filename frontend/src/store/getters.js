import { EDITIONS, VERIFY_TYPE } from '@/const/ccIndex';
import { supportsCloudCanalBuild, supportsCloudDMBuild } from '@/utils/product';
import { resolveVersionBadgeText } from '@/utils/version';

const RULE_KIND_CONF_MAP = {
  QUERY: 'queryConf',
  SENSITIVE: 'senConf'
};

export default {
  userInfo: (state) => state.userInfo,
  isDesktop: (state) => !!state.dmGlobalSetting.personal,
  isSaas: () => false,
  upgradeSidecar: (state) => supportsCloudCanalBuild && state.ccGlobalSetting.productConsolePackageMode === 'TGZ',
  blackUri: (state) => state.blackUri,
  globalConfig: (state) => state.globalConfig,
  isProductTrail: (state) => supportsCloudCanalBuild && state.ccGlobalSetting.productAuthType === EDITIONS.COMMUNITY_VERSION,
  isExperienceVersion: (state) => supportsCloudCanalBuild && state.ccGlobalSetting.productAuthType === EDITIONS.EXPERIENCE_VERSION,
  verifyType: () => VERIFY_TYPE.SMS_VERIFY_CODE,
  productClusterList: (state) => state.productClusterList,
  includesCC: () => false,
  includesDM: () => true,
  ifShowDsExtraConf: (state) => state.globalSetting.enableValidateDsExtraConf,
  displayVersion: (state) => resolveVersionBadgeText(state.dmGlobalSetting),
  getNodeType: (state) => (type, deep) => state.globalDsSetting[type].categories.levels[deep],
  getLevels: (state) => (type) => {
    if (state.globalDsSetting[type]) {
      return state.globalDsSetting[type]?.categories.levels;
    }
    return [];
  },
  getLeafGroup: (state) => (type, level) => state.globalDsSetting[type]?.categories?.leafGroup?.[level],
  getLeafExpand: (state) => (type) => state.globalDsSetting[type]?.categories.leafExpand,
  getDsClassify: (state) => (type) => {
    if (state.globalDsSetting && state.globalDsSetting[type] && state.globalDsSetting[type].constant) {
      return state.globalDsSetting[type].classify;
    }
  },
  getQuickQuery: (state) => (type) => {
    if (state.globalDsSetting && state.globalDsSetting[type] && state.globalDsSetting[type].constant) {
      return state.globalDsSetting[type].constant.quickQueryMap;
    }
  },
  getBrowserMenus:
    (state) =>
    (dsType = null, tarType = null) => {
      if (
        dsType &&
        tarType &&
        state.globalDsSetting &&
        state.globalDsSetting[dsType] &&
        state.globalDsSetting[dsType].menus &&
        state.globalDsSetting[dsType].menus[tarType]
      ) {
        return state.globalDsSetting[dsType].menus[tarType];
      } else {
        return state.dmGlobalSetting.menus[tarType];
      }
    },
  getMenus:
    (state) =>
    (menuType, dsType = null) =>
      dsType
        ? state.globalDsSetting &&
          state.globalDsSetting[dsType] &&
          state.globalDsSetting[dsType].menus &&
          state.globalDsSetting[dsType].menus[menuType]
        : state.dmGlobalSetting.menus[menuType],
  getQualifier: (state) => (dsType) => ({
    left: state.globalDsSetting[dsType].constant.leftQualifier || '"',
    right: state.globalDsSetting[dsType].constant.rightQualifier || '"'
  }),
  genQualifierText: (state) => (dsType, text) =>
    `${state.globalDsSetting[dsType].constant.leftQualifier}${text}${state.globalDsSetting[dsType].constant.rightQualifier}`,
  removeQualifierText: (state) => (dsType, text) => {
    const { leftQualifier, rightQualifier } = state.globalDsSetting[dsType].constant;
    text = text.replace(leftQualifier, '');
    text = text.replace(rightQualifier, '');
    return text;
  },
  targetDsList: (state) => (dsType) => state.globalDsSetting[dsType].targetDsList,
  ddlList: (state) => (dsType) => state.globalDsSetting[dsType].ddlList,
  supportRdbTx: (state) => (dsType) => state.globalDsSetting[dsType]?.features?.FUN_SUPPORT_RDB_TX,
  isolations: (state) => (dsType) => {
    if (state.globalDsSetting[dsType]) {
      return state.globalDsSetting[dsType].isolations;
    }
  },
  getEditor: (state) => (id) => state.editorSet[id],
  getTargetType: (state) => (targetType) => state.ruleSetting.queryConf.targets.find((target) => target.name === targetType),
  getSenMode: (state) => (senMode) => state.ruleSetting.senConf.senMode.find((sen) => sen.name === senMode),
  getMatchMode: (state) => (ruleKind, matchMode) =>
    state.ruleSetting[RULE_KIND_CONF_MAP[ruleKind]].matchMode.find((match) => match.name === matchMode),
  getScopeByMatchMode: (state) => (ruleKind, matchMode, rangeType) => {
    let text = '';
    state.ruleSetting[RULE_KIND_CONF_MAP[ruleKind]].matchMode.forEach((mode) => {
      if (mode.name === matchMode) {
        mode.children.forEach((child) => {
          if (child.name === rangeType) {
            text = child.i18n;
          }
        });
      }
    });

    return text;
  },
  getMatchModeList: (state) => (ruleKind) => state.ruleSetting[RULE_KIND_CONF_MAP[ruleKind]].matchMode,
  getScopeListByInstance: (state) => (ruleKind, matchMode, dsType) => {
    console.log(state.ruleSetting[RULE_KIND_CONF_MAP[ruleKind]].scopeByMatchMode[matchMode][dsType]);
    const scope = state.ruleSetting[RULE_KIND_CONF_MAP[ruleKind]].scopeByMatchMode[matchMode][dsType];
    return scope || [];
  },
  getScopeListByMatchMode: (state) => (ruleKind, matchMode) => {
    if (!ruleKind || !matchMode) {
      return [];
    }
    const scope = state.ruleSetting[RULE_KIND_CONF_MAP[ruleKind]].matchMode.find((item) => item.name === matchMode);
    return scope ? scope.children : [];
  },
  isInternalUser: (state) =>
    state.userInfo.accountType === 'PRIMARY_ACCOUNT' || (state.userInfo.accountType !== 'PRIMARY_ACCOUNT' && state.userInfo.bindType === 'INTERNAL'),
  isDark: (state) => state.theme === 'dark'
};

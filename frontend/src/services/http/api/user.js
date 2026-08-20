export const userApi = {
  login: '/login',
  logout: '/logout',
  loginMfaValid: '/loginMfaValid',
  requestJumpUrl: '/requestJumpUrl',
  getGlobalSettings: '/globalSettings',
  checkSupplement: '/checkSupplement',

  dmGlobalSettings: '/api/entry/dmGlobalSettings',
  dmConsoleSettings: '/api/entry/dmConsoleSettings',

  // rdp
  rdpUserResetPwdWithOriginPwd: '/api/entry/user/resetPwdWithOriginPwd',
  rdpUserWatermark: '/api/entry/user/waterMark',
  rdpUserQueryLoginUser: '/api/entry/user/queryLoginUser',
  rdpUserResourceSummary: '/api/entry/user/resourceSummary',
  rdpUserResetOpPasswd: '/api/entry/user/resetOpPasswd',
  rdpUserDbOpAudits: '/api/entry/user/dbopaudits', // Data source operating records
  rdpUserOpPasswdVerify: '/api/entry/user/opPasswdVerify',
  rdpUserListMyAuth: '/api/entry/user/listMyAuth',
  rdpUserListRules: '/api/entry/user/listRules', // Rule List
  rdpUserListMyAuthCategoryForMenu: '/api/entry/user/listMyAuthCategoryForMenu',
  rdpUserUpdateUserEmail: '/api/entry/user/updateUserEmail',
  rdpUserUpdateUserName: '/api/entry/user/updateUserName',
  rdpUserCheckProfileDuplicate: '/api/entry/user/checkProfileDuplicate',
  rdpUserUpdateUserPhone: '/api/entry/user/updateUserPhone',
  rdpUserQueryUserAkSk: '/api/entry/user/queryUserAkSk',
  rdpUserResetUserAkSk: '/api/entry/user/resetUserAkSk',
  rdpUserGetPrimaryAccountPwdPolicy: '/api/entry/user/getPrimaryAccountPwdPolicy',
  rdpUserGetSubAccountPwdPolicy: '/api/entry/user/getSubAccountPwdPolicy',

  rdpUserManagerListSubAccounts: '/api/entry/user/manager/listSubAccounts',
  rdpUserManagerUpdateSubAccount: '/api/entry/user/manager/updateSubAccount',
  rdpUserManagerCtrlAddSubAccount: '/api/entry/user/manager/ctrlAddSubAccount',
  rdpUserManagerAddSubAccount: '/api/entry/user/manager/addSubAccount',
  rdpUserManagerDeleteSubAccount: '/api/entry/user/manager/deleteSubAccount',
  rdpUserManagerCheckSubAccountDuplicate: '/api/entry/user/manager/checkSubAccountDuplicate',

  rdpUserConfigGetUserSpecifiedConfs: '/api/entry/user/config/getUserSpecifiedConfs',
  rdpUserConfigGetCurrUserConfigs: '/api/entry/user/config/getCurrUserConfigs',
  rdpUserConfigUpsertUserConfigs: '/api/entry/user/config/upsertUserConfigs'
};

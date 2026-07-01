export const userApi = {
  // ssoAuth: '/auth',*
  // register: '/register',
  // login: '/login',
  // logout: '/logout',
  // requestJumpUrl: '/requestJumpUrl',
  // getGlobalSettings: '/globalSettings',
  // listOrg: '/list_org',
  // checkSupplement: '/checkSupplement',
  // resetPwdUnLogin: '/resetpwdunlogin',
  // dmGlobalSettings: '/api/entry/dmGlobalSettings',
  // dmConsoleSettings: '/api/entry/dmConsoleSettings',

  // rdp
  // resetPwdWithOriginPwd: '/api/entry/user/resetPwdWithOriginPwd',
  // updateUserEmailWithPwd: '/api/entry/user/updateUserEmailWithPwd',
  // updateUserPhoneWithPwd: '/api/entry/user/updateUserPhoneWithPwd',
  // resetSubAccountPwd: '/api/entry/user/resetSubAccountPwd',
  // watermark: '/api/entry/user/waterMark',*
  // getUserInfo: '/api/entry/user/queryLoginUser',*
  // resetPassword: '/api/entry/user/manager/resetPasswd',
  // getSubAccountList: '/api/entry/user/manager/listSubAccounts',
  // updateSubAccount: '/api/entry/user/manager/updateSubAccount',
  // addSubAccount: '/api/entry/user/manager/addSubAccount',
  // deleteSubAccount: '/api/entry/user/manager/deleteSubAccount',
  // updateUserRole: '/api/entry/user/manager/updateUserRole',
  // stopSubAccount: '/api/entry/user/manager/updateAccountAbility',
  // checkSubAccountDuplicate: '/api/entry/user/manager/checkSubAccountDuplicate',
  // getResourceSummary: '/api/entry/user/resourceSummary',
  // resetOpPwd: '/api/entry/user/resetOpPasswd',
  // getDbopAudits: '/clauddm/console/api/v1/user/dpopaudits', /// data source operating records
  // verifyOpPwd: '/api/entry/user/opPasswdVerify',
  // listMyAuth: '/api/entry/user/listMyAuth',
  // listRules: '/api/entry/user/listRules',
  // getCurrUserConfigs: '/api/entry/user/config/getCurrUserConfigs',
  // updateUserConfigs: '/api/entry/user/config/upsertUserConfigs',
  // queryPrimaryUser: '/api/entry/user/queryPrimaryUser',
  // queryRemainingTrialDay: '/cloudcanal/console/api/v1/inner/saas/queryremainingtrialday',
  // saasCcProductNames: '/api/entry/saas/ccproductnames',

  auth: '/auth',
  signin: '/signin',
  login: '/login',
  logout: '/logout',
  loginMfaValid: '/loginMfaValid',
  requestJumpUrl: '/requestJumpUrl',
  getGlobalSettings: '/globalSettings',
  listOrg: '/list_org',
  checkSupplement: '/checkSupplement',

  dmGlobalSettings: '/api/entry/dmGlobalSettings',
  dmConsoleSettings: '/api/entry/dmConsoleSettings',

  // rdp
  rdpUserResetPwdWithOriginPwd: '/api/entry/user/resetPwdWithOriginPwd',
  rdpUserUpdateUserEmailWithPwd: '/api/entry/user/updateUserEmailWithPwd',
  rdpUserUpdateUserPhoneWithPwd: '/api/entry/user/updateUserPhoneWithPwd',
  rdpUserResetSubAccountPwd: '/api/entry/user/resetSubAccountPwd',
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
  // API
  rdpUserManagerCtrlAddSubAccount: '/api/entry/user/manager/ctrlAddSubAccount',
  rdpUserManagerAddSubAccount: '/api/entry/user/manager/addSubAccount',
  rdpUserManagerDeleteSubAccount: '/api/entry/user/manager/deleteSubAccount',
  rdpUserManagerUpdateUserRole: '/api/entry/user/manager/updateUserRole',
  rdpUserManagerStopSubAccount: '/api/entry/user/manager/updateAccountAbility',
  rdpUserManagerCheckSubAccountDuplicate: '/api/entry/user/manager/checkSubAccountDuplicate',

  rdpUserConfigGetUserSpecifiedConfs: '/api/entry/user/config/getUserSpecifiedConfs',
  rdpUserConfigGetCurrUserConfigs: '/api/entry/user/config/getCurrUserConfigs',
  rdpUserConfigUpsertUserConfigs: '/api/entry/user/config/upsertUserConfigs',

  // cc
  ccUserQueryLoginUser: '/cloudcanal/console/api/v1/inner/user/queryLoginUser',
  ccUserAddUserForPremise: '/cloudcanal/console/api/v1/inner/user/adduserforpremise'
};

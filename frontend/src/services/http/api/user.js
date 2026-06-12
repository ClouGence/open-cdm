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
  // dmGlobalSettings: '/clouddm/console/api/v1/dmGlobalSettings',
  // dmConsoleSettings: '/clouddm/console/api/v1/dmConsoleSettings',

  // rdp
  // resetPwdWithOriginPwd: '/rdp/console/api/v1/user/resetPwdWithOriginPwd',
  // updateUserEmailWithPwd: '/rdp/console/api/v1/user/updateUserEmailWithPwd',
  // updateUserPhoneWithPwd: '/rdp/console/api/v1/user/updateUserPhoneWithPwd',
  // resetSubAccountPwd: '/rdp/console/api/v1/user/resetSubAccountPwd',
  // watermark: '/rdp/console/api/v1/user/waterMark',*
  // getUserInfo: '/rdp/console/api/v1/user/queryLoginUser',*
  // resetPassword: '/rdp/console/api/v1/user/manager/resetPasswd',
  // getSubAccountList: '/rdp/console/api/v1/user/manager/listSubAccounts',
  // updateSubAccount: '/rdp/console/api/v1/user/manager/updateSubAccount',
  // addSubAccount: '/rdp/console/api/v1/user/manager/addSubAccount',
  // deleteSubAccount: '/rdp/console/api/v1/user/manager/deleteSubAccount',
  // updateUserRole: '/rdp/console/api/v1/user/manager/updateUserRole',
  // stopSubAccount: '/rdp/console/api/v1/user/manager/updateAccountAbility',
  // checkSubAccountDuplicate: '/rdp/console/api/v1/user/manager/checkSubAccountDuplicate',
  // getResourceSummary: '/rdp/console/api/v1/user/resourceSummary',
  // resetOpPwd: '/rdp/console/api/v1/user/resetOpPasswd',
  // getDbopAudits: '/clauddm/console/api/v1/user/dpopaudits', /// data source operating records
  // verifyOpPwd: '/rdp/console/api/v1/user/opPasswdVerify',
  // listMyAuth: '/rdp/console/api/v1/user/listMyAuth',
  // listRules: '/rdp/console/api/v1/user/listRules',
  // getCurrUserConfigs: '/rdp/console/api/v1/user/config/getCurrUserConfigs',
  // updateUserConfigs: '/rdp/console/api/v1/user/config/upsertUserConfigs',
  // queryPrimaryUser: '/rdp/console/api/v1/user/queryPrimaryUser',
  // queryRemainingTrialDay: '/cloudcanal/console/api/v1/inner/saas/queryremainingtrialday',
  // saasCcProductNames: '/rdp/console/api/v1/saas/ccproductnames',

  auth: '/auth',
  signin: '/signin',
  login: '/login',
  logout: '/logout',
  loginMfaValid: '/loginMfaValid',
  requestJumpUrl: '/requestJumpUrl',
  getGlobalSettings: '/globalSettings',
  listOrg: '/list_org',
  checkSupplement: '/checkSupplement',

  dmGlobalSettings: '/clouddm/console/api/v1/dmGlobalSettings',
  dmConsoleSettings: '/clouddm/console/api/v1/dmConsoleSettings',

  // rdp
  rdpUserResetPwdWithOriginPwd: '/rdp/console/api/v1/user/resetPwdWithOriginPwd',
  rdpUserUpdateUserEmailWithPwd: '/rdp/console/api/v1/user/updateUserEmailWithPwd',
  rdpUserUpdateUserPhoneWithPwd: '/rdp/console/api/v1/user/updateUserPhoneWithPwd',
  rdpUserResetSubAccountPwd: '/rdp/console/api/v1/user/resetSubAccountPwd',
  rdpUserWatermark: '/rdp/console/api/v1/user/waterMark',
  rdpUserQueryLoginUser: '/rdp/console/api/v1/user/queryLoginUser',
  rdpUserResourceSummary: '/rdp/console/api/v1/user/resourceSummary',
  rdpUserResetOpPasswd: '/rdp/console/api/v1/user/resetOpPasswd',
  rdpUserDbOpAudits: '/clouddm/console/api/v1/user/dbopaudits', // Data source operating records
  rdpUserOpPasswdVerify: '/rdp/console/api/v1/user/opPasswdVerify',
  rdpUserListMyAuth: '/rdp/console/api/v1/user/listMyAuth',
  rdpUserListRules: '/rdp/console/api/v1/user/listRules', // Rule List
  rdpUserListMyAuthCategoryForMenu: '/rdp/console/api/v1/user/listMyAuthCategoryForMenu',
  rdpUserUpdateUserEmail: '/rdp/console/api/v1/user/updateUserEmail',
  rdpUserUpdateUserName: '/rdp/console/api/v1/user/updateUserName',
  rdpUserCheckProfileDuplicate: '/rdp/console/api/v1/user/checkProfileDuplicate',
  rdpUserUpdateUserPhone: '/rdp/console/api/v1/user/updateUserPhone',
  rdpUserQueryUserAkSk: '/rdp/console/api/v1/user/queryUserAkSk',
  rdpUserResetUserAkSk: '/rdp/console/api/v1/user/resetUserAkSk',
  rdpUserGetPrimaryAccountPwdPolicy: '/rdp/console/api/v1/user/getPrimaryAccountPwdPolicy',
  rdpUserGetSubAccountPwdPolicy: '/rdp/console/api/v1/user/getSubAccountPwdPolicy',

  rdpUserManagerListSubAccounts: '/rdp/console/api/v1/user/manager/listSubAccounts',
  rdpUserManagerUpdateSubAccount: '/rdp/console/api/v1/user/manager/updateSubAccount',
  // API
  rdpUserManagerCtrlAddSubAccount: '/rdp/console/api/v1/user/manager/ctrlAddSubAccount',
  rdpUserManagerAddSubAccount: '/rdp/console/api/v1/user/manager/addSubAccount',
  rdpUserManagerDeleteSubAccount: '/rdp/console/api/v1/user/manager/deleteSubAccount',
  rdpUserManagerUpdateUserRole: '/rdp/console/api/v1/user/manager/updateUserRole',
  rdpUserManagerStopSubAccount: '/rdp/console/api/v1/user/manager/updateAccountAbility',
  rdpUserManagerCheckSubAccountDuplicate: '/rdp/console/api/v1/user/manager/checkSubAccountDuplicate',

  rdpUserConfigGetUserSpecifiedConfs: '/rdp/console/api/v1/user/config/getUserSpecifiedConfs',
  rdpUserConfigGetCurrUserConfigs: '/rdp/console/api/v1/user/config/getCurrUserConfigs',
  rdpUserConfigUpsertUserConfigs: '/rdp/console/api/v1/user/config/upsertUserConfigs',

  // cc
  ccUserQueryLoginUser: '/cloudcanal/console/api/v1/inner/user/queryLoginUser',
  ccUserAddUserForPremise: '/cloudcanal/console/api/v1/inner/user/adduserforpremise'
};

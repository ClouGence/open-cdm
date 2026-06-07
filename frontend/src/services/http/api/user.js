export const userApi = {
  // ssoAuth: '/auth',*
  // register: '/register',
  // login: '/login',
  // logout: '/logout',
  // requestJumpUrl: '/requestJumpUrl',
  // getGlobalSettings: '/global_settings',
  // listOrg: '/list_org',
  // checkSupplement: '/check_supplement',
  // resetPwdUnLogin: '/resetpwdunlogin',
  // dmGlobalSettings: '/clouddm/console/api/v1/dm_global_settings',
  // dmConsoleSettings: '/clouddm/console/api/v1/dm_console_settings',

  // rdp
  // resetPwdWithOriginPwd: '/rdp/console/api/v1/user/resetPwdWithOriginPwd',
  // updateUserEmailWithPwd: '/rdp/console/api/v1/user/updateUserEmailWithPwd',
  // updateUserPhoneWithPwd: '/rdp/console/api/v1/user/updateUserPhoneWithPwd',
  // resetSubAccountPwd: '/rdp/console/api/v1/user/resetSubAccountPwd',
  // watermark: '/rdp/console/api/v1/user/waterMark',*
  // getUserInfo: '/rdp/console/api/v1/user/queryLoginUser',*
  // resetPassword: '/rdp/console/api/v1/user/manager/resetpasswd',
  // getSubAccountList: '/rdp/console/api/v1/user/manager/listsubaccounts',
  // updateSubAccount: '/rdp/console/api/v1/user/manager/updatesubaccount',
  // addSubAccount: '/rdp/console/api/v1/user/manager/addsubaccount',
  // deleteSubAccount: '/rdp/console/api/v1/user/manager/deletesubaccount',
  // updateUserRole: '/rdp/console/api/v1/user/manager/updateuserrole',
  // stopSubAccount: '/rdp/console/api/v1/user/manager/updateaccountability',
  // checkSubAccountDuplicate: '/rdp/console/api/v1/user/manager/checksubaccountduplicate',
  // getResourceSummary: '/rdp/console/api/v1/user/resourceSummary',
  // resetOpPwd: '/rdp/console/api/v1/user/resetOpPasswd',
  // getDbOpAudits: '/clouddm/console/api/v1/user/dbopaudits', // 数据源操作记录
  // verifyOpPwd: '/rdp/console/api/v1/user/opPasswdVerify',
  // listMyAuth: '/rdp/console/api/v1/user/listMyAuth',
  // listRules: '/rdp/console/api/v1/user/listRules',
  // getCurrUserConfigs: '/rdp/console/api/v1/user/config/getcurruserconfigs',
  // updateUserConfigs: '/rdp/console/api/v1/user/config/upsertuserconfigs',
  // queryPrimaryUser: '/rdp/console/api/v1/user/queryPrimaryUser',
  // queryRemainingTrialDay: '/cloudcanal/console/api/v1/inner/saas/queryremainingtrialday',
  // saasCcProductNames: '/rdp/console/api/v1/saas/ccproductnames',

  auth: '/auth',
  signin: '/signin',
  login: '/login',
  logout: '/logout',
  loginMfaValid: '/loginMfaValid',
  requestJumpUrl: '/requestJumpUrl',
  getGlobalSettings: '/global_settings',
  listOrg: '/list_org',
  checkSupplement: '/check_supplement',

  dmGlobalSettings: '/clouddm/console/api/v1/dm_global_settings',
  dmConsoleSettings: '/clouddm/console/api/v1/dm_console_settings',

  // rdp
  rdpUserResetPwdWithOriginPwd: '/rdp/console/api/v1/user/resetPwdWithOriginPwd',
  rdpUserUpdateUserEmailWithPwd: '/rdp/console/api/v1/user/updateUserEmailWithPwd',
  rdpUserUpdateUserPhoneWithPwd: '/rdp/console/api/v1/user/updateUserPhoneWithPwd',
  rdpUserResetSubAccountPwd: '/rdp/console/api/v1/user/resetSubAccountPwd',
  rdpUserWatermark: '/rdp/console/api/v1/user/waterMark',
  rdpUserQueryLoginUser: '/rdp/console/api/v1/user/queryLoginUser',
  rdpUserResourceSummary: '/rdp/console/api/v1/user/resourceSummary',
  rdpUserResetOpPasswd: '/rdp/console/api/v1/user/resetOpPasswd',
  rdpUserDbOpAudits: '/clouddm/console/api/v1/user/dbopaudits', // 数据源操作记录
  rdpUserOpPasswdVerify: '/rdp/console/api/v1/user/opPasswdVerify',
  rdpUserListMyAuth: '/rdp/console/api/v1/user/listMyAuth',
  rdpUserListRules: '/rdp/console/api/v1/user/listRules', // 规则列表
  rdpUserListMyAuthCategoryForMenu: '/rdp/console/api/v1/user/listMyAuthCategoryForMenu',
  rdpUserUpdateUserEmail: '/rdp/console/api/v1/user/updateUserEmail',
  rdpUserUpdateUserName: '/rdp/console/api/v1/user/updateUserName',
  rdpUserCheckProfileDuplicate: '/rdp/console/api/v1/user/checkProfileDuplicate',
  rdpUserUpdateUserPhone: '/rdp/console/api/v1/user/updateUserPhone',
  rdpUserQueryUserAkSk: '/rdp/console/api/v1/user/queryUserAkSk',
  rdpUserResetUserAkSk: '/rdp/console/api/v1/user/resetUserAkSk',
  rdpUserGetPrimaryAccountPwdPolicy: '/rdp/console/api/v1/user/getPrimaryAccountPwdPolicy',
  rdpUserGetSubAccountPwdPolicy: '/rdp/console/api/v1/user/getSubAccountPwdPolicy',

  rdpUserManagerListSubAccounts: '/rdp/console/api/v1/user/manager/listsubaccounts',
  rdpUserManagerUpdateSubAccount: '/rdp/console/api/v1/user/manager/updatesubaccount',
  // API
  rdpUserManagerCtrlAddSubAccount: '/rdp/console/api/v1/user/manager/ctrl_addsubaccount',
  rdpUserManagerAddSubAccount: '/rdp/console/api/v1/user/manager/addsubaccount',
  rdpUserManagerDeleteSubAccount: '/rdp/console/api/v1/user/manager/deletesubaccount',
  rdpUserManagerUpdateUserRole: '/rdp/console/api/v1/user/manager/updateuserrole',
  rdpUserManagerStopSubAccount: '/rdp/console/api/v1/user/manager/updateaccountability',
  rdpUserManagerCheckSubAccountDuplicate: '/rdp/console/api/v1/user/manager/checksubaccountduplicate',

  rdpUserConfigGetUserSpecifiedConfs: '/rdp/console/api/v1/user/config/getUserSpecifiedConfs',
  rdpUserConfigGetCurrUserConfigs: '/rdp/console/api/v1/user/config/getcurruserconfigs',
  rdpUserConfigUpsertUserConfigs: '/rdp/console/api/v1/user/config/upsertuserconfigs',

  // cc
  ccUserQueryLoginUser: '/cloudcanal/console/api/v1/inner/user/queryLoginUser',
  ccUserAddUserForPremise: '/cloudcanal/console/api/v1/inner/user/adduserforpremise'
};

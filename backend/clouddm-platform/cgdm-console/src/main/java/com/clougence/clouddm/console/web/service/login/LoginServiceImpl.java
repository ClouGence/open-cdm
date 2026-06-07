/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.service.login;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.constants.MfaPreActionType;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.csrf.CsrfTokenService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.JwtService;
import com.clougence.clouddm.console.web.model.fo.LoginAutoRegisterFO;
import com.clougence.clouddm.console.web.model.fo.LoginFO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.clouddm.console.web.util.Sm2Utils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.AccountBindType;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthCsrfTokenDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.model.AddSubAccountMO;
import com.clougence.rdp.service.model.LoginMO;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pudding
 * @version 2020-01-17 15:29
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthDal          authDal;
    @Resource
    private SystemDal        systemDal;
    @Resource
    private NamingDao        namingDao;
    @Resource
    private DmConsoleConfig  config;
    @Resource
    private JwtService       jwtService;
    @Resource
    private CsrfTokenService csrfTokenService;
    @Resource
    private RdpUserService   rdpUserService;

    @Override
    public LoginMO login(LoginFO loginFO) {
        if (StringUtils.isBlank(loginFO.getAccount())) {
            return new LoginMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_MISSING.name()));
        }

        if (loginFO.getLoginType() == LoginAuthType.PASSWORD) {
            return this.loginByLocal(loginFO);
        } else {
            try {
                return this.loginByProvider(loginFO);
            } catch (ErrorMessageException e) {
                return loginFailedNotLimit(null, e);
            }
        }
    }

    //
    // login by local account
    //

    private LoginMO loginByLocal(LoginFO loginFO) {
        DmAuthUserDO user = this.authDal.queryLocalUserByLoginText(loginFO.getAccount());
        try {
            checkAccountStatus(user);
        } catch (ErrorMessageException e) {
            return loginFailedNotLimit(user, e);
        }

        try {
            this.checkByPassword(loginFO, user);
            return loginDone(user, LoginAuthType.PASSWORD);
        } catch (ErrorMessageException e) {
            return loginFailed(user, e);
        }
    }

    private void checkAccountStatus(DmAuthUserDO user) {
        if (user == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_NOT_EXIST.name()));
        }

        if (user.isDisable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_UNAVAILABLE.name()));
        }

        if (user.isLoginLocked()) {
            if (isAccountLockTimeExpire(user)) {
                Date t = new Date();
                // release the lock and reset the login fail count to 0
                this.authDal.userMapper().updateLoginLimitInfo(t, 0, false, user.getId());
                user.setLastTryLoginTime(t);
                user.setLoginLocked(false);
                user.setLoginFailCount(0);
            } else {
                long needWaitSeconds = Integer.parseInt(config.getResetLoginLimitationWaitTimeMin()) * 60L -
                                       (System.currentTimeMillis() - user.getLastTryLoginTime().getTime()) / 1000;
                String i18nKey = I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_LOCK_BY_PWD_ERROR.name();
                String expireMessage = DmI18nUtils.getMessage(i18nKey, config.getRetryLoginMaxCount(), String.valueOf(needWaitSeconds));
                throw new ErrorMessageException(expireMessage);
            }
        }
    }

    private void checkByPassword(LoginFO loginFO, DmAuthUserDO user) {
        if (StringUtils.isBlank(loginFO.getPassword())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWD_CAN_NOT_BE_BLANK.name()));
        }

        if (user.getParentId() != null) {
            Integer days = this.systemDal.fetchSystemConf(UserDefinedConfig.Fields.accountPwdExpireDays, Integer.class);
            if (days != null) {
                if (days > 0) {
                    Date d = user.getLastDateUpdatePwd();
                    if (d != null) {
                        LocalDateTime lastUpdateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(d.getTime()), ZoneId.systemDefault());
                        LocalDateTime limit = lastUpdateTime.plusDays(days);
                        if (limit.isBefore(LocalDateTime.now())) {
                            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWORD_EXPIRED.name(), days));
                        }
                    } else {
                        this.authDal.userMapper().updateLastUpdatePwdTimeById(user.getId());
                    }
                }
            }
        }

        String plainPwd = Sm2Utils.decrypt(this.config.getPrivateKey(), loginFO.getPassword());
        if (RdpAuthUtils.isErrorPasswd(user.getPassword(), plainPwd)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWORD_ERROR.name()));
        }
    }

    //
    // login by provider account
    //

    private LoginMO loginByProvider(LoginFO loginFO) {
        LoginAuthType loginType = loginFO.getLoginType();
        if (loginType.getBindType().getProvider() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_SUBACCOUNT_LOGIN_TYPE.name()));
        }

        LoginProvider loginProvider = loginType.getBindType().getProvider();
        LoginProviderSpi loginProviderSpi = PluginManager.findSpi(LoginProviderSpi.class, loginProvider.name());
        if (loginProviderSpi == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_PLUGIN_NOT_FOUND.name()));
        }

        String userAccount = StringUtils.defaultString(loginFO.getAccount());
        DmAuthUserDO rootUser = this.authDal.queryRootUser();
        if (rootUser == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_NOT_EXIST.name()));
        }
        if (rootUser.getAccountType() != AccountType.PRIMARY_ACCOUNT) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_OWNER_IS_NOT_PRIMARY_ERROR.name()));
        }

        if (rootUser.isDisable()) {
            return loginFailedNotLimit(rootUser, new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_DISABLED.name())));
        }

        LoginRequest request = new LoginRequest();
        request.setLoginAccount(userAccount);
        request.setLoginPassword(Sm2Utils.decrypt(this.config.getPrivateKey(), loginFO.getPassword()));
        request.setLoginVerifyCode(loginFO.getVerifyCode());
        request.setAccessToken(loginFO.getAccessToken());
        if (StringUtils.isNotBlank(loginFO.getToken())) {
            DmAuthCsrfTokenDO csrfTokenDO = this.csrfTokenService.pullToken(loginFO.getToken());
            if (csrfTokenDO != null) {
                request.setAccessToken(csrfTokenDO.getSecretToken());
            }
        }
        LoginResponse authUserDTO = loginProviderSpi.authLogin(rootUser.getUid(), request);
        UserData loginData = authUserDTO.getLoginUser();

        if (!authUserDTO.isSuccess()) {
            DmAuthUserDO loginUser = RdpConvertUtils.convertToRdpUserDO(loginType, rootUser, loginData);
            return loginFailedNotLimit(loginUser, new ErrorMessageException(authUserDTO.getErrMsg()));
        }
        if (loginData == null) {
            return loginFailedNotLimit(rootUser, new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_NOT_EXIST.name())));
        }

        DmAuthUserDO loginUser = RdpConvertUtils.convertToRdpUserDO(loginType, rootUser, loginData);
        DmAuthUserDO bindUser = this.queryProviderBindUser(loginType.getBindType(), loginUser);
        if (bindUser == null) {
            if (!hasRegisterInfo(loginFO)) {
                String csrfToken;
                if (loginData.getAccessToken() != null) {
                    csrfToken = this.csrfTokenService.pushToken(loginData.getAccessToken());
                } else {
                    csrfToken = null;
                }

                LoginAutoRegisterFO moreInfo = new LoginAutoRegisterFO();
                moreInfo.setAccount(this.namingDao.genLoginAccount());
                moreInfo.setName(loginUser.getUsername());
                moreInfo.setEmail(loginUser.getEmail());
                moreInfo.setPhone(loginUser.getPhone());
                moreInfo.setPrimaryUid(rootUser.getUid());

                LoginMO mo = new LoginMO();
                mo.setSuccess(true);
                mo.setNeedMore(true);
                mo.setToken(csrfToken);
                mo.setMoreInfo(moreInfo);
                mo.setErrMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_FIRST_TIME.name()));
                return mo;
            } else {
                LoginAutoRegisterFO moreInfo = loginFO.getRegisterInfo();
                try {
                    this.checkRegisterInfoUnique(rootUser, moreInfo);
                    bindUser = this.addProviderUser(rootUser, loginType.getBindType(), loginUser, moreInfo);
                } catch (Exception e) {
                    this.csrfTokenService.storeSecretToken(loginFO.getToken(), loginData.getAccessToken());// restore
                    throw e;
                }
            }
        } else {
            this.authDal.userMapper().updateAccessTokenByUid(bindUser.getUid(), loginUser.getAccessToken());
        }

        try {
            checkAccountStatus(bindUser);
            return loginDone(bindUser, loginType);
        } catch (ErrorMessageException e) {
            return loginFailedNotLimit(bindUser, e);
        }
    }

    private LoginMO loginDone(DmAuthUserDO user, LoginAuthType loginType) {
        long nowMs = System.currentTimeMillis();

        this.authDal.userMapper().updateLoginLimitInfo(new Date(nowMs), 0, false, user.getId());
        LoginMO re = new LoginMO();
        re.setSuccess(true);
        re.setUid(user.getUid());
        re.setUsername(user.getUsername());
        re.setLoginType(loginType);

        String jwtToken = this.jwtService.genJwtToken(user, loginType);
        re.setToken(jwtToken);
        if (user.getParentId() != null) {
            DmAuthUserDO rdpUserDO = authDal.userMapper().queryById(user.getParentId());
            if (rdpUserDO != null) {
                re.setPuid(rdpUserDO.getUid());
            }
        }

        if (user.isUseMfa()) {
            re.setNeedMfa(true);
            re.setMfaPreActionToken(this.jwtService.genMfaActionToken(user.getUid(), MfaPreActionType.LOGIN, jwtToken));
        }

        return re;
    }

    private LoginMO loginFailed(DmAuthUserDO user, ErrorMessageException e) {
        String errorMsg = e.getErrorMessage();
        if (isExceedLoginFailCount(user)) {
            long nowMs = System.currentTimeMillis();
            this.authDal.userMapper().updateLoginLimitInfo(new Date(nowMs), user.getLoginFailCount() + 1, true, user.getId());

            long needWaitSeconds = Integer.parseInt(this.config.getResetLoginLimitationWaitTimeMin()) * 60L -
                                   (System.currentTimeMillis() - user.getLastTryLoginTime().getTime()) / 1000;
            String failCnt = String.valueOf(Math.min(user.getLoginFailCount() + 1, config.getRetryLoginMaxCount()));
            String i18nKey = I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_LOCK_BY_PWD_ERROR.name();
            errorMsg = DmI18nUtils.getMessage(i18nKey, failCnt, String.valueOf(needWaitSeconds));
        } else {
            long nowMs = System.currentTimeMillis();
            this.authDal.userMapper().updateLoginLimitInfo(new Date(nowMs), user.getLoginFailCount() + 1, false, user.getId());
        }

        LoginMO loginMO = new LoginMO(false, errorMsg);
        if (user.getParentId() == null) {
            loginMO.setPuid(user.getUid());
        } else {
            DmAuthUserDO rdpUserDO = authDal.userMapper().queryById(user.getParentId());
            if (rdpUserDO != null) {
                loginMO.setPuid(rdpUserDO.getUid());
            }
        }
        loginMO.setUid(user.getUid());
        loginMO.setUsername(user.getUsername());
        return loginMO;
    }

    private LoginMO loginFailedNotLimit(DmAuthUserDO user, ErrorMessageException e) {
        LoginMO loginMO = new LoginMO(false, e.getErrorMessage());
        if (user == null) {
            return loginMO;
        }
        if (user.getParentId() == null) {
            loginMO.setPuid(user.getUid());
        } else {
            DmAuthUserDO rdpUserDO = authDal.userMapper().queryById(user.getParentId());
            if (rdpUserDO != null) {
                loginMO.setPuid(rdpUserDO.getUid());
            }
        }
        loginMO.setUid(user.getUid());
        loginMO.setUsername(user.getUsername());
        return loginMO;
    }

    private DmAuthUserDO addProviderUser(DmAuthUserDO rootUser, AccountBindType bindType, DmAuthUserDO bindUser, LoginAutoRegisterFO moreInfo) {
        String account = StringUtils.defaultIfBlank(moreInfo.getAccount(), this.namingDao.genLoginAccount());
        bindUser.setAccount(account);
        bindUser.setUsername(this.resolveProviderUsername(bindUser, moreInfo, account));
        bindUser.setEmail(moreInfo.getEmail());
        bindUser.setPhone(moreInfo.getPhone());
        AddSubAccountMO accountMO = this.rdpUserService.addSubAccountForBind(rootUser.getUid(), bindType, bindUser);

        if (!accountMO.isSuccess()) {
            throw new ErrorMessageException(accountMO.getErrorMsg());
        } else {
            return this.rdpUserService.getUserByUid(accountMO.getSubUid());
        }
    }

    private DmAuthUserDO queryProviderBindUser(AccountBindType bindType, DmAuthUserDO loginUser) {
        if (StringUtils.isBlank(loginUser.getBindAccount())) {
            return null;
        }
        return this.authDal.userMapper().queryByBindInfo(loginUser.getBindAccount(), bindType);
    }

    private void checkRegisterInfoUnique(DmAuthUserDO rootUser, LoginAutoRegisterFO moreInfo) {
        if (StringUtils.isNotBlank(moreInfo.getPhone())) {
            DmAuthUserDO phoneUser = this.authDal.userMapper().queryByPhoneAndParentId(moreInfo.getPhone(), rootUser.getId());
            if (phoneUser != null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_PHONE_EXIST_ERROR.name(), moreInfo.getPhone()));
            }
        }
        if (StringUtils.isNotBlank(moreInfo.getEmail())) {
            DmAuthUserDO emailUser = this.authDal.userMapper().queryByEmailAndParentId(moreInfo.getEmail(), rootUser.getId());
            if (emailUser != null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_EXIST_ERROR.name(), moreInfo.getEmail()));
            }
        }
    }

    private String resolveProviderUsername(DmAuthUserDO bindUser, LoginAutoRegisterFO moreInfo, String account) {
        if (StringUtils.isNotBlank(moreInfo.getName())) {
            return moreInfo.getName();
        }
        if (StringUtils.isNotBlank(bindUser.getUsername())) {
            return bindUser.getUsername();
        }
        if (StringUtils.isNotBlank(account)) {
            return account;
        }
        if (StringUtils.isNotBlank(bindUser.getBindAccount())) {
            return bindUser.getBindAccount();
        }
        return bindUser.getAccount();
    }

    private boolean hasRegisterInfo(LoginFO loginFO) {
        LoginAutoRegisterFO registerInfo = loginFO.getRegisterInfo();
        if (registerInfo == null) {
            return false;
        }

        return StringUtils.isNotBlank(registerInfo.getAccount()) || //
               StringUtils.isNotBlank(registerInfo.getName()) ||    //
               StringUtils.isNotBlank(registerInfo.getEmail()) ||   //
               StringUtils.isNotBlank(registerInfo.getPhone());
    }

    //

    protected boolean isExceedLoginFailCount(DmAuthUserDO userDO) {
        return userDO.getLoginFailCount() + 1 >= this.config.getRetryLoginMaxCount();
    }

    private boolean isAccountLockTimeExpire(DmAuthUserDO userDO) {
        return System.currentTimeMillis() - userDO.getLastTryLoginTime().getTime() > Long.parseLong(this.config.getResetLoginLimitationWaitTimeMin()) * 60 * 1000;
    }

    //
    //
    //

    @Override
    public boolean isLogoutUsingJump(String uid) {
        DmAuthUserDO user = this.authDal.userMapper().queryByUid(uid);
        if (user.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return false;
        }

        return user.getBindType() != AccountBindType.INTERNAL;
    }

    @Override
    public String logoutJumpUrl(String puid, String uid) {
        String homePath;
        if (StringUtils.isNotBlank(this.config.getDeployContextPath())) {
            homePath = this.config.getDeployContextPath();
        } else {
            homePath = "/";
        }

        DmAuthUserDO user = this.authDal.userMapper().queryByUid(uid);
        if (user.getAccountType() == AccountType.PRIMARY_ACCOUNT || user.getBindType() == AccountBindType.INTERNAL) {
            return homePath;
        }

        String csrfToken = this.csrfTokenService.randomToken();
        String redirectURL = RdpWebUtils.getContextPath() + ("callback/logout?" + //
                                                             "uid=" + uid + "&" + //
                                                             //"state=" + csrfToken + "&" +//
                                                             "provider=" + user.getBindType().getProvider().name());

        LoginProvider loginProvider = user.getBindType().getProvider();
        LoginProviderSpi loginProviderSpi = PluginManager.findSpi(LoginProviderSpi.class, loginProvider.name());
        if (loginProviderSpi == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_PLUGIN_NOT_FOUND.name()));
        }

        try {
            String jumpUrl = loginProviderSpi.logoutJumpUrl(puid, csrfToken, redirectURL, user.getAccessToken());
            AccountBindType bindType = user.getBindType();
            return jumpUrl;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }
}

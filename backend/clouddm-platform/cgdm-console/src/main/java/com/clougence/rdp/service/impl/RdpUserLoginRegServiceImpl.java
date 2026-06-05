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
package com.clougence.clouddm.console.web.component.config.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.config.UserConfigService;
import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.constants.MfaPreActionType;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.csrf.CsrfTokenService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.JwtService;
import com.clougence.clouddm.console.web.model.fo.LoginAutoRegisterFO;
import com.clougence.clouddm.console.web.model.fo.LoginFO;
import com.clougence.clouddm.console.web.model.vo.LoginUserVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserLoginRegService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.clouddm.console.web.util.Sm2Utils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.*;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpVerifyService;
import com.clougence.rdp.service.model.AddSubAccountMO;
import com.clougence.rdp.service.model.CheckVerifyMO;
import com.clougence.rdp.service.model.LoginMO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pudding
 * @version 2020-01-17 15:29
 */
@Service
@Slf4j
public class RdpUserLoginRegServiceImpl implements RdpUserLoginRegService {

    @Resource
    private AuthDal           authDal;
    @Resource
    private DmConsoleConfig   rdpConfig;
    @Resource
    private RdpUserService    rdpUserService;
    @Resource
    private JwtService        jwtService;
    @Resource
    private RdpVerifyService  rdpVerifyService;
    @Resource
    private UserConfigService userConfigService;
    @Resource
    private CsrfTokenService  csrfTokenService;

    @Override
    public LoginMO login(LoginFO loginFO) {
        if (StringUtils.isBlank(loginFO.getAccount())) {
            return new LoginMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_MISSING.name()));
        }

        if (loginFO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return this.loginByPrimaryAccount(loginFO);
        } else if (loginFO.getAccountType() == AccountType.SUB_ACCOUNT) {
            if (!loginFO.getAccount().contains("@")) {
                return new LoginMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SUB_ACCOUNT_FMT_ERROR.name()));
            }
            if (loginFO.getAccount().trim().charAt(0) == '@') {
                return new LoginMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_MISSING.name()));
            }

            return this.loginBySubAccount(loginFO);
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_ACCOUNT_TYPE.name()));
    }

    private LoginMO loginByPrimaryAccount(LoginFO loginFO) {
        //find user
        DmAuthUserDO user;
        switch (loginFO.getLoginType()) {
            case VERIFY: {
                user = this.authDal.userMapper().queryPrimaryByPhone(loginFO.getAccount());
                break;
            }
            case PASSWORD: {
                user = this.authDal.userMapper().queryPrimaryByEmail(loginFO.getAccount());
                if (user == null) {
                    user = this.authDal.userMapper().queryPrimaryByPhone(loginFO.getAccount());
                }
                break;
            }
            default: {
                return new LoginMO(false, DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_PRIMARY_LOGIN_TYPE.name()));
            }
        }

        // status check
        try {
            checkAccountStatus(loginFO, user);
        } catch (ErrorMessageException e) {
            return loginFailedNotLimit(user, e);
        }

        // auth check
        try {
            if (loginFO.getLoginType() == LoginAuthType.VERIFY) {
                checkByVerify(loginFO, user);
            } else if (loginFO.getLoginType() == LoginAuthType.PASSWORD) {
                checkByPasswordForPrimary(loginFO, user);
            } else {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_PRIMARY_LOGIN_TYPE.name()));
            }

            return loginDone(user);
        } catch (ErrorMessageException e) {
            return loginFailed(loginFO, user, e);
        }
    }

    private LoginMO loginBySubAccount(LoginFO loginFO) {
        if (loginFO.getLoginType() == LoginAuthType.PASSWORD) {
            DmAuthUserDO user = this.authDal.userMapper().queryBySubAccount(loginFO.getAccount());
            try {
                checkAccountStatus(loginFO, user);
            } catch (ErrorMessageException e) {
                return loginFailedNotLimit(user, e);
            }

            try {
                this.checkByPasswordSubAccount(loginFO, user);
                return loginDone(user);
            } catch (ErrorMessageException e) {
                return loginFailed(loginFO, user, e);
            }
        } else {
            try {
                return this.loginByProvider(loginFO);
            } catch (ErrorMessageException e) {
                return loginFailedNotLimit(null, e);
            }
        }
    }

    private void checkAccountStatus(LoginFO loginFO, DmAuthUserDO user) {
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
                long needWaitSeconds = Integer.parseInt(rdpConfig.getResetLoginLimitationWaitTimeMin()) * 60L -
                                       (System.currentTimeMillis() - user.getLastTryLoginTime().getTime()) / 1000;
                String i18nKey = loginFO.getLoginType() == LoginAuthType.VERIFY ? I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_LOCK_BY_VERIFY_ERROR
                    .name() : I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_LOCK_BY_PWD_ERROR.name();
                String expireMessage = DmI18nUtils.getMessage(i18nKey, rdpConfig.getRetryLoginMaxCount(), String.valueOf(needWaitSeconds));
                throw new ErrorMessageException(expireMessage);
            }
        }
    }

    private void checkByVerify(LoginFO loginFO, DmAuthUserDO user) {
        if (StringUtils.isBlank(loginFO.getVerifyCode())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_VERIFY_CODE_EMPTY.name()));
        }

        try {
            CheckVerifyMO mo = new CheckVerifyMO();
            mo.setVerifyCodeType(VerifyCodeType.LOGIN);
            mo.setVerifyType(VerifyType.SMS_VERIFY_CODE);
            mo.setVerifyCode(loginFO.getVerifyCode());
            mo.setPhoneNumber(loginFO.getAccount());
            //now only support phone login in china
            this.rdpVerifyService.checkVerifyCode(mo);
        } catch (Exception e) {
            log.error("login verify code failed.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_VERIFY_CODE_ERROR.name()));
        }
    }

    private void checkByPasswordForPrimary(LoginFO loginFO, DmAuthUserDO user) {
        if (StringUtils.isBlank(loginFO.getPassword())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWD_CAN_NOT_BE_BLANK.name()));
        }

        // check passwd
        String plainPwd = Sm2Utils.decrypt(this.rdpConfig.getPrivateKey(), loginFO.getPassword());
        if (RdpAuthUtils.isErrorPasswd(user.getPassword(), plainPwd)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWORD_ERROR.name()));
        }
    }

    private void checkByPasswordSubAccount(LoginFO loginFO, DmAuthUserDO user) {
        if (StringUtils.isBlank(loginFO.getPassword())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWD_CAN_NOT_BE_BLANK.name()));
        }

        DmAuthUserDO pUserDO = this.authDal.userMapper().queryById(user.getParentId());
        DmSysUserConfDO configDO = this.userConfigService.getSpecifiedConfig(pUserDO.getUid(), UserDefinedConfig.Fields.subAccountPwdExpireDays);
        if (configDO != null && StringUtils.isNotBlank(configDO.getConfigValue())) {
            int days = Integer.parseInt(configDO.getConfigValue());
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

        // check passwd
        String plainPwd = Sm2Utils.decrypt(this.rdpConfig.getPrivateKey(), loginFO.getPassword());
        if (RdpAuthUtils.isErrorPasswd(user.getPassword(), plainPwd)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWORD_ERROR.name()));
        }
    }

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

        String loginAccount = StringUtils.defaultString(loginFO.getAccount());
        String userAccount = null;
        DmAuthUserDO primaryUser = null;
        if (loginFO.getRegisterInfo() != null && StringUtils.isNotBlank(loginFO.getRegisterInfo().getPrimaryUid())) {
            primaryUser = this.authDal.userMapper().queryByUid(loginFO.getRegisterInfo().getPrimaryUid());
            int splitIdx = StringUtils.lastIndexOf(loginAccount, "@");
            userAccount = splitIdx > -1 ? loginAccount.substring(0, splitIdx) : loginAccount;
        }
        if (primaryUser == null) {
            userAccount = loginProviderSpi.loginExtractAccount(loginAccount);
            String userDomain = loginProviderSpi.loginExtractDomain(loginAccount);
            primaryUser = this.authDal.userMapper().queryPrimaryByDomain(userDomain);
        }
        if (primaryUser == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_NOT_EXIST.name()));
        }
        if (primaryUser.getAccountType() != AccountType.PRIMARY_ACCOUNT) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_OWNER_IS_NOT_PRIMARY_ERROR.name()));
        }

        if (primaryUser.isDisable()) {
            return loginFailedNotLimit(primaryUser, new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_DISABLED.name())));
        }

        LoginRequest request = new LoginRequest();
        request.setLoginAccount(userAccount);
        request.setLoginPassword(Sm2Utils.decrypt(this.rdpConfig.getPrivateKey(), loginFO.getPassword()));
        request.setLoginVerifyCode(loginFO.getVerifyCode());
        request.setAccessToken(loginFO.getAccessToken());
        if (StringUtils.isNotBlank(loginFO.getToken())) {
            DmAuthCsrfTokenDO csrfTokenDO = this.csrfTokenService.pullToken(loginFO.getToken());
            if (csrfTokenDO != null) {
                request.setAccessToken(csrfTokenDO.getSecretToken());
            }
        }
        LoginResponse authUserDTO = loginProviderSpi.authLogin(primaryUser.getUid(), request);
        UserData loginData = authUserDTO.getLoginUser();

        if (!authUserDTO.isSuccess()) {
            DmAuthUserDO loginUser = RdpConvertUtils.convertToRdpUserDO(loginType, primaryUser, loginData);
            return loginFailedNotLimit(loginUser, new ErrorMessageException(authUserDTO.getErrMsg()));
        }
        if (loginData == null) {
            return loginFailedNotLimit(primaryUser, new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_NOT_EXIST.name())));
        }

        DmAuthUserDO loginUser = RdpConvertUtils.convertToRdpUserDO(loginType, primaryUser, loginData);
        DmAuthUserDO bindUser = this.authDal.userMapper().queryBySubAccountAndBind(String.valueOf(primaryUser.getId()), loginFO.getAccount(), loginType.getBindType().name());
        if (bindUser == null) {
            if (loginFO.getRegisterInfo() == null) {
                String csrfToken;
                if (loginData.getAccessToken() != null) {
                    csrfToken = this.csrfTokenService.pushToken(loginData.getAccessToken());
                } else {
                    csrfToken = null;
                }

                LoginAutoRegisterFO moreInfo = new LoginAutoRegisterFO();
                moreInfo.setName(loginUser.getUsername());
                moreInfo.setEmail(loginUser.getEmail());
                moreInfo.setPhone(loginUser.getPhone());
                moreInfo.setPrimaryUid(primaryUser.getUid());

                LoginMO mo = new LoginMO();
                mo.setSuccess(true);
                mo.setNeedMore(true);
                mo.setToken(csrfToken);
                mo.setMoreInfo(moreInfo);
                mo.setErrMsg(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_FIRST_TIME.name()));
                return mo;
            } else {
                LoginAutoRegisterFO moreInfo = loginFO.getRegisterInfo();
                moreInfo.setPrimaryUid(primaryUser.getUid());
                try {
                    bindUser = this.registerBindUser(primaryUser, loginType.getBindType(), loginUser, moreInfo);
                } catch (Exception e) {
                    this.csrfTokenService.storeSecretToken(loginFO.getToken(), loginData.getAccessToken());// restore
                    throw e;
                }
            }
        } else {
            this.authDal.userMapper().updateAccessTokenByUid(bindUser.getUid(), loginUser.getAccessToken());
        }

        try {
            checkAccountStatus(loginFO, bindUser);
            return loginDone(bindUser);
        } catch (ErrorMessageException e) {
            return loginFailedNotLimit(bindUser, e);
        }
    }

    private LoginMO loginDone(DmAuthUserDO user) {
        long nowMs = System.currentTimeMillis();

        this.authDal.userMapper().updateLoginLimitInfo(new Date(nowMs), 0, false, user.getId());
        LoginMO re = new LoginMO();
        re.setSuccess(true);
        re.setUid(user.getUid());
        re.setUsername(user.getUsername());

        String jwtToken = this.jwtService.genJwtToken(user);
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

    private LoginMO loginFailed(LoginFO loginFO, DmAuthUserDO user, ErrorMessageException e) {
        String errorMsg = e.getErrorMessage();
        if (isExceedLoginFailCount(user)) {
            long nowMs = System.currentTimeMillis();
            this.authDal.userMapper().updateLoginLimitInfo(new Date(nowMs), user.getLoginFailCount() + 1, true, user.getId());

            long needWaitSeconds = Integer.parseInt(this.rdpConfig.getResetLoginLimitationWaitTimeMin()) * 60L -
                                   (System.currentTimeMillis() - user.getLastTryLoginTime().getTime()) / 1000;
            String failCnt = String.valueOf(Math.min(user.getLoginFailCount() + 1, rdpConfig.getRetryLoginMaxCount()));
            String i18nKey = loginFO.getLoginType() == LoginAuthType.VERIFY ? I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_LOCK_BY_VERIFY_ERROR
                .name() : I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_LOCK_BY_PWD_ERROR.name();
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

    private DmAuthUserDO registerBindUser(DmAuthUserDO primaryUser, AccountBindType bindType, DmAuthUserDO bindUser, LoginAutoRegisterFO moreInfo) {
        bindUser.setUsername(moreInfo.getName());
        bindUser.setEmail(moreInfo.getEmail());
        bindUser.setPhone(moreInfo.getPhone());
        AddSubAccountMO accountMO = this.rdpUserService.addSubAccountForBind(primaryUser.getUid(), bindType, bindUser);

        if (!accountMO.isSuccess()) {
            throw new ErrorMessageException(accountMO.getErrorMsg());
        } else {
            return this.rdpUserService.getUserByUid(accountMO.getSubUid());
        }
    }

    //
    //
    //

    @Override
    public void fillSubAccountPwdValidDays(LoginUserVO userVO, Date lastDateUpdatePwd, String pUid) {
        if (lastDateUpdatePwd == null || userVO.getAccountType() == null || userVO.getAccountType() != AccountType.SUB_ACCOUNT) {
            return;
        }

        DmSysUserConfDO configDO = this.userConfigService.getSpecifiedConfig(pUid, UserDefinedConfig.Fields.subAccountPwdExpireDays);
        if (configDO == null || StringUtils.isBlank(configDO.getConfigValue())) {
            return;
        }

        int days = Integer.parseInt(configDO.getConfigValue());
        if (days <= 0) {
            return;
        }

        LocalDateTime lastUpdateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastDateUpdatePwd.getTime()), ZoneId.systemDefault());
        LocalDateTime limit = lastUpdateTime.plusDays(days);

        Instant r = limit.toInstant(ZoneOffset.UTC);
        Instant n = LocalDateTime.now().toInstant(ZoneOffset.UTC);

        long diffMinutes = ChronoUnit.MINUTES.between(n, r);
        double diffDays = ((double) diffMinutes) / 60 / 24;

        userVO.setSubAccountPwdValidDays((long) (Math.floor(diffDays)));
    }

    protected boolean isExceedLoginFailCount(DmAuthUserDO userDO) {
        return userDO.getLoginFailCount() + 1 >= this.rdpConfig.getRetryLoginMaxCount();
    }

    private boolean isAccountLockTimeExpire(DmAuthUserDO userDO) {
        return System.currentTimeMillis() - userDO.getLastTryLoginTime().getTime() > Long.parseLong(this.rdpConfig.getResetLoginLimitationWaitTimeMin()) * 60 * 1000;
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
        if (StringUtils.isNotBlank(this.rdpConfig.getDeployContextPath())) {
            homePath = this.rdpConfig.getDeployContextPath();
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

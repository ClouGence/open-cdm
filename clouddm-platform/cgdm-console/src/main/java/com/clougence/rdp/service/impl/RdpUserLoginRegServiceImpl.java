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
package com.clougence.rdp.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.constants.MfaPreActionType;
import com.clougence.clouddm.console.web.constants.VerifyCodeType;
import com.clougence.clouddm.console.web.constants.VerifyType;
import com.clougence.clouddm.console.web.dal.enumeration.AccountBindType;
import com.clougence.clouddm.console.web.dal.enumeration.AccountType;
import com.clougence.clouddm.console.web.dal.enumeration.AreaCode;
import com.clougence.clouddm.console.web.dal.model.DmCsrfTokenDO;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.csrf.CsrfTokenService;
import com.clougence.clouddm.console.web.global.jwtsession.JwtService;
import com.clougence.clouddm.console.web.model.fo.LoginAutoRegisterFO;
import com.clougence.clouddm.console.web.model.fo.LoginFO;
import com.clougence.clouddm.console.web.model.vo.LoginUserVO;
import com.clougence.clouddm.console.web.util.*;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.model.RdpUserDO;
import com.clougence.clouddm.console.web.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpUserConfigService;
import com.clougence.rdp.service.RdpUserLoginRegService;
import com.clougence.rdp.service.RdpUserService;
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
    private DmConsoleConfig      rdpConfig;
    @Resource
    private RdpUserMapper        rdpUserMapper;
    @Resource
    private RdpUserService       rdpUserService;
    @Resource
    private JwtService           jwtService;
    @Resource
    private RdpVerifyService     rdpVerifyService;
    @Resource
    private RdpUserConfigService rdpUserConfigService;
    @Resource
    private CsrfTokenService     csrfTokenService;

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
        RdpUserDO user;
        switch (loginFO.getLoginType()) {
            case VERIFY: {
                user = this.rdpUserMapper.queryPrimaryByPhone(loginFO.getAccount());
                break;
            }
            case PASSWORD: {
                user = this.rdpUserMapper.queryPrimaryByEmail(loginFO.getAccount());
                if (user == null) {
                    user = this.rdpUserMapper.queryPrimaryByPhone(loginFO.getAccount());
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
            RdpUserDO user = this.rdpUserMapper.queryBySubAccount(loginFO.getAccount());
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

    private void checkAccountStatus(LoginFO loginFO, RdpUserDO user) {
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
                this.rdpUserMapper.updateLoginLimitInfo(t, 0, false, user.getId());
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

    private void checkByVerify(LoginFO loginFO, RdpUserDO user) {
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
            mo.setPhoneAreaCode(AreaCode.CHINA);

            this.rdpVerifyService.checkVerifyCode(mo);
        } catch (Exception e) {
            log.error("login verify code failed.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_VERIFY_CODE_ERROR.name()));
        }
    }

    private void checkByPasswordForPrimary(LoginFO loginFO, RdpUserDO user) {
        if (StringUtils.isBlank(loginFO.getPassword())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWD_CAN_NOT_BE_BLANK.name()));
        }

        // check passwd
        String plainPwd = Sm2Utils.decrypt(this.rdpConfig.getPrivateKey(), loginFO.getPassword());
        if (RdpAuthUtils.isErrorPasswd(user.getPassword(), plainPwd)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWORD_ERROR.name()));
        }
    }

    private void checkByPasswordSubAccount(LoginFO loginFO, RdpUserDO user) {
        if (StringUtils.isBlank(loginFO.getPassword())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWD_CAN_NOT_BE_BLANK.name()));
        }

        RdpUserDO pUserDO = this.rdpUserMapper.queryById(user.getParentId());
        RdpUserKvBaseConfigDO configDO = this.rdpUserConfigService.getSpecifiedConfig(pUserDO.getUid(), UserDefinedConfig.Fields.subAccountPwdExpireDays);
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
                    this.rdpUserMapper.updateLastUpdatePwdTimeById(user.getId());
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

        String userAccount = loginProviderSpi.loginExtractAccount(loginFO.getAccount());
        String userDomain = loginProviderSpi.loginExtractDomain(loginFO.getAccount());
        RdpUserDO primaryUser = this.rdpUserMapper.queryPrimaryByDomain(userDomain);
        if (primaryUser == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_NOT_EXIST.name()));
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
            DmCsrfTokenDO csrfTokenDO = this.csrfTokenService.pullToken(loginFO.getToken());
            if (csrfTokenDO != null) {
                request.setAccessToken(csrfTokenDO.getSecretToken());
            }
        }
        LoginResponse authUserDTO = loginProviderSpi.authLogin(primaryUser.getUid(), request);
        UserData loginData = authUserDTO.getLoginUser();

        if (!authUserDTO.isSuccess()) {
            RdpUserDO loginUser = RdpConvertUtils.convertToRdpUserDO(loginType, primaryUser, loginData);
            return loginFailedNotLimit(loginUser, new ErrorMessageException(authUserDTO.getErrMsg()));
        }
        if (loginData == null) {
            return loginFailedNotLimit(primaryUser, new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_NOT_EXIST.name())));
        }

        RdpUserDO loginUser = RdpConvertUtils.convertToRdpUserDO(loginType, primaryUser, loginData);
        RdpUserDO bindUser = this.rdpUserMapper.queryBySubAccountAndBind(String.valueOf(primaryUser.getId()), loginFO.getAccount(), loginType.getBindType().name());
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
            this.rdpUserMapper.updateAccessTokenByUid(bindUser.getUid(), loginUser.getAccessToken());
        }

        try {
            checkAccountStatus(loginFO, bindUser);
            return loginDone(bindUser);
        } catch (ErrorMessageException e) {
            return loginFailedNotLimit(bindUser, e);
        }
    }

    private LoginMO loginDone(RdpUserDO user) {
        long nowMs = System.currentTimeMillis();

        this.rdpUserMapper.updateLoginLimitInfo(new Date(nowMs), 0, false, user.getId());
        LoginMO re = new LoginMO();
        re.setSuccess(true);
        re.setUid(user.getUid());
        re.setUsername(user.getUsername());

        String jwtToken = this.jwtService.genJwtToken(user);
        re.setToken(jwtToken);
        if (user.getParentId() != null) {
            RdpUserDO rdpUserDO = rdpUserMapper.queryById(user.getParentId());
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

    private LoginMO loginFailed(LoginFO loginFO, RdpUserDO user, ErrorMessageException e) {
        String errorMsg = e.getErrorMessage();
        if (isExceedLoginFailCount(user)) {
            long nowMs = System.currentTimeMillis();
            this.rdpUserMapper.updateLoginLimitInfo(new Date(nowMs), user.getLoginFailCount() + 1, true, user.getId());

            long needWaitSeconds = Integer.parseInt(this.rdpConfig.getResetLoginLimitationWaitTimeMin()) * 60L -
                                   (System.currentTimeMillis() - user.getLastTryLoginTime().getTime()) / 1000;
            String failCnt = String.valueOf(Math.min(user.getLoginFailCount() + 1, rdpConfig.getRetryLoginMaxCount()));
            String i18nKey = loginFO.getLoginType() == LoginAuthType.VERIFY ? I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_LOCK_BY_VERIFY_ERROR
                .name() : I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_LOCK_BY_PWD_ERROR.name();
            errorMsg = DmI18nUtils.getMessage(i18nKey, failCnt, String.valueOf(needWaitSeconds));
        } else {
            long nowMs = System.currentTimeMillis();
            this.rdpUserMapper.updateLoginLimitInfo(new Date(nowMs), user.getLoginFailCount() + 1, false, user.getId());
        }

        LoginMO loginMO = new LoginMO(false, errorMsg);
        if (user.getParentId() == null) {
            loginMO.setPuid(user.getUid());
        } else {
            RdpUserDO rdpUserDO = rdpUserMapper.queryById(user.getParentId());
            if (rdpUserDO != null) {
                loginMO.setPuid(rdpUserDO.getUid());
            }
        }
        loginMO.setUid(user.getUid());
        loginMO.setUsername(user.getUsername());
        return loginMO;
    }

    private LoginMO loginFailedNotLimit(RdpUserDO user, ErrorMessageException e) {
        LoginMO loginMO = new LoginMO(false, e.getErrorMessage());
        if (user == null) {
            return loginMO;
        }
        if (user.getParentId() == null) {
            loginMO.setPuid(user.getUid());
        } else {
            RdpUserDO rdpUserDO = rdpUserMapper.queryById(user.getParentId());
            if (rdpUserDO != null) {
                loginMO.setPuid(rdpUserDO.getUid());
            }
        }
        loginMO.setUid(user.getUid());
        loginMO.setUsername(user.getUsername());
        return loginMO;
    }

    private RdpUserDO registerBindUser(RdpUserDO primaryUser, AccountBindType bindType, RdpUserDO bindUser, LoginAutoRegisterFO moreInfo) {
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

        RdpUserKvBaseConfigDO configDO = this.rdpUserConfigService.getSpecifiedConfig(pUid, UserDefinedConfig.Fields.subAccountPwdExpireDays);
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

    protected boolean isExceedLoginFailCount(RdpUserDO userDO) {
        return userDO.getLoginFailCount() + 1 >= this.rdpConfig.getRetryLoginMaxCount();
    }

    private boolean isAccountLockTimeExpire(RdpUserDO userDO) {
        return System.currentTimeMillis() - userDO.getLastTryLoginTime().getTime() > Long.parseLong(this.rdpConfig.getResetLoginLimitationWaitTimeMin()) * 60 * 1000;
    }

    //
    //
    //

    @Override
    public boolean isLogoutUsingJump(String uid) {
        RdpUserDO user = this.rdpUserMapper.queryByUid(uid);
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

        RdpUserDO user = this.rdpUserMapper.queryByUid(uid);
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

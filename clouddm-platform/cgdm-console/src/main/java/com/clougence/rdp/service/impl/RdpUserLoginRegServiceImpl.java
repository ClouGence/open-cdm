package com.clougence.rdp.service.impl;

import static com.clougence.rdp.component.sso.RdpSsoLoginRegService.OUT_OF_CHINA_DEFAULT_PHONE;
import static com.clougence.rdp.component.sso.RdpSsoLoginRegService.OUT_OF_CHINA_DEFAULT_PHONE_AREA;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.clouddm.sdk.security.login.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeployMode;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeploySite;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.rdp.component.csrf.RdpCsrfTokenService;
import com.clougence.rdp.component.jwtsession.RdpJwtService;
import com.clougence.rdp.component.jwtsession.RdpWebUtils;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.controller.model.enumeration.LoginAuthType;
import com.clougence.rdp.controller.model.enumeration.MfaPreActionType;
import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.controller.model.fo.LoginAutoRegisterFO;
import com.clougence.rdp.controller.model.fo.LoginFO;
import com.clougence.rdp.controller.model.fo.RegisterFO;
import com.clougence.rdp.controller.model.vo.LoginUserVO;
import com.clougence.rdp.dal.enumeration.AccountBindType;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.enumeration.AreaCode;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpCsrfTokenDO;
import com.clougence.rdp.dal.model.RdpRoleDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.*;
import com.clougence.rdp.service.model.AddSubAccountMO;
import com.clougence.rdp.service.model.CheckVerifyMO;
import com.clougence.rdp.service.model.LoginMO;
import com.clougence.rdp.util.RdpAuthUtils;
import com.clougence.rdp.util.RdpConvertUtils;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.rdp.util.Sm2Utils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pudding
 * @version 2020-01-17 15:29
 */
@Service
@Slf4j
public class RdpUserLoginRegServiceImpl implements RdpUserLoginRegService {

    @Resource
    private RdpConsoleConfig     rdpConfig;

    @Resource
    private RdpNamingService     rdpNamingService;

    @Resource
    private RdpUserMapper        rdpUserMapper;

    @Resource
    private RdpUserService       rdpUserService;

    @Resource
    private RdpRoleService       rdpRoleService;

    @Resource
    private RdpJwtService        rdpJwtService;

    @Resource
    private RdpVerifyService     rdpVerifyService;

    @Resource
    private RdpUserConfigService rdpUserConfigService;

    @Resource
    private RdpSysConfigService  rdpSysConfigService;

    @Resource
    private RdpDsEnvService      rdpDsEnvService;

    @Resource
    private RdpCsrfTokenService  csrfTokenService;

    @Override
    public LoginMO login(LoginFO loginFO) {
        if (StringUtils.isBlank(loginFO.getAccount())) {
            return new LoginMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_MISSING.name()));
        }

        if (loginFO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return this.loginByPrimaryAccount(loginFO);
        } else if (loginFO.getAccountType() == AccountType.SUB_ACCOUNT) {
            if (!loginFO.getAccount().contains("@")) {
                return new LoginMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SUB_ACCOUNT_FMT_ERROR.name()));
            }
            if (loginFO.getAccount().trim().charAt(0) == '@') {
                return new LoginMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_MISSING.name()));
            }

            return this.loginBySubAccount(loginFO);
        }

        throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_ACCOUNT_TYPE.name()));
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
                if (GlobalDeployMode.inPrivate()) {
                    user = this.rdpUserMapper.queryPrimaryByEmail(loginFO.getAccount());
                    if (user == null) {
                        user = this.rdpUserMapper.queryPrimaryByPhone(loginFO.getAccount());
                    }
                } else {
                    if (GlobalDeploySite.china == GlobalDeploySite.currDeploySite) {
                        user = this.rdpUserMapper.queryPrimaryByPhone(loginFO.getAccount());
                    } else {
                        user = this.rdpUserMapper.queryPrimaryByEmail(loginFO.getAccount());
                    }
                }
                break;
            }
            default: {
                return new LoginMO(false, RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_PRIMARY_LOGIN_TYPE.name()));
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
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_PRIMARY_LOGIN_TYPE.name()));
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
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_NOT_EXIST.name()));
        }

        if (user.isDisable()) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_UNAVAILABLE.name()));
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
                String expireMessage = RdpI18nUtils.getMessage(i18nKey, rdpConfig.getRetryLoginMaxCount(), String.valueOf(needWaitSeconds));
                throw new ErrorMessageException(expireMessage);
            }
        }
    }

    private void checkByVerify(LoginFO loginFO, RdpUserDO user) {
        if (StringUtils.isBlank(loginFO.getVerifyCode())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_VERIFY_CODE_EMPTY.name()));
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
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_VERIFY_CODE_ERROR.name()));
        }
    }

    private void checkByPasswordForPrimary(LoginFO loginFO, RdpUserDO user) {
        if (StringUtils.isBlank(loginFO.getPassword())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWD_CAN_NOT_BE_BLANK.name()));
        }

        // check passwd
        String plainPwd = Sm2Utils.decrypt(this.rdpConfig.getPrivateKey(), loginFO.getPassword());
        if (RdpAuthUtils.isErrorPasswd(user.getPassword(), plainPwd)) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWORD_ERROR.name()));
        }
    }

    private void checkByPasswordSubAccount(LoginFO loginFO, RdpUserDO user) {
        if (StringUtils.isBlank(loginFO.getPassword())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWD_CAN_NOT_BE_BLANK.name()));
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
                        throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWORD_EXPIRED.name(), days));
                    }
                } else {
                    this.rdpUserMapper.updateLastUpdatePwdTimeById(user.getId());
                }
            }
        }

        // check passwd
        String plainPwd = Sm2Utils.decrypt(this.rdpConfig.getPrivateKey(), loginFO.getPassword());
        if (RdpAuthUtils.isErrorPasswd(user.getPassword(), plainPwd)) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PASSWORD_ERROR.name()));
        }
    }

    private LoginMO loginByProvider(LoginFO loginFO) {
        LoginAuthType loginType = loginFO.getLoginType();
        if (loginType.getBindType().getProvider() == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_SUBACCOUNT_LOGIN_TYPE.name()));
        }

        LoginProvider loginProvider = loginType.getBindType().getProvider();
        LoginProviderSpi loginProviderSpi = PluginManager.findSpi(LoginProviderSpi.class, loginProvider.name());
        if (loginProviderSpi == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_PLUGIN_NOT_FOUND.name()));
        }

        String userAccount = loginProviderSpi.loginExtractAccount(loginFO.getAccount());
        String userDomain = loginProviderSpi.loginExtractDomain(loginFO.getAccount());
        RdpUserDO primaryUser = this.rdpUserMapper.queryPrimaryByDomain(userDomain);
        if (primaryUser == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_NOT_EXIST.name()));
        }

        if (primaryUser.isDisable()) {
            return loginFailedNotLimit(primaryUser, new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_DISABLED.name())));
        }

        LoginRequest request = new LoginRequest();
        request.setLoginAccount(userAccount);
        request.setLoginPassword(Sm2Utils.decrypt(this.rdpConfig.getPrivateKey(), loginFO.getPassword()));
        request.setLoginVerifyCode(loginFO.getVerifyCode());
        request.setAccessToken(loginFO.getAccessToken());
        if (StringUtils.isNotBlank(loginFO.getToken())) {
            RdpCsrfTokenDO csrfTokenDO = this.csrfTokenService.pullToken(loginFO.getToken());
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
            return loginFailedNotLimit(primaryUser, new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_NOT_EXIST.name())));
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
                mo.setErrMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_FIRST_TIME.name()));
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

        String jwtToken = this.rdpJwtService.genJwtToken(user);
        re.setToken(jwtToken);
        if (user.getParentId() != null) {
            RdpUserDO rdpUserDO = rdpUserMapper.queryById(user.getParentId());
            if (rdpUserDO != null) {
                re.setPuid(rdpUserDO.getUid());
            }
        }

        if (GlobalDeployMode.inPrivate() && user.isUseMfa()) {
            re.setNeedMfa(true);
            re.setMfaPreActionToken(this.rdpJwtService.genMfaActionToken(user.getUid(), MfaPreActionType.LOGIN, jwtToken));
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
            errorMsg = RdpI18nUtils.getMessage(i18nKey, failCnt, String.valueOf(needWaitSeconds));
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
    public ResWebData<Boolean> register(RegisterFO registerFO) {
        dataTruncation(registerFO);

        // check form
        if (validRegisterMode(registerFO)) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_NEED_PHONE_ERROR.name()));
        }

        // check conflict
        RdpUserDO checkUser;
        if (GlobalDeployMode.inPrivate()) {
            checkUser = this.rdpUserMapper.queryPrimaryByEmail(registerFO.getEmail());
            if (checkUser != null) {
                return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_EXIST_ERROR.name(), registerFO.getEmail()));
            }

            checkUser = this.rdpUserMapper.queryPrimaryByPhone(registerFO.getPhone());
            if (checkUser != null) {
                return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_PHONE_EXIST_ERROR.name(), registerFO.getPhone()));
            }
        } else {
            if (GlobalDeploySite.china == GlobalDeploySite.currDeploySite) {
                checkUser = this.rdpUserMapper.queryPrimaryByPhone(registerFO.getPhone());
                if (checkUser != null) {
                    return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_PHONE_EXIST_ERROR.name(), registerFO.getPhone()));
                }
            } else {
                checkUser = this.rdpUserMapper.queryPrimaryByEmail(registerFO.getEmail());
                if (checkUser != null) {
                    return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_EXIST_ERROR.name(), registerFO.getEmail()));
                }
            }
        }

        if (registerFO.getPhoneAreaCode() == null) {
            registerFO.setPhoneAreaCode(AreaCode.CHINA);
        }

        if (GlobalDeploySite.outChina() && registerFO.getPhone() == null) {
            registerFO.setPhone(OUT_OF_CHINA_DEFAULT_PHONE);
            registerFO.setPhoneAreaCode(OUT_OF_CHINA_DEFAULT_PHONE_AREA);
        }

        // check verify
        CheckVerifyMO verifyData;
        switch (registerFO.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyData = new CheckVerifyMO();
                verifyData.setPhoneNumber(registerFO.getPhone());
                verifyData.setPhoneAreaCode(registerFO.getPhoneAreaCode());
                verifyData.setVerifyCode(registerFO.getVerifyCode());
                verifyData.setVerifyType(VerifyType.SMS_VERIFY_CODE);
                verifyData.setVerifyCodeType(VerifyCodeType.REGISTER);
                break;
            case EMAIL_VERIFY_CODE:
                if (GlobalDeploySite.currDeploySite == GlobalDeploySite.china) {
                    return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_UNSUPPORTED_BY_EMAIL_ERROR.name()));
                }

                verifyData = new CheckVerifyMO();
                verifyData.setEmail(registerFO.getEmail());
                verifyData.setVerifyCode(registerFO.getVerifyCode());
                verifyData.setVerifyType(VerifyType.EMAIL_VERIFY_CODE);
                verifyData.setVerifyCodeType(VerifyCodeType.REGISTER);
                break;
            default:
                return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_INNER_UNSUPPORTED_TYPE_ERROR.name()));
        }

        this.rdpVerifyService.checkVerifyCode(verifyData);

        // init user and role
        try {
            return this.initUserAndRole(registerFO);
        } catch (Exception e) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(e.getMessage()));
        }
    }

    private void dataTruncation(RegisterFO signinFO) {
        if (signinFO.getKeyword() != null) {
            if (signinFO.getKeyword().length() >= RdpWebViewLogService.KEY_WORD_CONTENT_LENGTH) {
                signinFO.setKeyword(signinFO.getKeyword().substring(0, RdpWebViewLogService.KEY_WORD_CONTENT_LENGTH));
            }
        }

        if (signinFO.getSrc() != null) {
            if (signinFO.getSrc().length() >= RdpWebViewLogService.SRC_CONTENT_LENGTH) {
                signinFO.setSrc(signinFO.getSrc().substring(0, RdpWebViewLogService.SRC_CONTENT_LENGTH));
            }
        }
    }

    private boolean validRegisterMode(RegisterFO signinFO) {
        if (GlobalDeploySite.currDeploySite == GlobalDeploySite.china) {
            if (signinFO.getVerifyType() == VerifyType.EMAIL_VERIFY_CODE) {
                return true;
            } else
                return StringUtils.isBlank(signinFO.getPhone());
        } else {
            return false;
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public ResWebData<Boolean> initUserAndRole(RegisterFO registerFO) {
        RdpUserDO newUser = registerInner(registerFO);
        this.initInnerRole(newUser);

        return ResWebDataUtils.buildSuccess();
    }

    private RdpUserDO registerInner(RegisterFO userInfo) {
        String uid = this.rdpNamingService.genUid();

        String domain;
        if (StringUtils.isNotBlank(rdpConfig.getUserDomainSuffix())) {
            domain = uid + "." + rdpConfig.getUserDomainSuffix();
        } else {
            domain = uid + "." + RdpUserService.DEFAULT_USER_DOMAIN_SUFFIX;
        }

        RdpUserDO userDO = new RdpUserDO();
        userDO.setUid(uid);
        userDO.setPassword(CryptService.INSTANCE.encryptForOneWay(userInfo.getPassword()).getEncryptPassword());
        userDO.setCompany(userInfo.getCompany());
        userDO.setEmail(userInfo.getEmail());
        userDO.setPhoneAreaCode(userInfo.getPhoneAreaCode());
        userDO.setPhone(userInfo.getPhone());
        userDO.setUsername(userInfo.getUserName());
        userDO.setContactMe(userInfo.isContactMe());
        userDO.setSrc(userInfo.getSrc());
        userDO.setKeyword(userInfo.getKeyword());
        userDO.setClientId(userInfo.getClientId());
        userDO.setAccessKey(this.rdpNamingService.genAccessKey());
        userDO.setAccountType(AccountType.PRIMARY_ACCOUNT);
        userDO.setUserDomain(domain);
        userDO.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(this.rdpNamingService.genSecretKey()));
        this.rdpUserMapper.insert(userDO);
        return userDO;
    }

    private void initInnerRole(RdpUserDO newUser) {
        this.rdpRoleService.repairRoleForUser(newUser.getUid());

        List<RdpRoleDO> roles = this.rdpRoleService.listRoleByUID(newUser.getUid());
        RdpRoleDO adminRole = roles.stream().filter(roleDO -> StringUtils.equals(roleDO.getRoleName(), SecSysRole.ADMIN_ROLE_NAME)).findFirst().orElse(null);

        if (adminRole == null) {
            throw new IllegalStateException(I18nRdpMsgKeys.REGISTER_INNER_INIT_ROLE_TYPE_ERROR.name());
        }

        this.rdpUserMapper.updateRoleById(newUser.getId(), adminRole.getId());
        this.rdpUserConfigService.initUserConfigs(newUser.getUid());
        this.rdpSysConfigService.initUserSystemEnv(newUser.getUid());
        this.rdpDsEnvService.initPrimaryUserDefaultEnv(newUser.getUid(), newUser.getUid());
    }

    @Override
    public boolean isLogoutUsingJump(String uid) {
        RdpUserDO user = this.rdpUserMapper.queryByUid(uid);
        if (user.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return false;
        }

        return user.getBindType() != AccountBindType.INTERNAL && user.getBindType() != AccountBindType.MANAGED;
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
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_PLUGIN_NOT_FOUND.name()));
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

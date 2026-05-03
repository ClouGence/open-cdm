package com.clougence.rdp.controller;

import static com.clougence.rdp.constant.I18nRdpMsgKeys.LOGIN_MFA_PRE_ACTION_TOKEN_ERROR;
import static com.clougence.rdp.constant.I18nRdpMsgKeys.MFA_CODE_IS_INVALID;

import java.io.IOException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeployMode;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeploySite;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.model.feature.RdpFeatureIDs;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.rdp.component.csrf.RdpCsrfTokenService;
import com.clougence.rdp.component.jwtsession.RdpJwtService;
import com.clougence.rdp.component.jwtsession.RdpWebUtils;
import com.clougence.rdp.component.sso.RdpSubLoginService;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy;
import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.constant.operation.AuditType;
import com.clougence.rdp.controller.model.enumeration.LoginAuthType;
import com.clougence.rdp.controller.model.enumeration.MfaPreActionType;
import com.clougence.rdp.controller.model.fo.*;
import com.clougence.rdp.controller.model.fo.mfa.LoginMfaValidFO;
import com.clougence.rdp.controller.model.vo.RdpGlobalSettingsVO;
import com.clougence.rdp.dal.enumeration.RdpProduct;
import com.clougence.rdp.dal.mapper.RdpProductClusterMapper;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.*;
import com.clougence.rdp.service.model.CheckSubAccountMO;
import com.clougence.rdp.service.model.LoginMO;
import com.clougence.rdp.service.model.UpdateUserInfoMO;
import com.clougence.rdp.service.model.ValidateResultMO;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.rdp.util.Sm2Utils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class RdpHomeController {

    @Resource
    private RdpUserService          rdpUserService;

    @Resource
    private RdpUserLoginRegService  rdpLoginService;

    @Resource
    private RdpUserMfaService       rdpUserMfaService;

    @Resource
    private RdpSubLoginService      subLoginService;

    @Resource
    private RdpCsrfTokenService     rdpCsrfTokenService;

    @Resource
    private RdpConsoleConfig        rdpConfig;

    @Resource
    private RdpProductClusterMapper rdpProductClusterMapper;

    @Resource
    private RdpWebViewLogService    rdpWebViewLogService;

    @Resource
    private RdpOpAuditService       rdpOpAuditService;

    @Resource
    private RdpUserConfigService    rdpUserConfigService;

    @Resource
    private RdpJwtService           rdpJwtService;

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/healthcheck")
    public String healthCheck() {
        return "ok";
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ResWebData<?> register(@Valid @RequestBody RegisterFO fo) {
        //decrypt
        fo.setPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), fo.getPassword()));

        ValidateResultMO mo = validateRegisterKeyInfo(fo);
        if (!mo.isSuccess()) {
            return ResWebDataUtils.buildError(mo.getErrorMsg());
        }

        return this.rdpLoginService.register(fo);
    }

    protected ValidateResultMO validateRegisterKeyInfo(RegisterFO fo) {
        if (GlobalDeployMode.inCloud()) {
            if (StringUtils.isBlank(fo.getEmail())) {
                return new ValidateResultMO(false, "Email can not be blank.");
            }

            ValidateResultMO em = rdpUserService.validateByExpr(RdpUserService.EMAIL_VALIDATE_REGEX, "Error format of email.", fo.getEmail());
            if (em != null && !em.isSuccess()) {
                return em;
            }

            if (GlobalDeploySite.currDeploySite == GlobalDeploySite.china) {
                if (StringUtils.isBlank(fo.getPhone())) {
                    return new ValidateResultMO(false, "Phone can not be blank.");
                }

                ValidateResultMO pm = rdpUserService.validateByExpr(RdpUserService.CHINA_PHONE_VALIDATE_REGEX, "Error format of phone.", fo.getPhone());
                if (pm != null && !pm.isSuccess()) {
                    return pm;
                }
            }
        }

        ValidateResultMO mo = rdpUserService.validatePrimaryAccountPwd(fo.getPassword());
        if (mo != null && !mo.isSuccess()) {
            return mo;
        }

        return new ValidateResultMO(true, null);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/login", method = { RequestMethod.POST })
    public ResWebData<?> login(@Valid @RequestBody LoginFO loginFO, HttpServletRequest request, HttpServletResponse response) {
        if (GlobalDeployMode.inPrivate() && loginFO.getLoginType() == LoginAuthType.VERIFY) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_PRIVATE_VERIFY.name()));
        }

        LoginMO loginMO = this.rdpLoginService.login(loginFO);
        return commonLoginResult(loginMO, request, response);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/loginMfaValid", method = { RequestMethod.POST })
    public ResWebData<?> loginMfaValid(@Valid @RequestBody LoginMfaValidFO validFO, HttpServletRequest request, HttpServletResponse response) {
        DecodedJWT mfaJwt = rdpJwtService.verifyMfaActionToken(validFO.getMfaPreActionToken());
        if (mfaJwt == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(LOGIN_MFA_PRE_ACTION_TOKEN_ERROR.name(), RdpUserService.MFA_TOKEN_EXPIRE_SEC));
        }

        String uid = mfaJwt.getId();
        String preActionTypeStr = mfaJwt.getClaim(RdpUserMfaService.MFA_PRE_ACTION_TYPE).asString();
        String jwtTokenStr = mfaJwt.getClaim(RdpUserMfaService.MFA_LOGIN_JWT_TOKEN).asString();

        DecodedJWT loginJwt = rdpJwtService.verifyJwtToken(jwtTokenStr);
        if (loginJwt == null || !loginJwt.getId().equals(uid)) {
            throw new IllegalArgumentException("Uid in token is in-consistent.");
        }

        if (StringUtils.isBlank(uid) || StringUtils.isBlank(preActionTypeStr)) {
            throw new IllegalArgumentException("Mfa pre-action token' properties is empty.");
        }

        MfaPreActionType actionType = MfaPreActionType.valueOf(preActionTypeStr);
        if (actionType != MfaPreActionType.LOGIN) {
            throw new IllegalArgumentException("MFA pre-action type (" + actionType + ") is illegal.");
        }

        if (!rdpUserMfaService.validMfaCode(uid, validFO.getMfaCode())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(MFA_CODE_IS_INVALID.name()));
        }

        RdpUserDO userDO = rdpUserService.getUserByUid(uid);

        LoginMO re = new LoginMO();
        re.setSuccess(true);
        re.setUid(userDO.getUid());
        re.setUsername(userDO.getUsername());
        re.setToken(jwtTokenStr);
        return commonLoginResult(re, request, response);
    }

    @RequestAuth(strategy = AuthStrategy.RefAnyOnes, failedRedirectUrlTo = "/")
    @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
    public Object logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        response.addCookie(RdpWebUtils.newCookie("jwt_token", StringUtils.EMPTY, true, 0));
        rdpOpAuditService.logAndAddOperationAudit(//
                puid, uid, request.getRequestURI(), request.getRemoteAddr(), uid, "", SecurityLevel.NORMAL, AuditType.LOGOUT, ResourceType.ACCOUNT);

        String redirectUrl;
        if (this.rdpLoginService.isLogoutUsingJump(uid)) {
            redirectUrl = this.rdpLoginService.logoutJumpUrl(puid, uid);
        } else if (StringUtils.isNotBlank(this.rdpConfig.getDeployContextPath())) {
            redirectUrl = this.rdpConfig.getDeployContextPath();
        } else {
            redirectUrl = "/";
        }

        return this.redirectOrDone(request, response, redirectUrl);
    }

    private Object redirectOrDone(HttpServletRequest request, HttpServletResponse response, String redirectUrl) throws IOException {
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            return ResWebDataUtils.buildSuccess();
        } else {
            response.sendRedirect(redirectUrl);
            return "ok";
        }
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/check_supplement", method = { RequestMethod.POST })
    public ResWebData<?> checkSupplement(@Valid @RequestBody CheckSubAccountBindInfoFO checkFO, HttpServletResponse response) {
        String puid = checkFO.getPrimaryUid();
        CheckSubAccountMO re = rdpUserService.checkSubAccount(puid, checkFO);
        if (re.isSuccess()) {
            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(re.getErrorMsg());
        }
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/resetpwdunlogin", method = { RequestMethod.POST })
    public ResWebData<?> resetPwdUnLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid ResetPasswdFO resetPwdFO) {
        if (GlobalDeployMode.inPrivate()) {
            throw new RuntimeException("Private deploy can not reset password in un-login state, contact primary user to reset password.");
        }
        //decrypt
        resetPwdFO.setPassword(Sm2Utils.decrypt(rdpConfig.getPrivateKey(), resetPwdFO.getPassword()));

        UpdateUserInfoMO mo = rdpUserService.resetPassword(resetPwdFO);
        if (mo.isSuccess()) {
            Cookie cookie = new Cookie("jwt_token", StringUtils.EMPTY);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookie.setPath("/");

            if (StringUtils.isNotBlank(rdpConfig.getLoginCookieDomain())) {
                cookie.setDomain(rdpConfig.getLoginCookieDomain());
            }

            response.addCookie(cookie);
            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(mo.getErrorMsg());
        }
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/addviewlog", method = { RequestMethod.POST })
    public ResWebData<?> addviewlog(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid AddWebViewLogFO logFO) {
        try {
            String s = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNotBlank(s)) {
                String[] ip = s.split(",");
                if (ip.length >= 1) {
                    logFO.setClientId(ip[0]);
                }
            }

            String uid = (String) request.getAttribute(RdpUserService.UID);
            rdpWebViewLogService.addOneLog(logFO, uid);

            return ResWebDataUtils.buildSuccess();
        } catch (Exception e) {
            log.warn("add web log view failed,but ignore.msg:" + ExceptionUtils.getRootCauseMessage(e));
            return ResWebDataUtils.buildSuccess();
        }
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/global_settings", method = { RequestMethod.POST })
    public ResWebData<?> globalSettings(HttpServletRequest request, HttpServletResponse response) {
        RdpGlobalSettingsVO settings = new RdpGlobalSettingsVO();
        settings.setFeatures(new HashMap<>());
        settings.getFeatures().putAll(PluginManager.getFeatures());

        settings.setProductTrial(this.rdpConfig.isProductTrial());

        settings.setAuthOpPassword(this.rdpConfig.isOppassword());
        settings.setAuthVerifyCodeEnable(this.rdpConfig.isVerifyCodeEnable());

        settings.setDeployEnv(this.rdpConfig.getDeployEnv().name());
        settings.setDeploySite(GlobalDeploySite.currDeploySite.name());
        settings.setDeployMode(GlobalDeployMode.currDeployMode.name());

        settings.setEnableWaterMark(this.rdpConfig.isEnableWaterMark());
        settings.setEnableProductCluster(this.rdpConfig.isEnableProductCluster());
        settings.setDefaultProduct(this.rdpConfig.getDefaultProduct());

        if (GlobalDeployMode.inPrivate()) {
            settings.getFeatures().put(RdpFeatureIDs.ENABLE_REGISTER, false);
            settings.getFeatures().put(RdpFeatureIDs.ENABLE_SSO_LOGIN, false);
        } else {
            settings.getFeatures().put(RdpFeatureIDs.ENABLE_REGISTER, true);
            settings.getFeatures().put(RdpFeatureIDs.ENABLE_SSO_LOGIN, true);
        }

        List<String> strings = new ArrayList<>();
        strings.add(this.rdpConfig.getDefaultProduct().name());
        strings.addAll(this.rdpProductClusterMapper.supportProductType());
        settings.getFeatures().put(RdpFeatureIDs.PRODUCT_CLOUD_CANAL, strings.contains(RdpProduct.CloudCanal.name()));
        settings.getFeatures().put(RdpFeatureIDs.PRODUCT_CLOUD_DM, strings.contains(RdpProduct.CloudDM.name()));
        settings.getFeatures().put(RdpFeatureIDs.ENABLE_VALIDATE_DS_EXTRA_CONF, rdpConfig.getRdpDsConfigValidateEnable());

        settings.setMaxExportSize(this.rdpConfig.getMaxExportSize());
        return ResWebDataUtils.buildSuccess(settings);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/primary_user_domains", method = { RequestMethod.POST })
    public ResWebData<?> primaryUserDomains(HttpServletRequest request) {
        if (!GlobalDeployMode.inPrivate()) {
            return ResWebDataUtils.buildSuccess(Collections.emptyList());
        }

        List<Map<String, Object>> orgList = new ArrayList<>();
        for (RdpUserDO primary : this.rdpUserService.listPrimaryUser()) {
            RdpUserKvBaseConfigDO authTypeDO = this.rdpUserConfigService.getSpecifiedConfig(primary.getUid(), UserDefinedConfig.Fields.subAccountAuthType);
            LoginAuthType authType = LoginAuthType.PASSWORD;
            if (authTypeDO != null) {
                try {
                    authType = LoginAuthType.valueOfCode(authTypeDO.getConfigValue());
                    authType = authType == null ? LoginAuthType.PASSWORD : authType;
                } catch (Exception e) {
                    authType = LoginAuthType.PASSWORD;
                }
            }

            LoginProvider loginProvider = authType.getBindType().getProvider();
            Map<String, Object> attr = new HashMap<>();
            attr.put("domain", primary.getUserDomain());
            attr.put("domainUid", primary.getUid());
            attr.put("title", RdpI18nUtils.getMessage(authType.getI18nKey()));
            attr.put("loginType", authType.name());
            attr.put("jump", loginProvider != null && loginProvider.isJumpIn());
            orgList.add(attr);
        }
        return ResWebDataUtils.buildSuccess(orgList);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/requestJumpUrl", method = { RequestMethod.POST })
    public ResWebData<?> requestJumpUrl(HttpServletRequest request, @RequestBody @Valid RequestJumpUrlFO fo) {
        if (!GlobalDeployMode.inPrivate()) {
            return ResWebDataUtils.buildSuccess("");
        }

        LoginAuthType authType = fo.getType();
        String primaryUid = fo.getPrimaryUid();
        if (authType == null || StringUtils.isBlank(primaryUid)) {
            return ResWebDataUtils.buildSuccess("");
        }

        RdpUserKvBaseConfigDO authTypeDO = this.rdpUserConfigService.getSpecifiedConfig(primaryUid, UserDefinedConfig.Fields.subAccountAuthType);
        if (authTypeDO != null) {
            try {
                authType = LoginAuthType.valueOfCode(authTypeDO.getConfigValue());
                authType = authType == null ? LoginAuthType.PASSWORD : authType;
            } catch (Exception e) {
                authType = LoginAuthType.PASSWORD;
            }
        }
        LoginProvider loginProvider = authType.getBindType().getProvider();
        if (loginProvider == null || !loginProvider.isJumpIn()) {
            return ResWebDataUtils.buildSuccess("");
        }

        List<String> serviceNames = PluginManager.getSpiNamesByType(LoginProviderSpi.class);
        if (!serviceNames.contains(authType.getBindType().getProvider().name())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_PLUGIN_NOT_FOUND.name()));
        }

        if (!this.subLoginService.checkLoginEnable(primaryUid, authType.getBindType().getProvider())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_NOT_ENABLE.name()));
        }

        try {
            String csrfToken = this.rdpCsrfTokenService.randomTokenWithoutSave();
            String redirectURL = RdpWebUtils.getContextPath() + ("callback/auth?" + //
                                                                 "ownerUid=" + primaryUid + "&" + //
                                                                 //"state=" + csrfToken + "&" +//
                                                                 "provider=" + authType.getBindType().getProvider().name());
            this.rdpCsrfTokenService.storeJumpUrl(csrfToken, redirectURL);
            LoginProviderSpi loginProviderSpi = PluginManager.findSpi(LoginProviderSpi.class, authType.getBindType().getProvider().name());
            return ResWebDataUtils.buildSuccess(loginProviderSpi.loginJumpUrl(primaryUid, csrfToken, redirectURL));
        } catch (Exception e) {
            if (e instanceof ErrorMessageException) {
                throw (ErrorMessageException) e;
            } else {
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_ERROR.name(), e.getMessage()));
            }
        }
    }

    protected ResWebData<?> commonLoginResult(LoginMO loginMO, HttpServletRequest request, HttpServletResponse response) {
        if (loginMO.isSuccess()) {
            if (!loginMO.isNeedMore() && !loginMO.isNeedMfa()) {
                fillResponseJwtToken(loginMO.getToken(), response);
            }

            if (StringUtils.isBlank(loginMO.getPuid()) && StringUtils.isNotBlank(loginMO.getUid())) {
                loginMO.setPuid(loginMO.getUid());
            }

            if (loginMO.isSuccess() && !loginMO.isNeedMore() && !loginMO.isNeedMfa()) {
                rdpOpAuditService.logAndAddOperationAudit(loginMO.getPuid(), loginMO.getUid(), request.getRequestURI(), request.getRemoteAddr(), loginMO
                    .getUid(), "", SecurityLevel.NORMAL, AuditType.LOGIN_SUCCESS, ResourceType.ACCOUNT);
            }

            return ResWebDataUtils.buildSuccess(loginMO);
        } else {
            if (StringUtils.isNotBlank(loginMO.getPuid()) && StringUtils.isNotBlank(loginMO.getUid())) {
                rdpOpAuditService.logAndAddOperationAudit(loginMO.getPuid(), loginMO.getUid(), request.getRequestURI(), request.getRemoteAddr(), loginMO.getUid(), loginMO
                    .getErrMsg(), SecurityLevel.NORMAL, AuditType.LOGIN_FAIL, ResourceType.ACCOUNT);
            }

            return ResWebDataUtils.buildError(loginMO.getErrMsg());
        }
    }

    protected void fillResponseJwtToken(String token, HttpServletResponse response) {
        int cookieMaxAge = Math.max(RdpJwtService.minLoginExpireSec, this.rdpConfig.getLoginExpireTimeSec());
        Cookie cookie = RdpWebUtils.newCookie(RdpJwtService.jwtTokenName, token, false, cookieMaxAge);
        response.addCookie(cookie);
    }

    @RequestMapping(value = "/getPublicKey", method = { RequestMethod.POST })
    @RequestAuth(strategy = AuthStrategy.Ignore)
    public ResWebData<?> getPublicKey(HttpServletRequest request, HttpServletResponse response) {
        return ResWebDataUtils.buildSuccess(rdpConfig.getPublicKey());
    }
}

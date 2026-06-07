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
package com.clougence.clouddm.console.web.controller.system;

import static com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys.LOGIN_MFA_PRE_ACTION_TOKEN_ERROR;
import static com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys.MFA_CODE_IS_INVALID;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.ui.menus.UiMenuDef;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.impl.DsMenuUtils;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsMenu;
import com.clougence.clouddm.console.web.component.file.mode.FormatConvertDef;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.constants.MfaPreActionType;
import com.clougence.clouddm.console.web.constants.SystemStatus;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.csrf.CsrfTokenService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.JwtService;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy;
import com.clougence.clouddm.console.web.model.fo.AddWebViewLogFO;
import com.clougence.clouddm.console.web.model.fo.LoginFO;
import com.clougence.clouddm.console.web.model.fo.RequestJumpUrlFO;
import com.clougence.clouddm.console.web.model.fo.mfa.LoginMfaValidFO;
import com.clougence.clouddm.console.web.model.fo.user.CheckSubAccountBindInfoFO;
import com.clougence.clouddm.console.web.model.vo.ConsoleSettingsVO;
import com.clougence.clouddm.console.web.model.vo.GlobalSettingsVO;
import com.clougence.clouddm.console.web.model.vo.RdpGlobalSettingsVO;
import com.clougence.clouddm.console.web.model.vo.SystemStatusVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.login.LoginDefService;
import com.clougence.clouddm.console.web.service.login.LoginMFAService;
import com.clougence.clouddm.console.web.service.login.LoginService;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resultset.file.FileFormatConvert;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.RdpWebViewLogService;
import com.clougence.rdp.service.model.CheckSubAccountMO;
import com.clougence.rdp.service.model.LoginMO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DmHomeController {

    @Resource
    private DmAuthServiceForBiz  authServiceForBiz;
    @Resource
    private DmDsService          dsService;
    @Resource
    private WhiteListService     whiteListService;
    @Resource
    private LoginDefService      loginDefService;
    @Resource
    private DmConsoleConfig      rdpConfig;
    @Resource
    private RdpUserService       userService;
    @Resource
    private LoginService         loginService;
    @Resource
    private LoginMFAService      loginMFAService;
    @Resource
    private AuthDal              authDal;
    @Resource
    private CsrfTokenService     csrfTokenService;
    @Resource
    private RdpWebViewLogService viewLogService;
    @Resource
    private RdpOpAuditService    rdpOpAuditService;
    @Resource
    private JwtService           jwtService;

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/healthcheck")
    public String healthCheck() {
        return "ok";
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/login", method = { RequestMethod.POST })
    public ResWebData<?> login(@Valid @RequestBody LoginFO loginFO, HttpServletRequest request, HttpServletResponse response) {
        LoginMO loginMO = this.loginService.login(loginFO);
        return commonLoginResult(loginMO, request, response);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/loginMfaValid", method = { RequestMethod.POST })
    public ResWebData<?> loginMfaValid(@Valid @RequestBody LoginMfaValidFO validFO, HttpServletRequest request, HttpServletResponse response) {
        DecodedJWT mfaJwt = jwtService.verifyMfaActionToken(validFO.getMfaPreActionToken());
        if (mfaJwt == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(LOGIN_MFA_PRE_ACTION_TOKEN_ERROR.name(), RdpUserService.MFA_TOKEN_EXPIRE_SEC));
        }

        String uid = mfaJwt.getId();
        String preActionTypeStr = mfaJwt.getClaim(LoginMFAService.MFA_PRE_ACTION_TYPE).asString();
        String jwtTokenStr = mfaJwt.getClaim(LoginMFAService.MFA_LOGIN_JWT_TOKEN).asString();

        DecodedJWT loginJwt = jwtService.verifyJwtToken(jwtTokenStr);
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

        if (!loginMFAService.validMFA(uid, validFO.getMfaCode())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(MFA_CODE_IS_INVALID.name()));
        }

        DmAuthUserDO userDO = userService.getUserByUid(uid);

        LoginMO re = new LoginMO();
        re.setSuccess(true);
        re.setUid(userDO.getUid());
        re.setUsername(userDO.getUsername());
        re.setToken(jwtTokenStr);
        re.setLoginType(LoginAuthType.valueOfCode(loginJwt.getClaim(JwtService.LOGIN_TYPE).asString()));
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
        if (this.loginService.isLogoutUsingJump(uid)) {
            redirectUrl = this.loginService.logoutJumpUrl(puid, uid);
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
        CheckSubAccountMO re = userService.checkSubAccount(puid, checkFO);
        if (re.isSuccess()) {
            return ResWebDataUtils.buildSuccess();
        } else {
            return ResWebDataUtils.buildError(re.getErrorMsg());
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
            viewLogService.addOneLog(logFO, uid);

            return ResWebDataUtils.buildSuccess();
        } catch (Exception e) {
            log.warn("add web log view failed,but ignore.msg:" + ExceptionUtils.getRootCauseMessage(e));
            return ResWebDataUtils.buildSuccess();
        }
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/requestJumpUrl", method = { RequestMethod.POST })
    public ResWebData<?> requestJumpUrl(HttpServletRequest request, @RequestBody @Valid RequestJumpUrlFO fo) {
        LoginAuthType authType = fo.getType();
        if (authType == null) {
            return ResWebDataUtils.buildSuccess("");
        }

        DmAuthUserDO rootUser = this.authDal.queryRootUser();
        if (rootUser == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_NOT_EXIST.name()));
        }

        LoginProvider loginProvider = authType.getBindType().getProvider();
        if (loginProvider == null || !loginProvider.isJumpIn()) {
            return ResWebDataUtils.buildSuccess("");
        }

        List<String> serviceNames = PluginManager.getSpiNamesByType(LoginProviderSpi.class);
        if (!serviceNames.contains(authType.getBindType().getProvider().name())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_PLUGIN_NOT_FOUND.name()));
        }

        if (!this.loginDefService.checkLoginEnable(rootUser.getUid(), authType.getBindType().getProvider())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_NOT_ENABLE.name()));
        }

        try {
            String csrfToken = this.csrfTokenService.randomTokenWithoutSave();
            String redirectURL = RdpWebUtils.getContextPath() + ("callback/auth?" + //
                                                                 "provider=" + authType.getBindType().getProvider().name());
            this.csrfTokenService.storeJumpUrl(csrfToken, redirectURL);
            LoginProviderSpi loginProviderSpi = PluginManager.findSpi(LoginProviderSpi.class, authType.getBindType().getProvider().name());
            return ResWebDataUtils.buildSuccess(loginProviderSpi.loginJumpUrl(rootUser.getUid(), csrfToken, redirectURL));
        } catch (Exception e) {
            if (e instanceof ErrorMessageException) {
                throw (ErrorMessageException) e;
            } else {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_ERROR.name(), e.getMessage()));
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
        int cookieMaxAge = Math.max(JwtService.minLoginExpireSec, this.rdpConfig.getLoginExpireTimeSec());
        Cookie cookie = RdpWebUtils.newCookie(JwtService.jwtTokenName, token, false, cookieMaxAge);
        response.addCookie(cookie);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/dm_global_settings", method = { RequestMethod.POST })
    public ResWebData<?> dmGlobalSettings() {
        GlobalSettingsVO vo = new GlobalSettingsVO();
        vo.setVersion(GlobalConfUtils.getAppVersion());
        vo.setAloneMode(GlobalConfUtils.isAloneMode());

        SystemStatusVO systemStatus = new SystemStatusVO();
        boolean ready = PluginManager.isReady();
        systemStatus.setStatus(ready ? SystemStatus.Ready : SystemStatus.Starting);
        vo.setSystemStatus(systemStatus);
        vo.setPublicKey(this.rdpConfig.getPublicKey());
        if (ready) {
            vo.setLoginDef(this.loginDefService.listLoginDef());
            vo.setLoginDefault(this.loginDefService.resolveLoginDefault());
        }
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/global_settings", method = { RequestMethod.POST })
    public ResWebData<?> globalSettings(HttpServletRequest request, HttpServletResponse response) {
        RdpGlobalSettingsVO settings = new RdpGlobalSettingsVO();
        settings.setFeatures(new HashMap<>());
        settings.getFeatures().putAll(PluginManager.getFeatures());

        settings.setAuthOpPassword(this.rdpConfig.isOppassword());

        settings.setEnableWaterMark(this.rdpConfig.isEnableWaterMark());
        settings.setEnableProductCluster(this.rdpConfig.isEnableProductCluster());
        settings.setEnableValidateDsExtraConf(rdpConfig.getRdpDsConfigValidateEnable());

        settings.setMaxExportSize(this.rdpConfig.getMaxExportSize());
        return ResWebDataUtils.buildSuccess(settings);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/dm_console_settings", method = { RequestMethod.POST })
    public ResWebData<?> dmConsoleSettings(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        boolean isSubAccount = !StringUtils.equalsIgnoreCase(puid, uid);

        ConsoleSettingsVO settings = null;
        if (this.authServiceForBiz.checkRoleAuth(puid, uid, SecRoleAuthLabel.DM_DS_MAINTENANCE)) {
            List<DsMenu> envAllMenus = DsMenuUtils.generationDsMenus(UiMenuDef.DEFAULT_ENV);
            List<DsMenu> envMenus = envAllMenus.stream().filter(m -> {
                return whiteListService.checkMenuMaintenance(m.getMenuId());
            }).filter(m -> {
                return isSubAccount || !RdbUiMenuDef.MENU_BROWSE_PERMISSIONS.equalsIgnoreCase(m.getMenuId());
            }).collect(Collectors.toList());

            settings = new ConsoleSettingsVO();
            settings.setMenus(new HashMap<>());
            settings.getMenus().put(DsMenuType.Env.getTypeName(), envMenus);
            settings.setDsSettingDef(filterMenuBy(m -> whiteListService.checkMenuMaintenance(m.getMenuId()), isSubAccount));
        }

        if (settings == null && this.authServiceForBiz.checkRoleAuth(puid, uid, SecRoleAuthLabel.DM_OBJECT_MANAGER)) {
            List<DsMenu> envAllMenus = DsMenuUtils.generationDsMenus(UiMenuDef.DEFAULT_ENV);
            List<DsMenu> envMenus = envAllMenus.stream().filter(m -> whiteListService.checkMenuManager(m.getMenuId())).collect(Collectors.toList());

            settings = new ConsoleSettingsVO();
            settings.setMenus(new HashMap<>());
            settings.getMenus().put(DsMenuType.Env.getTypeName(), envMenus);
            settings.setDsSettingDef(filterMenuBy(m -> whiteListService.checkMenuManager(m.getMenuId()), isSubAccount));
        }

        if (settings == null) {
            List<DsMenu> envAllMenus = DsMenuUtils.generationDsMenus(UiMenuDef.DEFAULT_ENV);
            List<DsMenu> envMenus = envAllMenus.stream().filter(m -> whiteListService.checkMenuQuery(m.getMenuId())).collect(Collectors.toList());

            settings = new ConsoleSettingsVO();
            settings.setMenus(new HashMap<>());
            settings.getMenus().put(DsMenuType.Env.getTypeName(), envMenus);
            settings.setDsSettingDef(filterMenuBy(m -> whiteListService.checkMenuQuery(m.getMenuId()), isSubAccount));
        }

        settings.setFmtConvertDef(new ArrayList<>());
        List<String> convert = PluginManager.getSpiNamesByType(FileFormatConvert.class);
        for (String name : convert) {
            FileFormatConvert fmtConvert = PluginManager.findSpi(FileFormatConvert.class, name);

            FormatConvertDef def = new FormatConvertDef();
            def.setName(fmtConvert.name());
            def.setDescription(DmI18nUtils.getMessage(fmtConvert.descriptionI18n()));
            def.setIcon(fmtConvert.iconName());
            def.setOption(fmtConvert.getOption());
            settings.getFmtConvertDef().add(def);
        }
        settings.getFmtConvertDef().sort(Comparator.comparing(FormatConvertDef::getName));

        return ResWebDataUtils.buildSuccess(settings);
    }

    private Map<DataSourceType, DsConfig> filterMenuBy(Predicate<DsMenu> predicate, boolean isSubAccount) {
        Map<DataSourceType, DsConfig> dsConfigMap = this.dsService.dsConstantSettings();
        dsConfigMap.forEach((dsType, dsConfig) -> {
            if (dsConfig == null) {
                return;
            }
            Map<String, List<DsMenu>> copy = new HashMap<>();
            dsConfig.getMenus().forEach((k, v) -> {
                List<DsMenu> dsMenus = v.stream().filter(predicate).filter(m -> {
                    return isSubAccount || !RdbUiMenuDef.MENU_BROWSE_PERMISSIONS.equalsIgnoreCase(m.getMenuId());
                }).collect(Collectors.toList());

                // last menu can not be separator
                while (true) {
                    int size = dsMenus.size();
                    if (size > 0 && DsMenuUtils.isSeparator(dsMenus.get(size - 1))) {
                        dsMenus.remove(size - 1);
                    } else {
                        break;
                    }
                }

                copy.put(k, dsMenus);
            });
            dsConfig.setMenus(copy);
        });
        return dsConfigMap;
    }
}

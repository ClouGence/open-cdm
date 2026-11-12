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

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.menus.UiMenuDef;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.clouddm.console.web.component.config.UserConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.impl.DsMenuUtils;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsMenu;
import com.clougence.clouddm.console.web.component.file.mode.FormatConvertDef;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.constants.SystemStatus;
import com.clougence.clouddm.console.web.global.csrf.CsrfTokenService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy;
import com.clougence.clouddm.console.web.global.session.SessionCookieService;
import com.clougence.clouddm.console.web.global.session.SessionPrincipal;
import com.clougence.clouddm.console.web.global.session.SessionService;
import com.clougence.clouddm.console.web.model.fo.LoginFO;
import com.clougence.clouddm.console.web.model.fo.RequestJumpUrlFO;
import com.clougence.clouddm.console.web.model.fo.mfa.LoginMfaRecoveryFO;
import com.clougence.clouddm.console.web.model.fo.mfa.LoginMfaValidFO;
import com.clougence.clouddm.console.web.model.fo.user.CheckSubAccountBindInfoFO;
import com.clougence.clouddm.console.web.model.vo.*;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.login.LoginDefService;
import com.clougence.clouddm.console.web.service.login.LoginService;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.clouddm.dsfamily.definition.ui.browser.RdbUiMenuDef;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.ResourceType;
import com.clougence.clouddm.platform.dal.model.auth.AccountBindType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resultset.file.FileFormatConvert;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.ui.menus.DsMenuType;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.model.CheckSubAccountMO;
import com.clougence.rdp.service.model.LoginMO;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
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
    private WhiteListService     whiteListService;
    @Resource
    private LoginDefService      loginDefService;
    @Resource
    private ConsoleConfig        config;
    @Resource
    private RdpUserService       userService;
    @Resource
    private LoginService         loginService;
    @Resource
    private AuthDal              authDal;
    @Resource
    private CsrfTokenService     csrfTokenService;
    @Resource
    private RdpOpAuditService    auditService;
    @Resource
    private DmDsConfigService    dmDsConfigService;
    @Resource
    private UserConfigService    userConfigService;
    @Resource
    private SessionService       sessionService;
    @Resource
    private SessionCookieService sessionCookieService;

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
        LoginMO loginMO = loginService.loginMfaValid(validFO);
        return commonLoginResult(loginMO, request, response);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/loginMfaRecoveryStatus", method = { RequestMethod.POST })
    public ResWebData<?> loginMfaRecoveryStatus(@Valid @RequestBody LoginMfaRecoveryFO fo) {
        this.loginService.loginMfaRecoveryStatus(fo);
        Map<String, Object> result = new HashMap<>();
        result.put("recoveryRequired", true);
        return ResWebDataUtils.buildSuccess(result);
    }

    protected ResWebData<?> commonLoginResult(LoginMO loginMO, HttpServletRequest request, HttpServletResponse response) {
        if (loginMO.isSuccess()) {
            if (StringUtils.isBlank(loginMO.getPuid()) && StringUtils.isNotBlank(loginMO.getUid())) {
                loginMO.setPuid(loginMO.getUid());
            }

            if (loginMO.getSessionToken() != null) {
                this.sessionCookieService.write(response, loginMO.getSessionToken());
                this.sessionCookieService.clearLegacy(response);
                auditService.logAndAddOperationAudit(loginMO.getPuid(), loginMO.getUid(), request.getRequestURI(), request.getRemoteAddr(), loginMO
                    .getUid(), "", SecurityLevel.NORMAL, AuditType.LOGIN_SUCCESS, ResourceType.ACCOUNT);
            }

            return ResWebDataUtils.buildSuccess(loginMO);
        } else {
            if (StringUtils.isNotBlank(loginMO.getPuid()) && StringUtils.isNotBlank(loginMO.getUid())) {
                auditService.logAndAddOperationAudit(loginMO.getPuid(), loginMO.getUid(), request.getRequestURI(), request.getRemoteAddr(), loginMO.getUid(), loginMO
                    .getErrMsg(), SecurityLevel.NORMAL, AuditType.LOGIN_FAIL, ResourceType.ACCOUNT);
            }

            return ResWebDataUtils.buildError(loginMO.getErrMsg());
        }
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
    public Object logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SessionPrincipal principal = this.sessionService.authenticate(request);
        String redirectUrl = this.config.getDeployContextPath();
        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = "/";
        }
        this.sessionService.revokeCurrentSession(request);
        this.sessionCookieService.clear(response);
        this.sessionCookieService.clearLegacy(response);

        if (principal != null) {
            DmAuthUserDO user = this.userService.getUserByUid(principal.getUid());
            if (user != null) {
                String puid = user.getUid();
                if (user.getParentId() != null) {
                    DmAuthUserDO primaryUser = this.authDal.userMapper().queryById(user.getParentId());
                    if (primaryUser != null) {
                        puid = primaryUser.getUid();
                    }
                }
                auditService.logAndAddOperationAudit(//
                        puid, user.getUid(), request.getRequestURI(), request.getRemoteAddr(), user.getUid(), "", SecurityLevel.NORMAL, AuditType.LOGOUT, ResourceType.ACCOUNT);
                if (principal.getLoginType() == LoginAuthType.OIDC && user.getBindType() == AccountBindType.OIDC
                    && StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
                    redirectUrl = this.oidcLogoutJumpUrl(puid, user, redirectUrl);
                }
            }
        }

        return this.redirectOrDone(request, response, redirectUrl);
    }

    private String oidcLogoutJumpUrl(String puid, DmAuthUserDO user, String homeUrl) {
        try {
            LoginProviderSpi provider = PluginManager.findSpi(LoginProviderSpi.class, LoginProvider.OIDC.name());
            if (provider == null) {
                log.warn("OIDC logout provider unavailable for user {}", user.getUid());
                return homeUrl;
            }
            String state = this.csrfTokenService.randomToken();
            String callbackUrl = RdpWebUtils.getContextPath() + "callback/logout?uid=" + user.getUid() + "&provider=" + LoginProvider.OIDC.name();
            String jumpUrl = provider.logoutJumpUrl(puid, state, callbackUrl, user.getAccessToken());
            if (StringUtils.isNotBlank(jumpUrl)) {
                return jumpUrl;
            }
        } catch (Exception e) {
            String msg = "OIDC logout redirect failed for user " + user.getUid();
            log.error(msg, e);
        }
        return homeUrl;
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
    @RequestMapping(value = "/checkSupplement", method = { RequestMethod.POST })
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

        if (!this.loginDefService.checkLoginEnable(authType.getBindType().getProvider())) {
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

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/dmGlobalSettings", method = { RequestMethod.POST })
    public ResWebData<?> dmGlobalSettings() {
        GlobalSettingsVO vo = new GlobalSettingsVO();
        vo.setVersion(GlobalConfUtils.getAppVersion());
        vo.setAloneMode(GlobalConfUtils.isAloneMode());

        SystemStatusVO systemStatus = new SystemStatusVO();
        boolean ready = PluginManager.isReady();
        systemStatus.setStatus(ready ? SystemStatus.Ready : SystemStatus.Starting);
        vo.setSystemStatus(systemStatus);
        vo.setPublicKey(this.config.getPublicKey());
        if (ready) {
            vo.setLoginDef(this.loginDefService.listLoginDef());
            vo.setLoginDefault(this.loginDefService.resolveLoginDefault());
        }
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/globalSettings", method = { RequestMethod.POST })
    public ResWebData<?> globalSettings(HttpServletRequest request, HttpServletResponse response) {
        RdpGlobalSettingsVO vo = new RdpGlobalSettingsVO();
        vo.setFeatures(new HashMap<>());
        vo.getFeatures().putAll(PluginManager.getFeatures());
        vo.setEnableWaterMark(this.config.isEnableWaterMark());
        vo.setEnableProductCluster(this.config.isEnableProductCluster());
        vo.setEnableValidateDsExtraConf(config.getRdpDsConfigValidateEnable());
        vo.setMaxExportSize(this.config.getMaxExportSize());
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/dmConsoleSettings", method = { RequestMethod.POST })
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

        ArrayList<DataSourceType> dsList = settings.getDsSettingDef()//
            .keySet()
            .stream()
            .filter(this::displayDsPlugin)
            .collect(Collectors.toCollection(ArrayList::new));
        settings.setDsSupportNames(groupDsTypesByDisplay(dsList).stream().map(this::toDsSupportNames).collect(Collectors.toList()));
        settings.setSqlFileMaxSize(this.userConfigService.sqlFileMaxSize());
        settings.setLanguageMaxRequestKiloByte(this.userConfigService.languageMaxRequestKiloByte());
        settings.setApprovalExplainMaxSize(this.userConfigService.approvalExplainMaxSize());

        return ResWebDataUtils.buildSuccess(settings);
    }

    private boolean displayDsPlugin(DataSourceType dsType) {
        DsPluginInfo dsPlugin = PluginManager.findDsPlugin(dsType);
        return dsPlugin == null || dsPlugin.display();
    }

    private List<List<DataSourceType>> groupDsTypesByDisplay(List<DataSourceType> dsTypes) {
        Map<Integer, List<DataSourceType>> result = new TreeMap<>();
        for (DataSourceType dsType : dsTypes) {
            result.computeIfAbsent(dsType.getDisplayGroup(), key -> new ArrayList<>()).add(dsType);
        }
        result.values().forEach(group -> {
            group.sort(Comparator.comparingInt(DataSourceType::getOrder).thenComparingInt(Enum::ordinal).thenComparing(DataSourceType::getTypeName));
        });
        return new ArrayList<>(result.values());
    }

    private List<DsSupportNameVO> toDsSupportNames(List<DataSourceType> dsTypes) {
        return dsTypes.stream().map(dsType -> {
            DsSupportNameVO vo = new DsSupportNameVO();
            vo.setDsKey(dsType.getTypeName());
            vo.setDisplayName(resolveDsDisplayName(dsType));
            return vo;
        }).collect(Collectors.toList());
    }

    private String resolveDsDisplayName(DataSourceType dsType) {
        DsPluginInfo dsPlugin = PluginManager.findDsPlugin(dsType);
        if (dsPlugin == null || StringUtils.isBlank(dsPlugin.getDsName())) {
            return dsType.getTypeName();
        }

        String name = dsPlugin.getDsName();
        if (name.startsWith("i18n::")) {
            String i18nKey = name.substring("i18n::".length());
            I18nUtils i18nUtils = dsPlugin.getPlusI18nUtil();
            String i18nName = i18nUtils == null ? null : i18nUtils.getMessage(i18nKey);
            return StringUtils.isBlank(i18nName) || StringUtils.equals(i18nName, i18nKey) ? dsType.getTypeName() : i18nName;
        }
        return name;
    }

    private Map<DataSourceType, DsConfig> filterMenuBy(Predicate<DsMenu> predicate, boolean isSubAccount) {
        Map<DataSourceType, DsConfig> dsConfigMap = this.dmDsConfigService.dsConstantSettings();
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

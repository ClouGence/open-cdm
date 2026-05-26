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
package com.clougence.rdp.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.constants.EventType;
import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.dal.enumeration.AccountBindType;
import com.clougence.clouddm.console.web.dal.enumeration.AccountType;
import com.clougence.clouddm.console.web.dal.enumeration.RdpApprovalType;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.model.DmCsrfTokenDO;
import com.clougence.clouddm.console.web.dal.model.RdpUserDO;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.csrf.CsrfTokenService;
import com.clougence.clouddm.console.web.global.jwtsession.JwtService;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.SecurityLevel;
import com.clougence.clouddm.console.web.model.fo.LoginFO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalCallbackSpi;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.rdp.component.sso.RdpSubLoginService;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.constant.operation.AuditType;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.RdpUserLoginRegService;
import com.clougence.rdp.service.model.LoginMO;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/callback")
public class RdpCallbackController {

    @Resource
    private RdpUserMapper          userMapper;
    @Resource
    private ApprovalFlowService    approvalFlowService;
    @Resource
    private RdpSubLoginService     subLoginService;
    @Resource
    private RdpUserLoginRegService loginRegService;
    @Resource
    private DmConsoleConfig        rdpConfig;
    @Resource
    private RdpOpAuditService      opAuditService;
    @Resource
    private CsrfTokenService       csrfTokenService;

    @RequestMapping(value = "/event", method = { RequestMethod.POST, RequestMethod.GET })
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @SneakyThrows
    public Object event(@RequestParam(required = false) Map<String, String> params, //
                        @RequestParam String puid,                                  //
                        @RequestParam String platform,                              //
                        @RequestParam String eventType,                             //
                        HttpServletRequest request) {
        if (platform == null || eventType == null) {
            return "failed platform or event is null.";
        }

        String body = IOUtils.toString(request.getInputStream());
        params.put("requestBody", body);

        if (EventType.valueOfCode(eventType) == EventType.APPROVAL) {
            return doApproval(params, puid, RdpApprovalType.getByName(platform));
        }

        return "failed unsupported eventType " + eventType;
    }

    private Object doApproval(Map<String, String> params, String puid, RdpApprovalType platform) {
        RdpUserDO userDO = this.userMapper.queryByUid(puid);
        if (userDO == null || userDO.getAccountType() != AccountType.PRIMARY_ACCOUNT) {
            return "failed no user.";
        }

        if (!this.approvalFlowService.checkEnableApproval(puid, platform.getProviderType())) {
            return "failed approval not enable.";
        }

        ApprovalCallbackSpi service = PluginManager.findSpi(ApprovalCallbackSpi.class, platform.getProviderType().name());
        if (service != null) {
            return service.handle(puid, params);
        } else {
            return "failed unsupported provider " + platform.getProviderType().name();
        }
    }

    @RequestMapping(value = "/auth", method = { RequestMethod.POST, RequestMethod.GET })
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @SneakyThrows
    public Object auth(@RequestParam(required = false) Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        String provider = params.getOrDefault("provider", null);
        String ownerUid = params.getOrDefault("ownerUid", null);
        String state = params.getOrDefault("state", null);
        String error = params.getOrDefault("error", null);
        String error_description = params.getOrDefault("error_description", null);

        // for args
        DmCsrfTokenDO tokenDO = this.csrfTokenService.pullToken(state);
        if (tokenDO == null) {
            String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_INVALID_TOKEN_ERROR.name());
            return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_ARGS_ERROR.name(), message);
        }
        if (StringUtils.isBlank(provider) || StringUtils.isBlank(ownerUid)) {
            String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_MISSING_CORE_ARGS_ERROR.name());
            return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_ARGS_ERROR.name(), message);
        }
        // for params
        if (StringUtils.isNotBlank(error)) {
            return this.redirectToFailed(request, response, error, error_description);
        }
        LoginProvider providerEnum = LoginProvider.valueOfCode(provider);
        if (providerEnum == null) {
            String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_UNSUPPORTED_SUBACCOUNT_LOGIN_TYPE.name());
            return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_ARGS_ERROR.name(), message);
        }
        String code = params.getOrDefault("code", null);
        if (StringUtils.isBlank(code)) {
            String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_MISSING_AUTHENTICATION_CODE_ERROR.name());
            return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_ARGS_ERROR.name(), message);
        }

        // for provider
        LoginProviderSpi service = PluginManager.findSpi(LoginProviderSpi.class, providerEnum.name());
        if (service == null) {
            String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_PLUGIN_NOT_FOUND.name());
            return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_PROVIDER_ERROR.name(), message);
        }

        // for ownerUid
        RdpUserDO primaryUser = this.userMapper.queryByUid(ownerUid);
        if (primaryUser == null) {
            String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_NOT_EXIST.name());
            return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_OWNER_ERROR.name(), message);
        }
        if (primaryUser.getAccountType() != AccountType.PRIMARY_ACCOUNT) {
            String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_OWNER_IS_NOT_PRIMARY_ERROR.name());
            return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_OWNER_ERROR.name(), message);
        }
        if (primaryUser.isDisable()) {
            String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_DISABLED.name());
            return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_OWNER_ERROR.name(), message);
        }
        if (!this.subLoginService.checkLoginEnable(ownerUid, providerEnum)) {
            String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_SERVICE_NOT_ENABLE.name());
            return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_OWNER_ERROR.name(), message);
        }

        // fetch userInfo
        UserData fetchUser;
        try {
            LoginRequest loginReq = new LoginRequest();
            loginReq.setLoginAccount("@" + primaryUser.getUserDomain());
            loginReq.setAuthCode(code);
            loginReq.setJumpUrl(tokenDO.getJumpUrl());
            LoginResponse loginRes = service.authLogin(primaryUser.getUid(), loginReq);
            if (!loginRes.isSuccess()) {
                return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_USERINFO_ERROR.name(), loginRes.getErrMsg());
            } else {
                fetchUser = loginRes.getLoginUser();
            }
        } catch (Exception e) {
            if (e instanceof ThirdPartyApiException) {
                String messageKey = ((ThirdPartyApiException) e).getMessageKey();
                Object[] messageArgs = ((ThirdPartyApiException) e).getMessageArgs();
                String i18nMessage = DmI18nUtils.getMessage(messageKey, messageArgs);
                return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_USERINFO_ERROR.name(), i18nMessage);
            } else {
                return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_USERINFO_ERROR.name(), e.getMessage());
            }
        }

        // is first login
        String loginAcc = fetchUser.getSubAccount();
        String loginType = AccountBindType.valueOfProvider(providerEnum).name();
        RdpUserDO bindUser = this.userMapper.queryBySubAccountAndBind(String.valueOf(primaryUser.getId()), loginAcc, loginType);
        if (bindUser == null) {
            String csrfToken = this.csrfTokenService.pushToken(fetchUser.getAccessToken());
            return redirectToLogin(request, response, csrfToken, primaryUser.getUid(), fetchUser);
        }

        // sso login
        try {
            LoginFO loginFO = new LoginFO();
            loginFO.setAccountType(AccountType.SUB_ACCOUNT);
            loginFO.setLoginType(LoginAuthType.valueOfProvider(providerEnum));
            loginFO.setAccount(fetchUser.getSubAccount());
            loginFO.setPassword("");//empty
            loginFO.setAccessToken(fetchUser.getAccessToken());
            LoginMO login = this.loginRegService.login(loginFO);

            if (!login.isSuccess()) {
                if (StringUtils.isNotBlank(login.getPuid()) && StringUtils.isNotBlank(login.getUid())) {
                    opAuditService.logAndAddOperationAudit(login.getPuid(), login.getUid(), request.getRequestURI(), request.getRemoteAddr(), login.getUid(), login
                        .getErrMsg(), SecurityLevel.NORMAL, AuditType.LOGIN_FAIL, ResourceType.ACCOUNT);
                }
                return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_LOGIN_ERROR.name(), login.getErrMsg());
            } else {
                this.userMapper.updateUserName(login.getUid(), fetchUser.getUserName());
            }

            return this.redirectToHome(request, response, login);
        } catch (Exception e) {
            if (e instanceof ErrorMessageException) {
                return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_LOGIN_ERROR.name(), ((ErrorMessageException) e).getErrorMessage());
            } else if (e instanceof ThirdPartyApiException) {
                String messageKey = ((ThirdPartyApiException) e).getMessageKey();
                Object[] messageArgs = ((ThirdPartyApiException) e).getMessageArgs();
                return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_LOGIN_ERROR.name(), DmI18nUtils.getMessage(messageKey, messageArgs));
            } else {
                return this.redirectToFailed(request, response, I18nRdpMsgKeys.LOGIN_SSO_LOGIN_ERROR.name(), e.getMessage());
            }
        }
    }

    protected Object redirectToFailed(HttpServletRequest request, HttpServletResponse response, String errorCode, String errorMessage) throws IOException {
        String contextPath = this.rdpConfig.getDeployContextPath();
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        } else if (!StringUtils.endsWith(contextPath, "/")) {
            contextPath += "/";
        }

        response.sendRedirect(contextPath + "#/login?" +//
                              "error=" + URLEncoder.encode(DmI18nUtils.getMessage(errorCode), "UTF-8") + "&" +//
                              "error_description=" + URLEncoder.encode(errorMessage, "UTF-8"));
        return "failed.";
    }

    protected Object redirectToLogin(HttpServletRequest request, HttpServletResponse response, String registerToken, String primaryUid, UserData fetchUser) throws IOException {
        String contextPath = this.rdpConfig.getDeployContextPath();
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        } else if (!StringUtils.endsWith(contextPath, "/")) {
            contextPath += "/";
        }
        String subAccount = fetchUser.getSubAccount().substring(0, fetchUser.getUserDomain().length());
        String redirectUrl = contextPath + "#/login?" +//
                             "token=" + URLEncoder.encode(StringUtils.defaultString(registerToken, ""), "UTF-8") + "&" +//
                             "sub=" + URLEncoder.encode(StringUtils.defaultString(subAccount, ""), "UTF-8") + "&" +//
                             "account=" + URLEncoder.encode(StringUtils.defaultString(fetchUser.getSubAccount(), ""), "UTF-8") + "&" +//
                             "user=" + URLEncoder.encode(StringUtils.defaultString(fetchUser.getUserName(), ""), "UTF-8") + "&" +//
                             "phone=" + URLEncoder.encode(StringUtils.defaultString(fetchUser.getPhone(), ""), "UTF-8") + "&" +//
                             "email=" + URLEncoder.encode(StringUtils.defaultString(fetchUser.getEmail(), ""), "UTF-8") + "&" +//
                             "primary=" + URLEncoder.encode(StringUtils.defaultString(primaryUid, ""), "UTF-8");
        response.sendRedirect(redirectUrl);
        return "needMore.";
    }

    protected Object redirectToHome(HttpServletRequest request, HttpServletResponse response, LoginMO login) throws IOException {
        int cookieAge = Math.max(JwtService.minLoginExpireSec, this.rdpConfig.getLoginExpireTimeSec());
        Cookie cookie = RdpWebUtils.newCookie(JwtService.jwtTokenName, login.getToken(), false, cookieAge);

        response.addCookie(cookie);
        if (StringUtils.isNotBlank(this.rdpConfig.getDeployContextPath())) {
            response.sendRedirect(this.rdpConfig.getDeployContextPath());
        } else {
            response.sendRedirect("/");
        }

        return "ok.";
    }

    @RequestMapping(value = "/logout", method = { RequestMethod.POST, RequestMethod.GET })
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @SneakyThrows
    public Object logout(@RequestParam(required = false) Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        String uid = params.getOrDefault("uid", null);
        String state = params.getOrDefault("state", null);
        String provider = params.getOrDefault("provider", null);

        // for args
        DmCsrfTokenDO tokenDO = this.csrfTokenService.pullToken(state);
        if (tokenDO == null) {
            return this.redirectOrDone(request, response, "/");
        }

        response.addCookie(RdpWebUtils.newCookie("jwt_token", StringUtils.EMPTY, true, 0));
        return this.redirectOrDone(request, response, "/");
    }

    private Object redirectOrDone(HttpServletRequest request, HttpServletResponse response, String redirectUrl) throws IOException {
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            return ResWebDataUtils.buildSuccess();
        } else {
            response.sendRedirect(redirectUrl);
            return "ok";
        }
    }
}

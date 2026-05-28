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
package com.clougence.clouddm.console.web.global.jwtsession;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.web.util.WebUtils;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpLocal;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtManager {

    public final static String     CSRF_TOKEN_NAME        = "csrf-token";
    @Resource
    private DmConsoleConfig        rdpConfig;
    @Resource
    private JwtService             jwtService;
    @Resource
    private RdpUserService         rdpUserService;
    @Resource
    private DmAuthServiceForManage rdpAuthServiceForManage;
    @Resource
    private RdpRoleService         rdpRoleService;

    private final Set<String>      ignoreEndWithUrl       = new HashSet<>();
    private final Set<String>      includeVerifyStartWith = new HashSet<>();

    @PostConstruct
    public void init() throws Exception {
        this.configUrl(this.includeVerifyStartWith, this.ignoreEndWithUrl);
    }

    protected void configUrl(Set<String> includeVerifyStartWith, Set<String> ignoreEndWithUrl) {
        // RDP
        includeVerifyStartWith.add(RdpControllerUrlPrefix.CONSOLE_PREFIX);
        includeVerifyStartWith.add("/logout");
        includeVerifyStartWith.add("/switchSaasMode");

        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/user/resetpasswd");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/verify/sendcode");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/constant/sso_type");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/onlinecheckauthcode");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/checkpayresult");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/stripePayResult");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/user/recordandreturndownloadurlbydm");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/marketplace/notifications/handle");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/alipayLicenseCallback");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/alipayPrepayCallback");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/stripeLicenseCallback");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/stripePrepayCallback");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/authcode/order/autoapplylicense");
        ignoreEndWithUrl.add(RdpControllerUrlPrefix.CONSOLE_PREFIX + "/saas/queryPriceMeta");

        // DM
        includeVerifyStartWith.add(DmControllerUrlPrefix.CONSOLE_PREFIX);
        ignoreEndWithUrl.add(DmControllerUrlPrefix.CONSOLE_PREFIX + "/version");
        ignoreEndWithUrl.add(DmControllerUrlPrefix.CONSOLE_PREFIX + "/dm_global_settings");
    }

    private boolean ignoreVerify(HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (!this.includeVerifyStartWith.isEmpty()) {
            boolean includeTest = false;
            for (String include : this.includeVerifyStartWith) {
                includeTest = includeTest || StringUtils.startsWithIgnoreCase(uri, include);
            }
            if (!includeTest) {
                return true; // Do not include or ignore
            }
        }

        for (String ignore : this.ignoreEndWithUrl) {
            if (StringUtils.endsWithIgnoreCase(uri, ignore)) {
                return true;
            }
        }

        return false;
    }

    private boolean verifyAuth(HttpServletRequest request, RequestAuth requestAuth, DmAuthUserDO userDO) {
        if (userDO.isMaintainer() || userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return true;
        }

        if (requestAuth != null) {
            return testAuth(requestAuth, userDO.getRoleId());
        } else {
            return false;
        }
    }

    private boolean testAuth(RequestAuth authInfo, Long roleId) {
        if (authInfo == null) {
            return false;
        }

        if (authInfo.strategy() == RequestAuth.AuthStrategy.Ignore) {
            return true;
        }

        DmAuthRoleDO roleDO = this.rdpRoleService.fetchRoleById(roleId);
        if (roleDO == null) {
            return false;
        }

        if (authInfo.strategy() == RequestAuth.AuthStrategy.RefAnyOnes) {
            return true;
        }

        if (authInfo.strategy() == RequestAuth.AuthStrategy.RefRoleSet) {
            for (String labelKey : authInfo.value()) {
                if (roleDO.getRoleAuthLabels() != null && roleDO.getRoleAuthLabels().contains(labelKey)) {
                    return true;
                }
            }
        }

        return false;
    }

    private String[] fetchAuthLabel(Supplier<RequestAuth> authSupplier) {
        RequestAuth requestAuth = authSupplier.get();
        if (requestAuth != null) {
            if (requestAuth.strategy() == RequestAuth.AuthStrategy.Ignore || requestAuth.strategy() == RequestAuth.AuthStrategy.RefAnyOnes) {
                return new String[] { requestAuth.strategy().name() };
            }

            if (requestAuth.strategy() == RequestAuth.AuthStrategy.RefRoleSet) {
                return requestAuth.value();
            }
        }

        return null;
    }

    public JwtCheckResult preHandle(HttpServletRequest request, HttpServletResponse response, Supplier<RequestAuth> authSupplier, Object handler) {
        RequestAuth requestAuth = authSupplier.get();
        if (requestAuth != null) {
            RdpWebUtils.currentLocal().setAuthLabel(requestAuth.value());
        }

        String locale = request.getParameter("locale");
        if (StringUtils.isNotBlank(locale)) {
            RdpLocal.setLocal(Locale.forLanguageTag(locale));
        } else {
            RdpLocal.setLocal(request.getLocale());
        }

        if (request.getMethod().equals("OPTIONS")) {
            return responseOk();
        }

        if (ignoreVerify(request)) {
            return responseOk();
        }

        if (beforeVerifyJwt(request, handler)) {
            return responseOk();
        }

        // isLogin
        DecodedJWT jwt = this.jwtService.verify(request);
        if (jwt == null) {
            //            boolean canBeIgnore = requestAuth != null && requestAuth.strategy() == RequestAuth.AuthStrategy.Ignore;
            //            if (canBeIgnore) {
            //                return responseOk();
            //            } else {
            return responseNotLogin(requestAuth, request, response, "NotLogin.");
            //            }
        }

        this.jwtService.refreshCookiePeriodOfValidity(request, response);

        String uid = jwt.getId();
        if (StringUtils.isBlank(uid)) {
            String errorMessage = "Uid is blank,illegal login token.";
            log.error(errorMessage);
            return responseNotLogin(requestAuth, request, response, errorMessage);
        }

        if (this.rdpConfig.getActiveCsrfCheck()) {
            boolean isCsrfVerifySuccess = verifyCsrfToken(request, jwt.getToken());
            if (!isCsrfVerifySuccess) {
                String errorMessage = "Csrf verify failed. Maybe there have csrf attacks. Received request url is " + //
                                      request.getRequestURI() + ". Receive csrf token is:" + request.getHeader(CSRF_TOKEN_NAME) + ", jwt token is:" + jwt.getToken();
                log.error(errorMessage);
                return responseNotLogin(requestAuth, request, response, errorMessage);
            }
        }

        DmAuthUserDO userDO = this.rdpUserService.getUserByUid(uid);
        if (userDO == null) {
            String errorMessage = "user (" + uid + ") not exist.";
            return responseNotLogin(requestAuth, request, response, errorMessage);
        }
        if (userDO.isDisable()) {
            String errorMessage = "user (" + uid + ") is disabled.";
            return JwtCheckResult.builder()//
                .success(false)
                .message(errorMessage)
                .errorCode(401)
                .build();
        }

        Claim akClaim = jwt.getClaim(RdpUserService.ACCESSKEY);
        String ak = akClaim.asString();
        if (ak == null || !ak.equals(userDO.getAccessKey())) {
            String errorMessage = "user (" + uid + ") ak is not valid.";
            return responseNotLogin(requestAuth, request, response, errorMessage);
        }

        if (userDO.getRoleId() == null) {
            String errorMessage = uid + " empty.check user data is valid";
            log.error(errorMessage);
            return responseSystemError(errorMessage);
        }

        if (requestAuth != null && requestAuth.checkOpPassword() && this.rdpConfig.isOppassword()) {
            if (StringUtils.isBlank(userDO.getOpPassword())) {
                String errorMessage = "operate password not set. ";
                log.error(errorMessage);
                return responseNotSetOpPwd(errorMessage);
            } else {
                DecodedJWT opJwt = this.jwtService.verifyOpToken(request);
                if (opJwt == null) {
                    String errorMessage = "operate password cache invalid. ";
                    log.error(errorMessage);
                    return responseOpPwdInvalid(errorMessage);
                }
            }
        }
        // refresh token period of validity
        this.jwtService.refreshJwtTokenPeriodOfValidity(request, response, userDO);

        // check url authority
        if (verifyAuth(request, requestAuth, userDO)) {
            request.setAttribute(RdpUserService.UID, uid);
            request.setAttribute(RdpUserService.USER_ROLE, userDO.getRoleId());
            request.setAttribute(RdpUserService.IS_MAINTAINER, userDO.isMaintainer());

            DmAuthUserDO primaryUser;
            if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                request.setAttribute(RdpUserService.PUID, uid);
                primaryUser = userDO;
            } else {
                primaryUser = this.rdpUserService.getUserById(userDO.getParentId());
                request.setAttribute(RdpUserService.PUID, primaryUser.getUid());
            }

            RdpWebUtils.currentLocal().setCurrentUser(userDO);
            RdpWebUtils.currentLocal().setCurrentRole(userDO.getRoleId() == null ? null : this.rdpRoleService.fetchRoleById(userDO.getRoleId()));
            RdpWebUtils.currentLocal().setPrimaryUser(primaryUser);
            return responseOk();
        } else {
            String[] auths = fetchAuthLabel(authSupplier);
            StringBuilder authStrB = new StringBuilder();
            if (auths != null) {
                boolean first = true;
                for (String auth : auths) {
                    if (first) {
                        first = false;
                    } else {
                        authStrB.append(",");
                    }

                    AuthInfo authLabel = this.rdpAuthServiceForManage.getAuthLabel(auth);
                    if (authLabel != null) {
                        authStrB.append(DmI18nUtils.getMessage(authLabel.getKeyI18n()));
                    } else {
                        authStrB.append(auth);
                    }
                }
            }

            return responseNoPageAuthority(DmI18nUtils.getMessage(I18nRdpMsgKeys.AUTH_NO_AUTH_ERROR.name(), authStrB));
        }
    }

    private boolean verifyCsrfToken(HttpServletRequest request, @Nonnull String jwtToken) {
        Cookie coolieValue = WebUtils.getCookie(request, CSRF_TOKEN_NAME);
        String headerValue = request.getHeader(CSRF_TOKEN_NAME);
        String requestParameterValue = request.getParameter(CSRF_TOKEN_NAME);
        String csrfToken = "";
        if (coolieValue != null) {
            jwtToken = coolieValue.getValue();
        } else if (headerValue != null) {
            jwtToken = headerValue;
        } else if (requestParameterValue != null) {
            jwtToken = requestParameterValue;
        }

        if (StringUtils.isEmpty(csrfToken)) {
            return false;
        }
        return csrfToken.equals(jwtToken);
    }

    protected boolean beforeVerifyJwt(HttpServletRequest request, Object handler) {
        return false;
    }

    private JwtCheckResult responseOk() {
        return JwtCheckResult.builder()//
            .success(true)
            .message("ok.")
            .errorCode(200)
            .build();
    }

    @SneakyThrows
    private JwtCheckResult responseNotLogin(RequestAuth requestAuth, HttpServletRequest request, HttpServletResponse response, String message) {
        if (requestAuth != null && StringUtils.isNotBlank(requestAuth.failedRedirectUrlTo())) {
            response.sendRedirect(requestAuth.failedRedirectUrlTo());
        }

        return JwtCheckResult.builder()//
            .success(false)
            .message(message)
            .errorCode(401)
            .build();
    }

    private JwtCheckResult responseOpPwdInvalid(String message) {
        return JwtCheckResult.builder()//
            .success(false)
            .message(message)
            .errorCode(499)
            .build();
    }

    private JwtCheckResult responseNotSetOpPwd(String message) {
        return JwtCheckResult.builder()//
            .success(false)
            .message(message)
            .errorCode(498)
            .build();
    }

    private JwtCheckResult responseNoPageAuthority(String message) {
        return JwtCheckResult.builder()//
            .success(false)
            .message(message)
            .errorCode(406)
            .build();
    }

    private JwtCheckResult responseSystemError(String message) {
        return JwtCheckResult.builder()//
            .success(false)
            .message(message)
            .errorCode(500)
            .build();
    }
}

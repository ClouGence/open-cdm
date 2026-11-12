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
package com.clougence.clouddm.console.web.global.session;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.util.RdpLocal;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.utils.StringUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSessionManager {

    @Resource
    private ConsoleConfig            config;
    @Resource
    private RdpUserService           userService;
    @Resource
    private DmAuthServiceForManage   authServiceForManage;
    @Resource
    private RdpRoleService           roleService;
    @Resource
    private SessionService           sessionService;
    @Resource
    private SessionAccountPolicy     accountPolicy;
    @Resource
    private SessionCookieService     cookieService;

    public static final String       SESSION_PRINCIPAL = "DM_SESSION_PRINCIPAL";

    private final Set<PathPattern>   publicPaths       = new HashSet<>();
    private final Set<PathPattern>   protectedPaths    = new HashSet<>();
    private static final PathPattern LOGIN_USER_PATH   = PathPatternParser.defaultInstance.parse(DmControllerUrlPrefix.CONSOLE_PREFIX + "/user/queryLoginUser");

    @PostConstruct
    public void init() throws Exception {
        protectedPaths.add(PathPatternParser.defaultInstance.parse(DmControllerUrlPrefix.CONSOLE_PREFIX + "/**"));
        protectedPaths.add(PathPatternParser.defaultInstance.parse("/switchSaasMode/**"));

        publicPaths.add(PathPatternParser.defaultInstance.parse(DmControllerUrlPrefix.CONSOLE_PREFIX + "/verify/sendcode"));
        publicPaths.add(PathPatternParser.defaultInstance.parse(DmControllerUrlPrefix.CONSOLE_PREFIX + "/dmGlobalSettings"));
        publicPaths.add(PathPatternParser.defaultInstance.parse(DmControllerUrlPrefix.CONSOLE_PREFIX + "/resource/fetch"));
    }

    private boolean ignoreVerify(HttpServletRequest request) {
        PathContainer path = RdpWebUtils.getRequestPath(request);
        for (PathPattern publicPath : publicPaths) {
            if (publicPath.matches(path)) {
                return true;
            }
        }
        for (PathPattern protectedPath : protectedPaths) {
            if (protectedPath.matches(path)) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyAuth(RequestAuth requestAuth, DmAuthUserDO userDO, DmAuthRoleDO role) {
        if (userDO.isMaintainer() || userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return true;
        }

        if (requestAuth == null) {
            return false;
        }

        // role is non-null here: preHandle rejects a missing role before authorization
        if (requestAuth.strategy() == RequestAuth.AuthStrategy.Ignore || requestAuth.strategy() == RequestAuth.AuthStrategy.RefAnyOnes) {
            return true;
        }

        if (requestAuth.strategy() == RequestAuth.AuthStrategy.RefRoleSet) {
            for (String labelKey : requestAuth.value()) {
                if (role.getRoleAuthLabels() != null && role.getRoleAuthLabels().contains(labelKey)) {
                    return true;
                }
            }
        }

        return false;
    }

    private String[] fetchAuthLabel(RequestAuth requestAuth) {
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

    public SessionCheckResult preHandle(HttpServletRequest request, HttpServletResponse response, RequestAuth requestAuth) {
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

        // isLogin
        SessionPrincipal principal = this.sessionService.authenticate(request);
        if (principal == null) {
            return responseNotLogin(requestAuth, request, response, "NotLogin.");
        }

        String uid = principal.getUid();
        if (StringUtils.isBlank(uid)) {
            String errorMessage = "Uid is blank,illegal login token.";
            log.error(errorMessage);
            return responseNotLogin(requestAuth, request, response, errorMessage);
        }

        // CSRF check stays failure-closed: enabled config has no working verifier
        if (this.config.getActiveCsrfCheck()) {
            String errorMessage = "Csrf verify failed.";
            log.error(errorMessage);
            return responseNotLogin(requestAuth, request, response, errorMessage);
        }

        DmAuthUserDO userDO = this.userService.getUserByUid(uid);
        if (userDO == null) {
            String errorMessage = "user (" + uid + ") not exist.";
            return responseNotLogin(requestAuth, request, response, errorMessage);
        }
        if (userDO.isDisable()) {
            String errorMessage = "user (" + uid + ") is disabled.";
            return new SessionCheckResult(false, errorMessage, 401);
        }

        boolean passwordExpired = accountPolicy.isPasswordExpired(userDO);
        if (passwordExpired) {
            sessionService.revokeUserSessions(uid);
            if (!LOGIN_USER_PATH.matches(RdpWebUtils.getRequestPath(request))) {
                return responseNotLogin(requestAuth, request, response, "Password expired.");
            }
            cookieService.clear(response);
        }

        if (userDO.getRoleId() == null) {
            String errorMessage = uid + " empty.check user data is valid";
            log.error(errorMessage);
            return responseSystemError(errorMessage);
        }

        // check url authority
        DmAuthRoleDO role = this.roleService.fetchRoleById(userDO.getRoleId());
        if (role == null) {
            return responseSystemError("Account role is unavailable.");
        }
        if (verifyAuth(requestAuth, userDO, role)) {
            request.setAttribute(SESSION_PRINCIPAL, principal);
            request.setAttribute(RdpUserService.UID, uid);
            request.setAttribute(RdpUserService.USER_ROLE, userDO.getRoleId());
            request.setAttribute(RdpUserService.IS_MAINTAINER, userDO.isMaintainer());

            DmAuthUserDO primaryUser;
            if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                request.setAttribute(RdpUserService.PUID, uid);
                primaryUser = userDO;
            } else {
                primaryUser = this.userService.getUserById(userDO.getParentId());
                if (primaryUser == null) {
                    return responseNotLogin(requestAuth, request, response, "Primary account is unavailable.");
                }
                request.setAttribute(RdpUserService.PUID, primaryUser.getUid());
            }

            RdpWebUtils.currentLocal().setCurrentUser(userDO);
            RdpWebUtils.currentLocal().setCurrentRole(role);
            RdpWebUtils.currentLocal().setPrimaryUser(primaryUser);
            if (!passwordExpired && !"websocket".equalsIgnoreCase(request.getHeader("Upgrade"))) {
                this.sessionService.renewIfNeeded(principal);
                this.cookieService.refreshIfUnlimited(request, response);
            }
            return responseOk();
        } else {
            String[] auths = fetchAuthLabel(requestAuth);
            StringBuilder authStrB = new StringBuilder();
            if (auths != null) {
                boolean first = true;
                for (String auth : auths) {
                    if (first) {
                        first = false;
                    } else {
                        authStrB.append(",");
                    }

                    AuthInfo authLabel = this.authServiceForManage.getAuthLabel(auth);
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

    private SessionCheckResult responseOk() {
        return new SessionCheckResult(true, "ok.", 200);
    }

    private SessionCheckResult responseNotLogin(RequestAuth requestAuth, HttpServletRequest request, HttpServletResponse response, String message) {
        if (requestAuth != null && StringUtils.isNotBlank(requestAuth.failedRedirectUrlTo())) {
            try {
                response.sendRedirect(requestAuth.failedRedirectUrlTo());
            } catch (IOException e) {
                log.error("response redirect failed", e);
                return responseSystemError(message);
            }
        }

        return new SessionCheckResult(false, message, 401);
    }

    private SessionCheckResult responseNoPageAuthority(String message) {
        return new SessionCheckResult(false, message, 406);
    }

    private SessionCheckResult responseSystemError(String message) {
        return new SessionCheckResult(false, message, 500);
    }
}

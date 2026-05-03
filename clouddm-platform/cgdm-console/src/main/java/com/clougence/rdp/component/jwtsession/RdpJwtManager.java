package com.clougence.rdp.component.jwtsession;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.WebUtils;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.model.RdpRoleDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.rdp.service.RdpAuthServiceForManage;
import com.clougence.rdp.service.RdpRoleService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.rdp.util.RdpLocal;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RdpJwtManager {

    public final static String      CSRF_TOKEN_NAME        = "csrf-token";

    @Resource
    private RdpConsoleConfig        rdpConfig;

    @Resource
    private RdpJwtService           rdpJwtService;

    @Resource
    private RdpUserService          rdpUserService;

    @Resource
    private RdpAuthServiceForManage rdpAuthServiceForManage;

    @Resource
    private RdpRoleService          rdpRoleService;

    private final Set<String>       ignoreEndWithUrl       = new HashSet<>();
    private final Set<String>       includeVerifyStartWith = new HashSet<>();

    @PostConstruct
    public void init() throws Exception {
        this.configUrl(this.includeVerifyStartWith, this.ignoreEndWithUrl);
    }

    protected void configUrl(Set<String> includeVerifyStartWith, Set<String> ignoreEndWithUrl) {

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

    private boolean verifyAuth(HttpServletRequest request, RequestAuth requestAuth, RdpUserDO userDO) {
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

        RdpRoleDO roleDO = this.rdpRoleService.fetchRoleById(roleId);
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

    public RdpJwtCheckResult preHandle(HttpServletRequest request, HttpServletResponse response, Supplier<RequestAuth> authSupplier, Object handler) {
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
        DecodedJWT jwt = this.rdpJwtService.verify(request);
        if (jwt == null) {
            //            boolean canBeIgnore = requestAuth != null && requestAuth.strategy() == RequestAuth.AuthStrategy.Ignore;
            //            if (canBeIgnore) {
            //                return responseOk();
            //            } else {
            return responseNotLogin(requestAuth, request, response, "NotLogin.");
            //            }
        }

        this.rdpJwtService.refreshCookiePeriodOfValidity(request, response);

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

        RdpUserDO userDO = this.rdpUserService.getUserByUid(uid);
        if (userDO == null) {
            String errorMessage = "user (" + uid + ") not exist.";
            return responseNotLogin(requestAuth, request, response, errorMessage);
        }
        if (userDO.isDisable()) {
            String errorMessage = "user (" + uid + ") is disabled.";
            return RdpJwtCheckResult.builder()//
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
                DecodedJWT opJwt = this.rdpJwtService.verifyOpToken(request);
                if (opJwt == null) {
                    String errorMessage = "operate password cache invalid. ";
                    log.error(errorMessage);
                    return responseOpPwdInvalid(errorMessage);
                }
            }
        }
        // refresh token period of validity
        this.rdpJwtService.refreshJwtTokenPeriodOfValidity(request, response, userDO);

        // check url authority
        if (verifyAuth(request, requestAuth, userDO)) {
            request.setAttribute(RdpUserService.UID, uid);
            request.setAttribute(RdpUserService.USER_ROLE, userDO.getRoleId());
            request.setAttribute(RdpUserService.IS_MAINTAINER, userDO.isMaintainer());

            RdpUserDO primaryUser;
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
                        authStrB.append(RdpI18nUtils.getMessage(authLabel.getKeyI18n()));
                    } else {
                        authStrB.append(auth);
                    }
                }
            }

            return responseNoPageAuthority(RdpI18nUtils.getMessage(I18nRdpMsgKeys.AUTH_NO_AUTH_ERROR.name(), authStrB));
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

    private RdpJwtCheckResult responseOk() {
        return RdpJwtCheckResult.builder()//
            .success(true)
            .message("ok.")
            .errorCode(200)
            .build();
    }

    @SneakyThrows
    private RdpJwtCheckResult responseNotLogin(RequestAuth requestAuth, HttpServletRequest request, HttpServletResponse response, String message) {
        if (requestAuth != null && StringUtils.isNotBlank(requestAuth.failedRedirectUrlTo())) {
            response.sendRedirect(requestAuth.failedRedirectUrlTo());
        }

        return RdpJwtCheckResult.builder()//
            .success(false)
            .message(message)
            .errorCode(401)
            .build();
    }

    private RdpJwtCheckResult responseOpPwdInvalid(String message) {
        return RdpJwtCheckResult.builder()//
            .success(false)
            .message(message)
            .errorCode(499)
            .build();
    }

    private RdpJwtCheckResult responseNotSetOpPwd(String message) {
        return RdpJwtCheckResult.builder()//
            .success(false)
            .message(message)
            .errorCode(498)
            .build();
    }

    private RdpJwtCheckResult responseNoPageAuthority(String message) {
        return RdpJwtCheckResult.builder()//
            .success(false)
            .message(message)
            .errorCode(406)
            .build();
    }

    private RdpJwtCheckResult responseSystemError(String message) {
        return RdpJwtCheckResult.builder()//
            .success(false)
            .message(message)
            .errorCode(500)
            .build();
    }
}

package com.clougence.rdp.component.jwtsession;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.model.RdpRoleDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class RdpWebUtils {

    private static RdpConsoleConfig               rdpConfig;
    private static final ThreadLocal<RequestData> localData = ThreadLocal.withInitial(RequestData::new);

    public static void initUtils(RdpConsoleConfig rdpConfig) {
        if (RdpWebUtils.rdpConfig == null) {
            RdpWebUtils.rdpConfig = rdpConfig;
        }
    }

    static void initLocal(RdpConsoleConfig rdpConfig, HttpServletRequest request) {
        initUtils(rdpConfig);
        localData.set(initData(rdpConfig, request));
    }

    static void cleanLocal() {
        localData.set(new RequestData());
    }

    static RequestData currentLocal() {
        return localData.get();
    }

    private static RequestData initData(RdpConsoleConfig rdpConfig, HttpServletRequest request) {
        RequestData data = new RequestData();
        data.productCode = request.getHeader("X-Product-Code");
        data.setRequest(true);
        data.requestUri = request.getRequestURI();
        data.requestContextPath = rdpConfig.getDeployContextPath();
        if (StringUtils.isBlank(data.requestContextPath)) {
            data.requestContextPath = request.getScheme() + "://" + request.getHeader("host") + "/";
        } else if (!StringUtils.endsWith(data.requestContextPath, "/")) {
            data.requestContextPath += "/";
        }
        return data;
    }

    @Getter
    @Setter
    protected static class RequestData {

        private String    productCode;
        private String[]  authLabel;
        private RdpUserDO currentUser;
        private RdpRoleDO currentRole;
        private RdpUserDO primaryUser;
        private boolean   request;
        private String    requestUri;
        private String    requestContextPath;
    }

    //

    public static boolean isRequest() {
        RequestData data = currentLocal();
        if (data != null) {
            return data.request;
        }
        return false;
    }

    private static void checkInRequest() {
        if (!isRequest()) {
            throw new ErrorMessageException("context call is not request.");
        }
    }

    public static Long getCurrentRoleId() {
        checkInRequest();
        RequestData data = currentLocal();
        if (data != null && data.currentRole != null) {
            return data.currentRole.getId();
        }
        return null;
    }

    public static String getCurrentUid() {
        checkInRequest();
        RequestData data = currentLocal();
        if (data != null && data.currentUser != null) {
            return data.currentUser.getUid();
        }
        return null;
    }

    public static String getCurrentUserName() {
        checkInRequest();
        RequestData data = currentLocal();
        if (data != null && data.currentUser != null) {
            return data.currentUser.getUsername();
        }
        return null;
    }

    public static List<String> getCurrentUserAuthLabel() {
        checkInRequest();
        RequestData data = currentLocal();
        if (data != null && data.currentRole != null) {
            return Collections.unmodifiableList(data.currentRole.getRoleAuthLabels());
        }
        return null;
    }

    public static List<String> getRequestAuthLabel() {
        checkInRequest();
        RequestData data = currentLocal();
        if (data != null && data.currentRole != null) {
            return Collections.unmodifiableList(Arrays.asList(data.authLabel));
        }
        return null;
    }

    public static String getPrimaryUid() {
        checkInRequest();
        RequestData data = currentLocal();
        if (data != null && data.primaryUser != null) {
            return data.primaryUser.getUid();
        }
        return null;
    }

    public static boolean isPrimary() {
        checkInRequest();
        RequestData data = currentLocal();
        if (data != null && data.currentUser != null) {
            return data.currentUser.getAccountType() == AccountType.PRIMARY_ACCOUNT;
        }
        return false;
    }

    public static boolean isMaintainer() {
        checkInRequest();
        RequestData data = currentLocal();
        if (data != null && data.currentUser != null) {
            return data.currentUser.isMaintainer();
        }
        return false;
    }

    public static String getContextPath() {
        checkInRequest();
        RequestData data = currentLocal();
        if (data != null) {
            return data.requestContextPath;
        }
        return null;
    }

    @SneakyThrows
    public static Cookie newCookie(String name, String value, boolean httpOnly, int expiry) {
        checkInRequest();
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(expiry);

        // cookieDomain, first use LoginCookieDomain,second use DeployContextPath.
        String cookieDomain = rdpConfig.getLoginCookieDomain();
        if (StringUtils.isBlank(cookieDomain) && StringUtils.isNotBlank(rdpConfig.getDeployContextPath())) {
            URL contextURL = new URL(rdpConfig.getDeployContextPath());
            cookieDomain = contextURL.getHost();
        }

        if (StringUtils.isNotBlank(rdpConfig.getDeployContextPath())) {
            URL contextURL = new URL(rdpConfig.getDeployContextPath());
            cookie.setDomain(cookieDomain);
            cookie.setPath(StringUtils.isBlank(contextURL.getPath()) ? "/" : contextURL.getPath());
        } else {
            if (StringUtils.isNotBlank(cookieDomain)) {
                cookie.setDomain(cookieDomain);
            }
            cookie.setPath("/");
        }
        return cookie;
    }
}

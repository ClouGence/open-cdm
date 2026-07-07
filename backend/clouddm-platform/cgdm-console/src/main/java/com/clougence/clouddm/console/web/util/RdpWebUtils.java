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
package com.clougence.clouddm.console.web.util;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.StringUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class RdpWebUtils {

    private static ConsoleConfig                  config;
    private static final ThreadLocal<RequestData> localData = ThreadLocal.withInitial(RequestData::new);

    public static void initUtils(ConsoleConfig config) {
        if (RdpWebUtils.config == null) {
            RdpWebUtils.config = config;
        }
    }

    public static void initLocal(ConsoleConfig config, HttpServletRequest request) {
        initUtils(config);
        localData.set(initData(config, request));
    }

    public static void cleanLocal() {
        localData.set(new RequestData());
    }

    public static RequestData currentLocal() {
        return localData.get();
    }

    private static RequestData initData(ConsoleConfig config, HttpServletRequest request) {
        RequestData data = new RequestData();
        data.setRequest(true);
        data.requestUri = request.getRequestURI();
        data.requestContextPath = config.getDeployContextPath();
        if (StringUtils.isBlank(data.requestContextPath)) {
            data.requestContextPath = request.getScheme() + "://" + request.getHeader("host") + "/";
        } else if (!StringUtils.endsWith(data.requestContextPath, "/")) {
            data.requestContextPath += "/";
        }
        return data;
    }

    @Getter
    @Setter
    public static class RequestData {

        private String[]     authLabel;
        private DmAuthUserDO currentUser;
        private DmAuthRoleDO currentRole;
        private DmAuthUserDO primaryUser;
        private boolean      request;
        private String       requestUri;
        private String       requestContextPath;
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
        String cookieDomain = config.getLoginCookieDomain();
        if (StringUtils.isBlank(cookieDomain) && StringUtils.isNotBlank(config.getDeployContextPath())) {
            URL contextURL = new URL(config.getDeployContextPath());
            cookieDomain = contextURL.getHost();
        }

        if (StringUtils.isNotBlank(config.getDeployContextPath())) {
            URL contextURL = new URL(config.getDeployContextPath());
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

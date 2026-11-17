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

import java.net.URI;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.utils.StringUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Single cookie scope/deletion policy for the session cookie. Never mutates
 * request cookies.
 */
@Slf4j
@Service
public class SessionCookieService {

    public static final String SESSION_COOKIE_NAME = "dm_session_token";

    @Resource
    private ConsoleConfig      consoleConfig;
    @Resource
    private SessionConfig      sessionConfig;

    private String             cookiePath;
    private String             cookieDomain;
    private boolean            secure;

    @PostConstruct
    public void init() {
        this.cookieDomain = consoleConfig.getLoginCookieDomain();

        String deploy = consoleConfig.getDeployContextPath();
        if (StringUtils.isBlank(deploy)) {
            this.cookiePath = "/";
        } else {
            URI u = URI.create(deploy);
            this.cookiePath = normalizePath(u.getPath());
            this.secure = "https".equalsIgnoreCase(u.getScheme());
        }
        if (this.cookiePath == null) {
            throw new IllegalStateException("invalid cookie path derived from clougence.rdp.deploy.context-path: " + deploy);
        }
        // fail fast on an invalid domain/path before the first request writes headers
        scoped("").build();
    }

    private static String normalizePath(String path) {
        if (StringUtils.isBlank(path) || "/".equals(path)) {
            return "/";
        }
        if (!path.startsWith("/")) {
            return null;
        }
        String p = path;
        while (p.length() > 1 && p.endsWith("/")) {
            p = p.substring(0, p.length() - 1);
        }
        return p;
    }

    public void write(HttpServletResponse response, String token) {
        int maxAge = sessionConfig.getAbsoluteLifetimeSec();
        if (maxAge == 0) {
            maxAge = Integer.MAX_VALUE;
        }
        emit(response, token, maxAge);
    }

    public void refreshIfUnlimited(HttpServletRequest request, HttpServletResponse response) {
        // Browsers cap persistent cookie lifetimes, so refresh the stable token's expiry.
        if (sessionConfig.getAbsoluteLifetimeSec() == 0) {
            emit(response, extractToken(request), Integer.MAX_VALUE);
        }
    }

    public void clear(HttpServletResponse response) {
        // identical scope/flags as issuance so the browser actually drops it
        emit(response, "", 0);
    }

    private void emit(HttpServletResponse response, String value, int maxAge) {
        response.addHeader("Set-Cookie", scoped(value).maxAge(maxAge).build().toString());
    }

    private ResponseCookie.ResponseCookieBuilder scoped(String value) {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from(SESSION_COOKIE_NAME, value).path(cookiePath).httpOnly(true).sameSite("Lax");
        if (StringUtils.isNotBlank(cookieDomain)) {
            b.domain(cookieDomain);
        }
        if (secure) {
            b.secure(true);
        }
        return b;
    }

    /**
     * Cookie-only token extraction: exact 43-char URL-safe format; rejects
     * duplicate same-name cookies with differing values.
     */
    public static String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        String value = null;
        for (Cookie cookie : cookies) {
            if (!SESSION_COOKIE_NAME.equals(cookie.getName())) {
                continue;
            }
            String v = cookie.getValue();
            if (v == null || !SessionTokens.TOKEN_PATTERN.matcher(v).matches()) {
                return null;
            }
            if (value != null && !value.equals(v)) {
                return null;
            }
            value = v;
        }
        return value;
    }
}

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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

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
 * Single cookie scope/deletion policy for the session cookie and legacy JWT
 * tombstones. Never mutates request cookies.
 */
@Slf4j
@Service
public class SessionCookieService {

    public static final String   SESSION_COOKIE_NAME = "dm_session_token";
    private static final String  LEGACY_JWT_COOKIE   = "dm_jwt_token";
    private static final String  LEGACY_OP_COOKIE    = "dm_op_pwd_token";
    private static final Pattern TOKEN_FORMAT        = Pattern.compile("^[A-Za-z0-9_-]{43}$");
    private static final Pattern INVALID_SCOPE_CHAR  = Pattern.compile("[\\x00-\\x1f\\x7f;]");

    @Resource
    private ConsoleConfig        consoleConfig;
    @Resource
    private SessionConfig        sessionConfig;

    private String               cookiePath;
    private String               cookieDomain;
    private String               legacyPublicHost;
    private boolean              secure;

    @PostConstruct
    public void init() {
        this.cookieDomain = consoleConfig.getLoginCookieDomain();
        if (StringUtils.isNotBlank(cookieDomain) && !isValidDomain(cookieDomain)) {
            throw new IllegalStateException("invalid clougence.rdp.login.cookie.domain: " + cookieDomain);
        }

        String deploy = consoleConfig.getDeployContextPath();
        if (StringUtils.isBlank(deploy)) {
            this.cookiePath = "/";
        } else if (deploy.contains("://")) {
            URL url;
            try {
                url = new URL(deploy);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("invalid clougence.rdp.deploy.context-path: " + deploy, e);
            }
            String protocol = url.getProtocol();
            if (!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
                throw new IllegalStateException("clougence.rdp.deploy.context-path must be http(s): " + deploy);
            }
            if (StringUtils.isBlank(url.getHost()) || url.getQuery() != null || url.getRef() != null || url.getUserInfo() != null) {
                throw new IllegalStateException("invalid clougence.rdp.deploy.context-path: " + deploy);
            }
            this.cookiePath = normalizePath(url.getPath());
            this.secure = "https".equalsIgnoreCase(protocol);
            this.legacyPublicHost = url.getHost();
        } else {
            if (deploy.contains("?") || deploy.contains("#")) {
                throw new IllegalStateException("invalid clougence.rdp.deploy.context-path: " + deploy);
            }
            this.cookiePath = normalizePath(deploy);
        }
        if (this.cookiePath == null || INVALID_SCOPE_CHAR.matcher(cookiePath).find()) {
            throw new IllegalStateException("invalid cookie path derived from clougence.rdp.deploy.context-path: " + deploy);
        }
        // fail fast on an invalid domain/path before the first request writes headers
        scoped(SESSION_COOKIE_NAME, "").build();
    }

    private static boolean isValidDomain(String domain) {
        return !INVALID_SCOPE_CHAR.matcher(domain).find() && !domain.contains("/") && !domain.contains(":");
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

    public void write(HttpServletResponse response, IssuedSession issued) {
        ResponseCookie.ResponseCookieBuilder b = scoped(SESSION_COOKIE_NAME, issued.getToken());
        b.maxAge(sessionConfig.getAbsoluteLifetimeSec());
        response.addHeader("Set-Cookie", b.build().toString());
    }

    public void clear(HttpServletResponse response) {
        // identical scope/flags as issuance so the browser actually drops it
        ResponseCookie.ResponseCookieBuilder b = scoped(SESSION_COOKIE_NAME, "");
        b.maxAge(0);
        response.addHeader("Set-Cookie", b.build().toString());
    }

    /** Best-effort tombstones for legacy JWT cookies across their plausible scopes. */
    public void clearLegacy(HttpServletResponse response) {
        Set<String> domains = new LinkedHashSet<>();
        domains.add(null);
        if (StringUtils.isNotBlank(cookieDomain)) {
            domains.add(cookieDomain);
        }
        if (StringUtils.isNotBlank(legacyPublicHost) && !legacyPublicHost.equalsIgnoreCase(cookieDomain)) {
            domains.add(legacyPublicHost);
        }
        Set<String> paths = new LinkedHashSet<>();
        paths.add("/");
        paths.add(cookiePath);
        for (String domain : domains) {
            for (String path : paths) {
                emitTombstone(response, LEGACY_JWT_COOKIE, domain, path);
                emitTombstone(response, LEGACY_OP_COOKIE, domain, path);
            }
        }
    }

    private void emitTombstone(HttpServletResponse response, String name, String domain, String path) {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from(name, "").path(path).maxAge(0).httpOnly(true);
        if (StringUtils.isNotBlank(domain)) {
            b.domain(domain);
        }
        response.addHeader("Set-Cookie", b.build().toString());
    }

    private ResponseCookie.ResponseCookieBuilder scoped(String name, String value) {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from(name, value).path(cookiePath).httpOnly(true).sameSite("Lax");
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
            if (v == null || !TOKEN_FORMAT.matcher(v).matches()) {
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

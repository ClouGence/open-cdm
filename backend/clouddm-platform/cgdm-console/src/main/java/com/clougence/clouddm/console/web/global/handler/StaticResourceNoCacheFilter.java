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
package com.clougence.clouddm.console.web.global.handler;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class StaticResourceNoCacheFilter {

    private static final String INDEX_HTML = "/index.html";

    private StaticResourceNoCacheFilter() {
    }

    public static FilterRegistrationBean<Filter> indexHtml(String urlPathPrefix) {
        String normalizedPrefix = normalizePrefix(urlPathPrefix);
        Set<String> noCacheUrls = new HashSet<>();
        configUrl(normalizedPrefix, noCacheUrls);

        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setName("indexHtmlNoCacheFilter" + normalizedPrefix.replace('/', '_'));
        registration.addUrlPatterns(noCacheUrls.toArray(new String[0]));
        registration.setFilter((request, response, chain) -> {
            if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse
                && shouldNoCache(httpRequest, noCacheUrls)) {
                setNoCacheHeaders(httpResponse);
            }
            chain.doFilter(request, response);
        });
        return registration;
    }

    public static void configUrl(String urlPathPrefix, Set<String> noCacheUrls) {
        String normalizedPrefix = normalizePrefix(urlPathPrefix);
        if ("/".equals(normalizedPrefix)) {
            noCacheUrls.add("/");
            noCacheUrls.add(INDEX_HTML);
            return;
        }
        noCacheUrls.add(normalizedPrefix);
        noCacheUrls.add(normalizedPrefix + "/");
        noCacheUrls.add(normalizedPrefix + INDEX_HTML);
    }

    private static String normalizePrefix(String urlPathPrefix) {
        if (urlPathPrefix == null || urlPathPrefix.isBlank() || "/".equals(urlPathPrefix)) {
            return "/";
        }

        String normalized = urlPathPrefix.startsWith("/") ? urlPathPrefix : "/" + urlPathPrefix;
        while (normalized.endsWith("/") && normalized.length() > 1) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private static boolean shouldNoCache(HttpServletRequest request, Set<String> noCacheUrls) {
        String requestPath = getRequestPath(request);
        return noCacheUrls.contains(requestPath);
    }

    private static String getRequestPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();
        if (contextPath != null && !contextPath.isEmpty() && requestUri.startsWith(contextPath)) {
            return requestUri.substring(contextPath.length());
        }
        return requestUri;
    }

    private static void setNoCacheHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}

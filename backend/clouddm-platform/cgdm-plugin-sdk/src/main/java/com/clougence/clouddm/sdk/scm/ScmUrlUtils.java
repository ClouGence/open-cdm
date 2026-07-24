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
package com.clougence.clouddm.sdk.scm;

import java.net.URI;

public final class ScmUrlUtils {

    private ScmUrlUtils(){
    }

    public static String normalizeGitlabWebUrl(String serviceUrl) {
        String value = serviceUrl == null ? "" : serviceUrl.trim();
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        URI uri = URI.create(value);
        if (!("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme())) || uri.getHost() == null || uri.getHost().isBlank()
            || uri.getUserInfo() != null || uri.getQuery() != null || uri.getFragment() != null || uri.getPath().matches("(?i).*/api/v4$")) {
            throw new IllegalArgumentException("use the GitLab web root URL without credentials, query, fragment, or /api/v4");
        }
        return value;
    }
}

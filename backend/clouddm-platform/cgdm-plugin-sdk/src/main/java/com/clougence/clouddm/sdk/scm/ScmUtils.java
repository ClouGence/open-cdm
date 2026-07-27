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
import java.util.ArrayList;
import java.util.List;

public final class ScmUtils {

    private ScmUtils(){
    }

    public static String buildRepoPath(String repoSpace, String repoName) {
        if (repoSpace == null || repoSpace.trim().isEmpty()) {
            return repoName;
        }
        return repoSpace + "/" + repoName;
    }

    /**
     * Normalize a repository-relative directory path. An empty value means the
     * repository root. Parent traversal and NUL bytes are never permitted.
     */
    public static String normalizeDirectoryPath(String value) {
        String path = value == null ? "" : value.trim().replace('\\', '/');
        if (path.indexOf('\u0000') >= 0) {
            throw new IllegalArgumentException("invalid repository path");
        }

        List<String> segments = new ArrayList<>();
        for (String segment : path.split("/")) {
            if (segment.isEmpty() || ".".equals(segment)) {
                continue;
            }
            if ("..".equals(segment)) {
                throw new IllegalArgumentException("repository path must not contain parent traversal");
            }
            segments.add(segment);
        }
        return String.join("/", segments);
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

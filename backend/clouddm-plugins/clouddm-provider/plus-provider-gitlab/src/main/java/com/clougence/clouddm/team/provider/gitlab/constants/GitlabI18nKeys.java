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
package com.clougence.clouddm.team.provider.gitlab.constants;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/gitlab")
public interface GitlabI18nKeys {
    String PLUGIN_NAME_GITLAB            = "PLUGIN_NAME_GITLAB";
    String GITLAB_SCM_INVALID_URL        = "GITLAB_SCM_INVALID_URL";
    String GITLAB_SCM_API_ERROR          = "GITLAB_SCM_API_ERROR";
    String GITLAB_SCM_AUTH_ERROR         = "GITLAB_SCM_AUTH_ERROR";
    String GITLAB_SCM_NOT_FOUND_ERROR    = "GITLAB_SCM_NOT_FOUND_ERROR";
    String GITLAB_SCM_RATE_LIMIT_ERROR   = "GITLAB_SCM_RATE_LIMIT_ERROR";
    String GITLAB_SCM_FETCH_REPOS_LIMIT  = "GITLAB_SCM_FETCH_REPOS_LIMIT";
    String GITLAB_SCM_FETCH_BRANCH_LIMIT = "GITLAB_SCM_FETCH_BRANCH_LIMIT";
    String GITLAB_SCM_DOWNLOAD_ERROR     = "GITLAB_SCM_DOWNLOAD_ERROR";
    String GITLAB_SCM_ARCHIVE_LIMIT      = "GITLAB_SCM_ARCHIVE_LIMIT";
}

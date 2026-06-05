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
package com.clougence.clouddm.team.provider.gitee.constants;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/dm/sdk/i18n/gitee")
public interface GiteeI18nKeys {

    String GITEE_SCM_FETCH_REPOS_ERROR                 = "GITEE_SCM_FETCH_REPOS_ERROR";
    String GITEE_SCM_NOT_FOUND_REPO_ERROR              = "GITEE_SCM_NOT_FOUND_REPO_ERROR";
    String GITEE_SCM_DOWNLOAD_REPOS_ERROR              = "GITEE_SCM_DOWNLOAD_REPOS_ERROR";
    String GITEE_UNZIP_ZIP_NOT_EXIST_ERROR             = "GITEE_UNZIP_ZIP_NOT_EXIST_ERROR";
    String GITEE_UNZIP_DST_IS_FILE_ERROR               = "GITEE_UNZIP_DST_IS_FILE_ERROR";
    String GITEE_UNZIP_ERROR                           = "GITEE_UNZIP_ERROR";
    String GITEE_SCM_FETCH_BRANCH_MULTIPLE_REPOS_ERROR = "GITEE_SCM_FETCH_BRANCH_MULTIPLE_REPOS_ERROR";
    String GITEE_SCM_FETCH_BRANCH_ERROR                = "GITEE_SCM_FETCH_BRANCH_ERROR";
    String GITEE_SCM_FETCH_WEBHOOKS_ERROR              = "GITEE_SCM_FETCH_WEBHOOKS_ERROR";
    String GITEE_SCM_EVENT_PASSWORD_ERROR              = "GITEE_SCM_EVENT_PASSWORD_ERROR";
    String GITEE_SCM_EVENT_DECODER_ERROR               = "GITEE_SCM_EVENT_DECODER_ERROR";
    String GITEE_SCM_CREATE_WEBHOOKS_ERROR             = "GITEE_SCM_CREATE_WEBHOOKS_ERROR";
}

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
package com.clougence.clouddm.team.provider.gitlab.devops;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.scm.*;
import com.clougence.utils.function.ESupplier;

import okhttp3.OkHttpClient;

public class GitlabDevopsScmProviderSpi implements ScmProviderSpi {

    private final GitlabApiClient      apiClient;
    private final GitlabWebhookParser  webhookParser;
    private final GitlabArchiveService archiveService;

    public GitlabDevopsScmProviderSpi(OkHttpClient httpClient){
        this.apiClient = new GitlabApiClient(httpClient);
        this.webhookParser = new GitlabWebhookParser(apiClient);
        this.archiveService = new GitlabArchiveService(apiClient);
    }

    @Override
    public String name() {
        return ScmProviderNames.Gitlab.name();
    }

    @Override
    public String getServiceUrl() { return ""; }

    @Override
    public String getHelpUrl() { return "https://www.cdmgr.com/docs/integrations/devops/devops_cicd_gitlab"; }

    @Override
    public List<ScmEventType> devopsSupportEvents() {
        return List.of(ScmEventType.Push, ScmEventType.PullRequest);
    }

    @Override
    public List<ScmRepo> fetchRepoList(String serviceUrl, String accessToken, String filter) {
        return apiClient.fetchRepositories(serviceUrl, accessToken, filter);
    }

    @Override
    public ScmRepo fetchRepo(String serviceUrl, String accessToken, ScmRepo selection) {
        return apiClient.fetchRepository(serviceUrl, accessToken, selection);
    }

    @Override
    public List<ScmBranch> fetchBranchList(String serviceUrl, String accessToken, ScmRepo repo, String filter, boolean exactMatch) {
        return apiClient.fetchBranches(serviceUrl, accessToken, repo, filter, exactMatch);
    }

    @Override
    public ScmPathValidation validateScriptPath(String serviceUrl, String accessToken, ScmRepo repo, String scriptPath) {
        return archiveService.validateScriptPath(serviceUrl, accessToken, repo, scriptPath);
    }

    @Override
    public String fetchServerVersion(String serviceUrl, String accessToken) {
        return apiClient.fetchServerVersion(serviceUrl, accessToken);
    }

    @Override
    public ScmEvent readEvent(String serviceUrl, String accessToken, String repoId, String repoPath, String repoName, String password, String signingToken,
                              Map<String, List<String>> headers, String jsonBody) {
        return webhookParser.readEvent(serviceUrl, accessToken, password, signingToken, headers, jsonBody);
    }

    @Override
    public void downloadToLocal(ScmProvider scm, ScmRepo repo, ScmSaveTo saveTo, ESupplier<Boolean, Exception> watchdog) throws Exception {
        archiveService.downloadToLocal(scm, repo, saveTo, watchdog);
    }
}

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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assume;
import org.junit.Test;

import com.clougence.clouddm.sdk.scm.ScmBranch;
import com.clougence.clouddm.sdk.scm.ScmPathValidation;
import com.clougence.clouddm.sdk.scm.ScmRepo;

import okhttp3.OkHttpClient;

/**
 * Opt-in smoke test for a real self-managed GitLab instance. The test is skipped
 * unless {@code GITLAB_E2E_URL} and {@code GITLAB_E2E_PAT} are provided.
 */
public class GitlabDevopsScmProviderIntegrationTest {

    @Test
    public void shouldReadNestedPrivateProjectAtExactCommit() {
        String serviceUrl = System.getenv("GITLAB_E2E_URL");
        String accessToken = System.getenv("GITLAB_E2E_PAT");
        Assume.assumeTrue("real GitLab URL is not configured", serviceUrl != null && !serviceUrl.isBlank());
        Assume.assumeTrue("real GitLab PAT is not configured", accessToken != null && !accessToken.isBlank());

        String repoId = valueOrDefault(System.getenv("GITLAB_E2E_REPO_ID"), "1");
        String branchName = valueOrDefault(System.getenv("GITLAB_E2E_BRANCH"), "main");
        String scriptPath = valueOrDefault(System.getenv("GITLAB_E2E_SCRIPT_PATH"), "scripts");
        GitlabDevopsScmProviderSpi provider = new GitlabDevopsScmProviderSpi(new OkHttpClient());

        assertFalse(provider.fetchServerVersion(serviceUrl, accessToken).isBlank());
        List<ScmRepo> repos = provider.fetchRepoList(serviceUrl, accessToken, "database-release");
        ScmRepo selection = repos.stream().filter(repo -> repoId.equals(repo.getRepoId())).findFirst().orElseThrow();
        ScmRepo repo = provider.fetchRepo(serviceUrl, accessToken, selection);
        List<ScmBranch> branches = provider.fetchBranchList(serviceUrl, accessToken, repo, branchName, true);

        assertEquals(repoId, repo.getRepoId());
        assertEquals("clouddm-e2e/platform/database-release", repo.getRepoPath());
        assertFalse(branches.isEmpty());
        assertTrue(branches.get(0).getCommitId().matches("(?i)^[0-9a-f]{40,64}$"));

        repo.setCommitId(branches.get(0).getCommitId());
        ScmPathValidation validation = provider.validateScriptPath(serviceUrl, accessToken, repo, scriptPath);
        assertTrue(validation.isChecked());
        assertTrue(validation.getSqlFileCount() >= 1);
    }

    private static String valueOrDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}

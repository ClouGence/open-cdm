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
package com.clougence.clouddm.platform.plugin;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.model.fo.cicd.DevopsScmAddFO;
import com.clougence.clouddm.console.web.model.fo.cicd.DevopsScmUpdateFO;
import com.clougence.clouddm.console.web.service.cicd.DmScmServiceImpl;
import com.clougence.clouddm.console.web.service.cicd.domain.DmBranchDef;
import com.clougence.clouddm.console.web.service.cicd.domain.ScmConnectionTestResult;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.mapper.cicd.DmChangeFlowMapper;
import com.clougence.clouddm.platform.dal.mapper.gitops.DmGitOpsScmMapper;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeFlowDO;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.clouddm.platform.dal.model.gitops.ScmType;
import com.clougence.clouddm.sdk.scm.*;
import com.clougence.utils.function.ESupplier;

public class DmScmServiceImplTest {

    private ChangeFlowDal      changeFlowDal;
    private DmGitOpsScmMapper  scmMapper;
    private DmChangeFlowMapper flowMapper;
    private RecordingProvider  provider;
    private DmScmServiceImpl   service;

    @Before
    public void setUp() throws Exception {
        changeFlowDal = mock(ChangeFlowDal.class);
        scmMapper = mock(DmGitOpsScmMapper.class);
        flowMapper = mock(DmChangeFlowMapper.class);
        when(changeFlowDal.scmMapper()).thenReturn(scmMapper);
        when(changeFlowDal.flowMapper()).thenReturn(flowMapper);
        provider = new RecordingProvider();
        PluginManager.globalMeta().addSpi(ScmProviderSpi.class, provider);
        service = new DmScmServiceImpl();
        ReflectionTestUtils.setField(service, "changeFlowDal", changeFlowDal);
        service.init();
    }

    @Test
    public void shouldRequireHttpAcknowledgementAndPersistNormalizedUrl() {
        DevopsScmAddFO unacknowledged = addForm(false);
        try {
            service.addScm("owner", unacknowledged);
            fail("plain HTTP must require explicit acknowledgement");
        } catch (ErrorMessageException expected) {
            verify(scmMapper, never()).insert(any(DmGitOpsScmDO.class));
        }

        DevopsScmAddFO accepted = addForm(true);
        service.addScm("owner", accepted);

        ArgumentCaptor<DmGitOpsScmDO> captor = ArgumentCaptor.forClass(DmGitOpsScmDO.class);
        verify(scmMapper).insert(captor.capture());
        assertEquals("http://gitlab.example:8088/gitlab", captor.getValue().getScmServiceUrl());
        assertEquals("pat-secret", captor.getValue().getScmAccessToken());
        assertEquals("http://gitlab.example:8088/gitlab", provider.lastServiceUrl);
        assertEquals("pat-secret", provider.lastAccessToken);
    }

    @Test
    public void shouldReportSuccessfulConnectionWithNoVisibleProjects() {
        DevopsScmAddFO form = addForm(true);

        ScmConnectionTestResult result = service.testScmByConfig("owner", form);

        assertEquals(0, result.getProjectCount());
        assertEquals("19.2.0-ee", result.getServerVersion());
        assertNotNull(result.getWarning());
    }

    @Test
    public void shouldKeepBlankTokenAndDisableAffectedFlowsOnlyForForcedUrlChange() {
        DmGitOpsScmDO current = new DmGitOpsScmDO();
        current.setId(2L);
        current.setOwnerUid("owner");
        current.setScmType(ScmType.Gitlab);
        current.setScmServiceUrl("https://gitlab.old/subpath");
        current.setScmAccessToken("existing-token");
        when(scmMapper.queryByOwnerAndId("owner", 2L)).thenReturn(current);
        DmChangeFlowDO flow = new DmChangeFlowDO();
        flow.setId(9L);
        when(flowMapper.queryEnabledByOwnerAndScmId("owner", 2L)).thenReturn(List.of(flow));

        DevopsScmUpdateFO update = new DevopsScmUpdateFO();
        update.setScmId(2L);
        update.setNewServiceUrl("https://gitlab.new/another/");
        update.setNewAccessToken("   ");
        update.setForce(true);
        List<Long> affected = service.updateScmById("owner", update);

        assertEquals(List.of(9L), affected);
        assertEquals("existing-token", provider.lastAccessToken);
        verify(scmMapper).updateUrlByOwnerAndId("owner", 2L, "https://gitlab.new/another");
        verify(scmMapper, never()).updateTokenByOwnerAndId(anyString(), anyLong(), anyString());
        verify(flowMapper).disableByOwnerAndScmId("owner", 2L);
    }

    @Test
    public void shouldResolveLegacyProviderRepositoryByNamespaceWithoutRepoId() {
        ScmRepo first = repo(null, null, "group/a", "database");
        ScmRepo second = repo(null, null, "group/b", "database");
        provider.repos = List.of(first, second);
        ScmRepo selection = repo("101", "group/b/database", "group/b", "database");

        assertSame(second, provider.fetchRepo("https://gitlab.example", "token", selection));

        selection.setRepoSpace(null);
        selection.setRepoPath(null);
        assertNull(provider.fetchRepo("https://gitlab.example", "token", selection));
    }

    @Test
    public void shouldReturnOnlyTheRequestedExactBranch() {
        DmGitOpsScmDO current = new DmGitOpsScmDO();
        current.setId(3L);
        current.setOwnerUid("owner");
        current.setScmType(ScmType.Gitlab);
        current.setScmServiceUrl("https://gitlab.example");
        current.setScmAccessToken("token");
        when(scmMapper.queryByOwnerAndId("owner", 3L)).thenReturn(current);
        ScmBranch fuzzy = new ScmBranch();
        fuzzy.setBranchName("main-backup");
        fuzzy.setCommitId("a".repeat(40));
        ScmBranch exact = new ScmBranch();
        exact.setBranchName("main");
        exact.setCommitId("b".repeat(40));
        provider.branches = List.of(fuzzy, exact);

        DmBranchDef branch = service.fetchBranchByScmAndRepo("owner", 3L, "101", "group", "database", "main");

        assertNotNull(branch);
        assertEquals("main", branch.getBranch());
        assertEquals("b".repeat(40), branch.getBranchCommitId());
    }

    private static ScmRepo repo(String id, String path, String space, String name) {
        ScmRepo repo = new ScmRepo();
        repo.setRepoId(id);
        repo.setRepoPath(path);
        repo.setRepoSpace(space);
        repo.setRepoName(name);
        return repo;
    }

    private static DevopsScmAddFO addForm(boolean acknowledged) {
        DevopsScmAddFO form = new DevopsScmAddFO();
        form.setScmType(ScmType.Gitlab);
        form.setDisplay("Private GitLab");
        form.setServiceUrl(" http://gitlab.example:8088/gitlab/// ");
        form.setAccessToken("pat-secret");
        form.setPlainHttpAcknowledged(acknowledged);
        return form;
    }

    private static final class RecordingProvider implements ScmProviderSpi {
        private String lastServiceUrl;
        private String lastAccessToken;
        private List<ScmRepo> repos = Collections.emptyList();
        private List<ScmBranch> branches = Collections.emptyList();

        @Override
        public String name() {
            return ScmProviderNames.Gitlab.name();
        }

        @Override
        public String getServiceUrl() { return ""; }

        @Override
        public String getHelpUrl() { return "help"; }

        @Override
        public List<ScmEventType> devopsSupportEvents() {
            return List.of(ScmEventType.Push, ScmEventType.PullRequest);
        }

        @Override
        public List<ScmRepo> fetchRepoList(String serviceUrl, String accessToken, String filter) {
            lastServiceUrl = serviceUrl;
            lastAccessToken = accessToken;
            return repos;
        }

        @Override
        public ScmRepo fetchRepo(String serviceUrl, String accessToken, ScmRepo selection) {
            return ScmRepoUtils.findUnique(repos, selection);
        }

        @Override
        public String fetchServerVersion(String serviceUrl, String accessToken) {
            return "19.2.0-ee";
        }

        @Override
        public List<ScmBranch> fetchBranchList(String serviceUrl, String accessToken, ScmRepo repo, String filter,
                                               boolean exactMatch) {
            return branches;
        }

        @Override
        public ScmPathValidation validateScriptPath(String serviceUrl, String accessToken, ScmRepo repo, String scriptPath) {
            return new ScmPathValidation();
        }

        @Override
        public ScmEvent readEvent(String serviceUrl, String accessToken, String repoId, String repoPath, String repoName,
                                  String password, String signingToken, Map<String, List<String>> headers, String jsonBody) {
            return null;
        }

        @Override
        public void downloadToLocal(ScmProvider scm, ScmRepo repo, ScmSaveTo saveTo, ESupplier<Boolean, Exception> watchdog) {
        }
    }
}

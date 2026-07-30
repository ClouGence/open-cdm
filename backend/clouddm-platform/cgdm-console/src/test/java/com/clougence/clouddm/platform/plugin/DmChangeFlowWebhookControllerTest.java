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

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.controller.cicd.DmChangeFlowWebhookController;
import com.clougence.clouddm.console.web.service.cicd.DmChangeService;
import com.clougence.clouddm.console.web.service.cicd.DmScmService;
import com.clougence.clouddm.console.web.service.cicd.domain.ChangeTriggerContext;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.mapper.cicd.DmChangeFlowMapper;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeFlowDO;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.clouddm.platform.dal.model.gitops.ScmType;
import com.clougence.clouddm.sdk.scm.*;
import com.clougence.utils.function.ESupplier;

public class DmChangeFlowWebhookControllerTest {

    private ChangeFlowDal                 changeFlowDal;
    private DmChangeFlowMapper            flowMapper;
    private DmChangeService               changeService;
    private DmScmService                  scmService;
    private DmChangeFlowWebhookController controller;

    @Before
    public void setUp() {
        changeFlowDal = mock(ChangeFlowDal.class);
        flowMapper = mock(DmChangeFlowMapper.class);
        changeService = mock(DmChangeService.class);
        scmService = mock(DmScmService.class);
        when(changeFlowDal.flowMapper()).thenReturn(flowMapper);
        PluginManager.globalMeta().addSpi(ScmProviderSpi.class, new ContractScmProvider());

        controller = new DmChangeFlowWebhookController();
        ReflectionTestUtils.setField(controller, "changeFlowDal", changeFlowDal);
        ReflectionTestUtils.setField(controller, "dmChangeService", changeService);
        ReflectionTestUtils.setField(controller, "dmScmService", scmService);
    }

    @Test
    public void shouldReturnNotFoundAndAuthenticationContractStatuses() throws Exception {
        when(flowMapper.queryByOwnerAndId("owner", 1L)).thenReturn(null);
        assertEquals(404, callback("accepted", "legacy").getStatusCode().value());

        stubFlow();
        assertEquals(401, callback("accepted", "wrong").getStatusCode().value());
        assertEquals(400, callback("malformed", "legacy").getStatusCode().value());
        verifyNoInteractions(changeService);
    }

    @Test
    public void shouldReturnSuccessForFilteredAndDuplicateEventsWithoutTriggering() throws Exception {
        stubFlow();
        when(changeService.triggerChangeSuggest(eq("owner"), eq(1L), any(ChangeTriggerContext.class)))
            .thenReturn(ResWebDataUtils.buildSuccess("duplicate change trigger ignored."));

        assertEquals(200, callback("filtered", "legacy").getStatusCode().value());
        assertEquals(200, callback("accepted", "legacy").getStatusCode().value());

        verify(changeService).verifyFlow("owner", 1L);
        verify(changeService, times(1)).triggerChangeSuggest(eq("owner"), eq(1L), any(ChangeTriggerContext.class));
    }

    @Test
    public void shouldReserveAndTriggerUsingImmutableCommitSha() throws Exception {
        stubFlow();
        when(changeService.triggerChangeSuggest(eq("owner"), eq(1L), any(ChangeTriggerContext.class))).thenReturn(ResWebDataUtils.buildSuccess("created"));

        ResponseEntity<ResWebData<?>> response = callback("accepted", "legacy");

        assertEquals(200, response.getStatusCode().value());
        verify(changeService).triggerChangeSuggest(eq("owner"), eq(1L), argThat(context -> ContractScmProvider.COMMIT.equals(context.getCommitId())
                                                                                       && "delivery-1".equals(context.getDeliveryId())
                                                                                       && "WebhookPush".equals(context.getTriggerType())));
    }

    @Test
    public void shouldFilterEventsThatOmitTheBoundStableRepositoryId() throws Exception {
        stubFlow();

        ResponseEntity<ResWebData<?>> response = callback("missing-id", "legacy");

        assertEquals(200, response.getStatusCode().value());
        verify(changeService, never()).verifyFlow("owner", 1L);
        verify(changeService, never()).triggerChangeSuggest(anyString(), anyLong(), any(ChangeTriggerContext.class));
    }

    @Test
    public void shouldReturnServerErrorWhenTransactionalTriggerFails() throws Exception {
        stubFlow();
        when(changeService.triggerChangeSuggest(eq("owner"), eq(1L), any(ChangeTriggerContext.class))).thenThrow(new IllegalStateException("transient"));

        ResponseEntity<ResWebData<?>> response = callback("accepted", "legacy");

        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    public void shouldRejectInvalidUtf8BeforeProviderParsing() throws Exception {
        stubFlow();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(new byte[] { (byte) 0xC3, (byte) 0x28 });
        request.addHeader("X-Gitlab-Token", "legacy");

        ResponseEntity<ResWebData<?>> response = controller.callback("owner", "1", null, ScmProviderNames.Gitlab, request);

        assertEquals(400, response.getStatusCode().value());
        verifyNoInteractions(changeService);
    }

    private void stubFlow() {
        stubFlow("owner");
    }

    private void stubFlow(String owner) {
        DmChangeFlowDO flow = new DmChangeFlowDO();
        flow.setId(1L);
        flow.setOwnerUid(owner);
        flow.setRefScmId(2L);
        flow.setRefScmType(ScmType.Gitlab);
        flow.setScmRepoIdentifier("101");
        flow.setScmRepoSpace("group/sub");
        flow.setScmRepoName("database");
        flow.setScmRepoUrl("https://gitlab.example/group/sub/database");
        flow.setScmRepoBranch("main");
        flow.setScmRepoEvent(ScmEventType.Push);
        flow.setScmBindWebhookPwd("legacy");
        flow.setEnable(true);
        flow.setEnableWebhook(true);
        when(flowMapper.queryByOwnerAndId(owner, 1L)).thenReturn(flow);

        DmGitOpsScmDO scm = new DmGitOpsScmDO();
        scm.setId(2L);
        scm.setScmType(ScmType.Gitlab);
        scm.setScmServiceUrl("https://gitlab.example");
        scm.setScmAccessToken("pat");
        when(scmService.queryScmById(owner, 2L)).thenReturn(scm);
    }

    private ResponseEntity<ResWebData<?>> callback(String body, String token) throws Exception {
        return callback("owner", body, token);
    }

    private ResponseEntity<ResWebData<?>> callback(String owner, String body, String token) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(body.getBytes(StandardCharsets.UTF_8));
        request.addHeader("X-Gitlab-Token", token);
        return controller.callback(owner, "1", null, ScmProviderNames.Gitlab, request);
    }

    private static final class ContractScmProvider implements ScmProviderSpi {
        /**
         * Valid immutable commit SHA used by the Webhook contract tests.
         */
        private static final String COMMIT = "a".repeat(40);

        @Override
        public String name() {
            return ScmProviderNames.Gitlab.name();
        }

        @Override
        public String getServiceUrl() { return ""; }

        @Override
        public String getHelpUrl() { return ""; }

        @Override
        public List<ScmEventType> devopsSupportEvents() {
            return List.of(ScmEventType.Push);
        }

        @Override
        public List<ScmRepo> fetchRepoList(String serviceUrl, String accessToken, String filter) {
            return Collections.emptyList();
        }

        @Override
        public ScmRepo fetchRepo(String serviceUrl, String accessToken, ScmRepo selection) {
            return null;
        }

        @Override
        public List<ScmBranch> fetchBranchList(String serviceUrl, String accessToken, ScmRepo repo, String filter, boolean exactMatch) {
            return Collections.emptyList();
        }

        @Override
        public ScmPathValidation validateScriptPath(String serviceUrl, String accessToken, ScmRepo repo, String scriptPath) {
            return new ScmPathValidation();
        }

        @Override
        public String fetchServerVersion(String serviceUrl, String accessToken) {
            return null;
        }

        @Override
        public ScmEvent readEvent(String serviceUrl, String accessToken, String repoId, String repoPath, String repoName,
                                  String password, String signingToken, Map<String, List<String>> headers, String jsonBody) {
            if (!"legacy".equals(firstHeader(headers, "X-Gitlab-Token"))) {
                throw new ScmWebhookException(401, "invalid token");
            }
            if ("malformed".equals(jsonBody)) {
                throw new ScmWebhookException(400, "malformed");
            }
            ScmEvent event = new ScmEvent();
            event.setDeliveryId("delivery-1");
            event.setEventId(COMMIT);
            event.setEventType(ScmEventType.Push);
            event.setStatus("filtered".equals(jsonBody) ? ScmEventStatus.Delete : ScmEventStatus.Update);
            event.setTarRepoId("missing-id".equals(jsonBody) ? null : "101");
            event.setTarRepoPath("group/sub");
            event.setTarRepoName("database");
            event.setTarRepoBranch("main");
            return event;
        }

        @Override
        public void downloadToLocal(ScmProvider scm, ScmRepo repo, ScmSaveTo saveTo, ESupplier<Boolean, Exception> watchdog) {
        }

        private static String firstHeader(Map<String, List<String>> headers, String name) {
            return headers.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(name))
                .map(Map.Entry::getValue)
                .filter(values -> !values.isEmpty())
                .map(values -> values.get(0))
                .findFirst()
                .orElse(null);
        }
    }
}

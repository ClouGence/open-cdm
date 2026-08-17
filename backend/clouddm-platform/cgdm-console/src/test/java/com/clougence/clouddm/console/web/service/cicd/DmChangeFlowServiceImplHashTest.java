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
package com.clougence.clouddm.console.web.service.cicd;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.springframework.test.util.ReflectionTestUtils;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeFlowParentConfigFO;
import com.clougence.clouddm.console.web.model.fo.cicd.GuideCheckFlowFO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeFlowRelationItemVO;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.entry.UserCacheEntry;
import com.clougence.clouddm.platform.dal.mapper.cicd.DmChangeBatchMapper;
import com.clougence.clouddm.platform.dal.mapper.cicd.DmChangeFlowMapper;
import com.clougence.clouddm.platform.dal.mapper.cicd.DmChangeMapper;
import com.clougence.clouddm.platform.dal.model.cicd.*;

public class DmChangeFlowServiceImplHashTest {

    @Test
    public void shouldIsolateRepositoryIdsByScmConfiguration() {
        DmChangeFlowServiceImpl service = new DmChangeFlowServiceImpl();
        GuideCheckFlowFO firstInstance = flow(11);
        GuideCheckFlowFO secondInstance = flow(22);

        assertNotEquals(service.toHash(firstInstance), service.toHash(secondInstance));
    }

    @Test
    public void shouldRejectParentCycle() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO child = builtInFlow(1L, null);
        DmChangeFlowDO descendant = builtInFlow(2L, 1L);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(child);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 2L)).thenReturn(descendant);
        when(fixture.flowMapper.queryByOwnerAndId("owner", 1L)).thenReturn(child);

        assertError(() -> fixture.service.updateParent("owner", 1L, 2L));

        verify(fixture.flowMapper, never()).updateParentByOwnerAndId(anyString(), anyLong(), any(), any(), anyBoolean(), anyBoolean());
    }

    @Test
    public void shouldUpdateAndDetachParentWhenRelationTreeIsIdle() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO child = builtInFlow(1L, null);
        DmChangeFlowDO parent = builtInFlow(2L, null);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(child);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 2L)).thenReturn(parent);
        when(fixture.flowMapper.queryByOwnerAndId("owner", 1L)).thenReturn(child);
        when(fixture.flowMapper.queryByOwnerAndId("owner", 2L)).thenReturn(parent);

        fixture.service.updateParent("owner", 1L, 2L);
        verify(fixture.flowMapper).updateParentByOwnerAndId("owner", 1L, ChangeFlowType.BUILT_IN, 2L, false, false);

        child.setRefParentFlowId(2L);
        fixture.service.updateParent("owner", 1L, null);
        verify(fixture.flowMapper).updateParentByOwnerAndId("owner", 1L, ChangeFlowType.BUILT_IN, null, false, false);
    }

    @Test
    public void shouldDetachAllChangedFlowsBeforeApplyingBatchParents() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO first = builtInFlow(1L, 2L);
        DmChangeFlowDO second = builtInFlow(4L, 3L);
        DmChangeFlowDO oldFirstParent = builtInFlow(2L, null);
        DmChangeFlowDO oldSecondParent = builtInFlow(3L, null);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate(eq("owner"), anyLong())).thenAnswer(invocation -> {
            long flowId = invocation.getArgument(1);
            if (flowId == 1L) {
                return first;
            }
            if (flowId == 2L) {
                return oldFirstParent;
            }
            if (flowId == 3L) {
                return oldSecondParent;
            }
            return flowId == 4L ? second : null;
        });
        when(fixture.flowMapper.queryByOwnerAndId(eq("owner"), anyLong())).thenAnswer(invocation -> {
            long flowId = invocation.getArgument(1);
            if (flowId == 1L) {
                return first;
            }
            if (flowId == 2L) {
                return oldFirstParent;
            }
            if (flowId == 3L) {
                return oldSecondParent;
            }
            return flowId == 4L ? second : null;
        });
        ChangeFlowParentConfigFO firstChange = parentChange(1L, 3L);
        ChangeFlowParentConfigFO secondChange = parentChange(4L, 2L);

        fixture.service.updateParents("owner", List.of(firstChange, secondChange));

        InOrder updates = inOrder(fixture.flowMapper);
        updates.verify(fixture.flowMapper).updateParentByOwnerAndId("owner", 1L, ChangeFlowType.BUILT_IN, null, false, false);
        updates.verify(fixture.flowMapper).updateParentByOwnerAndId("owner", 4L, ChangeFlowType.BUILT_IN, null, false, false);
        updates.verify(fixture.flowMapper).updateParentByOwnerAndId("owner", 1L, ChangeFlowType.BUILT_IN, 3L, false, false);
        updates.verify(fixture.flowMapper).updateParentByOwnerAndId("owner", 4L, ChangeFlowType.BUILT_IN, 2L, false, false);
    }

    @Test
    public void shouldRejectAttachingDisabledBuiltInFlow() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO child = builtInFlow(1L, null);
        child.setEnable(false);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(child);

        assertError(() -> fixture.service.updateParent("owner", 1L, 2L));

        verify(fixture.flowMapper, never()).queryByOwnerAndIdForUpdate("owner", 2L);
        verify(fixture.flowMapper, never()).updateParentByOwnerAndId(anyString(), anyLong(), any(), any(), anyBoolean(), anyBoolean());
    }

    @Test
    public void shouldAllowDisabledBuiltInFlowToDetach() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO child = builtInFlow(1L, 2L);
        child.setEnable(false);
        DmChangeFlowDO parent = builtInFlow(2L, null);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(child);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 2L)).thenReturn(parent);
        when(fixture.flowMapper.queryByOwnerAndId("owner", 1L)).thenReturn(child);
        when(fixture.flowMapper.queryByOwnerAndId("owner", 2L)).thenReturn(parent);

        fixture.service.updateParent("owner", 1L, null);

        verify(fixture.flowMapper).updateParentByOwnerAndId("owner", 1L, ChangeFlowType.BUILT_IN, null, false, false);
    }

    @Test
    public void shouldRejectRelationUpdateWhenRootHasRunningBatch() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO child = builtInFlow(1L, 2L);
        DmChangeFlowDO parent = builtInFlow(2L, null);
        DmChangeBatchDO runningBatch = new DmChangeBatchDO();
        runningBatch.setBatchStatus(ChangeBatchStatus.RUNNING);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(child);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 2L)).thenReturn(parent);
        when(fixture.flowMapper.queryByOwnerAndId("owner", 2L)).thenReturn(parent);
        when(fixture.batchMapper.queryRunningByRootFlow("owner", 2L)).thenReturn(runningBatch);

        assertError(() -> fixture.service.updateParent("owner", 1L, null));

        verify(fixture.flowMapper, never()).updateParentByOwnerAndId(anyString(), anyLong(), any(), any(), anyBoolean(), anyBoolean());
    }

    @Test
    public void shouldCascadeDisableAndEnableThroughAllDescendants() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO parent = builtInFlow(1L, null);
        DmChangeFlowDO child = builtInFlow(2L, 1L);
        DmChangeFlowDO grandchild = builtInFlow(3L, 2L);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(parent);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 2L)).thenReturn(child);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 3L)).thenReturn(grandchild);
        when(fixture.flowMapper.queryChildren("owner", 1L)).thenReturn(List.of(child));
        when(fixture.flowMapper.queryChildren("owner", 2L)).thenReturn(List.of(grandchild));

        fixture.service.disableGitOpsFlow("owner", 1L);

        verify(fixture.flowMapper).disableFlowByOwnerAndId("owner", 1L);
        verify(fixture.flowMapper).disableFlowByOwnerAndId("owner", 2L);
        verify(fixture.flowMapper).disableFlowByOwnerAndId("owner", 3L);

        fixture.service.enableGitOpsFlow("owner", 1L);

        verify(fixture.flowMapper).enableFlowByOwnerAndId("owner", 1L);
        verify(fixture.flowMapper).enableFlowByOwnerAndId("owner", 2L);
        verify(fixture.flowMapper).enableFlowByOwnerAndId("owner", 3L);
    }

    @Test
    public void shouldRejectCascadeDisableWhileAnyRelatedFlowHasRunningBatch() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO parent = builtInFlow(1L, null);
        DmChangeFlowDO child = builtInFlow(2L, 1L);
        DmChangeFlowDO grandchild = builtInFlow(3L, 2L);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(parent);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 2L)).thenReturn(child);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 3L)).thenReturn(grandchild);
        when(fixture.flowMapper.queryChildren("owner", 1L)).thenReturn(List.of(child));
        when(fixture.flowMapper.queryChildren("owner", 2L)).thenReturn(List.of(grandchild));
        when(fixture.changeCascadeService.hasRunningBatchForFlows(eq("owner"), anyCollection())).thenReturn(true);

        assertError(() -> fixture.service.disableGitOpsFlow("owner", 1L));

        verify(fixture.flowMapper, never()).disableFlowByOwnerAndId(anyString(), anyLong());
    }

    @Test
    public void shouldCascadeArchiveAndRecoverFromRootFlow() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO parent = builtInFlow(1L, null);
        parent.setFlowName("parent");
        DmChangeFlowDO child = builtInFlow(2L, 1L);
        child.setFlowName("child");
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(parent);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 2L)).thenReturn(child);
        when(fixture.flowMapper.queryChildren("owner", 1L)).thenReturn(List.of(child));

        fixture.service.archiveFlow("owner", 1L, "operator");

        verify(fixture.flowMapper).updateStatusByOwnerAndId("owner", 1L, ChangeFlowStatus.ARCHIVE);
        verify(fixture.flowMapper).updateStatusByOwnerAndId("owner", 2L, ChangeFlowStatus.ARCHIVE);
        parent.setChangeFlowStatus(ChangeFlowStatus.ARCHIVE);
        child.setChangeFlowStatus(ChangeFlowStatus.ARCHIVE);

        fixture.service.recoverFlowTo("owner", 1L, ChangeFlowStatus.NORMAL);

        verify(fixture.flowMapper).enableFlowByOwnerAndId("owner", 1L);
        verify(fixture.flowMapper).enableFlowByOwnerAndId("owner", 2L);
    }

    @Test
    public void shouldCascadeDeleteFromArchivedRootFlow() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO parent = builtInFlow(1L, null);
        parent.setChangeFlowStatus(ChangeFlowStatus.ARCHIVE);
        DmChangeFlowDO child = builtInFlow(2L, 1L);
        child.setChangeFlowStatus(ChangeFlowStatus.ARCHIVE);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(parent);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 2L)).thenReturn(child);
        when(fixture.flowMapper.queryChildren("owner", 1L)).thenReturn(List.of(child));

        fixture.service.deleteFlow("owner", 1L);

        InOrder deletionOrder = inOrder(fixture.flowMapper);
        deletionOrder.verify(fixture.flowMapper).deleteByOwnerAndId("owner", 2L);
        deletionOrder.verify(fixture.flowMapper).deleteByOwnerAndId("owner", 1L);
    }

    @Test
    public void shouldRejectArchiveFromChildFlow() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO child = builtInFlow(2L, 1L);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 2L)).thenReturn(child);

        assertError(() -> fixture.service.archiveFlow("owner", 2L, "operator"));

        verify(fixture.flowMapper, never()).updateStatusByOwnerAndId(anyString(), anyLong(), any());
    }

    @Test
    public void shouldEnableBuiltInFlowWithoutScmHashConflictCheck() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO flow = builtInFlow(1L, null);
        flow.setEnable(false);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(flow);

        fixture.service.enableGitOpsFlow("owner", 1L);

        verify(fixture.flowMapper, never()).queryEnabledByOwnerAndHash(anyString(), anyLong());
        verify(fixture.flowMapper).enableFlowByOwnerAndId("owner", 1L);
    }

    @Test
    public void shouldExcludeNonBuiltInParentCandidates() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO builtIn = builtInFlow(1L, null);
        DmChangeFlowDO scm = scmFlow(2L);
        when(fixture.flowMapper.queryParentCandidates("owner")).thenReturn(List.of(scm, builtIn));

        List<ChangeFlowRelationItemVO> candidates = fixture.service.queryParentCandidates("owner", null);

        assertEquals(1, candidates.size());
        assertEquals(1L, (long) candidates.get(0).getFlowId());
        assertEquals(ChangeFlowType.BUILT_IN, candidates.get(0).getFlowType());
    }

    @Test
    public void shouldRejectNonBuiltInParent() {
        RelationFixture fixture = relationFixture();
        DmChangeFlowDO child = builtInFlow(1L, null);
        DmChangeFlowDO scm = scmFlow(2L);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(child);
        when(fixture.flowMapper.queryByOwnerAndIdForUpdate("owner", 2L)).thenReturn(scm);

        assertError(() -> fixture.service.updateParent("owner", 1L, 2L));

        verify(fixture.flowMapper, never()).updateParentByOwnerAndId(anyString(), anyLong(), any(), any(), anyBoolean(), anyBoolean());
    }

    private static RelationFixture relationFixture() {
        ChangeFlowDal dal = mock(ChangeFlowDal.class);
        DmChangeFlowMapper flowMapper = mock(DmChangeFlowMapper.class);
        DmChangeMapper changeMapper = mock(DmChangeMapper.class);
        DmChangeBatchMapper batchMapper = mock(DmChangeBatchMapper.class);
        ChangeCascadeService changeCascadeService = mock(ChangeCascadeService.class);
        ObjectCacheDao objectCacheDao = mock(ObjectCacheDao.class);
        ImSenderService senderService = mock(ImSenderService.class);
        when(dal.flowMapper()).thenReturn(flowMapper);
        when(dal.changeMapper()).thenReturn(changeMapper);
        when(dal.batchMapper()).thenReturn(batchMapper);
        when(flowMapper.queryChildren(anyString(), anyLong())).thenReturn(Collections.emptyList());
        when(changeMapper.queryUnlockedChangesByFlowIds(anyString(), anyCollection())).thenReturn(Collections.emptyList());
        UserCacheEntry operator = new UserCacheEntry();
        operator.setRoleName("admin");
        operator.setUserName("operator");
        when(objectCacheDao.queryByUid("operator")).thenReturn(operator);
        DmChangeFlowServiceImpl service = new DmChangeFlowServiceImpl();
        ReflectionTestUtils.setField(service, "changeFlowDal", dal);
        ReflectionTestUtils.setField(service, "objectCacheDao", objectCacheDao);
        ReflectionTestUtils.setField(service, "senderService", senderService);
        ReflectionTestUtils.setField(service, "changeCascadeService", changeCascadeService);
        return new RelationFixture(service, flowMapper, batchMapper, changeCascadeService);
    }

    private static DmChangeFlowDO builtInFlow(long id, Long parentId) {
        DmChangeFlowDO flow = baseFlow(id);
        flow.setFlowType(ChangeFlowType.BUILT_IN);
        flow.setRefParentFlowId(parentId);
        return flow;
    }

    private static DmChangeFlowDO scmFlow(long id) {
        DmChangeFlowDO flow = baseFlow(id);
        flow.setFlowType(ChangeFlowType.SCM);
        return flow;
    }

    private static ChangeFlowParentConfigFO parentChange(long flowId, Long parentFlowId) {
        ChangeFlowParentConfigFO change = new ChangeFlowParentConfigFO();
        change.setFlowId(flowId);
        change.setParentFlowId(parentFlowId);
        return change;
    }

    private static DmChangeFlowDO baseFlow(long id) {
        DmChangeFlowDO flow = new DmChangeFlowDO();
        flow.setId(id);
        flow.setOwnerUid("owner");
        flow.setChangeFlowStatus(ChangeFlowStatus.NORMAL);
        flow.setDsType(DataSourceType.MySQL);
        flow.setEnable(true);
        flow.setEnableWebhook(false);
        flow.setEnableTrigger(false);
        return flow;
    }

    private static void assertError(Runnable action) {
        try {
            action.run();
            fail("expected ErrorMessageException");
        } catch (ErrorMessageException expected) {
            assertNotNull(expected);
        }
    }

    private static final class RelationFixture {
        private final DmChangeFlowServiceImpl service;
        private final DmChangeFlowMapper      flowMapper;
        private final DmChangeBatchMapper     batchMapper;
        private final ChangeCascadeService    changeCascadeService;

        private RelationFixture(DmChangeFlowServiceImpl service, DmChangeFlowMapper flowMapper, DmChangeBatchMapper batchMapper,
                                ChangeCascadeService changeCascadeService) {
            this.service = service;
            this.flowMapper = flowMapper;
            this.batchMapper = batchMapper;
            this.changeCascadeService = changeCascadeService;
        }
    }

    private static GuideCheckFlowFO flow(long scmId) {
        GuideCheckFlowFO flow = new GuideCheckFlowFO();
        flow.setRepoScmId(scmId);
        flow.setRepoScmUrl("https://gitlab.example/gitlab/group/database");
        flow.setRepoId("1");
        flow.setRepoBranch("main");
        flow.setDsId("mysql-test");
        flow.setDsLevels(List.of("mysql-test", "database"));
        return flow;
    }
}

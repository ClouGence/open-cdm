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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.service.cicd.domain.ChangeTriggerContext;
import com.clougence.clouddm.console.web.service.cicd.domain.CreateSuggest;
import com.clougence.clouddm.console.web.service.cicd.domain.CreateSuggestType;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.mapper.cicd.DmChangeFlowMapper;
import com.clougence.clouddm.platform.dal.mapper.cicd.DmChangeMapper;
import com.clougence.clouddm.platform.dal.mapper.cicd.DmChangeTriggerReceiptMapper;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.gitops.ScmType;

public class DmChangeServiceImplTest {

    private ChangeFlowDal                changeFlowDal;
    private DmChangeFlowMapper           flowMapper;
    private DmChangeMapper               changeMapper;
    private DmChangeTriggerReceiptMapper receiptMapper;
    private DmChangeServiceImpl          changeService;

    @Before
    public void setUp() {
        changeFlowDal = mock(ChangeFlowDal.class);
        flowMapper = mock(DmChangeFlowMapper.class);
        changeMapper = mock(DmChangeMapper.class);
        receiptMapper = mock(DmChangeTriggerReceiptMapper.class);
        when(changeFlowDal.flowMapper()).thenReturn(flowMapper);
        when(changeFlowDal.changeMapper()).thenReturn(changeMapper);
        when(changeFlowDal.triggerReceiptMapper()).thenReturn(receiptMapper);

        DmChangeFlowDO flow = new DmChangeFlowDO();
        flow.setId(1L);
        flow.setOwnerUid("owner");
        flow.setRefScmType(ScmType.Gitlab);
        flow.setChangeFlowStatus(ChangeFlowStatus.NORMAL);
        flow.setEnable(true);
        when(flowMapper.queryByOwnerAndId("owner", 1L)).thenReturn(flow);
        when(flowMapper.queryByOwnerAndIdForUpdate("owner", 1L)).thenReturn(flow);

        changeService = new DmChangeServiceImpl();
        ReflectionTestUtils.setField(changeService, "changeFlowDal", changeFlowDal);
    }

    @Test
    public void shouldIgnoreDuplicateCommitAcrossTriggerSources() {
        when(receiptMapper.reserve(any(DmChangeTriggerReceiptDO.class))).thenReturn(0);

        ResWebData<String> result = changeService.triggerChangeSuggest("owner", 1L, ChangeTriggerContext.manual("abc123", "operator"));

        assertTrue(result.isSuccess());
        assertEquals("duplicate change trigger ignored.", result.getData());
        verifyNoInteractions(changeMapper);

        ArgumentCaptor<DmChangeTriggerReceiptDO> receiptCaptor = ArgumentCaptor.forClass(DmChangeTriggerReceiptDO.class);
        verify(receiptMapper).reserve(receiptCaptor.capture());
        DmChangeTriggerReceiptDO receipt = receiptCaptor.getValue();
        assertEquals("owner", receipt.getOwnerUid());
        assertEquals(1L, receipt.getRefFlowId());
        assertEquals(ScmType.Gitlab, receipt.getProvider());
        assertEquals("abc123", receipt.getCommitId());
        assertEquals("Manual", receipt.getTriggerType());
        verify(flowMapper).queryByOwnerAndIdForUpdate("owner", 1L);
    }

    @Test
    public void shouldRollbackReceiptWhenChangeCreationFails() throws Exception {
        Transactional transactional = DmChangeServiceImpl.class.getMethod("triggerChangeSuggest", String.class, long.class, ChangeTriggerContext.class)
            .getAnnotation(Transactional.class);
        assertNotNull(transactional);
        assertArrayEquals(new Class<?>[] { Throwable.class }, transactional.rollbackFor());

        when(receiptMapper.reserve(any(DmChangeTriggerReceiptDO.class))).thenReturn(1);
        when(changeMapper.queryUnlockedChange("owner", 1L)).thenThrow(new IllegalStateException("transient"));

        try {
            changeService.triggerChangeSuggest("owner", 1L, ChangeTriggerContext.remote("abc123"));
            fail("expected trigger failure");
        } catch (ErrorMessageException e) {
            assertEquals("transient", e.getMessage());
        }

        verify(receiptMapper).reserve(any(DmChangeTriggerReceiptDO.class));
    }

    @Test
    public void shouldQueueDifferentCommitWithoutRestartingCurrentChange() {
        DmChangeDO change = new DmChangeDO();
        change.setCurrentStep(ChangeStep.INIT);
        change.setLastCommitId("old-commit");
        when(changeMapper.queryUnlockedChange("owner", 1L)).thenReturn(List.of(change));

        CreateSuggest differentCommit = changeService.createChangeSuggest("owner", 1L, "new-commit");
        CreateSuggest sameCommit = changeService.createChangeSuggest("owner", 1L, "old-commit");

        assertEquals(CreateSuggestType.Later, differentCommit.getSuggestType());
        assertSame(change, differentCommit.getChange());
        assertEquals(CreateSuggestType.Restart, sameCommit.getSuggestType());
    }

}

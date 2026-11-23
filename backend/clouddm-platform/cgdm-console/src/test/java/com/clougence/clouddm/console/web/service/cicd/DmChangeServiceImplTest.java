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

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.SimpleTransactionStatus;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.component.approval.handler.ChangeApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalMO;
import com.clougence.clouddm.console.web.component.cicd.ChangeSqlService;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.component.cicd.action.AbstractChangeAction;
import com.clougence.clouddm.console.web.component.cicd.action.ChangeActionForApproval;
import com.clougence.clouddm.console.web.component.cicd.action.ChangeActionForFinish;
import com.clougence.clouddm.console.web.component.cicd.impl.ChangeScheduleServiceImpl;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfo;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.service.cicd.domain.ChangeTriggerContext;
import com.clougence.clouddm.console.web.service.cicd.domain.CreateSuggest;
import com.clougence.clouddm.console.web.service.cicd.domain.CreateSuggestType;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.mapper.approval.DmApprovalMapper;
import com.clougence.clouddm.platform.dal.mapper.cicd.*;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBiz;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.gitops.ScmType;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.function.EFunction;

public class DmChangeServiceImplTest {

    private ChangeFlowDal                changeFlowDal;
    private DmChangeFlowMapper           flowMapper;
    private DmChangeMapper               changeMapper;
    private DmChangeTriggerReceiptMapper receiptMapper;
    private DmChangeServiceImpl          changeService;
    private PlatformTransactionManager   transactions;

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
        transactions = mock(PlatformTransactionManager.class);
        when(transactions.getTransaction(any())).thenAnswer(invocation -> new SimpleTransactionStatus());
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

    @Test
    public void shouldFinishDisabledFlowOnlyAfterTicketCompletion() {
        DmChangeFlowDO flow = flowMapper.queryByOwnerAndId("owner", 1L);
        flow.setEnable(false);
        flow.setFlowType(ChangeFlowType.SCM);
        DmChangeDO change = new DmChangeDO();
        change.setId(10L);
        change.setOwnerUid("owner");
        change.setRefFlowId(1L);
        change.setVersion(2);
        change.setCurrentStep(ChangeStep.FINISH);
        when(changeMapper.assignReadyChange(10L, 3)).thenReturn(1);

        AbstractChangeAction action = mock(AbstractChangeAction.class, CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(action, "changeFlowDal", changeFlowDal);
        ReflectionTestUtils.setField(action, "senderService", mock(ImSenderService.class));

        assertEquals(Boolean.TRUE, ReflectionTestUtils.invokeMethod(action, "doCommonAction", change));
        verify(changeMapper, never()).updateStatusTo(anyLong(), anyInt(), any(), anyString());
        verify(changeFlowDal, never()).scmMapper();

        change.setCurrentStep(ChangeStep.INIT);
        assertEquals(Boolean.FALSE, ReflectionTestUtils.invokeMethod(action, "doCommonAction", change));
        verify(changeMapper).updateStatusTo(eq(10L), eq(4), eq(ChangeStatus.FAILED), anyString());
    }

    @Test
    public void shouldNotFinishArchivedOrDeletedFlow() {
        DmChangeFlowDO flow = flowMapper.queryByOwnerAndId("owner", 1L);
        DmChangeDO change = new DmChangeDO();
        change.setId(10L);
        change.setOwnerUid("owner");
        change.setRefFlowId(1L);
        change.setVersion(2);
        change.setCurrentStep(ChangeStep.FINISH);
        when(changeMapper.assignReadyChange(10L, 3)).thenReturn(1);

        AbstractChangeAction action = mock(AbstractChangeAction.class, CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(action, "changeFlowDal", changeFlowDal);
        ReflectionTestUtils.setField(action, "senderService", mock(ImSenderService.class));

        flow.setChangeFlowStatus(ChangeFlowStatus.ARCHIVE);
        assertEquals(Boolean.FALSE, ReflectionTestUtils.invokeMethod(action, "doCommonAction", change));
        flow.setChangeFlowStatus(ChangeFlowStatus.NORMAL);
        flow.setDeleted(true);
        assertEquals(Boolean.FALSE, ReflectionTestUtils.invokeMethod(action, "doCommonAction", change));
        verify(changeMapper, times(2)).updateStatusTo(eq(10L), eq(4), eq(ChangeStatus.FAILED), anyString());
    }

    @Test
    public void shouldRecoverLegacyFinishFailureWithoutReplayingTicket() {
        DmChangeFlowDO flow = flowMapper.queryByOwnerAndId("owner", 1L);
        flow.setEnable(false);
        DmChangeDO change = new DmChangeDO();
        change.setId(10L);
        change.setOwnerUid("owner");
        change.setRefFlowId(1L);
        change.setVersion(5);
        change.setCurrentStep(ChangeStep.FINISH);
        change.setCurrentStatus(ChangeStatus.FAILED);
        when(changeMapper.queryFailedFinishChanges()).thenReturn(List.of(change));
        when(flowMapper.queryByIds("owner", Set.of(1L))).thenReturn(List.of(flow));
        when(changeMapper.updateStatusTo(10L, 5, ChangeStatus.READY, "")).thenReturn(1, 0);

        ChangeScheduleServiceImpl scheduler = new ChangeScheduleServiceImpl();
        ReflectionTestUtils.setField(scheduler, "changeFlowDal", changeFlowDal);
        ReflectionTestUtils.invokeMethod(scheduler, "recoverFailedFinishChanges");
        ReflectionTestUtils.invokeMethod(scheduler, "recoverFailedFinishChanges");

        verify(changeMapper, times(2)).updateStatusTo(10L, 5, ChangeStatus.READY, "");
        verify(changeMapper, never()).updateStepTo(anyLong(), anyInt(), any(), anyString());
        verify(changeMapper, never()).lockChangeById(anyLong(), anyInt());
    }

    @Test
    public void shouldNotRecoverMissingArchivedOrDeletedFlow() {
        DmChangeFlowDO flow = flowMapper.queryByOwnerAndId("owner", 1L);
        DmChangeDO change = new DmChangeDO();
        change.setId(10L);
        change.setOwnerUid("owner");
        change.setRefFlowId(1L);
        when(changeMapper.queryFailedFinishChanges()).thenReturn(List.of(change));
        when(flowMapper.queryByIds("owner", Set.of(1L))).thenReturn(List.of(flow), List.of(flow), List.of());

        ChangeScheduleServiceImpl scheduler = new ChangeScheduleServiceImpl();
        ReflectionTestUtils.setField(scheduler, "changeFlowDal", changeFlowDal);
        flow.setChangeFlowStatus(ChangeFlowStatus.ARCHIVE);
        ReflectionTestUtils.invokeMethod(scheduler, "recoverFailedFinishChanges");
        flow.setChangeFlowStatus(ChangeFlowStatus.NORMAL);
        flow.setDeleted(true);
        ReflectionTestUtils.invokeMethod(scheduler, "recoverFailedFinishChanges");
        ReflectionTestUtils.invokeMethod(scheduler, "recoverFailedFinishChanges");

        verify(changeMapper, never()).updateStatusTo(anyLong(), anyInt(), any(), anyString());
    }

    @Test
    public void shouldAdvanceCompletedApprovalUsingTheNextVersion() {
        DmChangeDO change = new DmChangeDO();
        change.setId(10L);
        change.setOwnerUid("owner");
        change.setRefFlowId(1L);
        change.setVersion(5);
        change.setCurrentStep(ChangeStep.APPROVAL);
        change.setCurrentStatus(ChangeStatus.WAIT);
        when(changeMapper.queryChangeById(10L)).thenReturn(change);
        when(changeMapper.updateStepTo(eq(10L), eq(5), eq(ChangeStep.FINISH), anyString())).thenReturn(1);
        when(changeMapper.updateStatusTo(eq(10L), eq(6), eq(ChangeStatus.READY), anyString())).thenReturn(1);

        ApprovalMO info = new ApprovalMO();
        info.setChangeOwnerUid("owner");
        info.setChangeId(10L);

        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        ticket.setTicketInfo(JsonUtils.toJson(info));

        DmApprovalMapper approvals = mock(DmApprovalMapper.class);
        when(approvals.queryById(11L)).thenReturn(ticket);
        ApprovalDal approvalDal = mock(ApprovalDal.class);
        when(approvalDal.approvalMapper()).thenReturn(approvals);

        ChangeTicketInfo ticketInfo = new ChangeTicketInfo();
        ticketInfo.setTicketId(11L);

        DmChangeItemDO item = new DmChangeItemDO();
        item.setContent(JsonUtils.toJson(ticketInfo));

        DmChangeItemMapper items = mock(DmChangeItemMapper.class);
        when(items.queryChangeItemByChangeId("owner", 10L, ChangeItemType.TICKET)).thenReturn(List.of(item));
        when(changeFlowDal.changeItemMapper()).thenReturn(items);

        ChangeApprovalHandler handler = new ChangeApprovalHandler();
        ReflectionTestUtils.setField(handler, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(handler, "changeFlowDal", changeFlowDal);
        ImSenderService sender = mock(ImSenderService.class);
        when(sender.getFlowLanguage("owner", 1L)).thenReturn("en_US");

        handler.approvalCompleted(11L, ApprovalBiz.DM_CHANGE, sender);

        var order = inOrder(changeMapper);
        order.verify(changeMapper).updateStepTo(eq(10L), eq(5), eq(ChangeStep.FINISH), anyString());
        order.verify(changeMapper).updateStatusTo(eq(10L), eq(6), eq(ChangeStatus.READY), anyString());
    }

    @Test
    public void shouldRecoverStaleTransfersOnlyOnSchedulerLeader() {
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.isSchedulerLeader()).thenReturn(false);
        ChangeCascadeService cascade = mock(ChangeCascadeService.class);
        when(cascade.recoverStaleTransfers(any(Date.class))).thenReturn(1);
        ChangeScheduleServiceImpl scheduler = new ChangeScheduleServiceImpl();
        ReflectionTestUtils.setField(scheduler, "cluster", cluster);
        ReflectionTestUtils.setField(scheduler, "changeFlowDal", changeFlowDal);
        ReflectionTestUtils.setField(scheduler, "changeCascadeService", cascade);

        ReflectionTestUtils.invokeMethod(scheduler, "scanPendingJob");

        verifyNoInteractions(changeMapper, cascade);
        long before = System.currentTimeMillis();
        when(cluster.isSchedulerLeader()).thenReturn(true);

        ReflectionTestUtils.invokeMethod(scheduler, "scanPendingJob");

        ArgumentCaptor<Date> staleBefore = ArgumentCaptor.forClass(Date.class);
        verify(cascade).recoverStaleTransfers(staleBefore.capture());
        assertTrue(staleBefore.getValue().getTime() >= before - 15 * 60 * 1000);
        assertTrue(staleBefore.getValue().getTime() <= System.currentTimeMillis() - 15 * 60 * 1000);
        verify(cascade).finishCompletedBatches();
        verify(cascade).queryReadyTransfers(any(Date.class), eq(50));
    }

    @Test
    public void shouldStoreBaselineAndLockCompletedChangeOnDisabledFlow() {
        DmChangeFlowDO flow = flowMapper.queryByOwnerAndId("owner", 1L);
        flow.setEnable(false);
        DmChangeDO change = new DmChangeDO();
        change.setId(10L);
        change.setOwnerUid("owner");
        change.setRefFlowId(1L);
        change.setVersion(2);
        change.setCurrentStep(ChangeStep.FINISH);
        when(changeMapper.assignReadyChange(10L, 3)).thenReturn(1);

        DmChangeDO assigned = new DmChangeDO();
        assigned.setId(10L);
        assigned.setOwnerUid("owner");
        assigned.setRefFlowId(1L);
        assigned.setVersion(4);
        when(changeMapper.queryChangeById(10L)).thenReturn(assigned);
        when(changeMapper.updateStatusTo(10L, 4, ChangeStatus.FINISH, "")).thenReturn(1);
        when(changeMapper.lockChangeById(10L, 5)).thenReturn(1);

        DmChangeFlowItemMapper flowItems = mock(DmChangeFlowItemMapper.class);
        when(changeFlowDal.flowItemMapper()).thenReturn(flowItems);
        ChangeCascadeService cascade = mock(ChangeCascadeService.class);
        ChangeActionForFinish action = new ChangeActionForFinish();
        ReflectionTestUtils.setField(action, "changeFlowDal", changeFlowDal);
        ReflectionTestUtils.setField(action, "senderService", mock(ImSenderService.class));
        ReflectionTestUtils.setField(action, "changeCascadeService", cascade);
        action.doAction(change);

        var order = inOrder(flowItems, changeMapper, cascade);
        order.verify(flowItems).deleteItemByFlowId("owner", 1L);
        order.verify(flowItems).insertFromChangeItems("owner", 10L, 1L);
        order.verify(changeMapper).updateStatusTo(10L, 4, ChangeStatus.FINISH, "");
        order.verify(changeMapper).lockChangeById(10L, 5);
        order.verify(cascade).onChangeFinished(assigned);
    }

    @Test
    public void shouldCommitTicketAndWaitBeforeSendingTheSuccessNotification() {
        ImSenderService sender = mock(ImSenderService.class);
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        ticket.setTicketTitle("ticket-title");
        ChangeActionForApproval action = approvalAction(ticket, sender);

        action.doAction(pendingChange(ChangeStep.APPROVAL));

        var order = inOrder(changeMapper, action, changeFlowDal.changeItemMapper(), transactions, sender);
        order.verify(changeMapper).assignReadyChange(10L, 3);
        order.verify(action).createApproval(any(), any(), any(), any());
        order.verify(changeFlowDal.changeItemMapper()).insert(argThat((DmChangeItemDO item) -> item.getRefChangeId() == 10L));
        order.verify(changeMapper).updateStatusTo(eq(10L), eq(4), eq(ChangeStatus.WAIT), anyString());
        order.verify(transactions).commit(any());
        order.verify(sender).sendMessage(eq("owner"), eq(1L), eq(ImMessageType.ChangeNotice), anyString());
    }

    @Test
    public void shouldRollbackTicketCreationWhenTheWaitTransitionLosesItsVersion() {
        ImSenderService sender = mock(ImSenderService.class);
        ChangeActionForApproval action = approvalAction(new DmApprovalDO(), sender);
        when(changeMapper.updateStatusTo(eq(10L), eq(4), eq(ChangeStatus.WAIT), anyString())).thenReturn(0);

        action.doAction(pendingChange(ChangeStep.APPROVAL));

        verify(transactions).rollback(any());
        verify(transactions, never()).commit(any());
        verify(changeMapper).updateStatusTo(eq(10L), eq(3), eq(ChangeStatus.FAILED), anyString());
        verify(sender, never()).sendMessage(anyString(), anyLong(), any(ImMessageType.class), anyString());
    }

    @Test
    public void shouldNotSendTicketSuccessWhenTheTransactionIsRollbackOnly() {
        ImSenderService sender = mock(ImSenderService.class);
        ChangeActionForApproval action = approvalAction(new DmApprovalDO(), sender);
        doThrow(new UnexpectedRollbackException("nested process creation rolled back")).when(transactions).commit(any());

        action.doAction(pendingChange(ChangeStep.APPROVAL));

        verify(changeMapper).updateStatusTo(eq(10L), eq(3), eq(ChangeStatus.FAILED), anyString());
        verify(sender, never()).sendMessage(anyString(), anyLong(), any(ImMessageType.class), anyString());
    }

    @Test
    public void shouldNotCreateAnotherTicketWhenTheVersionClaimIsLost() {
        ImSenderService sender = mock(ImSenderService.class);
        ChangeActionForApproval action = approvalAction(new DmApprovalDO(), sender);
        when(changeMapper.assignReadyChange(10L, 3)).thenReturn(0);

        action.doAction(pendingChange(ChangeStep.APPROVAL));

        verify(action, never()).createApproval(any(), any(), any(), any());
        verify(changeFlowDal.changeItemMapper(), never()).insert(any(DmChangeItemDO.class));
        verify(sender, never()).sendMessage(anyString(), anyLong(), any(ImMessageType.class), anyString());
    }

    @Test
    public void shouldKeepCommittedTicketWhenTheSuccessNotificationFails() {
        ImSenderService sender = mock(ImSenderService.class);
        ChangeActionForApproval action = approvalAction(new DmApprovalDO(), sender);
        doThrow(new ErrorMessageException("message provider unavailable")).when(sender).sendMessage(anyString(), anyLong(), any(ImMessageType.class), anyString());

        action.doAction(pendingChange(ChangeStep.APPROVAL));

        verify(transactions).commit(any());
        verify(changeMapper).updateStatusTo(eq(10L), eq(4), eq(ChangeStatus.WAIT), anyString());
        verify(changeMapper, never()).updateStatusTo(anyLong(), anyInt(), eq(ChangeStatus.FAILED), anyString());
    }

    private DmChangeDO pendingChange(ChangeStep step) {
        DmChangeDO change = new DmChangeDO();
        change.setId(10L);
        change.setOwnerUid("owner");
        change.setRefFlowId(1L);
        change.setVersion(2);
        change.setCurrentStep(step);
        change.setCurrentStatus(ChangeStatus.READY);
        return change;
    }

    private ChangeActionForApproval approvalAction(DmApprovalDO ticket, ImSenderService sender) {
        DmChangeDO current = pendingChange(ChangeStep.APPROVAL);
        current.setVersion(4);
        when(changeMapper.assignReadyChange(10L, 3)).thenReturn(1);
        when(changeMapper.queryChangeById(10L)).thenReturn(current);
        when(changeMapper.updateStatusTo(eq(10L), eq(4), eq(ChangeStatus.WAIT), anyString())).thenReturn(1);
        flowMapper.queryByOwnerAndId("owner", 1L).setFlowType(ChangeFlowType.BUILT_IN);
        when(changeFlowDal.changeItemMapper()).thenReturn(mock(DmChangeItemMapper.class));

        DmDsConfigService configurations = mock(DmDsConfigService.class);
        DsLevels levels = mock(DsLevels.class);
        when(configurations.parseLevels(nullable(String.class))).thenReturn(levels);
        ChangeSqlService sqlFiles = mock(ChangeSqlService.class);
        doAnswer(invocation -> {
            EFunction<Path, DmApprovalDO, Exception> consumer = invocation.getArgument(1);
            return consumer.eApply(Path.of("fixture.sql"));
        }).when(sqlFiles).consumeSqlFile(eq(10L), any());

        ChangeActionForApproval action = spy(new ChangeActionForApproval());
        ReflectionTestUtils.setField(action, "changeFlowDal", changeFlowDal);
        ReflectionTestUtils.setField(action, "senderService", sender);
        ReflectionTestUtils.setField(action, "txManager", transactions);
        ReflectionTestUtils.setField(action, "dmDsConfigService", configurations);
        ReflectionTestUtils.setField(action, "changeSqlService", sqlFiles);
        doReturn(ticket).when(action).createApproval(any(), same(levels), any(), any());
        return action;
    }

}

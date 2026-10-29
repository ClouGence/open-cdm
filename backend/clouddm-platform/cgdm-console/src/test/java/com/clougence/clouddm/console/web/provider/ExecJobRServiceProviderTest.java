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
package com.clougence.clouddm.console.web.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.SimpleTransactionStatus;

import com.clougence.clouddm.api.console.autoexec.AutoExecTaskPackageInfo;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecMessageDTO;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.approval.CustomApprovalService;
import com.clougence.clouddm.console.web.component.execute.AutoExecService;
import com.clougence.clouddm.console.web.component.execute.impl.AutoExecServiceImpl;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.mapper.approval.DmApprovalMapper;
import com.clougence.clouddm.platform.dal.mapper.execution.DmExecAutoJobMapper;
import com.clougence.clouddm.platform.dal.mapper.execution.DmExecAutoTaskMapper;
import com.clougence.clouddm.platform.dal.mapper.monitor.DmMonBizLogMapper;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStatus;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecTaskStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoTaskDO;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonBizLogDO;
import com.clougence.clouddm.platform.dal.model.monitor.LogDependBizType;
import com.clougence.clouddm.platform.dal.model.monitor.Loglevel;

public class ExecJobRServiceProviderTest {

    private DmExecAutoJobMapper        jobMapper;
    private DmExecAutoTaskMapper       taskMapper;
    private DmMonBizLogMapper          bizLogMapper;
    private ApprovalStateService       approvalStateService;
    private ExecJobRServiceProvider    provider;
    private AutoExecServiceImpl        dispatchService;
    private CustomApprovalService      customApprovalService;
    private DmApprovalMapper           approvalMapper;
    private PlatformTransactionManager txManager;
    private DmApprovalDO               ticket;

    @Before
    public void setUp() {
        ExecutionDal executionDal = mock(ExecutionDal.class);
        jobMapper = mock(DmExecAutoJobMapper.class);
        taskMapper = mock(DmExecAutoTaskMapper.class);
        when(executionDal.autoJobMapper()).thenReturn(jobMapper);
        when(executionDal.autoTaskMapper()).thenReturn(taskMapper);

        MonitorDal monitorDal = mock(MonitorDal.class);
        bizLogMapper = mock(DmMonBizLogMapper.class);
        when(monitorDal.bizLogMapper()).thenReturn(bizLogMapper);

        approvalStateService = mock(ApprovalStateService.class);

        DmExecAutoJobDO job = new DmExecAutoJobDO();
        job.setId(6L);
        job.setBizId("ticket-job");
        job.setDependOnBizId("approval-biz");
        job.setUid("executor");
        job.setStatus(AutoExecJobStatus.EXECUTING);
        when(jobMapper.selectById(6L)).thenReturn(job);
        when(jobMapper.queryById(6L)).thenReturn(job);

        DmExecAutoTaskDO task = new DmExecAutoTaskDO();
        task.setId(9L);
        task.setAutoExecJobId(6L);
        task.setBizId("ticket-task");
        task.setExecOrder(1);
        task.setExecSql("select 1");
        task.setQueryId("task-query");
        task.setStatus(AutoExecTaskStatus.EXECUTING);
        task.setExecCount(0);
        when(taskMapper.queryByQueryId("task-query")).thenReturn(task);
        when(jobMapper.markJobFailedIfActive(6L)).thenReturn(1);
        when(jobMapper.finishJobIfActive(6L)).thenReturn(1);
        when(jobMapper.pauseJobIfActive(6L)).thenReturn(1);

        provider = new ExecJobRServiceProvider();
        ReflectionTestUtils.setField(provider, "execDal", executionDal);
        ReflectionTestUtils.setField(provider, "monitorDal", monitorDal);
        ReflectionTestUtils.setField(provider, "approvalStateService", approvalStateService);
        ReflectionTestUtils.setField(provider, "autoExecService", mock(AutoExecService.class));

        ApprovalDal approvalDal = mock(ApprovalDal.class);
        approvalMapper = mock(DmApprovalMapper.class);
        when(approvalDal.approvalMapper()).thenReturn(approvalMapper);
        ticket = new DmApprovalDO();
        ticket.setId(11L);
        ticket.setTicketStatus(ApprovalStatus.WAIT_EXEC);
        when(approvalMapper.queryByBizId("approval-biz")).thenReturn(ticket);
        when(approvalMapper.selectByIdForUpdate(11L)).thenReturn(ticket);

        customApprovalService = mock(CustomApprovalService.class);
        when(customApprovalService.isCustomApproval(ticket)).thenReturn(true);
        txManager = mock(PlatformTransactionManager.class);
        when(txManager.getTransaction(any())).thenAnswer(invocation -> new SimpleTransactionStatus());

        dispatchService = spy(new AutoExecServiceImpl());
        ReflectionTestUtils.setField(dispatchService, "execDal", executionDal);
        ReflectionTestUtils.setField(dispatchService, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(dispatchService, "customApprovalService", customApprovalService);
        ReflectionTestUtils.setField(dispatchService, "approvalStateService", approvalStateService);
        ReflectionTestUtils.setField(dispatchService, "txManager", txManager);
    }

    @Test
    public void shouldIgnoreLateDispatchAfterJobTerminalTransition() {
        for (AutoExecJobStatus terminal : new AutoExecJobStatus[] { AutoExecJobStatus.FINISH, AutoExecJobStatus.TERMINATION, AutoExecJobStatus.FAILED }) {
            DmExecAutoJobDO job = jobMapper.queryById(6L);
            job.setStatus(terminal);

            dispatchService.dispatchJob(6L);
        }

        verify(jobMapper, never()).claimJobForPackaging(anyLong());
        verify(jobMapper, never()).updateJobStatus(anyLong(), any());
        verify(customApprovalService, never()).canExecute(any(), any());
        verifyNoInteractions(approvalStateService);
        verify(dispatchService, never()).create(anyLong());
    }

    @Test
    public void shouldIgnoreLateDispatchAfterTicketTerminalTransition() {
        jobMapper.queryById(6L).setStatus(AutoExecJobStatus.INIT);
        for (ApprovalStatus status : ApprovalStatus.values()) {
            if (ApprovalStatus.isEndStatus(status)) {
                ticket.setTicketStatus(status);
                dispatchService.dispatchJob(6L);
            }
        }

        verify(jobMapper, never()).claimJobForPackaging(anyLong());
        verify(jobMapper, never()).updateJobStatus(anyLong(), any());
        verifyNoInteractions(approvalStateService);
        verify(dispatchService, never()).create(anyLong());
    }

    @Test
    public void shouldCheckLockedTicketStateBeforeClaimingDispatch() {
        jobMapper.queryById(6L).setStatus(AutoExecJobStatus.INIT);
        DmApprovalDO finished = new DmApprovalDO();
        finished.setTicketStatus(ApprovalStatus.FINISHED);
        when(approvalMapper.selectByIdForUpdate(11L)).thenReturn(finished);

        dispatchService.dispatchJob(6L);

        verify(approvalMapper).selectByIdForUpdate(11L);
        verify(jobMapper, never()).claimJobForPackaging(anyLong());
        verifyNoInteractions(approvalStateService);
    }

    @Test
    public void shouldFailUnauthorizedDispatchOnlyAfterClaimInOneTransaction() {
        jobMapper.queryById(6L).setStatus(AutoExecJobStatus.INIT);
        when(jobMapper.claimJobForPackaging(6L)).thenReturn(1);

        dispatchService.dispatchJob(6L);

        var order = inOrder(approvalMapper, jobMapper, customApprovalService, approvalStateService, txManager);
        order.verify(approvalMapper).selectByIdForUpdate(11L);
        order.verify(jobMapper).claimJobForPackaging(6L);
        order.verify(customApprovalService).canExecute(ticket, "executor");
        order.verify(jobMapper).updateJobStatus(6L, AutoExecJobStatus.FAILED);
        order.verify(approvalStateService).failExecution(eq("approval-biz"), any());
        order.verify(txManager).commit(any());
        verify(dispatchService, never()).create(anyLong());
    }

    @Test
    public void shouldRollBackDispatchClaimWhenSavingTicketFailureFails() {
        jobMapper.queryById(6L).setStatus(AutoExecJobStatus.INIT);
        when(jobMapper.claimJobForPackaging(6L)).thenReturn(1);
        doThrow(new IllegalStateException("save failure")).when(approvalStateService).failExecution(eq("approval-biz"), any());

        try {
            dispatchService.dispatchJob(6L);
            fail("Expected saving the ticket failure to fail");
        } catch (IllegalStateException e) {
            assertEquals("save failure", e.getMessage());
        }

        verify(txManager).rollback(any());
        verify(txManager, never()).commit(any());
        verify(dispatchService, never()).create(anyLong());
    }

    @Test
    public void shouldPrepareAuthorizedDispatchAfterClaimCommits() {
        jobMapper.queryById(6L).setStatus(AutoExecJobStatus.INIT);
        when(jobMapper.claimJobForPackaging(6L)).thenReturn(1);
        when(customApprovalService.canExecute(ticket, "executor")).thenReturn(true);
        doReturn(mock(AutoExecTaskPackageInfo.class)).when(dispatchService).create(6L);

        dispatchService.dispatchJob(6L);

        var order = inOrder(jobMapper, customApprovalService, txManager, dispatchService);
        order.verify(jobMapper).claimJobForPackaging(6L);
        order.verify(customApprovalService).canExecute(ticket, "executor");
        order.verify(txManager).commit(any());
        order.verify(dispatchService).create(6L);
        verifyNoInteractions(approvalStateService);
    }

    @Test
    public void shouldPreserveLegacyDispatchWithoutCustomPermissionCheck() {
        jobMapper.queryById(6L).setStatus(AutoExecJobStatus.INIT);
        when(customApprovalService.isCustomApproval(ticket)).thenReturn(false);
        when(jobMapper.claimJobForPackaging(6L)).thenReturn(1);
        doReturn(mock(AutoExecTaskPackageInfo.class)).when(dispatchService).create(6L);

        dispatchService.dispatchJob(6L);

        verify(jobMapper).claimJobForPackaging(6L);
        verify(approvalMapper, never()).selectByIdForUpdate(anyLong());
        verify(customApprovalService, never()).canExecute(any(), any());
        verify(dispatchService).create(6L);
        verifyNoInteractions(approvalStateService);
    }

    @Test
    public void shouldIgnoreDispatchWhenClaimIsLostAfterInitialRead() {
        jobMapper.queryById(6L).setStatus(AutoExecJobStatus.INIT);

        dispatchService.dispatchJob(6L);

        verify(jobMapper).claimJobForPackaging(6L);
        verify(jobMapper, never()).updateJobStatus(anyLong(), any());
        verify(customApprovalService, never()).canExecute(any(), any());
        verifyNoInteractions(approvalStateService);
        verify(dispatchService, never()).create(anyLong());
    }

    @Test
    public void shouldNotifyApprovalWhenJobFails() {
        ReflectionTestUtils.invokeMethod(provider, "jobFailed", AutoExecMessageDTO.jobFailedMessage(6L, "task-query"));

        verify(jobMapper).markJobFailedIfActive(6L);
        verify(approvalStateService).failExecution("approval-biz", null);
    }

    @Test
    public void shouldPersistTaskFailureReason() {
        AutoExecMessageDTO message = AutoExecMessageDTO.taskFailMessage("task-query", "Table already exists", 1);
        ReflectionTestUtils.invokeMethod(provider, "taskFailed", message);

        verify(taskMapper).updateById(argThat((DmExecAutoTaskDO task) -> task.getStatus() == AutoExecTaskStatus.FAILED));
        verify(bizLogMapper).insert(argThat((DmMonBizLogDO log) -> log.getLogLevel() == Loglevel.ERROR && log.getDependOnBizType() == LogDependBizType.AUTO_EXEC_TASK
                                                                   && "ticket-task".equals(log.getDependOnBizId()) && "Table already exists".equals(log.getContent())));
    }

    @Test
    public void shouldIgnoreDuplicateJobFailureAfterTerminalTransition() {
        when(jobMapper.markJobFailedIfActive(6L)).thenReturn(0);

        ReflectionTestUtils.invokeMethod(provider, "jobFailed", AutoExecMessageDTO.jobFailedMessage(6L, "task-query"));

        verify(approvalStateService, never()).failExecution(anyString(), any());
        verifyNoInteractions(bizLogMapper);
    }

    @Test
    public void shouldIgnoreJobFailureForTaskFromAnotherJob() {
        DmExecAutoTaskDO foreignTask = new DmExecAutoTaskDO();
        foreignTask.setId(9L);
        foreignTask.setAutoExecJobId(7L);
        when(providerTaskMapper().queryByQueryId("task-query")).thenReturn(foreignTask);

        ReflectionTestUtils.invokeMethod(provider, "jobFailed", AutoExecMessageDTO.jobFailedMessage(6L, "task-query"));

        verify(jobMapper, never()).markJobFailedIfActive(anyLong());
        verifyNoInteractions(approvalStateService, bizLogMapper);
    }

    @Test
    public void shouldNotifyApprovalWhenJobFinishes() {
        ReflectionTestUtils.invokeMethod(provider, "jobFinish", AutoExecMessageDTO.jobFinishMessage(6L, 1L));

        verify(jobMapper).finishJobIfActive(6L);
        verify(approvalStateService).completeExecution("approval-biz");
    }

    @Test
    public void shouldIgnoreJobFinishAfterAnotherTerminalTransition() {
        when(jobMapper.finishJobIfActive(6L)).thenReturn(0);

        ReflectionTestUtils.invokeMethod(provider, "jobFinish", AutoExecMessageDTO.jobFinishMessage(6L, 1L));

        verify(approvalStateService, never()).completeExecution(anyString());
        verifyNoInteractions(bizLogMapper);
    }

    @Test
    public void shouldIgnoreJobPauseAfterTerminalTransition() {
        when(jobMapper.pauseJobIfActive(6L)).thenReturn(0);

        ReflectionTestUtils.invokeMethod(provider, "jobPause", AutoExecMessageDTO.jobPauseMessage(6L));

        verify(jobMapper).pauseJobIfActive(6L);
        verifyNoInteractions(bizLogMapper);
    }

    private DmExecAutoTaskMapper providerTaskMapper() {
        ExecutionDal executionDal = (ExecutionDal) ReflectionTestUtils.getField(provider, "execDal");
        return executionDal.autoTaskMapper();
    }
}

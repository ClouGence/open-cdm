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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.SimpleTransactionStatus;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.console.autoexec.AutoExecTaskPackageInfo;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecMessageDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecRService;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.approval.CustomApprovalService;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.clouddm.console.web.component.execute.AutoExecService;
import com.clougence.clouddm.console.web.component.execute.asyntask.*;
import com.clougence.clouddm.console.web.component.execute.control.ConsoleTaskRouter;
import com.clougence.clouddm.console.web.component.execute.impl.AutoExecScheduleService;
import com.clougence.clouddm.console.web.component.execute.impl.AutoExecServiceImpl;
import com.clougence.clouddm.console.web.component.execute.model.ConsoleTaskControlAction;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.service.asyntask.AsyncTaskServiceServiceImpl;
import com.clougence.clouddm.console.web.service.cluster.WorkerServiceImpl;
import com.clougence.clouddm.console.web.service.faker.FakerService;
import com.clougence.clouddm.console.web.service.faker.asyntask.FakerAsyncTask;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.mapper.approval.DmApprovalMapper;
import com.clougence.clouddm.platform.dal.mapper.execution.DmExecAsyncTaskMapper;
import com.clougence.clouddm.platform.dal.mapper.execution.DmExecAutoJobMapper;
import com.clougence.clouddm.platform.dal.mapper.execution.DmExecAutoTaskMapper;
import com.clougence.clouddm.platform.dal.mapper.monitor.DmMonBizLogMapper;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStatus;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.execution.*;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonBizLogDO;
import com.clougence.clouddm.platform.dal.model.monitor.LogDependBizType;
import com.clougence.clouddm.platform.dal.model.monitor.Loglevel;
import com.clougence.clouddm.sdk.model.faker.FakerRunStatus;
import com.clougence.clouddm.sdk.model.faker.FakerStatusDTO;
import com.clougence.utils.ThreadUtils;
import com.clougence.utils.future.CgFutureObj;

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
        job.setNormal(true);
        job.setLastReportTime(new Date());
        job.setScheduleTime(new Date());
        job.setConfig(new RsExecAutoJobConfigObj());
        when(jobMapper.selectById(6L)).thenReturn(job);
        when(jobMapper.queryById(6L)).thenReturn(job);
        when(jobMapper.queryByDependOnBizId("approval-biz")).thenReturn(job);
        when(jobMapper.queryByIdForUpdate(6L)).thenReturn(job);

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
        ReflectionTestUtils.setField(dispatchService, "cluster", mock(ConsoleClusterService.class));
        ReflectionTestUtils.setField(dispatchService, "execDal", executionDal);
        ReflectionTestUtils.setField(dispatchService, "monitorDal", monitorDal);
        ReflectionTestUtils.setField(dispatchService, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(dispatchService, "customApprovalService", customApprovalService);
        ReflectionTestUtils.setField(dispatchService, "approvalStateService", approvalStateService);
        ReflectionTestUtils.setField(dispatchService, "txManager", txManager);
    }

    @Test
    public void shouldRestoreLegacyTaskTrackingWithoutRecreatingWorkerJobs() {
        DmExecAsyncTaskMapper mapper = mock(DmExecAsyncTaskMapper.class);
        ExecutionDal dal = mock(ExecutionDal.class);
        when(dal.asyncTaskMapper()).thenReturn(mapper);
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.isAcceptingWork()).thenReturn(true);
        when(cluster.getConsoleInstanceId()).thenReturn("restoring-console");
        ConsoleConfig config = mock(ConsoleConfig.class);
        when(config.getAsyncTaskQueueSize()).thenReturn(1);
        ScheduleExecutor executor = mock(ScheduleExecutor.class);
        when(executor.getQueueSize()).thenReturn(0, 0, 1);
        FakerService faker = mock(FakerService.class);
        FakerStatusDTO status = new FakerStatusDTO();
        status.setStatus(FakerRunStatus.RUNNING);
        when(faker.hasInstanceById("user", "session")).thenReturn(true);
        when(faker.tailStatus("user", "session")).thenReturn(status);
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBean("fakerAsyncTask", AsyncTask.class)).thenAnswer(invocation -> {
            FakerAsyncTask task = new FakerAsyncTask();
            ReflectionTestUtils.setField(task, "fakerService", faker);
            ReflectionTestUtils.setField(task, "executionDal", dal);
            return task;
        });
        List<DmExecAsyncTaskDO> rows = new ArrayList<>();
        for (AsyncTaskStatus taskStatus : List.of(AsyncTaskStatus.RUNNING, AsyncTaskStatus.PAUSING, AsyncTaskStatus.CANCELING)) {
            DmExecAsyncTaskDO row = new DmExecAsyncTaskDO();
            row.setId((long) rows.size() + 1);
            row.setConsoleInstanceId("restoring-console");
            row.setStatus(taskStatus);
            row.setHandlerName("fakerAsyncTask");
            row.setConfigData("{\"userId\":\"user\",\"sessionId\":\"session\"}");
            rows.add(row);
        }

        when(mapper.queryRunningTasks("restoring-console")).thenReturn(rows);
        DmExecAsyncTaskDO waiting = new DmExecAsyncTaskDO();
        waiting.setId(99L);
        when(mapper.queryWaitTask(anyInt(), eq("restoring-console"))).thenReturn(List.of(waiting));
        AsyncTaskServiceImpl service = new AsyncTaskServiceImpl();
        Map<Long, AsyncTask> running = new ConcurrentHashMap<>();
        ReflectionTestUtils.setField(service, "cluster", cluster);
        ReflectionTestUtils.setField(service, "config", config);
        ReflectionTestUtils.setField(service, "executionDal", dal);
        ReflectionTestUtils.setField(service, "applicationContext", context);
        ReflectionTestUtils.setField(service, "scheduleExecutor", executor);
        ReflectionTestUtils.setField(service, "scheduleTaskMap", running);
        ReflectionTestUtils.setField(service, "requestSchedule", new AtomicInteger());
        ReflectionTestUtils.invokeMethod(service, "doSchedule");
        ReflectionTestUtils.invokeMethod(service, "doSchedule");
        verify(mapper, times(1)).queryRunningTasks("restoring-console");
        verify(executor, times(3)).submitTask(any(), any());
        verify(mapper, never()).updateStatusTo(anyLong(), anyString(), anyString());
        assertEquals(3, running.size());
        try (MockedStatic<DmGlobalEventBus> events = mockStatic(DmGlobalEventBus.class)) {
            for (AsyncTask task : new ArrayList<>(running.values())) {
                task.run();
                CgFutureObj<?> future = (CgFutureObj<?>) ReflectionTestUtils.getField(task, "future");
                if (ReflectionTestUtils.invokeMethod(task, "getInterruptedType") != null) {
                    future.completed(null);
                }
            }
        }

        verify(faker, never()).execute(anyString(), any());
        verify(faker, never()).resume(anyString(), anyString());
        verify(faker).pause("user", "session");
        verify(faker).close("user", "session");
        verify(mapper).updateStatusTo(2L, "PAUSE", "by PAUSE");
        verify(mapper).updateStatusTo(3L, "CANCEL", "by CANCEL");
        assertEquals(AsyncTaskStatus.RUNNING, rows.get(0).getStatus());
        assertEquals(1, running.size());
    }

    @Test
    public void shouldWaitForTheExistingFakerWorkerToReconnectBeforeResuming() {
        DmExecAsyncTaskMapper mapper = mock(DmExecAsyncTaskMapper.class);
        ExecutionDal dal = mock(ExecutionDal.class);
        when(dal.asyncTaskMapper()).thenReturn(mapper);
        FakerService faker = mock(FakerService.class);
        when(faker.hasInstanceById("user", "session")).thenReturn(true);
        FakerStatusDTO status = new FakerStatusDTO();
        status.setStatus(FakerRunStatus.PAUSE);
        when(faker.tailStatus("user", "session")).thenThrow(new IllegalStateException("Worker is reconnecting")).thenReturn(status);
        FakerAsyncTask task = new FakerAsyncTask();
        DmExecAsyncTaskDO row = new DmExecAsyncTaskDO();
        row.setId(1L);
        row.setStatus(AsyncTaskStatus.RUNNING);
        row.setConfigData("{\"userId\":\"user\",\"sessionId\":\"session\"}");
        ReflectionTestUtils.setField(task, "taskDO", row);
        ReflectionTestUtils.setField(task, "fakerService", faker);
        ReflectionTestUtils.setField(task, "executionDal", dal);
        try (MockedStatic<DmGlobalEventBus> events = mockStatic(DmGlobalEventBus.class)) {
            task.run();
            verify(faker, never()).resume(anyString(), anyString());
            task.run();
            task.run();
        }

        verify(faker, times(1)).resume("user", "session");
        verify(faker, never()).execute(anyString(), any());
        verify(mapper, never()).updateStatusTo(anyLong(), anyString(), anyString());
        assertEquals(AsyncTaskStatus.RUNNING, row.getStatus());
        assertFalse(((CgFutureObj<?>) ReflectionTestUtils.getField(task, "future")).isDone());
    }

    @Test
    public void shouldBindNewAsyncTasksBeforeActivation() {
        DmExecAsyncTaskMapper mapper = mock(DmExecAsyncTaskMapper.class);
        ExecutionDal dal = mock(ExecutionDal.class);
        when(dal.asyncTaskMapper()).thenReturn(mapper);
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.getConsoleInstanceId()).thenReturn("creating-console");
        AsyncTaskServiceServiceImpl service = new AsyncTaskServiceServiceImpl();
        ReflectionTestUtils.setField(service, "cluster", cluster);
        ReflectionTestUtils.setField(service, "executionDal", dal);
        AsyncTaskConfig config = new AsyncTaskConfig();
        config.setHandlerType(HelloWordTask.class);
        config.setBizId("legacy-assignment-boundary");
        List<DmExecAsyncTaskDO> rows = service.storeTask("user", config);
        assertEquals(1, rows.size());
        assertEquals("creating-console", rows.get(0).getConsoleInstanceId());
        assertEquals(AsyncTaskStatus.INIT, rows.get(0).getStatus());
        verify(mapper).insert(rows.get(0));
    }

    @Test(timeout = 10000)
    public void shouldStopTheAsyncDispatcherWhileItsSleepConsumesTheInterrupt() throws Exception {
        AsyncTaskServiceImpl service = asyncLifecycleService();
        Thread dispatcher = null;
        ScheduleExecutor executor = null;
        try {
            service.init();
            dispatcher = (Thread) ReflectionTestUtils.getField(service, "scheduleWorkThread");
            executor = (ScheduleExecutor) ReflectionTestUtils.getField(service, "scheduleExecutor");
            assertEquals("AsyncTask-Dispatcher", dispatcher.getName());
            assertTrue(dispatcher.isDaemon());
            service.init();
            assertSame(dispatcher, ReflectionTestUtils.getField(service, "scheduleWorkThread"));
            assertSame(executor, ReflectionTestUtils.getField(service, "scheduleExecutor"));
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
            while (dispatcher.getState() != Thread.State.TIMED_WAITING && System.nanoTime() < deadline) {
                Thread.sleep(10);
            }

            assertEquals(Thread.State.TIMED_WAITING, dispatcher.getState());
            service.stop();
            service.stop();
            dispatcher.join(2000);
            assertFalse(dispatcher.isAlive());
            service.init();
            assertSame(dispatcher, ReflectionTestUtils.getField(service, "scheduleWorkThread"));
            assertSame(executor, ReflectionTestUtils.getField(service, "scheduleExecutor"));
        } finally {
            service.stop();
            if (executor != null) {
                executor.close();
                for (Thread worker : (Thread[]) ReflectionTestUtils.getField(executor, "workerThreads")) {
                    worker.join(2000);
                    assertFalse(worker.isAlive());
                }
            }

            if (dispatcher != null) {
                dispatcher.join(2000);
            }
        }
    }

    @Test
    public void shouldNotCreateAsyncResourcesAfterStopBeforeInit() throws Exception {
        AsyncTaskServiceImpl service = asyncLifecycleService();
        service.stop();
        service.stop();
        service.init();

        assertNull(ReflectionTestUtils.getField(service, "scheduleWorkThread"));
        assertNull(ReflectionTestUtils.getField(service, "scheduleExecutor"));
    }

    @Test
    public void shouldCloseTheOwnedExecutorWhenAsyncDispatcherInitializationFails() throws Exception {
        AsyncTaskServiceImpl service = asyncLifecycleService();
        IllegalStateException failure = new IllegalStateException("dispatcher thread creation failed");
        try (MockedConstruction<ScheduleExecutor> executors = mockConstruction(ScheduleExecutor.class);
                MockedStatic<ThreadUtils> threads = mockStatic(ThreadUtils.class, invocation -> {
                    if (invocation.getMethod().getName().equals("daemonThread") && invocation.getArguments().length == 2) {
                        throw failure;
                    }
                    return invocation.callRealMethod();
                })) {
            try {
                try {
                    service.init();
                    fail("dispatcher initialization must fail");
                } catch (IllegalStateException expected) {
                    assertSame(failure, expected);
                }

                assertEquals(1, executors.constructed().size());
                ScheduleExecutor executor = executors.constructed().get(0);
                verify(executor).close();
                assertNull(ReflectionTestUtils.getField(service, "scheduleWorkThread"));
                service.stop();
                service.init();
                assertEquals(1, executors.constructed().size());
                verify(executor, times(1)).close();
            } finally {
                service.stop();
            }
        }
    }

    @Test(timeout = 5000)
    public void shouldKeepOneWorkerCheckerAndNeverReviveItAfterStop() throws Exception {
        WorkerServiceImpl service = new WorkerServiceImpl();
        ReflectionTestUtils.setField(service, "cluster", mock(ConsoleClusterService.class));
        Thread checker = null;
        try {
            service.init();
            checker = (Thread) ReflectionTestUtils.getField(service, "checker");
            assertEquals("checkWorkerStatus", checker.getName());
            assertTrue(checker.isDaemon());
            service.init();
            assertSame(checker, ReflectionTestUtils.getField(service, "checker"));
            service.stop();
            service.stop();
            checker.join(2000);
            assertFalse(checker.isAlive());
            service.init();
            assertSame(checker, ReflectionTestUtils.getField(service, "checker"));
            assertFalse(checker.isAlive());
        } finally {
            service.stop();
            if (checker != null) {
                checker.join(2000);
            }
        }
    }

    @Test
    public void shouldNotStartAWorkerCheckerAfterStopBeforeInit() {
        WorkerServiceImpl service = new WorkerServiceImpl();
        ReflectionTestUtils.setField(service, "cluster", mock(ConsoleClusterService.class));
        service.stop();
        service.stop();
        service.init();

        assertNull(ReflectionTestUtils.getField(service, "checker"));
    }

    @Test(timeout = 5000)
    public void shouldKeepAutoExecPoolsUntilStopAndNeverRecreateThem() throws Exception {
        AutoExecScheduleService service = autoLifecycleService();
        try {
            service.init();
            ThreadPoolExecutor workers = (ThreadPoolExecutor) ReflectionTestUtils.getField(service, "threadPoolExecutor");
            ScheduledThreadPoolExecutor timer = (ScheduledThreadPoolExecutor) ReflectionTestUtils.getField(service, "scheduledThreadPoolExecutor");
            assertEquals(10, workers.getCorePoolSize());
            assertEquals(10, workers.getMaximumPoolSize());
            assertEquals(1, workers.getKeepAliveTime(TimeUnit.MINUTES));
            assertEquals(2, workers.getQueue().remainingCapacity());
            assertEquals(2, timer.getCorePoolSize());
            assertTrue(workers.getRejectedExecutionHandler() instanceof ThreadPoolExecutor.AbortPolicy);
            service.init();
            assertSame(workers, ReflectionTestUtils.getField(service, "threadPoolExecutor"));
            assertSame(timer, ReflectionTestUtils.getField(service, "scheduledThreadPoolExecutor"));
            service.stop();
            service.stop();
            assertTrue(workers.awaitTermination(2, TimeUnit.SECONDS));
            assertTrue(timer.awaitTermination(2, TimeUnit.SECONDS));
            service.init();
            assertSame(workers, ReflectionTestUtils.getField(service, "threadPoolExecutor"));
            assertSame(timer, ReflectionTestUtils.getField(service, "scheduledThreadPoolExecutor"));
            assertTrue(workers.isShutdown());
            assertTrue(timer.isShutdown());
        } finally {
            service.stop();
        }
    }

    @Test
    public void shouldNotCreateAutoExecPoolsAfterStopBeforeInit() {
        AutoExecScheduleService service = autoLifecycleService();
        service.stop();
        service.stop();
        service.init();

        assertNull(ReflectionTestUtils.getField(service, "threadPoolExecutor"));
        assertNull(ReflectionTestUtils.getField(service, "scheduledThreadPoolExecutor"));
    }

    @Test
    public void shouldCloseBothAutoExecPoolsWhenSchedulingInitializationFails() {
        AutoExecScheduleService service = autoLifecycleService();
        IllegalStateException failure = new IllegalStateException("periodic task registration failed");
        try (MockedConstruction<ScheduledThreadPoolExecutor> timers = mockConstruction(ScheduledThreadPoolExecutor.class, (timer, context) -> {
            doThrow(failure).when(timer).scheduleWithFixedDelay(any(Runnable.class), eq(0L), eq(1L), eq(TimeUnit.MINUTES));
        })) {
            try {
                try {
                    service.init();
                    fail("periodic task registration must fail");
                } catch (IllegalStateException expected) {
                    assertSame(failure, expected);
                }

                ThreadPoolExecutor workers = (ThreadPoolExecutor) ReflectionTestUtils.getField(service, "threadPoolExecutor");
                assertTrue(workers.isShutdown());
                assertEquals(1, timers.constructed().size());
                ScheduledThreadPoolExecutor timer = timers.constructed().get(0);
                verify(timer).scheduleWithFixedDelay(any(Runnable.class), eq(5L), eq(5L), eq(TimeUnit.SECONDS));
                verify(timer).shutdownNow();
                service.stop();
                service.init();
                assertEquals(1, timers.constructed().size());
                verify(timer, times(1)).shutdownNow();
                assertSame(workers, ReflectionTestUtils.getField(service, "threadPoolExecutor"));
            } finally {
                service.stop();
            }
        }
    }

    private AsyncTaskServiceImpl asyncLifecycleService() {
        ConsoleConfig config = mock(ConsoleConfig.class);
        when(config.getAsyncThreadCount()).thenReturn(1);
        when(config.getAsyncTaskQueueSize()).thenReturn(2);
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getClassLoader()).thenReturn(getClass().getClassLoader());
        AsyncTaskServiceImpl service = new AsyncTaskServiceImpl();
        ReflectionTestUtils.setField(service, "config", config);
        ReflectionTestUtils.setField(service, "applicationContext", context);
        ReflectionTestUtils.setField(service, "cluster", mock(ConsoleClusterService.class));
        return service;
    }

    private AutoExecScheduleService autoLifecycleService() {
        ConsoleConfig config = mock(ConsoleConfig.class);
        when(config.getAsyncTaskQueueSize()).thenReturn(2);
        AutoExecScheduleService service = new AutoExecScheduleService();
        ReflectionTestUtils.setField(service, "config", config);
        ReflectionTestUtils.setField(service, "cluster", mock(ConsoleClusterService.class));
        return service;
    }

    @Test
    public void shouldContinueScanningAfterTransientLeaderLookupFailure() {
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.isSchedulerLeader()).thenThrow(new IllegalStateException("database temporarily unavailable")).thenReturn(true);
        ExecutionDal executionDal = mock(ExecutionDal.class);
        when(executionDal.autoJobMapper()).thenReturn(jobMapper);
        when(jobMapper.queryByStatusesAndLastReportTimeBefore(eq(List.of(AutoExecJobStatus.PACKAGING, AutoExecJobStatus.WAIT_EXEC)), any(Date.class))).thenReturn(List.of());
        AutoExecScheduleService scheduler = new AutoExecScheduleService();
        ReflectionTestUtils.setField(scheduler, "cluster", cluster);
        ReflectionTestUtils.setField(scheduler, "executionDal", executionDal);

        ReflectionTestUtils.invokeMethod(scheduler, "updateOverOutJob");
        ReflectionTestUtils.invokeMethod(scheduler, "updateOverOutJob");

        verify(jobMapper).queryByStatusesAndLastReportTimeBefore(eq(List.of(AutoExecJobStatus.PACKAGING, AutoExecJobStatus.WAIT_EXEC)), any(Date.class));
    }

    @Test
    public void shouldFailStaleDispatchWithoutResubmittingSql() {
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.isSchedulerLeader()).thenReturn(true);
        ExecutionDal executionDal = mock(ExecutionDal.class);
        when(executionDal.autoJobMapper()).thenReturn(jobMapper);
        DmExecAutoJobDO stale = new DmExecAutoJobDO();
        stale.setId(6L);
        stale.setDependOnBizId("approval-biz");
        stale.setStatus(AutoExecJobStatus.WAIT_EXEC);
        List<AutoExecJobStatus> statuses = List.of(AutoExecJobStatus.PACKAGING, AutoExecJobStatus.WAIT_EXEC);
        when(jobMapper.queryByStatusesAndLastReportTimeBefore(eq(statuses), any(Date.class))).thenReturn(List.of(stale));
        when(jobMapper.updateStatusByIdAndLastReportTimeBefore(eq(6L), eq(AutoExecJobStatus.FAILED), eq(statuses), any(Date.class))).thenReturn(1);
        AutoExecService autoExecService = mock(AutoExecService.class);
        AutoExecScheduleService scheduler = new AutoExecScheduleService();
        ReflectionTestUtils.setField(scheduler, "cluster", cluster);
        ReflectionTestUtils.setField(scheduler, "executionDal", executionDal);
        ReflectionTestUtils.setField(scheduler, "approvalStateService", approvalStateService);
        ReflectionTestUtils.setField(scheduler, "autoExecService", autoExecService);

        ReflectionTestUtils.invokeMethod(scheduler, "updateOverOutJob");

        ArgumentCaptor<Date> before = ArgumentCaptor.forClass(Date.class);
        verify(jobMapper).queryByStatusesAndLastReportTimeBefore(eq(statuses), before.capture());
        verify(jobMapper).updateStatusByIdAndLastReportTimeBefore(6L, AutoExecJobStatus.FAILED, statuses, before.getValue());
        verify(approvalStateService).failExecution(eq("approval-biz"), anyString());
        verifyNoInteractions(autoExecService);
        verify(jobMapper, never()).retryJob(anyLong());
        verify(jobMapper, never()).claimJobForPackaging(anyLong());
    }

    @Test
    public void shouldNotFailTicketWhenStaleStatusOrHeartbeatChangesBeforeUpdate() {
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.isSchedulerLeader()).thenReturn(true);
        ExecutionDal executionDal = mock(ExecutionDal.class);
        when(executionDal.autoJobMapper()).thenReturn(jobMapper);
        DmExecAutoJobDO stale = new DmExecAutoJobDO();
        stale.setId(6L);
        stale.setDependOnBizId("approval-biz");
        List<AutoExecJobStatus> statuses = List.of(AutoExecJobStatus.PACKAGING, AutoExecJobStatus.WAIT_EXEC);
        when(jobMapper.queryByStatusesAndLastReportTimeBefore(eq(statuses), any(Date.class))).thenReturn(List.of(stale));
        AutoExecScheduleService scheduler = new AutoExecScheduleService();
        ReflectionTestUtils.setField(scheduler, "cluster", cluster);
        ReflectionTestUtils.setField(scheduler, "executionDal", executionDal);
        ReflectionTestUtils.setField(scheduler, "approvalStateService", approvalStateService);

        ReflectionTestUtils.invokeMethod(scheduler, "updateOverOutJob");

        verify(jobMapper).updateStatusByIdAndLastReportTimeBefore(eq(6L), eq(AutoExecJobStatus.FAILED), eq(statuses), any(Date.class));
        verifyNoInteractions(approvalStateService);
    }

    @Test
    public void shouldOfferOnlyManualEndForStaleRunningJob() {
        DmExecAutoJobDO job = jobMapper.queryById(6L);
        job.setLastReportTime(new Date(System.currentTimeMillis() - 6 * 60 * 1000));
        for (AutoExecJobStatus status : new AutoExecJobStatus[] { AutoExecJobStatus.EXECUTING, AutoExecJobStatus.PAUSING }) {
            job.setStatus(status);

            DmAutoExecJobVO result = dispatchService.queryAutoExecJob("approval-biz", true);

            assertEquals(status, result.getStatus());
            assertFalse(result.isNormal());
            assertEquals(DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_EXECUTION_UNKNOWN.name()), result.getMessage());
            assertTrue(result.isCanEnd());
            assertFalse(result.isCanRetry());
            assertFalse(result.isCanRestart());
            assertFalse(result.isCanPause());
            assertEquals(status, job.getStatus());
        }

        verify(jobMapper, never()).updateById(any(DmExecAutoJobDO.class));
        verifyNoInteractions(approvalStateService);
    }

    @Test
    public void shouldKeepLongRunningJobActiveWhileHeartbeatsContinue() {
        DmExecAutoJobDO job = jobMapper.queryById(6L);
        job.setScheduleTime(new Date(System.currentTimeMillis() - 60 * 60 * 1000));

        DmAutoExecJobVO result = dispatchService.queryAutoExecJob("approval-biz", true);

        assertTrue(result.isNormal());
        assertTrue(result.isCanPause());
        assertFalse(result.isCanEnd());
        assertFalse(result.isCanRetry());
    }

    @Test
    public void shouldEndStaleRunningJobWithoutSendingOrResubmittingSql() {
        DmExecAutoJobDO job = jobMapper.queryById(6L);
        job.setLastReportTime(new Date(System.currentTimeMillis() - 6 * 60 * 1000));
        AutoExecRService execRService = mock(AutoExecRService.class);
        ReflectionTestUtils.setField(dispatchService, "execRService", execRService);
        for (AutoExecJobStatus status : new AutoExecJobStatus[] { AutoExecJobStatus.EXECUTING, AutoExecJobStatus.PAUSING }) {
            job.setStatus(status);

            dispatchService.endJob("approval-biz");

            assertEquals(AutoExecJobStatus.TERMINATION, job.getStatus());
        }

        verify(jobMapper, times(2)).queryByIdForUpdate(6L);
        verify(taskMapper, times(2)).cancelAllWaitTask(6L);
        verify(approvalStateService, times(2)).cancelExecution("approval-biz");
        verify(bizLogMapper, times(2)).insert(argThat((DmMonBizLogDO log) -> log.getLogLevel() == Loglevel.WARING && log.getDependOnBizType() == LogDependBizType.AUTO_EXEC_JOB));
        verify(jobMapper, never()).retryJob(anyLong());
        verify(jobMapper, never()).claimJobForPackaging(anyLong());
        verifyNoInteractions(execRService);
    }

    @Test
    public void shouldRejectManualEndWhenHeartbeatResumesBeforeLock() {
        DmExecAutoJobDO observed = jobMapper.queryById(6L);
        observed.setLastReportTime(new Date(System.currentTimeMillis() - 6 * 60 * 1000));
        DmExecAutoJobDO current = new DmExecAutoJobDO();
        current.setId(6L);
        current.setStatus(AutoExecJobStatus.EXECUTING);
        current.setLastReportTime(new Date());
        when(jobMapper.queryByIdForUpdate(6L)).thenReturn(current);

        try {
            dispatchService.endJob("approval-biz");
            fail("Expected resumed heartbeat to reject manual end");
        } catch (ErrorMessageException expected) {
            assertEquals(AutoExecJobStatus.EXECUTING, current.getStatus());
        }

        verify(jobMapper, never()).updateById(any(DmExecAutoJobDO.class));
        verify(taskMapper, never()).cancelAllWaitTask(anyLong());
        verifyNoInteractions(approvalStateService, bizLogMapper);
    }

    @Test
    public void shouldRejectRetryForStaleRunningJob() {
        DmExecAutoJobDO job = jobMapper.queryById(6L);
        job.setLastReportTime(new Date(System.currentTimeMillis() - 6 * 60 * 1000));
        for (AutoExecJobStatus status : new AutoExecJobStatus[] { AutoExecJobStatus.EXECUTING, AutoExecJobStatus.PAUSING }) {
            job.setStatus(status);
            try {
                dispatchService.retryJob("approval-biz");
                fail("Expected unknown execution to reject retry");
            } catch (ErrorMessageException expected) {
                assertEquals(status, job.getStatus());
            }
        }

        verify(jobMapper, never()).retryJob(anyLong());
        verify(taskMapper, never()).retryTask(anyLong());
    }

    @Test
    public void shouldIgnoreLatePauseAfterManualTermination() {
        DmExecAutoJobDO job = jobMapper.queryById(6L);
        job.setStatus(AutoExecJobStatus.TERMINATION);
        AutoExecRService execRService = mock(AutoExecRService.class);
        ReflectionTestUtils.setField(dispatchService, "execRService", execRService);

        dispatchService.stopJob("approval-biz");

        assertEquals(AutoExecJobStatus.TERMINATION, job.getStatus());
        verify(jobMapper, never()).updateById(any(DmExecAutoJobDO.class));
        verifyNoInteractions(execRService, approvalStateService);
    }

    @Test
    public void shouldIgnoreLateTaskStartAfterLockedJobTerminalTransition() {
        DmExecAutoTaskDO task = taskMapper.queryByQueryId("task-query");
        task.setStatus(AutoExecTaskStatus.WAIT_EXEC);
        DmExecAutoJobDO current = new DmExecAutoJobDO();
        current.setId(6L);
        when(jobMapper.queryByIdForUpdate(6L)).thenReturn(current);
        for (AutoExecJobStatus terminal : new AutoExecJobStatus[] { AutoExecJobStatus.FINISH, AutoExecJobStatus.FAILED, AutoExecJobStatus.TERMINATION }) {
            current.setStatus(terminal);

            ReflectionTestUtils.invokeMethod(provider, "taskStart", AutoExecMessageDTO.taskStartMessage("task-query"));

            assertEquals(AutoExecTaskStatus.WAIT_EXEC, task.getStatus());
        }

        verify(jobMapper, times(3)).queryByIdForUpdate(6L);
        verify(taskMapper, never()).updateById(any(DmExecAutoTaskDO.class));
        verifyNoInteractions(approvalStateService);
    }

    @Test
    public void shouldLockParentJobBeforeMarkingTaskAndTicketRunning() {
        DmExecAutoTaskDO task = taskMapper.queryByQueryId("task-query");
        task.setStatus(AutoExecTaskStatus.WAIT_EXEC);

        ReflectionTestUtils.invokeMethod(provider, "taskStart", AutoExecMessageDTO.taskStartMessage("task-query"));

        var order = inOrder(jobMapper, taskMapper, approvalStateService);
        order.verify(jobMapper).queryByIdForUpdate(6L);
        order.verify(taskMapper).updateById(argThat((DmExecAutoTaskDO update) -> update.getStatus() == AutoExecTaskStatus.EXECUTING));
        order.verify(approvalStateService).markExecutionRunning("approval-biz");
    }

    @Test
    public void shouldIgnoreLateStartForCanceledOrFinishedTask() {
        DmExecAutoTaskDO task = taskMapper.queryByQueryId("task-query");
        for (AutoExecTaskStatus terminal : new AutoExecTaskStatus[] { AutoExecTaskStatus.CANCELED, AutoExecTaskStatus.FINISH }) {
            task.setStatus(terminal);

            ReflectionTestUtils.invokeMethod(provider, "taskStart", AutoExecMessageDTO.taskStartMessage("task-query"));

            assertEquals(terminal, task.getStatus());
        }

        verify(taskMapper, never()).updateById(any(DmExecAutoTaskDO.class));
        verifyNoInteractions(approvalStateService);
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

    @Test
    public void shouldCancelInactiveAsyncTaskWithoutContactingItsDeadOwner() {
        DmExecAsyncTaskMapper mapper = mock(DmExecAsyncTaskMapper.class);
        ConsoleTaskRouter router = mock(ConsoleTaskRouter.class);
        AsyncTaskServiceImpl service = asyncControls(mapper, router);
        when(mapper.updateFromInitTo(18L, AsyncTaskStatus.CANCEL.name(), "cancel")).thenReturn(1);
        when(mapper.updateFromBlockTo(18L, AsyncTaskStatus.CANCEL.name(), "cancel")).thenReturn(1);
        when(mapper.updateFromPauseTo(18L, AsyncTaskStatus.CANCEL.name(), "cancel")).thenReturn(1);
        try (MockedStatic<DmGlobalEventBus> events = mockStatic(DmGlobalEventBus.class)) {
            for (AsyncTaskStatus status : new AsyncTaskStatus[] { AsyncTaskStatus.INIT, AsyncTaskStatus.BLOCK, AsyncTaskStatus.PAUSE }) {
                DmExecAsyncTaskDO task = new DmExecAsyncTaskDO();
                task.setId(18L);
                task.setStatus(status);
                task.setConsoleInstanceId("dead-console");
                when(mapper.queryById(18L)).thenReturn(task);

                service.cancelTask(18L, "cancel");

                assertEquals(AsyncTaskStatus.CANCEL, task.getStatus());
                events.verify(() -> DmGlobalEventBus.triggerDmAsyncEvent(task));
            }
        }

        verifyNoInteractions(router);
    }

    @Test
    public void shouldNotPublishCancellationWhenInactiveTaskStartsConcurrently() {
        DmExecAsyncTaskMapper mapper = mock(DmExecAsyncTaskMapper.class);
        ConsoleTaskRouter router = mock(ConsoleTaskRouter.class);
        AsyncTaskServiceImpl service = asyncControls(mapper, router);
        try (MockedStatic<DmGlobalEventBus> events = mockStatic(DmGlobalEventBus.class)) {
            for (AsyncTaskStatus status : new AsyncTaskStatus[] { AsyncTaskStatus.INIT, AsyncTaskStatus.BLOCK, AsyncTaskStatus.PAUSE }) {
                DmExecAsyncTaskDO task = new DmExecAsyncTaskDO();
                task.setId(18L);
                task.setStatus(status);
                task.setConsoleInstanceId("dead-console");
                when(mapper.queryById(18L)).thenReturn(task);
                doAnswer(invocation -> {
                    task.setStatus(AsyncTaskStatus.RUNNING);
                    return 0;
                }).when(mapper).updateFromInitTo(18L, AsyncTaskStatus.CANCEL.name(), "cancel");
                doAnswer(invocation -> {
                    task.setStatus(AsyncTaskStatus.RUNNING);
                    return 0;
                }).when(mapper).updateFromBlockTo(18L, AsyncTaskStatus.CANCEL.name(), "cancel");
                doAnswer(invocation -> {
                    task.setStatus(AsyncTaskStatus.RUNNING);
                    return 0;
                }).when(mapper).updateFromPauseTo(18L, AsyncTaskStatus.CANCEL.name(), "cancel");

                service.cancelTask(18L, "cancel");

                assertEquals(AsyncTaskStatus.RUNNING, task.getStatus());
            }

            events.verifyNoInteractions();
        }

        verifyNoInteractions(router);
        verify(mapper, never()).updateStatusTo(anyLong(), anyString(), anyString());
    }

    @Test
    public void shouldKeepRunningAsyncControlOnItsOwner() {
        DmExecAsyncTaskMapper mapper = mock(DmExecAsyncTaskMapper.class);
        ConsoleTaskRouter router = mock(ConsoleTaskRouter.class);
        AsyncTaskServiceImpl service = asyncControls(mapper, router);
        DmExecAsyncTaskDO task = new DmExecAsyncTaskDO();
        task.setId(18L);
        task.setStatus(AsyncTaskStatus.RUNNING);
        task.setConsoleInstanceId("original-console");
        when(mapper.queryById(18L)).thenReturn(task);
        when(router.forwardAsyncControl(any(), any(ConsoleTaskControlAction.class), anyString())).thenReturn(true);
        try (MockedStatic<DmGlobalEventBus> events = mockStatic(DmGlobalEventBus.class)) {
            service.cancelTask(18L, "cancel");
            service.pauseTask(18L, "pause");

            assertEquals(AsyncTaskStatus.RUNNING, task.getStatus());
            verify(router).forwardAsyncControl(task, ConsoleTaskControlAction.CANCEL_ASYNC, "cancel");
            verify(router).forwardAsyncControl(task, ConsoleTaskControlAction.PAUSE_ASYNC, "pause");
            events.verifyNoInteractions();
        }

        verify(mapper, never()).updateFromRunningTo(anyLong(), anyString(), anyString());
        verify(mapper, never()).updateStatusTo(anyLong(), anyString(), anyString());
    }

    @Test
    public void shouldPauseBlockedAsyncTaskLocallyOnlyAfterSuccessfulCas() {
        DmExecAsyncTaskMapper mapper = mock(DmExecAsyncTaskMapper.class);
        ConsoleTaskRouter router = mock(ConsoleTaskRouter.class);
        AsyncTaskServiceImpl service = asyncControls(mapper, router);
        DmExecAsyncTaskDO task = new DmExecAsyncTaskDO();
        task.setId(18L);
        task.setStatus(AsyncTaskStatus.BLOCK);
        task.setConsoleInstanceId("dead-console");
        when(mapper.queryById(18L)).thenReturn(task);
        when(mapper.updateFromBlockTo(18L, AsyncTaskStatus.PAUSE.name(), "pause")).thenReturn(1);
        try (MockedStatic<DmGlobalEventBus> events = mockStatic(DmGlobalEventBus.class)) {
            service.pauseTask(18L, "pause");

            assertEquals(AsyncTaskStatus.PAUSE, task.getStatus());
            events.verify(() -> DmGlobalEventBus.triggerDmAsyncEvent(task));
            events.clearInvocations();
            task.setStatus(AsyncTaskStatus.BLOCK);
            doAnswer(invocation -> {
                task.setStatus(AsyncTaskStatus.RUNNING);
                return 0;
            }).when(mapper).updateFromBlockTo(18L, AsyncTaskStatus.PAUSE.name(), "pause");

            service.pauseTask(18L, "pause");

            assertEquals(AsyncTaskStatus.RUNNING, task.getStatus());
            events.verifyNoInteractions();
        }

        verifyNoInteractions(router);
    }

    @Test
    public void shouldIgnoreQueuedTransitioningAndFinishedAsyncControlWithoutContactingItsOwner() {
        DmExecAsyncTaskMapper mapper = mock(DmExecAsyncTaskMapper.class);
        ConsoleTaskRouter router = mock(ConsoleTaskRouter.class);
        AsyncTaskServiceImpl service = asyncControls(mapper, router);
        try (MockedStatic<DmGlobalEventBus> events = mockStatic(DmGlobalEventBus.class)) {
            for (AsyncTaskStatus status : new AsyncTaskStatus[] { AsyncTaskStatus.WAIT_START, AsyncTaskStatus.PAUSING, AsyncTaskStatus.CANCELING, AsyncTaskStatus.FAILURE,
                                                                  AsyncTaskStatus.CANCEL, AsyncTaskStatus.COMPLETE }) {
                DmExecAsyncTaskDO task = new DmExecAsyncTaskDO();
                task.setId(18L);
                task.setStatus(status);
                task.setConsoleInstanceId("dead-console");
                when(mapper.queryById(18L)).thenReturn(task);

                service.cancelTask(18L, "cancel");
                service.pauseTask(18L, "pause");

                assertEquals(status, task.getStatus());
            }

            events.verifyNoInteractions();
        }

        verifyNoInteractions(router);
    }

    private AsyncTaskServiceImpl asyncControls(DmExecAsyncTaskMapper mapper, ConsoleTaskRouter router) {
        ExecutionDal dal = mock(ExecutionDal.class);
        when(dal.asyncTaskMapper()).thenReturn(mapper);
        AsyncTaskServiceImpl service = new AsyncTaskServiceImpl();
        ReflectionTestUtils.setField(service, "executionDal", dal);
        ReflectionTestUtils.setField(service, "taskRouter", router);
        ReflectionTestUtils.setField(service, "scheduleTaskMap", Map.of());
        return service;
    }
}

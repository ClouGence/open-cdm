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
package com.clougence.clouddm.console.web.component.approval.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.LongStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.ApprovalPersonService;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.approval.handler.ChangeApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.handler.QueryApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.component.approval.schedule.ApprovalTaskProcessor;
import com.clougence.clouddm.console.web.component.approval.schedule.ApprovalTaskScheduler;
import com.clougence.clouddm.console.web.service.datasource.DmDsWebService;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.mapper.approval.DmApprovalMapper;
import com.clougence.clouddm.platform.dal.mapper.approval.DmApprovalPersonMapper;
import com.clougence.clouddm.platform.dal.mapper.approval.DmApprovalProcessMapper;
import com.clougence.clouddm.platform.dal.mapper.auth.DmAuthResMapper;
import com.clougence.clouddm.platform.dal.mapper.auth.DmAuthUserMapper;
import com.clougence.clouddm.platform.dal.mapper.system.DmSysUserConfMapper;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.utils.JsonUtils;

public class ApprovalFlowServiceImplTest {

    private ApprovalDal             approvalDal;
    private DmApprovalMapper        approvalMapper;
    private DmApprovalPersonMapper  personMapper;
    private DmApprovalProcessMapper processMapper;
    private DmAuthResMapper         authResMapper;
    private DmAuthUserMapper        authUserMapper;
    private ApprovalStateService    approvalStateService;
    private ApprovalFlowServiceImpl flowService;

    @Before
    public void setUp() {
        approvalDal = mock(ApprovalDal.class);
        approvalMapper = mock(DmApprovalMapper.class);
        personMapper = mock(DmApprovalPersonMapper.class);
        processMapper = mock(DmApprovalProcessMapper.class);
        when(approvalDal.approvalMapper()).thenReturn(approvalMapper);
        when(approvalDal.personMapper()).thenReturn(personMapper);
        when(approvalDal.processMapper()).thenReturn(processMapper);

        AuthDal authDal = mock(AuthDal.class);
        authResMapper = mock(DmAuthResMapper.class);
        authUserMapper = mock(DmAuthUserMapper.class);
        when(authDal.resMapper()).thenReturn(authResMapper);
        when(authDal.userMapper()).thenReturn(authUserMapper);

        ChangeApprovalHandler handler = new ChangeApprovalHandler();
        ReflectionTestUtils.setField(handler, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(handler, "authDal", authDal);
        QueryApprovalHandler queryHandler = new QueryApprovalHandler();
        ReflectionTestUtils.setField(queryHandler, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(queryHandler, "authDal", authDal);

        ApprovalPersonService personService = new ApprovalPersonService();
        ReflectionTestUtils.setField(personService, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(personService, "authDal", authDal);

        approvalStateService = mock(ApprovalStateService.class);
        flowService = new ApprovalFlowServiceImpl(Arrays.asList(handler, queryHandler));
        ReflectionTestUtils.setField(flowService, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(flowService, "authDal", authDal);
        ReflectionTestUtils.setField(flowService, "approvalStateService", approvalStateService);
        ReflectionTestUtils.setField(flowService, "approvalPersonService", personService);
    }

    @Test
    public void shouldInitializeCompleteChangeApprovalUsersBeforeCreatingApprovalProcess() {
        assertCompleteApprovalUsers(ApprovalBiz.DM_CHANGE);
    }

    @Test
    public void shouldInitializeCompleteQueryApprovalUsersBeforeCreatingApprovalProcess() {
        assertCompleteApprovalUsers(ApprovalBiz.DM_QUERY);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void assertCompleteApprovalUsers(ApprovalBiz approvalBiz) {
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        ticket.setBizId("change-101");
        ticket.setOwnerUid("requester");
        ticket.setPrimaryUid("primary");
        ticket.setBindDsId(91L);
        ticket.setLevels(Collections.singletonList("schema_a"));
        ticket.setApproBiz(approvalBiz);
        ticket.setApproType(ApprovalType.Internal);
        when(approvalMapper.queryById(11L)).thenReturn(ticket);

        DmAuthUserDO primary = user("primary", "admin");
        primary.setId(1L);
        DmAuthUserDO eligible = user("eligible", "owner");
        DmAuthUserDO global = user("global", "global-user");
        DmAuthUserDO requester = user("requester", "requester-user");
        when(authUserMapper.queryByUid(anyString())).thenAnswer(invocation -> {
            String uid = invocation.getArgument(0);
            return switch (uid) {
                case "primary" -> primary;
                case "eligible" -> eligible;
                case "global" -> global;
                case "requester" -> requester;
                default -> null;
            };
        });

        RsAuthPersonObj eligiblePerson = person("eligible", "owner", //
                Collections.singletonList(SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE), //
                Collections.singletonList(SecDataAuthLabel.DM_DAUTH_TICKET));
        RsAuthPersonObj missingTicketRole = person("no-role", "no-role-user", //
                Collections.singletonList(SecRoleAuthLabel.RDP_WORKER_ORDER_READ), //
                Collections.singletonList(SecDataAuthLabel.DM_DAUTH_TICKET));
        RsAuthPersonObj missingDatasourceAuth = person("no-ds-auth", "no-ds-auth-user", //
                Collections.singletonList(SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE), //
                Collections.singletonList(SecDataAuthLabel.DM_DAUTH_QUERY));
        when(authUserMapper.queryApproPerson(AccountType.SUB_ACCOUNT, 1L, 91L, "/schema_a/")).thenReturn(Arrays.asList(eligiblePerson, missingTicketRole, missingDatasourceAuth));
        when(authResMapper.listEffectiveGlobalAuthUsersByPrimaryUid("primary", AuthKind.DataSource)).thenReturn(Collections.singletonList(global));
        when(personMapper.queryByTicketBzId("change-101")).thenReturn(Collections.emptyList());

        List<DmApprovalProcessDO> initialized = new ArrayList<>();
        when(approvalStateService.initializeProcess(eq(11L), any(), eq(ApprovalProcessStatus.INIT), any())).thenAnswer(invocation -> {
            DmApprovalProcessDO process = new DmApprovalProcessDO();
            process.setId((long) initialized.size() + 1);
            process.setTicketId(11L);
            process.setTicketStage(invocation.getArgument(1));
            process.setProcessStatus(ApprovalProcessStatus.INIT);
            process.setStageContext(invocation.getArgument(3));
            initialized.add(process);
            return process;
        });

        flowService.createProcess(11L, approvalBiz, true);

        verify(authUserMapper).queryApproPerson(AccountType.SUB_ACCOUNT, 1L, 91L, "/schema_a/");
        verify(authResMapper).listEffectiveGlobalAuthUsersByPrimaryUid("primary", AuthKind.DataSource);

        ArgumentCaptor<List> personCaptor = ArgumentCaptor.forClass(List.class);
        verify(personMapper).insertPersonBatch(personCaptor.capture());
        List<DmApprovalPersonDO> inserted = personCaptor.getValue();
        assertEquals(Arrays.asList("primary", "eligible", "global"), inserted.stream().map(DmApprovalPersonDO::getPersonUid).toList());

        DmApprovalProcessDO approvalProcess = initialized.stream()//
            .filter(process -> process.getTicketStage() == ApprovalStage.APPROVAL)
            .findFirst()
            .orElseThrow();
        ApprovalStageMO stage = JsonUtils.toObj(approvalProcess.getStageContext(), ApprovalStageMO.class);
        assertEquals(Arrays.asList("admin", "owner", "global-user"), stage.getExecUserName());
    }

    @Test
    public void shouldInspectIdleTicketsWithoutLoadingSqlOrWriting() {
        ThreadPoolExecutor executor = mock(ThreadPoolExecutor.class);
        doAnswer(invocation -> {
            invocation.<Runnable> getArgument(0).run();
            return null;
        }).when(executor).execute(any(Runnable.class));
        ApprovalTaskScheduler scheduler = scheduler(executor);
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        ticket.setBindDsId(91L);
        ticket.setApproBiz(ApprovalBiz.DM_QUERY);
        ticket.setApproType(ApprovalType.Internal);
        when(approvalMapper.queryScheduleInfo(11L)).thenReturn(ticket);

        for (ApprovalStatus status : List.of(ApprovalStatus.WAIT_APPROVAL, ApprovalStatus.EXEC_FAIL)) {
            ticket.setTicketStatus(status);
            assertTrue(scheduler.trySchedule(11L));
            assertTrue(scheduler.trySchedule(11L));
        }

        verify(approvalMapper, times(4)).queryScheduleInfo(11L);
        verifyNoMoreInteractions(approvalMapper);
        verifyNoInteractions(personMapper, processMapper);
    }

    @Test
    public void shouldVisitEveryPageWithoutChangingModificationTimes() {
        ThreadPoolExecutor executor = mock(ThreadPoolExecutor.class);
        ApprovalTaskScheduler scheduler = scheduler(executor);
        Object scan = ReflectionTestUtils.getField(scheduler, "activeScan");
        when(approvalMapper.queryScheduleUpperId()).thenReturn(45L, 100L);
        when(approvalMapper.listScheduleTicketIds(anyLong(), eq(45L), eq(false), eq(20))).thenAnswer(invocation -> {
            long afterId = invocation.getArgument(0);
            return LongStream.rangeClosed(1, 45).filter(id -> id > afterId).limit(20).boxed().toList();
        });

        for (int page = 0; page < 3; page++) {
            ReflectionTestUtils.invokeMethod(scheduler, "doSchedule", scan);
        }
        ReflectionTestUtils.invokeMethod(scheduler, "doSchedule", scan);

        verify(executor, times(45)).execute(any(Runnable.class));
        verify(approvalMapper).queryScheduleUpperId();
        verify(approvalMapper).listScheduleTicketIds(0, 45, false, 20);
        verify(approvalMapper).listScheduleTicketIds(20, 45, false, 20);
        verify(approvalMapper).listScheduleTicketIds(40, 45, false, 20);
        assertEquals(0L, ReflectionTestUtils.getField(scan, "afterId"));
        assertEquals(0L, ReflectionTestUtils.getField(scan, "upperId"));
        assertTrue((long) ReflectionTestUtils.getField(scan, "nextScanTime") > System.currentTimeMillis());
        verifyNoMoreInteractions(approvalMapper);
    }

    @Test
    public void shouldResumeRejectedTicketAndReleaseItsQueueMarker() {
        ThreadPoolExecutor executor = mock(ThreadPoolExecutor.class);
        doThrow(new RejectedExecutionException("full")).doNothing().when(executor).execute(any(Runnable.class));
        ApprovalTaskScheduler scheduler = scheduler(executor);
        Object scan = ReflectionTestUtils.getField(scheduler, "activeScan");
        when(approvalMapper.queryScheduleUpperId()).thenReturn(2L);
        when(approvalMapper.listScheduleTicketIds(0, 2, false, 20)).thenReturn(List.of(1L, 2L));

        ReflectionTestUtils.invokeMethod(scheduler, "doSchedule", scan);
        assertEquals(0L, ReflectionTestUtils.getField(scan, "afterId"));
        assertTrue(((Set<?>) ReflectionTestUtils.getField(scheduler, "taskInQueueSet")).isEmpty());
        ReflectionTestUtils.invokeMethod(scheduler, "doSchedule", scan);

        assertEquals(Set.of(1L, 2L), ReflectionTestUtils.getField(scheduler, "taskInQueueSet"));
        verify(executor, times(3)).execute(any(Runnable.class));
        verify(approvalMapper).queryScheduleUpperId();
    }

    @Test
    public void shouldKeepIdleScanIndependentAndSkipAlreadyQueuedTickets() {
        ThreadPoolExecutor executor = mock(ThreadPoolExecutor.class);
        ApprovalTaskScheduler scheduler = scheduler(executor);
        Object activeScan = ReflectionTestUtils.getField(scheduler, "activeScan");
        Object idleScan = ReflectionTestUtils.getField(scheduler, "idleScan");
        when(approvalMapper.queryScheduleUpperId()).thenReturn(2L);
        when(approvalMapper.listScheduleTicketIds(0, 2, false, 20)).thenReturn(List.of(1L));
        when(approvalMapper.listScheduleTicketIds(0, 2, true, 20)).thenReturn(List.of(2L));
        assertTrue(scheduler.trySchedule(1L));

        ReflectionTestUtils.invokeMethod(scheduler, "doSchedule", activeScan);
        ReflectionTestUtils.invokeMethod(scheduler, "doSchedule", idleScan);

        verify(executor, times(2)).execute(any(Runnable.class));
        assertEquals(5000L, ReflectionTestUtils.getField(activeScan, "intervalMillis"));
        assertEquals(60000L, ReflectionTestUtils.getField(idleScan, "intervalMillis"));
        assertTrue((long) ReflectionTestUtils.getField(idleScan, "nextScanTime") > (long) ReflectionTestUtils.getField(activeScan, "nextScanTime"));
    }

    @Test
    public void shouldRetryAfterWorkerFailureAndIgnoreTicketsClosedWhileQueued() {
        ThreadPoolExecutor executor = mock(ThreadPoolExecutor.class);
        doAnswer(invocation -> {
            invocation.<Runnable> getArgument(0).run();
            return null;
        }).when(executor).execute(any(Runnable.class));
        ApprovalTaskScheduler scheduler = scheduler(executor);
        DmApprovalDO closed = new DmApprovalDO();
        closed.setTicketStatus(ApprovalStatus.CLOSED);
        when(approvalMapper.queryScheduleInfo(11L)).thenThrow(new IllegalStateException("connection lost")).thenReturn(closed).thenReturn(null);

        assertTrue(scheduler.trySchedule(11L));
        assertTrue(scheduler.trySchedule(11L));
        assertTrue(scheduler.trySchedule(11L));

        verify(approvalMapper, times(3)).queryScheduleInfo(11L);
        verifyNoMoreInteractions(approvalMapper);
        assertTrue(((Set<?>) ReflectionTestUtils.getField(scheduler, "taskInQueueSet")).isEmpty());
    }

    @Test
    public void shouldPreserveConfirmationContextAndSkipReorderedPeople() {
        ApprovalTaskProcessor processor = new ApprovalTaskProcessor(Collections.emptyList());
        ApprovalPersonService personService = mock(ApprovalPersonService.class);
        ReflectionTestUtils.setField(processor, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(processor, "approvalPersonService", personService);
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        DmApprovalProcessDO process = new DmApprovalProcessDO();
        process.setId(12L);
        ApprovalStageMO context = new ApprovalStageMO();
        context.setExecUserName(List.of("alice", "bob"));
        context.setExecMsg("keep confirmation message");
        context.setAutoExecute(true);
        process.setStageContext(JsonUtils.toJson(context));
        when(processMapper.queryByStage(11L, ApprovalStage.CONFIRM)).thenReturn(process);
        when(personService.replacePersons(eq(ticket), anyList())).thenReturn(List.of("bob", "alice"), List.of("alice", "carol"));

        ReflectionTestUtils.invokeMethod(processor, "updatePerson", Collections.emptyList(), ticket, ApprovalStage.CONFIRM);
        verify(processMapper, never()).updateContextById(anyLong(), anyString());
        ReflectionTestUtils.invokeMethod(processor, "updatePerson", Collections.emptyList(), ticket, ApprovalStage.CONFIRM);

        ArgumentCaptor<String> written = ArgumentCaptor.forClass(String.class);
        verify(processMapper).updateContextById(eq(12L), written.capture());
        ApprovalStageMO changed = JsonUtils.toObj(written.getValue(), ApprovalStageMO.class);
        assertEquals(List.of("alice", "carol"), changed.getExecUserName());
        assertEquals("keep confirmation message", changed.getExecMsg());
        assertTrue(changed.isAutoExecute());
    }

    @Test
    public void shouldNotReplacePeopleAfterConfirmationCompletes() {
        ApprovalTaskProcessor processor = new ApprovalTaskProcessor(Collections.emptyList());
        ApprovalPersonService personService = mock(ApprovalPersonService.class);
        ReflectionTestUtils.setField(processor, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(processor, "approvalPersonService", personService);
        DmApprovalDO oldTicket = new DmApprovalDO();
        oldTicket.setId(11L);
        oldTicket.setTicketStatus(ApprovalStatus.WAIT_CONFIRM);
        DmApprovalDO confirmed = new DmApprovalDO();
        confirmed.setTicketStatus(ApprovalStatus.WAIT_EXEC);
        when(approvalMapper.selectByIdForUpdate(11L)).thenReturn(confirmed);

        processor.processWaitConfirm(oldTicket);

        verify(approvalMapper).selectByIdForUpdate(11L);
        verifyNoInteractions(personService, personMapper, processMapper);
    }

    @Test
    public void shouldKeepExternalRefreshIntervalSemantics() {
        ApprovalTaskProcessor processor = new ApprovalTaskProcessor(Collections.emptyList());
        SystemDal systemDal = mock(SystemDal.class);
        DmSysUserConfMapper configMapper = mock(DmSysUserConfMapper.class);
        ApprovalProviderServiceImpl provider = mock(ApprovalProviderServiceImpl.class);
        when(systemDal.userConfMapper()).thenReturn(configMapper);
        ReflectionTestUtils.setField(processor, "systemDal", systemDal);
        ReflectionTestUtils.setField(processor, "approvalProviderService", provider);
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        ticket.setPrimaryUid("primary");
        DmApprovalProcessDO process = new DmApprovalProcessDO();
        process.setGmtModified(new Date(System.currentTimeMillis() - 10000));

        ReflectionTestUtils.invokeMethod(processor, "getLastInfoIfNecessary", process, ticket);
        DmSysUserConfDO config = new DmSysUserConfDO();
        when(configMapper.queryByUidAndConfigName(eq("primary"), anyString())).thenReturn(config);
        for (String interval : List.of("0", "-1", "86400")) {
            config.setConfigValue(interval);
            ReflectionTestUtils.invokeMethod(processor, "getLastInfoIfNecessary", process, ticket);
        }
        verifyNoInteractions(provider);
        config.setConfigValue("1");
        ReflectionTestUtils.invokeMethod(processor, "getLastInfoIfNecessary", process, ticket);
        verify(provider).refreshApprovalStatus(11L);
    }

    @Test
    public void shouldNotRewriteExecutionStatesAlreadyReported() {
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        for (ApprovalHandler handler : List.of(new QueryApprovalHandler(), new ChangeApprovalHandler())) {
            ReflectionTestUtils.setField(handler, "approvalStateService", approvalStateService);
            ticket.setTicketStatus(ApprovalStatus.EXEC_FAIL);
            if (handler instanceof ChangeApprovalHandler) {
                ReflectionTestUtils.invokeMethod(handler, "updateExecutionStatus", ticket, AutoExecJobStatus.FAILED, null);
            } else {
                ReflectionTestUtils.invokeMethod(handler, "updateExecutionStatus", ticket, AutoExecJobStatus.FAILED);
            }
            ticket.setTicketStatus(ApprovalStatus.EXEC_PAUSE);
            if (handler instanceof ChangeApprovalHandler) {
                ReflectionTestUtils.invokeMethod(handler, "updateExecutionStatus", ticket, AutoExecJobStatus.PAUSE, null);
            } else {
                ReflectionTestUtils.invokeMethod(handler, "updateExecutionStatus", ticket, AutoExecJobStatus.PAUSE);
            }
        }
        verifyNoInteractions(approvalStateService);
    }

    @Test
    public void shouldNotPollExecutionAfterAnotherWorkerFinishesTicket() {
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        ticket.setDeleted(false);
        ticket.setTicketStatus(ApprovalStatus.FINISHED);
        when(approvalMapper.queryById(11L)).thenReturn(ticket);
        for (ApprovalHandler handler : List.of(new QueryApprovalHandler(), new ChangeApprovalHandler())) {
            ReflectionTestUtils.setField(handler, "approvalDal", approvalDal);
            ReflectionTestUtils.setField(handler, "approvalStateService", approvalStateService);
            handler.executeTicket(11L, handler.handleType(), null);
            handler.runningCheck(11L, handler.handleType(), null);
        }
        verifyNoInteractions(approvalStateService);
        verify(approvalMapper, times(4)).queryById(11L);
        verifyNoMoreInteractions(approvalMapper);
    }

    private ApprovalTaskScheduler scheduler(ThreadPoolExecutor executor) {
        ApprovalTaskScheduler scheduler = new ApprovalTaskScheduler();
        ReflectionTestUtils.setField(scheduler, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(scheduler, "threadPoolExecutor", executor);
        ReflectionTestUtils.setField(scheduler, "taskInQueueSet", ConcurrentHashMap.newKeySet());
        ReflectionTestUtils.setField(scheduler, "approvalFlowService", mock(ApprovalFlowService.class));
        DmDsWebService dsService = mock(DmDsWebService.class);
        DmDsDO datasource = new DmDsDO();
        datasource.setLifeCycleState(LifeCycleState.CREATED);
        when(dsService.queryById(91L)).thenReturn(datasource);
        ReflectionTestUtils.setField(scheduler, "dsService", dsService);
        return scheduler;
    }

    private DmAuthUserDO user(String uid, String username) {
        DmAuthUserDO user = new DmAuthUserDO();
        user.setUid(uid);
        user.setUsername(username);
        return user;
    }

    private RsAuthPersonObj person(String uid, String username, List<String> roleLabels, List<String> dataLabels) {
        RsAuthPersonObj person = new RsAuthPersonObj();
        person.setUid(uid);
        person.setUsername(username);
        person.setRoleAuthLabels(roleLabels);
        person.setResAuthLabel(dataLabels);
        return person;
    }
}

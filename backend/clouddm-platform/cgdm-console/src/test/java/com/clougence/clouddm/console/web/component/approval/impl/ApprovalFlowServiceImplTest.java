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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.*;
import com.clougence.clouddm.console.web.component.approval.handler.ChangeApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.handler.QueryApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.model.*;
import com.clougence.clouddm.console.web.component.approval.schedule.ApprovalTaskProcessor;
import com.clougence.clouddm.console.web.component.approval.schedule.ApprovalTaskScheduler;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.constants.DmConfirmActionType;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpApprovalFO;
import com.clougence.clouddm.console.web.service.datasource.DmDsWebService;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.mapper.approval.*;
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
import com.clougence.clouddm.platform.dal.model.execution.AutoExecType;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivityStatus;
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

    @Test
    public void shouldPreserveApprovalProviderResponseFields() {
        ApprovalProviderOptionVO provider = new ApprovalProviderOptionVO();
        provider.setApprovalType("Internal");
        provider.setI18n("Internal approval");
        provider.setAvailable(true);
        provider.setDesc("");
        String expected = "{\"approvalType\":\"Internal\",\"i18n\":\"Internal approval\",\"available\":true,\"desc\":\"\"}";
        assertEquals(expected, JsonUtils.toJson(provider));
    }

    @Test
    public void shouldReadStoredApprovalAndExecutionContexts() {
        String stored = "{\"customApproval\":{\"schemaVersion\":2,\"mode\":\"CUSTOM\",\"executionAuthorization\":\"ENGINE\",\"currentActivityId\":13},"
                        + "\"customExecution\":{\"schemaVersion\":1,\"authorizationMode\":\"ENGINE\",\"executionMode\":\"AUTO\","
                        + "\"participants\":[{\"userUid\":\"executor\",\"userName\":\"Executor\"}]}}";
        ApprovalStageMO context = JsonUtils.toObj(stored, ApprovalStageMO.class);
        assertEquals(Long.valueOf(13), context.getCustomApproval().getCurrentActivityId());
        assertEquals(ApprovalExecutionMode.AUTO, context.getCustomExecution().getExecutionMode());
        assertEquals("executor", context.getCustomExecution().getParticipants().get(0).getUserUid());

        ApprovalStageMO restored = JsonUtils.toObj(JsonUtils.toJson(context), ApprovalStageMO.class);
        assertEquals(context.getCustomApproval(), restored.getCustomApproval());
        assertEquals(context.getCustomExecution(), restored.getCustomExecution());

        context.getCustomApproval().setCurrentActivityId(null);
        assertFalse(JsonUtils.toJson(context).contains("\"currentActivityId\""));
    }

    @Test
    public void shouldPreserveExecutionRequestFingerprint() {
        DmAutoExecConfigFO config = new DmAutoExecConfigFO();
        config.setAutoExecType(AutoExecType.IMMEDIATE);
        ApprovalExecutionOptions options = new ApprovalExecutionOptions();
        options.setConfirmActionType(DmConfirmActionType.CONFIRM);
        options.setAutoExecConfig(config);
        options.setComment("approved");
        String expected = "{\"confirmActionType\":\"CONFIRM\",\"autoExecConfig\":" + JsonUtils.toJson(config) + ",\"comment\":\"approved\"}";
        assertEquals(expected, CustomApprovalConfigService.serializeContext(options));

        options.setComment(null);
        String withoutComment = "{\"confirmActionType\":\"CONFIRM\",\"autoExecConfig\":" + JsonUtils.toJson(config) + ",\"comment\":null}";
        assertEquals(withoutComment, CustomApprovalConfigService.serializeContext(options));
    }

    @Test
    public void shouldReadStoredExecutionAttempt() {
        String stored = "{\"executionAttempt\":{\"requestId\":\"request-1\",\"confirmUid\":\"executor\",\"fingerprint\":\"request-options\","
                        + "\"jobBizId\":\"job-1\",\"request\":\"{}\",\"languageTag\":\"zh-CN\",\"status\":\"PREPARING\"," + "\"runId\":\"run-1\",\"claimedAt\":123456789}}";
        ApprovalExecutionAttempt attempt = JsonUtils.toObj(stored, ApprovalStageMO.class).getExecutionAttempt();
        assertEquals(ApprovalExecutionAttemptStatus.PREPARING, attempt.getStatus());
        assertEquals(Long.valueOf(123456789), attempt.getClaimedAt());
        assertEquals("executor", attempt.getConfirmUid());
        assertEquals("{}", attempt.getRequest());

        ApprovalExecutionAttempt restored = JsonUtils.toObj(JsonUtils.toJson(attempt), ApprovalExecutionAttempt.class);
        assertEquals(attempt, restored);
    }

    @Test
    public void approvalAvailabilityRequiresCredentialsWithoutEnableFlag() {
        SystemDal systemDal = mock(SystemDal.class);
        DmSysUserConfMapper configMapper = mock(DmSysUserConfMapper.class);
        when(systemDal.userConfMapper()).thenReturn(configMapper);
        ApprovalProviderServiceImpl providerService = new ApprovalProviderServiceImpl(List.of());
        ReflectionTestUtils.setField(providerService, "systemDal", systemDal);

        Map<ApprovalProvider, List<String>> requiredConfigs = Map
            .of(ApprovalProvider.DingTalk, List.of(RootUserConfig.Fields.dingApprovalConfigAk, RootUserConfig.Fields.dingApprovalConfigSk), ApprovalProvider.Feishu, List
                .of(RootUserConfig.Fields.feishuApprovalAppID, RootUserConfig.Fields.feishuApprovalAppSecret), ApprovalProvider.Wechat, List
                    .of(RootUserConfig.Fields.wechatApprovalCorpId, RootUserConfig.Fields.wechatApprovalAgentId, RootUserConfig.Fields.wechatApprovalSecret, RootUserConfig.Fields.wechatApprovalToken, RootUserConfig.Fields.wechatApprovalEncodingAesKey));
        for (Map.Entry<ApprovalProvider, List<String>> entry : requiredConfigs.entrySet()) {
            List<String> keys = entry.getValue();
            List<DmSysUserConfDO> configs = new ArrayList<>();
            when(configMapper.listByUidAndConfigNames("primary", keys)).thenReturn(configs);
            assertFalse(providerService.isApprovalConfigured("primary", entry.getKey()));

            for (String key : keys) {
                DmSysUserConfDO config = new DmSysUserConfDO();
                config.setConfigName(key);
                config.setConfigValue("test-value");
                configs.add(config);
            }

            assertTrue(providerService.isApprovalConfigured("primary", entry.getKey()));
            configs.get(0).setConfigValue(" ");
            assertFalse(providerService.isApprovalConfigured("primary", entry.getKey()));
            configs.remove(0);
            assertFalse(providerService.isApprovalConfigured("primary", entry.getKey()));
        }

        assertTrue(providerService.isApprovalConfigured("primary", ApprovalProvider.Internal));
        verify(configMapper, never()).queryByUidAndConfigName(anyString(), anyString());
    }

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
        List<String> participantUids = new ArrayList<>();
        for (DmApprovalPersonDO participant : inserted) {
            participantUids.add(participant.getPersonUid());
        }

        assertEquals(Arrays.asList("primary", "eligible", "global"), participantUids);

        DmApprovalProcessDO approvalProcess = null;
        for (DmApprovalProcessDO process : initialized) {
            if (process.getTicketStage() == ApprovalStage.APPROVAL) {
                approvalProcess = process;
                break;
            }
        }

        assertNotNull(approvalProcess);
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
            List<Long> ticketIds = new ArrayList<>();
            for (long id = afterId + 1; id <= 45 && ticketIds.size() < 20; id++) {
                ticketIds.add(id);
            }
            return ticketIds;
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

    @Test
    public void shouldKeepApprovalTaskStatusNamesInJson() {
        for (ApprovalNodeTaskStatus status : ApprovalNodeTaskStatus.values()) {
            DmApprovalNodeTaskDO task = new DmApprovalNodeTaskDO();
            task.setStatus(status);
            String json = JsonUtils.toJson(task);
            assertTrue(json.contains("\"status\":\"" + status.name() + "\""));
            assertEquals(status, JsonUtils.toObj(json, DmApprovalNodeTaskDO.class).getStatus());
        }
    }

    @Test
    public void shouldKeepApprovalConfigurationEnumNamesInJson() {
        for (ApprovalExecutionMode mode : ApprovalExecutionMode.values()) {
            CustomApprovalFlowConfig flow = new CustomApprovalFlowConfig();
            flow.setExecutionMode(mode);
            String json = JsonUtils.toJson(flow);
            assertTrue(json.contains("\"executionMode\":\"" + mode.name() + "\""));
            assertEquals(mode, JsonUtils.toObj(json, CustomApprovalFlowConfig.class).getExecutionMode());
        }

        for (ApprovalParticipantType type : ApprovalParticipantType.values()) {
            ApprovalExecutionContext execution = new ApprovalExecutionContext();
            execution.setParticipantType(type);
            String json = JsonUtils.toJson(execution);
            assertTrue(json.contains("\"participantType\":\"" + type.name() + "\""));
            assertEquals(type, JsonUtils.toObj(json, ApprovalExecutionContext.class).getParticipantType());
        }

        for (ApprovalTemplateSource source : ApprovalTemplateSource.values()) {
            DmApprovalTemplateDO template = new DmApprovalTemplateDO();
            template.setTemplateSource(source);
            String json = JsonUtils.toJson(template);
            assertTrue(json.contains("\"templateSource\":\"" + source.name() + "\""));
            assertEquals(source, JsonUtils.toObj(json, DmApprovalTemplateDO.class).getTemplateSource());
        }
    }

    @Test
    public void shouldCountOneVoteOnceAndCancelRemainingTasksAtThreshold() {
        assertCustomVote(false);
    }

    @Test
    public void shouldRejectWholeApprovalOnFirstValidRejection() {
        assertCustomVote(true);
    }

    private void assertCustomVote(boolean rejected) {
        ApprovalHandler handler = mock(ApprovalHandler.class);
        when(handler.handleType()).thenReturn(ApprovalBiz.DM_QUERY);
        CustomApprovalService approvalService = new CustomApprovalServiceImpl();
        ReflectionTestUtils.setField(approvalService, "handlers", List.of(handler));
        CustomApprovalConfigService config = mock(CustomApprovalConfigService.class);
        DmApprovalNodeTaskMapper tasks = mock(DmApprovalNodeTaskMapper.class);
        DmApprovalProcessActivityMapper activities = mock(DmApprovalProcessActivityMapper.class);
        when(approvalDal.activityMapper()).thenReturn(activities);
        ReflectionTestUtils.setField(approvalService, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(approvalService, "authDal", mock(AuthDal.class, RETURNS_DEEP_STUBS));
        ReflectionTestUtils.setField(approvalService, "configService", config);
        when(approvalDal.nodeTaskMapper()).thenReturn(tasks);
        ReflectionTestUtils.setField(approvalService, "stateService", approvalStateService);
        when(config.isEligibleUser(eq("primary"), any(), eq(SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE))).thenReturn(true);

        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        ticket.setPrimaryUid("primary");
        ticket.setApproType(ApprovalType.Internal);
        ticket.setApproBiz(ApprovalBiz.DM_QUERY);
        ticket.setApproTemplateIdentity("CUSTOM-test");
        ticket.setTicketStatus(ApprovalStatus.WAIT_APPROVAL);
        ticket.setDeleted(false);
        when(approvalMapper.selectByIdForUpdate(11L)).thenReturn(ticket);

        DmApprovalProcessDO process = new DmApprovalProcessDO();
        process.setId(12L);
        process.setProcessStatus(ApprovalProcessStatus.INIT);
        ApprovalStageMO context = new ApprovalStageMO();
        CustomApprovalContext customContext = new CustomApprovalContext();
        customContext.setSchemaVersion(2);
        customContext.setMode("CUSTOM");
        customContext.setExecutionAuthorization("ENGINE");
        customContext.setCurrentActivityId(13L);
        context.setCustomApproval(customContext);
        process.setStageContext(JsonUtils.toJson(context));
        when(processMapper.queryByStage(11L, ApprovalStage.APPROVAL)).thenReturn(process);
        when(processMapper.queryByStage(11L, ApprovalStage.CONFIRM)).thenReturn(new DmApprovalProcessDO());
        when(processMapper.queryByStage(11L, ApprovalStage.EXECUTION)).thenReturn(new DmApprovalProcessDO());
        DmApprovalProcessActivityDO activity = new DmApprovalProcessActivityDO();
        activity.setId(13L);
        activity.setProcessId(12L);
        activity.setTicketId(11L);
        activity.setOrderNumber(1);
        activity.setTaskStatus("RUNNING");
        activity.setContext("{\"effectiveRequiredCount\":1}");
        when(activities.listCustomActivities(11L, 12L)).thenReturn(List.of(activity));
        DmApprovalNodeTaskDO alice = new DmApprovalNodeTaskDO();
        alice.setId(14L);
        alice.setApproverUid("alice");
        alice.setApproverName("Alice");
        alice.setStatus(ApprovalNodeTaskStatus.PENDING);
        DmApprovalNodeTaskDO bob = new DmApprovalNodeTaskDO();
        bob.setId(15L);
        bob.setApproverUid("bob");
        bob.setStatus(ApprovalNodeTaskStatus.PENDING);
        when(tasks.listByActivityIdForUpdate(13L)).thenReturn(List.of(alice, bob));
        when(tasks.listByActivityId(13L)).thenReturn(List.of(alice, bob));
        RdpApprovalFO request = new RdpApprovalFO();
        request.setTicketId(11L);
        request.setProcessActivityId(13L);
        request.setRejected(rejected);
        request.setComment("first decision");

        approvalService.submitApproval("primary", "alice", request);
        ticket.setTicketStatus(ApprovalStatus.FINISHED);
        request.setComment("retry must not replace the first decision");
        approvalService.submitApproval("primary", "alice", request);

        assertEquals("first decision", alice.getComment());
        assertEquals(ApprovalNodeTaskStatus.CANCELED, bob.getStatus());
        verify(tasks).updateById(alice);
        verify(tasks).updateById(bob);
        if (rejected) {
            assertEquals(ApprovalNodeTaskStatus.REJECTED, alice.getStatus());
            assertEquals("REFUSE", activity.getTaskStatus());
            verify(approvalStateService).finalizeApproval(11L, ApprovalStatus.REJECTED, "first decision");
            verify(approvalStateService).updateProcessStatus(11L, ApprovalStage.CONFIRM, ApprovalProcessStatus.CLOSED, null);
            verify(approvalStateService).updateProcessStatus(11L, ApprovalStage.EXECUTION, ApprovalProcessStatus.CLOSED, null);
            verify(handler, never()).approvalApproved(anyLong(), any(), any());
        } else {
            assertEquals(ApprovalNodeTaskStatus.APPROVED, alice.getStatus());
            assertEquals("COMPLETED", activity.getTaskStatus());
            verify(handler).approvalApproved(11L, ApprovalBiz.DM_QUERY, null);
        }

        verifyNoInteractions(personMapper);
    }

    @Test
    public void shouldUseFrozenExecutionListWithoutPrimaryOrResourceFallback() {
        CustomApprovalService approvalService = new CustomApprovalServiceImpl();
        CustomApprovalConfigService config = mock(CustomApprovalConfigService.class);
        ReflectionTestUtils.setField(approvalService, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(approvalService, "authDal", mock(AuthDal.class, RETURNS_DEEP_STUBS));
        ReflectionTestUtils.setField(approvalService, "configService", config);
        when(config.isEligibleUser(eq("primary"), any(), eq(SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE))).thenReturn(true);
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        ticket.setPrimaryUid("primary");
        ticket.setOwnerUid("applicant");
        ticket.setApproType(ApprovalType.Internal);
        ticket.setApproTemplateIdentity("CUSTOM-test");
        ApprovalStageMO approvalContext = new ApprovalStageMO();
        CustomApprovalContext customContext = new CustomApprovalContext();
        customContext.setSchemaVersion(2);
        customContext.setMode("CUSTOM");
        customContext.setExecutionAuthorization("ENGINE");
        approvalContext.setCustomApproval(customContext);
        DmApprovalProcessDO approval = new DmApprovalProcessDO();
        approval.setStageContext(JsonUtils.toJson(approvalContext));
        when(processMapper.queryByStage(11L, ApprovalStage.APPROVAL)).thenReturn(approval);
        ApprovalStageMO executionContext = new ApprovalStageMO();
        ApprovalExecutor executor = new ApprovalExecutor();
        executor.setUserUid("executor");
        executor.setUserName("Executor");

        ApprovalExecutionContext executionConfig = new ApprovalExecutionContext();
        executionConfig.setSchemaVersion(1);
        executionConfig.setAuthorizationMode("ENGINE");
        executionConfig.setParticipants(List.of(executor));
        executionContext.setCustomExecution(executionConfig);
        DmApprovalProcessDO execution = new DmApprovalProcessDO();
        execution.setStageContext(JsonUtils.toJson(executionContext));
        when(processMapper.queryByStage(11L, ApprovalStage.EXECUTION)).thenReturn(execution);

        assertTrue(approvalService.canExecute(ticket, "executor"));
        assertEquals(false, approvalService.canExecute(ticket, "primary"));
        assertEquals(false, approvalService.canExecute(ticket, "applicant"));
        verifyNoInteractions(authResMapper, personMapper);
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

    @Test
    public void shouldAllowTheSameNodeIdInDifferentApprovalFlows() {
        CustomApprovalConfigService config = spy(new CustomApprovalConfigServiceImpl());
        AuthDal auth = mock(AuthDal.class);
        SystemDal system = mock(SystemDal.class);
        when(system.userConfMapper()).thenReturn(mock(DmSysUserConfMapper.class));
        DmApprovalTemplateMapper templates = mock(DmApprovalTemplateMapper.class);
        ReflectionTestUtils.setField(config, "authDal", auth);
        ReflectionTestUtils.setField(config, "systemDal", system);
        ReflectionTestUtils.setField(config, "approvalDal", approvalDal);
        when(auth.userMapper()).thenReturn(authUserMapper);
        when(approvalDal.templateMapper()).thenReturn(templates);
        DmAuthUserDO primary = user("primary", "admin");
        when(authUserMapper.lockByUid("primary")).thenReturn(primary);
        when(authUserMapper.queryByUid("primary")).thenReturn(primary);
        doReturn(true).when(config).isEligibleUser(eq("primary"), eq(primary), anyString());

        CustomApprovalNodeConfig node = new CustomApprovalNodeConfig();
        node.setNodeId(UUID.randomUUID().toString());
        node.setApproverType(ApprovalParticipantType.PERSON);
        node.setUserUid("primary");
        CustomApprovalFlowConfig definition = new CustomApprovalFlowConfig();
        definition.setNodes(List.of(node));
        definition.setExecutionMode(ApprovalExecutionMode.AUTO);
        DmApprovalTemplateDO other = new DmApprovalTemplateDO();
        other.setEngineConfigId("another-engine");
        other.setTemplateContent(JsonUtils.toJson(definition));
        when(templates.listCustomTemplates("primary")).thenReturn(List.of(other));

        ApprovalEngineSaveFO request = new ApprovalEngineSaveFO();
        request.setEngineName("Copied approval flow");
        request.setDefinition(definition);
        config.saveEngine("primary", request);

        ArgumentCaptor<DmApprovalTemplateDO> saved = ArgumentCaptor.forClass(DmApprovalTemplateDO.class);
        verify(templates).insert(saved.capture());
        assertEquals(node.getNodeId(), config.parseFlowConfig(saved.getValue()).getNodes().get(0).getNodeId());
    }

    @Test(expected = ErrorMessageException.class)
    public void shouldRejectDuplicateNodeIdsWithinOneApprovalFlow() {
        CustomApprovalConfigService config = spy(new CustomApprovalConfigServiceImpl());
        doReturn(List.of(user("primary", "admin"))).when(config)
            .resolveParticipants(anyString(), any(ApprovalParticipantType.class), nullable(String.class), anyString(), any(), anyString());
        CustomApprovalNodeConfig node = new CustomApprovalNodeConfig();
        node.setNodeId(UUID.randomUUID().toString());
        node.setApproverType(ApprovalParticipantType.PERSON);
        node.setUserUid("primary");
        CustomApprovalFlowConfig definition = new CustomApprovalFlowConfig();
        definition.setNodes(List.of(node, node));
        definition.setExecutionMode(ApprovalExecutionMode.AUTO);

        config.validateFlowConfig("primary", definition, new ApprovalTeamConfig());
    }

    @Test
    public void shouldIgnoreCloseCallbackForUnknownProviderTask() {
        ApprovalProviderServiceImpl providerService = new ApprovalProviderServiceImpl(List.of());
        ReflectionTestUtils.setField(providerService, "approvalDal", approvalDal);
        DmApprovalProcessActivityMapper activityMapper = mock(DmApprovalProcessActivityMapper.class);
        when(approvalDal.activityMapper()).thenReturn(activityMapper);

        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        when(approvalMapper.queryByApproIdentity("provider-instance", "DingTalk", "primary")).thenReturn(ticket);
        DmApprovalProcessDO process = new DmApprovalProcessDO();
        process.setId(12L);
        when(processMapper.queryByStage(11L, ApprovalStage.APPROVAL)).thenReturn(process);
        DmApprovalProcessActivityDO activity = new DmApprovalProcessActivityDO();
        activity.setActivityId("provider-node");
        activity.setContext("[]");
        when(activityMapper.queryByProcessIdAndActivityIdForUpdate(12L, "provider-node")).thenReturn(activity);

        ApprovalActivity callback = new ApprovalActivity();
        callback.setPuid("primary");
        callback.setPlatform("DingTalk");
        callback.setApprovalIdentity("provider-instance");
        callback.setActivityId("provider-node");
        callback.setTaskId("unknown-task");
        callback.setStatus(ApprovalActivityStatus.CLOSE);
        providerService.updateActivity(callback);

        verify(activityMapper, never()).updateContext(anyLong(), anyString(), anyString());
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

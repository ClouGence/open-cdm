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

import static com.clougence.clouddm.api.console.sqlaudit.Type.SQL_END;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.MockedStatic;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.SimpleTransactionStatus;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstructUtils;
import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.ConsoleErrorCode;
import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.i18n.I18nDictionary;
import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.api.sidecar.session.execute.AsyncWaitResult;
import com.clougence.clouddm.api.sidecar.session.execute.ExecuteRService;
import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
import com.clougence.clouddm.api.sidecar.session.execute.StatusDTO;
import com.clougence.clouddm.api.sidecar.status.WorkerStatusRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.ConnSetupProcessor;
import com.clougence.clouddm.comm.ConsoleRSocketServer;
import com.clougence.clouddm.comm.RSocketSerialization;
import com.clougence.clouddm.comm.component.RSocketExceptionManager;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.impl.MainRequestDispatcher;
import com.clougence.clouddm.comm.component.impl.RSocketApiManager;
import com.clougence.clouddm.comm.component.server.RSocketServerSender;
import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.constants.rsocket.RSocketLogNames;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.comm.model.*;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.comm.model.rsocket.RSocketRegisterInfo;
import com.clougence.clouddm.comm.util.RSocketRespUtil;
import com.clougence.clouddm.console.web.component.approval.ApprovalStarter;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.component.cluster.ConsoleInternalClient;
import com.clougence.clouddm.console.web.component.cluster.model.*;
import com.clougence.clouddm.console.web.component.config.*;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.execute.AsyncTaskService;
import com.clougence.clouddm.console.web.component.execute.control.ConsoleTaskRouter;
import com.clougence.clouddm.console.web.component.execute.impl.QueryServiceImpl;
import com.clougence.clouddm.console.web.component.execute.model.ConsoleTaskControlAction;
import com.clougence.clouddm.console.web.component.execute.model.ConsoleTaskControlRequest;
import com.clougence.clouddm.console.web.constants.ConsoleInternalPaths;
import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.controller.cluster.ClusterController;
import com.clougence.clouddm.console.web.controller.cluster.ConsoleInternalController;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.global.notify.DmWorkerRegisterNotify;
import com.clougence.clouddm.console.web.global.rsocket.*;
import com.clougence.clouddm.console.web.global.ws.WsChannel;
import com.clougence.clouddm.console.web.global.ws.WsType;
import com.clougence.clouddm.console.web.model.fo.UpdateUserEmailWithPwdFO;
import com.clougence.clouddm.console.web.model.fo.UpdateUserPhoneWithPwdFO;
import com.clougence.clouddm.console.web.model.fo.UpsertUserConfigFO;
import com.clougence.clouddm.console.web.model.lo.UpsertUserConfigLO;
import com.clougence.clouddm.console.web.service.cluster.WorkerServiceImpl;
import com.clougence.clouddm.console.web.service.login.LoginDefService;
import com.clougence.clouddm.console.web.service.login.LoginStarter;
import com.clougence.clouddm.console.web.service.security.AuditService;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.access.impl.ObjectQueryDaoImpl;
import com.clougence.clouddm.platform.dal.mapper.auth.DmAuthRoleMapper;
import com.clougence.clouddm.platform.dal.mapper.auth.DmAuthUserMapper;
import com.clougence.clouddm.platform.dal.mapper.execution.DmExecAsyncTaskMapper;
import com.clougence.clouddm.platform.dal.mapper.execution.DmExecSessionMapper;
import com.clougence.clouddm.platform.dal.mapper.system.DmSysClusterMapper;
import com.clougence.clouddm.platform.dal.mapper.system.DmSysConsoleMapper;
import com.clougence.clouddm.platform.dal.mapper.system.DmSysUserConfMapper;
import com.clougence.clouddm.platform.dal.mapper.system.DmSysWorkerMapper;
import com.clougence.clouddm.platform.dal.model.auth.AccountBindType;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAsyncTaskDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSessionDO;
import com.clougence.clouddm.platform.dal.model.system.*;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.rdp.service.impl.RdpUserServiceImpl;
import com.clougence.rdp.service.model.UserConfigMO;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectReader;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.exceptions.ConnectionErrorException;
import io.rsocket.exceptions.RejectedSetupException;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.OkHttpClient;
import reactor.core.publisher.Mono;

/**
 * Sends the 1 argument fetchDsConfig request that the sidecar AutoExecJob sends, through the console RSocket registry and
 * MainRequestDispatcher, and checks that the response carries the plugin serialization provider the sidecar needs to decode it.
 */
public class ConfigRServiceDispatchTest {

    @Serialization(provider = "test-ds")
    public static class PluginDsConfig extends DataSourceConfig {
    }

    @Test
    public void objectQueriesReadCurrentUserAndRoleWithoutCaching() {
        AuthDal authDal = mock(AuthDal.class);
        DmAuthUserMapper users = mock(DmAuthUserMapper.class);
        DmAuthRoleMapper roles = mock(DmAuthRoleMapper.class);
        when(authDal.userMapper()).thenReturn(users);
        when(authDal.roleMapper()).thenReturn(roles);
        ObjectQueryDaoImpl dao = new ObjectQueryDaoImpl();
        ReflectionTestUtils.setField(dao, "authDal", authDal);

        DmAuthUserDO user = new DmAuthUserDO();
        user.setId(1L);
        user.setUid("user");
        user.setRoleId(2L);
        user.setUsername("before");
        user.setAccessKey("ak");
        user.setAccountType(AccountType.PRIMARY_ACCOUNT);
        when(users.queryByUid("user")).thenReturn(user);
        when(users.queryById(1L)).thenReturn(user);
        when(users.queryByAccessKey("ak")).thenReturn(user);

        DmAuthRoleDO role = new DmAuthRoleDO();
        role.setRoleName("before-role");
        when(roles.selectById(2L)).thenReturn(role);
        assertEquals("before", dao.queryByUid("user").getUserName());
        assertEquals("before-role", dao.queryByUserNumberId(1L).getRoleName());
        assertEquals("before", dao.queryByAk("ak").getUserName());

        user.setUsername("after");
        role.setRoleName("after-role");
        assertEquals("after", dao.queryByUid("user").getUserName());
        assertEquals("after-role", dao.queryByUserNumberId(1L).getRoleName());
        assertEquals("after", dao.queryByAk("ak").getUserName());
        when(users.queryByUid("user")).thenReturn(null);
        when(users.queryById(1L)).thenReturn(null);
        when(users.queryByAccessKey("ak")).thenReturn(null);
        assertNull(dao.queryByUid("user"));
        assertNull(dao.queryByUserNumberId(1L));
        assertNull(dao.queryByAk("ak"));
    }

    @Test
    public void ownershipChecksReadWorkerAndClusterChangesWithoutCaching() {
        SystemDal systemDal = mock(SystemDal.class);
        DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
        DmSysClusterMapper clusters = mock(DmSysClusterMapper.class);
        when(systemDal.workerMapper()).thenReturn(workers);
        when(systemDal.clusterMapper()).thenReturn(clusters);
        ObjectQueryDaoImpl dao = new ObjectQueryDaoImpl();
        ReflectionTestUtils.setField(dao, "systemDal", systemDal);
        ReflectionTestUtils.setField(dao, "i18nDictionary", mock(I18nDictionary.class));

        DmSysWorkerDO worker = new DmSysWorkerDO();
        worker.setId(1L);
        worker.setWorkerSeqNumber("worker");
        worker.setUid("owner");
        when(workers.selectById(1L)).thenReturn(worker);
        when(workers.getByWsn("worker")).thenReturn(worker);

        DmSysClusterDO cluster = new DmSysClusterDO();
        cluster.setUid("owner");
        when(clusters.selectById(2L)).thenReturn(cluster);
        dao.ownWorker("owner", 1L);
        dao.ownCluster("owner", 2L);
        assertEquals("owner", dao.queryByWsn("worker").getOwnerUid());

        worker.setUid("new-owner");
        cluster.setUid("new-owner");
        try {
            dao.ownWorker("owner", 1L);
            fail("ownership check must reject stale or missing objects");
        } catch (ErrorMessageException expected) {
            assertEquals(DmErrorCode.O_AUTHORITY.code(), expected.getErrorCode());
        }
        try {
            dao.ownCluster("owner", 2L);
            fail("ownership check must reject stale or missing objects");
        } catch (ErrorMessageException expected) {
            assertEquals(DmErrorCode.O_AUTHORITY.code(), expected.getErrorCode());
        }
        dao.ownWorker("new-owner", 1L);
        dao.ownCluster("new-owner", 2L);
        assertEquals("new-owner", dao.queryByWsn("worker").getOwnerUid());

        when(workers.selectById(1L)).thenReturn(null);
        when(workers.getByWsn("worker")).thenReturn(null);
        when(clusters.selectById(2L)).thenReturn(null);
        try {
            dao.ownWorker("new-owner", 1L);
            fail("ownership check must reject stale or missing objects");
        } catch (ErrorMessageException expected) {
            assertEquals(DmErrorCode.O_DATA.code(), expected.getErrorCode());
        }
        try {
            dao.ownCluster("new-owner", 2L);
            fail("ownership check must reject stale or missing objects");
        } catch (ErrorMessageException expected) {
            assertEquals(DmErrorCode.O_DATA.code(), expected.getErrorCode());
        }
        assertNull(dao.queryByWsn("worker"));
    }

    @Test
    public void consoleClusterProtocolKeepsItsExistingWireValues() {
        assertArrayEquals(new String[] { "PAUSE_ASYNC", "CANCEL_ASYNC" }, Arrays.stream(ConsoleTaskControlAction.values()).map(Enum::name).toArray(String[]::new));
        assertArrayEquals(new String[] { "STARTING", "READY", "DRAINING" }, Arrays.stream(ConsoleLeaseState.values()).map(Enum::name).toArray(String[]::new));
        assertEquals("X-Cdm-Origin", ConsoleInternalClient.HEADER_ORIGIN);
        assertEquals("X-Cdm-Target", ConsoleInternalClient.HEADER_TARGET);
        assertEquals("X-Cdm-Time", ConsoleInternalClient.HEADER_TIME);
        assertEquals("X-Cdm-Nonce", ConsoleInternalClient.HEADER_NONCE);
        assertEquals("X-Cdm-Deadline", ConsoleInternalClient.HEADER_DEADLINE);
        assertEquals("X-Cdm-Signature", ConsoleInternalClient.HEADER_SIGNATURE);
        assertEquals("EXECUTION_UNKNOWN", RSocketRespCode.EXECUTION_UNKNOWN.getCode());
        assertEquals("OWNER_CHANGED_NOT_DISPATCHED", RSocketRespCode.OWNER_CHANGED_NOT_DISPATCHED.getCode());
        assertEquals("QUERY_SESSION_INTERRUPTED", DmErrorCode.QUERY_SESSION_INTERRUPTED.code());
        assertEquals("INTERRUPTED", DmExecSessionDO.INTERRUPTED_BATCH_ID);
    }

    @Test
    public void taskControlRejectsNullAndUnknownWireActionsBeforeDispatch() throws Exception {
        ConsoleInternalClient internalClient = mock(ConsoleInternalClient.class);
        when(internalClient.verify(any(HttpServletRequest.class), anyString())).thenReturn("console-a");
        ExecutionDal executionDal = mock(ExecutionDal.class);
        AsyncTaskService tasks = mock(AsyncTaskService.class);
        ConsoleInternalController controller = new ConsoleInternalController();
        ReflectionTestUtils.setField(controller, "internalClient", internalClient);
        ReflectionTestUtils.setField(controller, "executionDal", executionDal);
        ReflectionTestUtils.setField(controller, "asyncTasks", tasks);
        controller.init();
        try {
            MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();
            for (Object action : Arrays.asList(null, "UNKNOWN_ACTION", "pause_async", "PAUSE", "CANCEL", "", 0, 1, true)) {
                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("action", action);
                payload.put("taskId", 42L);
                payload.put("locale", "en-US");
                String body = JsonUtils.toJson(new ConsoleInternalRequest(ConsoleInternalAction.CONTROL_TASK, payload));
                mvc.perform(post(ConsoleInternalPaths.DISPATCH).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
            }

            for (String invalid : List
                .of("{}", "[]", "{", "{\"action\":\"UNKNOWN\",\"payload\":{}}", "{\"action\":0,\"payload\":{}}", "{\"action\":\"CONTROL_TASK\",\"payload\":[]}")) {
                mvc.perform(post(ConsoleInternalPaths.DISPATCH).contentType(MediaType.APPLICATION_JSON).content(invalid)).andExpect(status().isBadRequest());
            }

            verifyNoInteractions(executionDal, tasks);
        } finally {
            controller.stop();
        }
    }

    @Test(timeout = 10000)
    public void typedTaskControlRoundTripsThroughTheExistingJsonAndHttpProtocol() throws Exception {
        ConsoleClusterService origin = mock(ConsoleClusterService.class);
        when(origin.getInstanceId()).thenReturn("console-a");
        DmSysConsoleDO target = new DmSysConsoleDO();
        target.setInstanceId("console-b");
        when(origin.findAliveByConsoleInstanceId("console-b")).thenReturn(target);
        when(origin.getConsoleInstanceId()).thenReturn("console-a");
        ConsoleInternalClient internalClient = mock(ConsoleInternalClient.class);
        when(internalClient.verify(any(HttpServletRequest.class), anyString())).thenReturn("console-a");
        ConsoleTaskRouter router = new ConsoleTaskRouter();
        ReflectionTestUtils.setField(router, "cluster", origin);
        ReflectionTestUtils.setField(router, "internalClient", internalClient);

        ConsoleClusterService receiver = mock(ConsoleClusterService.class);
        when(receiver.getInstanceId()).thenReturn("console-b");
        when(receiver.getConsoleInstanceId()).thenReturn("console-b");
        DmExecAsyncTaskDO task = new DmExecAsyncTaskDO();
        task.setId(42L);
        task.setConsoleInstanceId("console-b");
        DmExecAsyncTaskMapper mapper = mock(DmExecAsyncTaskMapper.class);
        when(mapper.queryById(42L)).thenReturn(task);
        ExecutionDal executionDal = mock(ExecutionDal.class);
        when(executionDal.asyncTaskMapper()).thenReturn(mapper);
        AsyncTaskService tasks = mock(AsyncTaskService.class);
        ConsoleInternalController controller = new ConsoleInternalController();
        ReflectionTestUtils.setField(controller, "cluster", receiver);
        ReflectionTestUtils.setField(controller, "internalClient", internalClient);
        ReflectionTestUtils.setField(controller, "executionDal", executionDal);
        ReflectionTestUtils.setField(controller, "asyncTasks", tasks);
        controller.init();
        try {
            MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();
            List<String> transmitted = new ArrayList<>();
            when(internalClient.post(eq(target), eq(ConsoleInternalPaths.DISPATCH), any(ConsoleInternalRequest.class), anyLong())).thenAnswer(invocation -> {
                ConsoleInternalRequest request = invocation.getArgument(2);
                String body = JsonUtils.toJson(request);
                transmitted.add(((ConsoleTaskControlRequest) request.getPayload()).getAction());
                MvcResult pending = mvc
                    .perform(post(ConsoleInternalPaths.DISPATCH).contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header(ConsoleInternalClient.HEADER_DEADLINE, String.valueOf((long) invocation.getArgument(3))))
                    .andExpect(request().asyncStarted())
                    .andReturn();
                mvc.perform(asyncDispatch(pending)).andExpect(status().isOk()).andExpect(content().string("null"));
                return "null";
            });

            assertTrue(router.forwardAsyncControl(task, ConsoleTaskControlAction.PAUSE_ASYNC, "pause request"));
            assertTrue(router.forwardAsyncControl(task, ConsoleTaskControlAction.CANCEL_ASYNC, "cancel request"));
            assertEquals(List.of("PAUSE_ASYNC", "CANCEL_ASYNC"), transmitted);
            verify(tasks).pauseTask(42L, "pause request");
            verify(tasks).cancelTask(42L, "cancel request");
            verifyNoMoreInteractions(tasks);
        } finally {
            controller.stop();
        }
    }

    @Test
    public void userEventUsesTheSameDispatchEndpointAndBroadcastsLocally() throws Exception {
        ConsoleInternalClient internalClient = mock(ConsoleInternalClient.class);
        when(internalClient.verify(any(HttpServletRequest.class), anyString())).thenReturn("console-a");
        WsChannel channels = mock(WsChannel.class);
        ConsoleInternalController controller = new ConsoleInternalController();
        ReflectionTestUtils.setField(controller, "internalClient", internalClient);
        ReflectionTestUtils.setField(controller, "channels", channels);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

        ConsoleNotification notification = new ConsoleNotification();
        notification.setUid("user-1");
        notification.setType(WsType.WS_RES_EXPORT_EVENT);
        notification.setData(Map.of("stage", "COMPLETE"));
        String body = JsonUtils.toJson(new ConsoleInternalRequest(ConsoleInternalAction.DELIVER_USER_EVENT, notification));
        MvcResult pending = mvc.perform(post(ConsoleInternalPaths.DISPATCH).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(request().asyncStarted()).andReturn();
        mvc.perform(asyncDispatch(pending)).andExpect(status().isOk()).andExpect(content().string(""));
        verify(channels).broadcastLocal(WsType.WS_RES_EXPORT_EVENT, "user-1", Map.of("stage", "COMPLETE"));

        notification.setType(null);
        String invalid = JsonUtils.toJson(new ConsoleInternalRequest(ConsoleInternalAction.DELIVER_USER_EVENT, notification));
        mvc.perform(post(ConsoleInternalPaths.DISPATCH).contentType(MediaType.APPLICATION_JSON).content(invalid)).andExpect(status().isBadRequest());
        doThrow(new IllegalArgumentException("invalid signature")).when(internalClient).verify(any(HttpServletRequest.class), eq(body));
        mvc.perform(post(ConsoleInternalPaths.DISPATCH).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isForbidden());
        verifyNoMoreInteractions(channels);
    }

    @Test
    public void sidecarFetchDsConfigByDsIdReturnsPluginConfig() throws Exception {
        DmDsConfigService dsConfigService = mock(DmDsConfigService.class);
        when(dsConfigService.fetchDsConfigFromExists(42L)).thenReturn(new PluginDsConfig());
        ConfigRServiceProvider provider = new ConfigRServiceProvider();
        ReflectionTestUtils.setField(provider, "dsConfigService", dsConfigService);
        RSocketApiManager
            .scanAllApiAndRegister(getClass().getClassLoader(), "com.clougence.clouddm.console.web.provider", type -> type == ConfigRServiceProvider.class ? provider : null);

        List<String> encodeProviders = new ArrayList<>();
        RSocketSerialization serialization = new RSocketSerialization() {

            @Override
            public String encode(String provider, Object argData) {
                encodeProviders.add(provider);
                return JsonUtils.toJson(argData);
            }

            @Override
            public Object decode(String provider, String jsonData, Type tryType) {
                return RSocketSerializationImpl.DEFAULT.decode(provider, jsonData, tryType);
            }
        };
        CompletableFuture<RSocketRespDTO<?>> sentBack = new CompletableFuture<>();
        RSocketRequestManager requestManager = mock(RSocketRequestManager.class);
        doAnswer(invocation -> sentBack.complete(invocation.getArgument(0))).when(requestManager).sendAsyncResultBack(any());
        MainRequestDispatcher dispatcher = new MainRequestDispatcher(requestManager, throwable -> {
        }, serialization, (request, requester) -> {
        });

        RSocketRequestWrapperDTO request = new RSocketRequestWrapperDTO();
        request.setRequestId("fetch-ds-config");
        request.setRSocketDirectionType(RSocketDirectionType.CLIENT_TO_SERVER);
        request.setApiFullMethodName(ConfigRService.class.getCanonicalName() + ".fetchDsConfig");
        request.setParamJsonValues(new ArrayList<>(List.of(JsonUtils.toJson(new RSocketParam(null, JsonUtils.toJson(42L))))));
        dispatcher.dispatchByRouteName(request, null);

        RSocketRespDTO<?> response = sentBack.get(20, TimeUnit.SECONDS);
        assertEquals(response.getMsg(), RSocketRespCode.SUCCESS.getCode(), response.getCode());
        verify(dsConfigService).fetchDsConfigFromExists(42L);
        RSocketParam result = JsonUtils.toObj(String.valueOf(response.getData()), RSocketParam.class);
        assertEquals("test-ds", result.getSerializer());
        assertEquals(List.of("test-ds"), encodeProviders);
    }

    @Test
    public void invalidWorkerCredentialsNeverClaimAnOwner() {
        for (String invalidField : List.of("ak", "sk")) {
            AuthDal authDal = mock(AuthDal.class);
            SystemDal systemDal = mock(SystemDal.class);
            DmAuthUserMapper users = mock(DmAuthUserMapper.class);
            DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
            when(authDal.userMapper()).thenReturn(users);
            when(systemDal.workerMapper()).thenReturn(workers);

            DmAuthUserDO user = new DmAuthUserDO();
            user.setUid("test-user");
            user.setSecretKey(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt("test-secret"));
            when(users.queryByAccessKey("test-ak")).thenReturn(user);

            DmSysWorkerDO worker = new DmSysWorkerDO();
            worker.setUid("test-user");
            worker.setWorkerSeqNumber("test-worker");
            worker.setOwnerInstanceId("original-owner");
            worker.setConnectionGeneration(7);
            when(workers.getByWsn("test-worker")).thenReturn(worker);

            ConnAuthDTO auth = new ConnAuthDTO();
            auth.setAk("test-ak");
            auth.setSk("test-secret");
            auth.setWsn("test-worker");
            auth.setIp("127.0.0.1");
            if ("ak".equals(invalidField)) {
                auth.setAk("wrong-ak");
            } else {
                auth.setSk("wrong-secret");
            }

            ConsoleClusterService cluster = mock(ConsoleClusterService.class);
            RSocketExceptionManager errors = mock(RSocketExceptionManager.class);
            DmServerSideRegistry registry = new DmServerSideRegistry(authDal, systemDal, mock(ExecutionDal.class), List.of(), cluster, errors);
            ConnectionSetupPayload setup = mock(ConnectionSetupPayload.class);
            when(setup.getDataUtf8()).thenReturn(JsonUtils.toJson(auth));
            SocketAcceptor acceptor = mock(SocketAcceptor.class);
            DmConsoleSAInterceptor interceptor = new DmConsoleSAInterceptor(registry);
            try {
                interceptor.apply(acceptor).accept(setup, mock(RSocket.class)).block(Duration.ofSeconds(5));
                fail("invalid worker credentials must be rejected");
            } catch (RejectedSetupException expected) {
                assertEquals("worker credentials are invalid", expected.getMessage());
            }

            verify(acceptor, never()).accept(any(), any());
            verify(workers, never()).lockByWsn(anyString());
            verify(workers, never()).updateConnectionByWsn(any());
            verifyNoInteractions(errors);
            assertEquals("original-owner", worker.getOwnerInstanceId());
            assertEquals(7, worker.getConnectionGeneration());
        }
    }

    @Test(timeout = 15000)
    public void reusedAuthenticationReadersKeepConcurrentConnectionDtosIndependent() throws Exception {
        ServerSideRegistry registry = mock(ServerSideRegistry.class);
        DmConsoleSAInterceptor interceptor = new DmConsoleSAInterceptor(registry);
        ConnSetupProcessor processor = new ConnSetupProcessor(registry);
        ObjectReader interceptorReader = (ObjectReader) ReflectionTestUtils.getField(interceptor, "authReader");
        ObjectReader setupReader = (ObjectReader) ReflectionTestUtils.getField(processor, "authReader");
        assertNotNull(interceptorReader);
        assertNotNull(setupReader);
        Set<ConnAuthDTO> identities = Collections.synchronizedSet(Collections.newSetFromMap(new IdentityHashMap<>()));
        ExecutorService executor = Executors.newFixedThreadPool(8);
        CountDownLatch start = new CountDownLatch(1);
        try {
            List<Future<?>> requests = new ArrayList<>();
            for (int worker = 0; worker < 8; worker++) {
                int workerId = worker;
                requests.add(executor.submit(() -> {
                    start.await();
                    for (int round = 0; round < 16; round++) {
                        String value = workerId + "-" + round;
                        String json = "{\"ak\":\"ak-" + value + "\",\"sk\":\"sk-" + value + "\",\"wsn\":\"worker-" + value + "\",\"ip\":\"127.0.0.1\",\"sendAuthTimeMs\":" + round
                                      + "}";
                        for (ObjectReader reader : List.of(interceptorReader, setupReader)) {
                            ConnAuthDTO first = reader.readValue(json);
                            ConnAuthDTO second = reader.readValue(json);
                            assertTrue(identities.add(first));
                            assertTrue(identities.add(second));
                            assertNotSame(first, second);
                            assertEquals("ak-" + value, first.getAk());
                            assertEquals("sk-" + value, first.getSk());
                            assertEquals("worker-" + value, first.getWsn());
                            assertEquals("127.0.0.1", first.getIp());
                            assertEquals(round, first.getSendAuthTimeMs());
                            first.setAk("changed locally");
                            assertEquals("ak-" + value, second.getAk());
                        }
                    }
                    return null;
                }));
            }

            start.countDown();
            for (Future<?> request : requests) {
                request.get(10, TimeUnit.SECONDS);
            }

            assertEquals(512, identities.size());
            assertSame(interceptorReader, ReflectionTestUtils.getField(interceptor, "authReader"));
            assertSame(setupReader, ReflectionTestUtils.getField(processor, "authReader"));
            verifyNoInteractions(registry);
        } finally {
            start.countDown();
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    @Test
    public void reusedInterceptorReaderPreservesAuthenticationAndParsingErrorClassification() {
        ServerSideRegistry registry = mock(ServerSideRegistry.class);
        DmConsoleSAInterceptor interceptor = new DmConsoleSAInterceptor(registry);
        ObjectReader reader = (ObjectReader) ReflectionTestUtils.getField(interceptor, "authReader");
        SocketAcceptor downstream = mock(SocketAcceptor.class);
        RSocket socket = mock(RSocket.class);
        when(downstream.accept(any(), eq(socket))).thenReturn(Mono.just(socket));
        SocketAcceptor acceptor = interceptor.apply(downstream);
        for (String worker : List.of("worker-a", "worker-b")) {
            ConnectionSetupPayload setup = mock(ConnectionSetupPayload.class);
            when(setup.getDataUtf8()).thenReturn("{\"ak\":\"ak\",\"sk\":\"sk\",\"wsn\":\"" + worker + "\",\"ip\":\"127.0.0.1\"}");
            assertSame(socket, acceptor.accept(setup, socket).block(Duration.ofSeconds(5)));
        }

        ArgumentCaptor<ConnAuthDTO> authenticated = ArgumentCaptor.forClass(ConnAuthDTO.class);
        verify(registry, times(2)).checkAuth(authenticated.capture());
        assertNotSame(authenticated.getAllValues().get(0), authenticated.getAllValues().get(1));
        assertEquals("worker-a", authenticated.getAllValues().get(0).getWsn());
        assertEquals("worker-b", authenticated.getAllValues().get(1).getWsn());
        for (String json : List.of("{", "{\"ak\":\"ak\",\"sk\":\"sk\",\"wsn\":\"worker\",\"ip\":\"127.0.0.1\",\"unknown\":true}")) {
            ConnectionSetupPayload setup = mock(ConnectionSetupPayload.class);
            when(setup.getDataUtf8()).thenReturn(json);
            try {
                acceptor.accept(setup, socket).block(Duration.ofSeconds(5));
                fail("malformed JSON and unknown fields must retain the setup parsing failure classification");
            } catch (ConnectionErrorException expected) {
                assertEquals("console is temporarily unavailable", expected.getMessage());
            }
        }

        ConnectionSetupPayload blankSecret = mock(ConnectionSetupPayload.class);
        when(blankSecret.getDataUtf8()).thenReturn("{\"ak\":\"ak\",\"sk\":\" \",\"wsn\":\"worker\",\"ip\":\"127.0.0.1\"}");
        try {
            acceptor.accept(blankSecret, socket).block(Duration.ofSeconds(5));
            fail("invalid credentials must remain a rejected setup");
        } catch (RejectedSetupException expected) {
            assertEquals("worker credentials are invalid", expected.getMessage());
        }

        verify(downstream, times(2)).accept(any(), eq(socket));
        verifyNoMoreInteractions(registry, downstream);
        assertSame(reader, ReflectionTestUtils.getField(interceptor, "authReader"));
    }

    @Test
    public void reusedSetupReaderPreservesRegistrationAndFailedConnectionCleanup() {
        ServerSideRegistry registry = mock(ServerSideRegistry.class);
        ConnSetupProcessor processor = new ConnSetupProcessor(registry);
        ObjectReader reader = (ObjectReader) ReflectionTestUtils.getField(processor, "authReader");
        RSocketRequester requester = mock(RSocketRequester.class);
        RSocket socket = mock(RSocket.class);
        when(requester.rsocket()).thenReturn(socket);
        when(socket.onClose()).thenReturn(Mono.never());
        for (String worker : List.of("worker-a", "worker-b")) {
            String json = "{\"ak\":\"ak\",\"sk\":\"sk\",\"wsn\":\"" + worker + "\",\"ip\":\"127.0.0.1\"}";
            Mono<Void> connected = ReflectionTestUtils.invokeMethod(processor, "channelConnect", requester, json);
            assertNotNull(connected);
            connected.block(Duration.ofSeconds(5));
        }

        ArgumentCaptor<RSocketRegisterInfo> registered = ArgumentCaptor.forClass(RSocketRegisterInfo.class);
        verify(registry, times(2)).register(registered.capture());
        assertNotSame(registered.getAllValues().get(0), registered.getAllValues().get(1));
        assertEquals("worker-a", registered.getAllValues().get(0).getWorkerSeqNumber());
        assertEquals("worker-b", registered.getAllValues().get(1).getWorkerSeqNumber());
        assertEquals("127.0.0.1", registered.getAllValues().get(0).getWorkerIp());
        verify(registry, never()).unRegister(any());
        verify(socket, never()).dispose();
        for (String json : List
            .of("{", "{\"ak\":\"ak\",\"sk\":\"sk\",\"wsn\":\"worker\",\"ip\":\"127.0.0.1\",\"unknown\":true}", "{\"ak\":\"ak\",\"sk\":\" \",\"wsn\":\"worker\",\"ip\":\"127.0.0.1\"}")) {
            Mono<Void> connected = ReflectionTestUtils.invokeMethod(processor, "channelConnect", requester, json);
            assertNotNull(connected);
            try {
                connected.block(Duration.ofSeconds(5));
                fail("invalid setup data must fail registration and release the connection");
            } catch (ConnectionErrorException expected) {
                assertEquals("worker connection temporarily unavailable", expected.getMessage());
            }
        }

        verify(registry, times(3)).unRegister(any());
        verify(socket, times(3)).dispose();
        verifyNoMoreInteractions(registry);
        assertSame(reader, ReflectionTestUtils.getField(processor, "authReader"));
    }

    @Test
    public void setupRegistrationIsLazyAndCleansOnlyTheFailedSubscriptionIdentity() {
        ServerSideRegistry registry = mock(ServerSideRegistry.class);
        ConnSetupProcessor processor = new ConnSetupProcessor(registry);
        RSocketRequester requester = mock(RSocketRequester.class);
        RSocket socket = mock(RSocket.class);
        when(requester.rsocket()).thenReturn(socket);
        when(socket.onClose()).thenReturn(Mono.never());
        String json = "{\"ak\":\"ak\",\"sk\":\"sk\",\"wsn\":\"worker\",\"ip\":\"127.0.0.1\"}";
        Mono<Void> connected = ReflectionTestUtils.invokeMethod(processor, "channelConnect", requester, json);
        assertNotNull(connected);
        verifyNoInteractions(registry, requester, socket);

        connected.block(Duration.ofSeconds(5));
        connected.block(Duration.ofSeconds(5));
        doThrow(new IllegalStateException("registration failed")).when(registry).register(any());
        try {
            connected.block(Duration.ofSeconds(5));
            fail("a failed subscription must retain the registration error classification");
        } catch (ConnectionErrorException expected) {
            assertEquals("worker connection temporarily unavailable", expected.getMessage());
        }

        ArgumentCaptor<RSocketRegisterInfo> registered = ArgumentCaptor.forClass(RSocketRegisterInfo.class);
        verify(registry, times(3)).register(registered.capture());
        List<RSocketRegisterInfo> identities = registered.getAllValues();
        assertNotSame(identities.get(0), identities.get(1));
        assertNotSame(identities.get(0), identities.get(2));
        assertNotSame(identities.get(1), identities.get(2));
        for (RSocketRegisterInfo identity : identities) {
            assertEquals("worker", identity.getWorkerSeqNumber());
            assertEquals("127.0.0.1", identity.getWorkerIp());
            assertSame(requester, identity.getRequester());
        }

        ArgumentCaptor<RSocketRegisterInfo> released = ArgumentCaptor.forClass(RSocketRegisterInfo.class);
        verify(registry).unRegister(released.capture());
        assertSame(identities.get(2), released.getValue());
        verify(socket).dispose();
        verifyNoMoreInteractions(registry);
    }

    @Test
    public void shutdownClosesEverySocketWhenUnregisteringFails() {
        DmServerSideRegistry registry = mock(DmServerSideRegistry.class);
        RSocketRequester first = mock(RSocketRequester.class);
        RSocketRequester second = mock(RSocketRequester.class);
        RSocket firstSocket = mock(RSocket.class);
        RSocket secondSocket = mock(RSocket.class);
        when(first.rsocket()).thenReturn(firstSocket);
        when(second.rsocket()).thenReturn(secondSocket);
        Map<String, RSocketRequester> connections = new LinkedHashMap<>();
        connections.put("first-worker", first);
        connections.put("second-worker", second);
        when(registry.getRequesterMap()).thenReturn(connections);
        RSocketRegisterInfo firstInfo = new RSocketRegisterInfo();
        firstInfo.setRequester(first);
        RSocketRegisterInfo secondInfo = new RSocketRegisterInfo();
        secondInfo.setRequester(second);
        when(registry.connection("first-worker")).thenReturn(firstInfo);
        when(registry.connection("second-worker")).thenReturn(secondInfo);
        doThrow(new IllegalStateException("metadata unavailable")).when(registry).unRegister(firstInfo);
        DmServerSender sender = new DmServerSender(mock(RSocketRequestManager.class),
            mock(SystemDal.class),
            registry,
            RSocketSerializationImpl.DEFAULT,
            mock(ConsoleClusterService.class),
            new ConsoleClusterProperties(),
            mock(ConsoleInternalClient.class));

        sender.shutdown();

        verify(registry).unRegister(firstInfo);
        verify(registry).unRegister(secondInfo);
        verify(firstSocket).dispose();
        verify(secondSocket).dispose();
    }

    @Test
    public void firstProviderSnapshotRebuildsExistingClientsAndRetriesFailedRefreshes() throws Exception {
        String owner = "provider-owner";
        DmSysUserConfDO authConfig = new DmSysUserConfDO();
        authConfig.setConfigName(RootUserConfig.Fields.accountAuthType);
        authConfig.setConfigValue("OIDC");
        DmSysUserConfDO valueConfig = new DmSysUserConfDO();
        valueConfig.setConfigName("fixtureLoginValue");
        valueConfig.setConfigValue("v2");
        DmSysUserConfMapper configs = mock(DmSysUserConfMapper.class);
        when(configs.listByUid(owner)).thenReturn(List.of(authConfig, valueConfig));
        SystemDal systemDal = mock(SystemDal.class);
        when(systemDal.userConfMapper()).thenReturn(configs);
        UserConfigKvDef authDefinition = new UserConfigKvDef();
        authDefinition.setConfigName(RootUserConfig.Fields.accountAuthType);
        authDefinition.setDefaultValue("PASSWORD");
        authDefinition.setUserConfigTagType(UserConfigTagType.SECURITY);
        UserConfigKvDef valueDefinition = new UserConfigKvDef();
        valueDefinition.setConfigName("fixtureLoginValue");
        valueDefinition.setUserConfigTagType(UserConfigTagType.OIDC);
        UserConfigServiceImpl definitions = mock(UserConfigServiceImpl.class);
        when(definitions.fetchUserDefinedDefaultConfig(owner)).thenReturn(List.of(authDefinition, valueDefinition));
        AuthDal authDal = mock(AuthDal.class);
        when(authDal.isRootUser(owner)).thenReturn(true);
        LoginDefService loginDefinitions = mock(LoginDefService.class);
        when(loginDefinitions.checkLoginEnable(LoginProvider.OIDC)).thenReturn(true);
        when(loginDefinitions.listConfLoginTypes()).thenReturn(List.of(LoginAuthType.OIDC));
        LoginStarter login = new LoginStarter();
        ReflectionTestUtils.setField(login, "authDal", authDal);
        ReflectionTestUtils.setField(login, "loginDefService", loginDefinitions);
        ProviderConfigRefresh refresh = new ProviderConfigRefresh();
        ReflectionTestUtils.setField(refresh, "systemDal", systemDal);
        ReflectionTestUtils.setField(refresh, "userConfigService", definitions);
        ReflectionTestUtils.setField(refresh, "loginStarter", login);
        AtomicReference<String> client = new AtomicReference<>("v1");
        AtomicBoolean removedClientRunning = new AtomicBoolean(true);
        AtomicBoolean rejectStart = new AtomicBoolean();
        LoginProviderSpi oidc = mock(LoginProviderSpi.class);
        LoginProviderSpi feishu = mock(LoginProviderSpi.class);
        doAnswer(invocation -> {
            client.set(null);
            return null;
        }).when(oidc).stop(eq(owner), any(LifeSpiRequest.class));
        doAnswer(invocation -> {
            if (rejectStart.get()) {
                throw new IllegalStateException("provider client initialization failed");
            }

            client.compareAndSet(null, valueConfig.getConfigValue());
            return null;
        }).when(oidc).start(eq(owner), any(LifeSpiRequest.class));
        doAnswer(invocation -> {
            removedClientRunning.set(false);
            return null;
        }).when(feishu).stop(eq(owner), any(LifeSpiRequest.class));

        try (MockedStatic<PluginManager> plugins = mockStatic(PluginManager.class)) {
            plugins.when(() -> PluginManager.findSpi(LoginProviderSpi.class, LoginProvider.OIDC.name())).thenReturn(oidc);
            plugins.when(() -> PluginManager.findSpi(LoginProviderSpi.class, LoginProvider.Feishu.name())).thenReturn(feishu);

            ReflectionTestUtils.invokeMethod(refresh, "refreshLogin", owner);
            assertEquals("v2", client.get());
            assertFalse(removedClientRunning.get());
            ReflectionTestUtils.invokeMethod(refresh, "refreshLogin", owner);
            verify(oidc).start(eq(owner), any(LifeSpiRequest.class));
            verify(feishu).stop(eq(owner), any(LifeSpiRequest.class));

            valueConfig.setConfigValue("v1");

            ReflectionTestUtils.invokeMethod(refresh, "refreshLogin", owner);
            assertEquals("v1", client.get());

            authConfig.setConfigValue("PASSWORD");

            ReflectionTestUtils.invokeMethod(refresh, "refreshLogin", owner);
            assertNull(client.get());

            authConfig.setConfigValue("OIDC");
            valueConfig.setConfigValue("v3");
            rejectStart.set(true);

            try {
                ReflectionTestUtils.invokeMethod(refresh, "refreshLogin", owner);
                fail("failed initialization must not commit the configuration snapshot");
            } catch (IllegalStateException expected) {
                assertEquals("provider configuration is not ready", expected.getMessage());
            }

            Map<?, ?> snapshots = (Map<?, ?>) ReflectionTestUtils.getField(refresh, "loginSnapshots");
            assertFalse(snapshots.containsKey(owner));
            rejectStart.set(false);
            ReflectionTestUtils.invokeMethod(refresh, "refreshLogin", owner);
            assertEquals("v3", client.get());
            ReflectionTestUtils.invokeMethod(refresh, "refreshLogin", owner);
            verify(oidc, times(4)).start(eq(owner), any(LifeSpiRequest.class));
            verify(feishu, never()).start(eq(owner), any(LifeSpiRequest.class));
        }
    }

    @Test
    public void configurationNotificationsOnlyIncludeAffectedDomains() {
        ProviderConfigRefresh refresh = spy(new ProviderConfigRefresh());
        ScheduledExecutorService executor = mock(ScheduledExecutorService.class);
        doAnswer(call -> {
            ((Runnable) call.getArgument(0)).run();
            return null;
        }).when(executor).execute(any(Runnable.class));
        ReflectionTestUtils.setField(refresh, "executor", executor);
        doNothing().when(refresh).apply(any(ProviderConfigChange.class));
        for (UserConfigTagType tag : List.of(UserConfigTagType.COMMON, UserConfigTagType.QUERY_RESULT, UserConfigTagType.SECURITY)) {
            refresh.notifyUserConfig("owner", List.of(providerConfigChange("unrelated", tag)));
        }

        refresh.notifyUserConfig("owner", List.of());
        verifyNoInteractions(executor);
        refresh.notifyUserConfig("owner", List.of(providerConfigChange("ldap-setting", UserConfigTagType.LDAP_CONFIG)));
        ArgumentCaptor<ProviderConfigChange> changes = ArgumentCaptor.forClass(ProviderConfigChange.class);
        verify(refresh).apply(changes.capture());
        assertEquals("owner", changes.getValue().getOwnerUid());
        assertTrue(changes.getValue().isLogin());
        assertFalse(changes.getValue().isApproval());
        clearInvocations(refresh);
        refresh.notifyUserConfig("owner", List.of(providerConfigChange("feishu-setting", UserConfigTagType.FEISHU)));
        verify(refresh).apply(changes.capture());
        assertTrue(changes.getValue().isLogin());
        assertTrue(changes.getValue().isApproval());
    }

    @Test
    public void providerNotificationsRunOnlyAfterConfigurationCommit() {
        for (String failure : List.of("none", "validation", "write", "commit")) {
            PlatformTransactionManager transactions = mock(PlatformTransactionManager.class);
            SimpleTransactionStatus transaction = new SimpleTransactionStatus();
            when(transactions.getTransaction(any())).thenReturn(transaction);
            SystemDal systemDal = mock(SystemDal.class);
            DmSysUserConfMapper configs = mock(DmSysUserConfMapper.class);
            when(systemDal.userConfMapper()).thenReturn(configs);
            DmSysUserConfDO oldConfig = new DmSysUserConfDO();
            oldConfig.setConfigValue("before");
            when(configs.queryByUidAndConfigName("owner", "setting")).thenReturn(oldConfig);
            UserConfigKvDef definition = new UserConfigKvDef();
            definition.setConfigName("setting");
            definition.setUserConfigTagType(UserConfigTagType.OIDC);
            RdpNotifyService validator = mock(RdpNotifyService.class);
            ProviderConfigRefresh refresh = mock(ProviderConfigRefresh.class);
            UserConfigServiceImpl service = spy(new UserConfigServiceImpl());
            ReflectionTestUtils.setField(service, "txManager", transactions);
            ReflectionTestUtils.setField(service, "systemDal", systemDal);
            ReflectionTestUtils.setField(service, "providerConfig", refresh);
            ReflectionTestUtils.setField(service, "notifyServices", List.of(validator));
            doReturn(List.of(definition)).when(service).fetchUserDefinedDefaultConfig("owner");
            IllegalStateException expected = new IllegalStateException(failure);
            if ("validation".equals(failure)) {
                doThrow(expected).when(validator).notifyUserConfig(eq("owner"), anyList());
            } else if ("write".equals(failure)) {
                doThrow(expected).when(configs).updateUserConfig("owner", "setting", "after");
            } else if ("commit".equals(failure)) {
                doThrow(expected).when(transactions).commit(transaction);
            }

            UpsertUserConfigFO update = new UpsertUserConfigFO();
            update.setUpdateConfigs(Map.of("setting", "after"));
            try {
                List<UpsertUserConfigLO> result = service.upsertConfigValue("owner", update);
                assertEquals("none", failure);
                assertEquals(1, result.size());
                InOrder order = inOrder(configs, validator, transactions, refresh);
                order.verify(configs).updateUserConfig("owner", "setting", "after");
                order.verify(validator).notifyUserConfig(eq("owner"), anyList());
                order.verify(transactions).commit(transaction);
                order.verify(refresh)
                    .notifyUserConfig(eq("owner"), argThat(changes -> changes.size() == 1 && "setting".equals(changes.get(0).getConfig())
                                                                      && changes.get(0).getTagType() == UserConfigTagType.OIDC));
            } catch (IllegalStateException error) {
                assertSame(expected, error);
                verifyNoInteractions(refresh);
                if (!"commit".equals(failure)) {
                    verify(transactions).rollback(transaction);
                }
            }
        }
    }

    @Test
    public void providerConfigurationRefreshesLocallyAfterSaveAndOnPeerReconciliation() {
        String owner = "owner";
        String setting = "feishu-setting";
        DmAuthUserDO user = new DmAuthUserDO();
        user.setUid(owner);
        DmAuthUserMapper users = mock(DmAuthUserMapper.class);
        when(users.listPrimaryAccount()).thenReturn(List.of(user));
        AuthDal authDal = mock(AuthDal.class);
        when(authDal.userMapper()).thenReturn(users);

        DmSysUserConfDO saved = new DmSysUserConfDO();
        saved.setConfigName(setting);
        saved.setConfigValue("before");
        DmSysUserConfMapper configs = mock(DmSysUserConfMapper.class);
        when(configs.listByUid(owner)).thenReturn(List.of(saved));
        SystemDal systemDal = mock(SystemDal.class);
        when(systemDal.userConfMapper()).thenReturn(configs);

        UserConfigKvDef definition = new UserConfigKvDef();
        definition.setConfigName(setting);
        definition.setUserConfigTagType(UserConfigTagType.FEISHU);
        UserConfigServiceImpl definitions = mock(UserConfigServiceImpl.class);
        when(definitions.fetchUserDefinedDefaultConfig(owner)).thenReturn(List.of(definition));

        LoginStarter localLogin = mock(LoginStarter.class);
        ApprovalStarter localApproval = mock(ApprovalStarter.class);
        ScheduledExecutorService localExecutor = mock(ScheduledExecutorService.class);
        ProviderConfigRefresh local = new ProviderConfigRefresh();
        ReflectionTestUtils.setField(local, "authDal", authDal);
        ReflectionTestUtils.setField(local, "systemDal", systemDal);
        ReflectionTestUtils.setField(local, "userConfigService", definitions);
        ReflectionTestUtils.setField(local, "loginStarter", localLogin);
        ReflectionTestUtils.setField(local, "approvalStarter", localApproval);
        ReflectionTestUtils.setField(local, "executor", localExecutor);

        LoginStarter peerLogin = mock(LoginStarter.class);
        ApprovalStarter peerApproval = mock(ApprovalStarter.class);
        ProviderConfigRefresh peer = new ProviderConfigRefresh();
        ReflectionTestUtils.setField(peer, "authDal", authDal);
        ReflectionTestUtils.setField(peer, "systemDal", systemDal);
        ReflectionTestUtils.setField(peer, "userConfigService", definitions);
        ReflectionTestUtils.setField(peer, "loginStarter", peerLogin);
        ReflectionTestUtils.setField(peer, "approvalStarter", peerApproval);

        ReflectionTestUtils.invokeMethod(local, "reconcile");
        ReflectionTestUtils.invokeMethod(peer, "reconcile");
        clearInvocations(localLogin, localApproval, peerLogin, peerApproval);

        saved.setConfigValue("after");
        local.notifyUserConfig(owner, List.of(providerConfigChange(setting, UserConfigTagType.FEISHU)));

        ArgumentCaptor<Runnable> queued = ArgumentCaptor.forClass(Runnable.class);
        verify(localExecutor).execute(queued.capture());
        verifyNoInteractions(localLogin, localApproval, peerLogin, peerApproval);

        queued.getValue().run();
        verify(localLogin).notifyUserConfig(eq(owner), argThat(changes -> "after".equals(changes.get(0).getNewValue())));
        verify(localApproval).notifyUserConfig(eq(owner), argThat(changes -> "after".equals(changes.get(0).getNewValue())));
        verifyNoInteractions(peerLogin, peerApproval);

        ReflectionTestUtils.invokeMethod(peer, "reconcile");
        verify(peerLogin).notifyUserConfig(eq(owner), argThat(changes -> "after".equals(changes.get(0).getNewValue())));
        verify(peerApproval).notifyUserConfig(eq(owner), argThat(changes -> "after".equals(changes.get(0).getNewValue())));
        ReflectionTestUtils.invokeMethod(peer, "reconcile");
        verifyNoMoreInteractions(peerLogin, peerApproval);
    }

    @Test
    public void providerReconciliationRetriesFailedOwners() {
        ProviderConfigRefresh refresh = spy(new ProviderConfigRefresh());
        AuthDal authDal = mock(AuthDal.class);
        DmAuthUserMapper users = mock(DmAuthUserMapper.class);
        when(authDal.userMapper()).thenReturn(users);
        ReflectionTestUtils.setField(refresh, "authDal", authDal);
        DmAuthUserDO first = new DmAuthUserDO();
        first.setUid("first");
        DmAuthUserDO second = new DmAuthUserDO();
        second.setUid("second");
        when(users.listPrimaryAccount()).thenReturn(List.of(first, second));
        AtomicBoolean firstAttempt = new AtomicBoolean(true);
        doAnswer(call -> {
            ProviderConfigChange change = call.getArgument(0);
            if ("first".equals(change.getOwnerUid()) && firstAttempt.getAndSet(false)) {
                throw new IllegalStateException("temporary provider failure");
            }
            return null;
        }).when(refresh).apply(any(ProviderConfigChange.class));
        ReflectionTestUtils.invokeMethod(refresh, "reconcile");
        ReflectionTestUtils.invokeMethod(refresh, "reconcile");
        verify(refresh, times(2)).apply(argThat(change -> "first".equals(change.getOwnerUid()) && change.isLogin() && change.isApproval()));
        verify(refresh, times(2)).apply(argThat(change -> "second".equals(change.getOwnerUid()) && change.isLogin() && change.isApproval()));
    }

    @Test
    public void failedLoginRefreshDoesNotPreventApprovalRefreshOrCommitItsSnapshot() {
        String owner = "shared-provider-owner";
        SystemDal systemDal = mock(SystemDal.class);
        DmSysUserConfMapper configs = mock(DmSysUserConfMapper.class);
        when(systemDal.userConfMapper()).thenReturn(configs);
        when(configs.listByUid(owner)).thenReturn(List.of());
        UserConfigKvDef definition = new UserConfigKvDef();
        definition.setConfigName("shared-setting");
        definition.setDefaultValue("current");
        definition.setUserConfigTagType(UserConfigTagType.FEISHU);
        UserConfigServiceImpl definitions = mock(UserConfigServiceImpl.class);
        when(definitions.fetchUserDefinedDefaultConfig(owner)).thenReturn(List.of(definition));
        LoginStarter login = mock(LoginStarter.class);
        doThrow(new IllegalStateException("login restart failed")).doNothing().when(login).notifyUserConfig(eq(owner), anyList());
        ApprovalStarter approval = mock(ApprovalStarter.class);
        ProviderConfigRefresh refresh = new ProviderConfigRefresh();
        ReflectionTestUtils.setField(refresh, "systemDal", systemDal);
        ReflectionTestUtils.setField(refresh, "userConfigService", definitions);
        ReflectionTestUtils.setField(refresh, "loginStarter", login);
        ReflectionTestUtils.setField(refresh, "approvalStarter", approval);
        ProviderConfigChange change = new ProviderConfigChange();
        change.setOwnerUid(owner);
        change.setLogin(true);
        change.setApproval(true);
        try {
            refresh.apply(change);
            fail("the failed login update must remain visible and retryable");
        } catch (IllegalStateException expected) {
            assertEquals("provider configuration is not ready", expected.getMessage());
        }

        verify(approval).notifyUserConfig(eq(owner), anyList());
        refresh.apply(change);
        refresh.apply(change);
        verify(login, times(2)).notifyUserConfig(eq(owner), anyList());
        verifyNoMoreInteractions(approval);
    }

    private UserConfigMO providerConfigChange(String name, UserConfigTagType tag) {
        UserConfigMO config = new UserConfigMO();
        config.setConfig(name);
        config.setTagType(tag);
        config.setUpdate(true);
        return config;
    }

    @Test
    public void contactUpdatesUseExistingExternalLoginClient() {
        DmAuthUserDO external = new DmAuthUserDO();
        external.setUid("external-user");
        external.setUsername("external-login");
        external.setParentId(42L);
        external.setAccountType(AccountType.SUB_ACCOUNT);
        external.setBindType(AccountBindType.LDAP);
        DmAuthUserDO primary = new DmAuthUserDO();
        primary.setUid("provider-owner");

        AuthDal authDal = mock(AuthDal.class);
        DmAuthUserMapper users = mock(DmAuthUserMapper.class);
        when(authDal.userMapper()).thenReturn(users);
        when(users.queryByUid(external.getUid())).thenReturn(external);
        when(users.queryById(external.getParentId())).thenReturn(primary);
        ProviderConfigRefresh refresh = mock(ProviderConfigRefresh.class);
        LoginProviderSpi provider = mock(LoginProviderSpi.class);
        when(provider.authLogin(eq(primary.getUid()), any(LoginRequest.class))).thenReturn(new LoginResponse(null, true));
        RdpUserServiceImpl service = new RdpUserServiceImpl();
        ReflectionTestUtils.setField(service, "authDal", authDal);

        UpdateUserPhoneWithPwdFO phone = new UpdateUserPhoneWithPwdFO();
        phone.setPhone("13800000000");
        phone.setPassword("verification-password");
        UpdateUserEmailWithPwdFO email = new UpdateUserEmailWithPwdFO();
        email.setEmail("contact@example.invalid");
        email.setPassword("verification-password");

        try (MockedStatic<PluginManager> plugins = mockStatic(PluginManager.class)) {
            plugins.when(() -> PluginManager.findSpi(LoginProviderSpi.class, LoginProvider.LDAP.name())).thenReturn(provider);

            assertTrue(service.updateUserPhoneWithPwd(external.getUid(), phone).isSuccess());
            assertTrue(service.updateUserEmailWithPwd(external.getUid(), email).isSuccess());

            InOrder order = inOrder(refresh, provider, users);
            order.verify(provider)
                .authLogin(eq(primary.getUid()), argThat(request -> external.getUsername().equals(request.getLoginAccount())
                                                                    && phone.getPassword().equals(request.getLoginPassword()) && request.getLoginVerifyCode() == null));
            order.verify(users).updateUserContactInfo(external.getUid(), phone.getPhone(), null);
            order.verify(provider).authLogin(eq(primary.getUid()), any(LoginRequest.class));
            order.verify(users).updateUserContactInfo(external.getUid(), null, email.getEmail());

            verify(provider, times(2)).authLogin(eq(primary.getUid()), any(LoginRequest.class));
            verifyNoInteractions(refresh);
        }
    }

    @Test
    public void primaryAndInternalContactUpdatesKeepLocalPasswordChecks() {
        AuthDal authDal = mock(AuthDal.class);
        DmAuthUserMapper users = mock(DmAuthUserMapper.class);
        when(authDal.userMapper()).thenReturn(users);
        ProviderConfigRefresh refresh = mock(ProviderConfigRefresh.class);
        RdpUserServiceImpl service = new RdpUserServiceImpl();
        ReflectionTestUtils.setField(service, "authDal", authDal);

        UpdateUserPhoneWithPwdFO phone = new UpdateUserPhoneWithPwdFO();
        phone.setPhone("13800000000");
        phone.setPassword("verification-password");
        UpdateUserEmailWithPwdFO email = new UpdateUserEmailWithPwdFO();
        email.setEmail("contact@example.invalid");
        email.setPassword("verification-password");

        try (MockedStatic<RdpAuthUtils> passwords = mockStatic(RdpAuthUtils.class); MockedStatic<PluginManager> plugins = mockStatic(PluginManager.class)) {
            passwords.when(() -> RdpAuthUtils.isErrorPasswd("encoded-password", phone.getPassword())).thenReturn(false);

            for (AccountType type : List.of(AccountType.PRIMARY_ACCOUNT, AccountType.SUB_ACCOUNT)) {
                DmAuthUserDO user = new DmAuthUserDO();
                user.setUid(type.name());
                user.setAccountType(type);
                user.setBindType(AccountBindType.INTERNAL);
                user.setParentId(42L);
                user.setPassword("encoded-password");
                when(users.queryByUid(user.getUid())).thenReturn(user);

                assertTrue(service.updateUserPhoneWithPwd(user.getUid(), phone).isSuccess());
                assertTrue(service.updateUserEmailWithPwd(user.getUid(), email).isSuccess());
            }

            passwords.verify(() -> RdpAuthUtils.isErrorPasswd("encoded-password", phone.getPassword()), times(4));
            plugins.verifyNoInteractions();
        }

        verifyNoInteractions(refresh);
        verify(users, never()).queryById(anyLong());
    }

    @Test
    public void changedConnectionIsNotAutomaticallyRerouted() throws Exception {
        assertRoutingRetryContract(true);
    }

    @Test
    public void unknownTransportOutcomeIsNeverAutomaticallyReplayed() throws Exception {
        assertRoutingRetryContract(false);
    }

    @Test
    public void registryReportsUnexpectedFailuresWithoutAlertingExpectedRejections() {
        SystemDal systemDal = mock(SystemDal.class);
        DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
        when(systemDal.workerMapper()).thenReturn(workers);
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.getInstanceId()).thenReturn("console-a");
        when(cluster.isAcceptingWork()).thenReturn(true);
        when(cluster.isAlive("console-b")).thenReturn(true);
        RSocketExceptionManager errors = mock(RSocketExceptionManager.class);
        DmWorkerRegisterNotify callback = mock(DmWorkerRegisterNotify.class);
        ExecutionDal executionDal = mock(ExecutionDal.class);
        DmExecSessionMapper sessions = mock(DmExecSessionMapper.class);
        when(executionDal.sessionMapper()).thenReturn(sessions);
        DmServerSideRegistry registry = new DmServerSideRegistry(mock(AuthDal.class), systemDal, executionDal, List.of(callback), cluster, errors);
        RSocketRegisterInfo info = new RSocketRegisterInfo();
        info.setWorkerSeqNumber("test-worker");
        RSocketRequester requester = mock(RSocketRequester.class);
        when(requester.rsocket()).thenReturn(mock(RSocket.class));
        info.setRequester(requester);

        IllegalStateException databaseFailure = new IllegalStateException("metadata unavailable");
        when(workers.lockByWsn("test-worker")).thenThrow(databaseFailure);
        try {
            registry.register(info);
            fail("unexpected database failure must propagate");
        } catch (IllegalStateException expected) {
            assertSame(databaseFailure, expected);
        }

        verify(errors).saveExcOrAlert(databaseFailure);

        DmSysWorkerDO worker = new DmSysWorkerDO();
        worker.setWorkerSeqNumber("test-worker");
        worker.setOwnerInstanceId("console-b");
        worker.setConnectionGeneration(7);
        worker.setConnStatus(WorkerConnStatus.CONNECTED);
        doReturn(worker).when(workers).lockByWsn("test-worker");
        try {
            registry.register(info);
            fail("an active owner must reject another registration");
        } catch (IllegalStateException expected) {
            assertEquals("worker already has an active owner", expected.getMessage());
        }

        // A stale disposed local entry must not steal ownership from another alive Console
        RSocketRegisterInfo staleLocal = new RSocketRegisterInfo();
        staleLocal.setWorkerSeqNumber("test-worker");
        staleLocal.setOwnerConsoleId("console-a");
        staleLocal.setConnectionGeneration(6);
        RSocketRequester staleRequester = mock(RSocketRequester.class);
        RSocket staleSocket = mock(RSocket.class);
        when(staleSocket.isDisposed()).thenReturn(true);
        when(staleRequester.rsocket()).thenReturn(staleSocket);
        staleLocal.setRequester(staleRequester);
        @SuppressWarnings("unchecked")
        Map<String, RSocketRegisterInfo> localConns = (Map<String, RSocketRegisterInfo>) ReflectionTestUtils.getField(registry, "connections");
        localConns.put("test-worker", staleLocal);
        try {
            registry.register(info);
            fail("stale disposed local entry must not steal active peer owner");
        } catch (IllegalStateException expected) {
            assertEquals("worker already has an active owner", expected.getMessage());
        } finally {
            localConns.remove("test-worker");
        }

        verifyNoMoreInteractions(errors);

        worker.setConnStatus(WorkerConnStatus.DISCONNECTED);
        when(workers.updateConnectionByWsnAndOwner(any())).thenReturn(1);
        IllegalStateException callbackFailure = new IllegalStateException("registration callback failed");
        doThrow(callbackFailure).when(callback).notifyRegister("test-worker");
        try {
            registry.register(info);
            fail("unexpected callback failure must propagate");
        } catch (IllegalStateException expected) {
            assertSame(callbackFailure, expected);
        }

        verify(errors).saveExcOrAlert(callbackFailure);
        InOrder claimedBeforeInvalidation = inOrder(workers, sessions, callback);
        claimedBeforeInvalidation.verify(workers).updateConnectionByWsn(any());
        claimedBeforeInvalidation.verify(sessions).updateActiveBatchIdByWsn("test-worker", DmExecSessionDO.INTERRUPTED_BATCH_ID);
        claimedBeforeInvalidation.verify(callback).notifyRegister("test-worker");

        IllegalStateException unregisterFailure = new IllegalStateException("metadata unavailable during unregister");
        doThrow(unregisterFailure).when(workers).updateConnectionByWsnAndOwner(any());
        try {
            registry.unRegister(info);
            fail("unexpected unregister failure must propagate");
        } catch (IllegalStateException expected) {
            assertSame(unregisterFailure, expected);
        }

        verify(errors).saveExcOrAlert(unregisterFailure);
    }

    @Test
    public void forwardedRequestRestoresTheCallerAndExecutorMdc() throws Exception {
        ConsoleInternalClient internalClient = mock(ConsoleInternalClient.class);
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.getInstanceId()).thenReturn("console-b");
        DmServerSender sender = mock(DmServerSender.class);
        ConsoleInternalController controller = new ConsoleInternalController();
        ReflectionTestUtils.setField(controller, "internalClient", internalClient);
        ReflectionTestUtils.setField(controller, "cluster", cluster);
        ReflectionTestUtils.setField(controller, "sender", sender);
        ReflectionTestUtils.setField(controller, "channels", mock(WsChannel.class));
        ThreadPoolExecutor executor;
        Map<String, String> previous = MDC.getCopyOfContextMap();
        try {
            assertNull(ReflectionTestUtils.getField(controller, "calls"));
            ReflectionTestUtils.invokeMethod(controller, "init");
            executor = (ThreadPoolExecutor) ReflectionTestUtils.getField(controller, "calls");
            assertEquals(4, executor.getCorePoolSize());
            assertEquals(16, executor.getMaximumPoolSize());
            assertEquals(30, executor.getKeepAliveTime(TimeUnit.SECONDS));
            assertEquals(64, executor.getQueue().remainingCapacity());
            assertTrue(executor.getRejectedExecutionHandler() instanceof ThreadPoolExecutor.AbortPolicy);
            Thread worker = executor.getThreadFactory().newThread(() -> {
            });

            assertTrue(worker.isDaemon());
            assertEquals("console-peer-rpc", worker.getName());
            ReflectionTestUtils.invokeMethod(controller, "init");
            assertSame(executor, ReflectionTestUtils.getField(controller, "calls"));
            executor.setCorePoolSize(1);
            executor.setMaximumPoolSize(1);
            executor.submit(() -> MDC.setContextMap(Map.of("module", "executor-module", "trace", "executor-trace"))).get(5, TimeUnit.SECONDS);
            when(sender.sendLocal(any())).thenAnswer(invocation -> {
                assertEquals("caller-module", MDC.get("module"));
                assertEquals("caller-trace", MDC.get("trace"));
                return RSocketRespUtil.buildSuccess("pong");
            });

            ConsoleForwardRequest request = new ConsoleForwardRequest();
            request.setOriginConsoleId("console-a");
            request.setTargetConsoleId("console-b");
            request.setForwardCount(1);
            request.setDeadline(System.currentTimeMillis() + 10000);
            request.setRoute("test.worker.ping");
            request.setLocale("en-US");
            request.setParams(List.of());
            String body = JsonUtils.toJson(request);
            HttpServletRequest servletRequest = mock(HttpServletRequest.class);
            when(internalClient.verify(servletRequest, body)).thenReturn("console-a");
            when(servletRequest.getHeader(ConsoleInternalClient.HEADER_DEADLINE)).thenReturn(String.valueOf(request.getDeadline()));

            MDC.setContextMap(Map.of("module", "caller-module", "trace", "caller-trace"));
            assertEquals("1", controller.forward(body, servletRequest).get(5, TimeUnit.SECONDS).getCode());
            assertEquals("caller-module", MDC.get("module"));
            assertEquals("caller-trace", MDC.get("trace"));
            assertEquals(Map.of("module", "executor-module", "trace", "executor-trace"), executor.submit(() -> {
                return MDC.getCopyOfContextMap();
            }).get(5, TimeUnit.SECONDS));
            controller.stop();
            controller.stop();
            ReflectionTestUtils.invokeMethod(controller, "init");
            assertSame(executor, ReflectionTestUtils.getField(controller, "calls"));
            assertTrue(executor.isShutdown());
        } finally {
            controller.stop();
            if (previous == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(previous);
            }
        }
    }

    @Test
    public void internalControllerStoppedBeforeInitDoesNotCreateAnExecutor() {
        ConsoleInternalController controller = new ConsoleInternalController();
        ReflectionTestUtils.setField(controller, "internalClient", mock(ConsoleInternalClient.class));
        ReflectionTestUtils.setField(controller, "cluster", mock(ConsoleClusterService.class));
        ReflectionTestUtils.setField(controller, "sender", mock(RSocketServerSender.class));
        ReflectionTestUtils.setField(controller, "channels", mock(WsChannel.class));
        try {
            assertNull(ReflectionTestUtils.getField(controller, "calls"));
            controller.stop();
            controller.stop();
            ReflectionTestUtils.invokeMethod(controller, "init");
            assertNull(ReflectionTestUtils.getField(controller, "calls"));
        } finally {
            controller.stop();
        }
    }

    @Test
    public void peerClientCreatesItsOwnedResourcesOnlyDuringInitAndDoesNotReviveAfterStop() {
        ConsoleInternalClient internalClient = new ConsoleInternalClient();
        ReflectionTestUtils.setField(internalClient, "properties", new ConsoleClusterProperties());
        ReflectionTestUtils.setField(internalClient, "cluster", mock(ConsoleClusterService.class));
        try {
            assertNull(ReflectionTestUtils.getField(internalClient, "client"));
            assertNull(ReflectionTestUtils.getField(internalClient, "mac"));
            ReflectionTestUtils.invokeMethod(internalClient, "init");
            OkHttpClient client = (OkHttpClient) ReflectionTestUtils.getField(internalClient, "client");
            assertNotNull(client);
            assertNull(ReflectionTestUtils.getField(internalClient, "mac"));
            assertFalse(client.retryOnConnectionFailure());
            assertFalse(client.followRedirects());
            assertFalse(client.followSslRedirects());
            assertFalse(client.dispatcher().executorService().isShutdown());
            ReflectionTestUtils.invokeMethod(internalClient, "init");
            assertSame(client, ReflectionTestUtils.getField(internalClient, "client"));

            internalClient.stop();
            internalClient.stop();
            ReflectionTestUtils.invokeMethod(internalClient, "init");
            assertSame(client, ReflectionTestUtils.getField(internalClient, "client"));
            assertNull(ReflectionTestUtils.getField(internalClient, "mac"));
            assertTrue(client.dispatcher().executorService().isShutdown());
            assertEquals(0, client.connectionPool().connectionCount());
        } finally {
            internalClient.stop();
        }
    }

    @Test
    public void peerClientStoppedBeforeInitDoesNotCreateAClient() {
        ConsoleInternalClient internalClient = new ConsoleInternalClient();
        ReflectionTestUtils.setField(internalClient, "properties", new ConsoleClusterProperties());
        ReflectionTestUtils.setField(internalClient, "cluster", mock(ConsoleClusterService.class));
        try {
            internalClient.stop();
            internalClient.stop();
            ReflectionTestUtils.invokeMethod(internalClient, "init");
            assertNull(ReflectionTestUtils.getField(internalClient, "client"));
            assertNull(ReflectionTestUtils.getField(internalClient, "mac"));
        } finally {
            internalClient.stop();
        }
    }

    @Test
    public void peerHmacInitializationPreservesTheExistingUtf8SignatureAcrossCallsAndLifecycle() throws Exception {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setEnabled(true);
        properties.setSecret("共享密钥🔐");
        ConsoleInternalClient internalClient = new ConsoleInternalClient();
        ReflectionTestUtils.setField(internalClient, "properties", properties);
        ReflectionTestUtils.setField(internalClient, "cluster", mock(ConsoleClusterService.class));
        try {
            assertNull(ReflectionTestUtils.getField(internalClient, "mac"));
            assertNull(ReflectionTestUtils.getField(internalClient, "client"));
            internalClient.init();
            Mac initialized = (Mac) ReflectionTestUtils.getField(internalClient, "mac");
            OkHttpClient client = (OkHttpClient) ReflectionTestUtils.getField(internalClient, "client");
            assertNotNull(initialized);
            assertNotNull(client);
            for (int round = 0; round < 16; round++) {
                String origin = "console-a";
                String target = "console-b";
                String path = ConsoleInternalPaths.DISPATCH;
                String deadline = "1800000030000";
                String timestamp = "1800000000000";
                String nonce = "utf8-" + round;
                String body = "{\"message\":\"中文与emoji🔐\",\"round\":" + round + "}\n";
                String canonical = origin + "\n" + target + "\nPOST\n" + path + "\n" + deadline + "\n" + timestamp + "\n" + nonce + "\n" + body;
                Mac oracle = Mac.getInstance("HmacSHA256");
                oracle.init(new SecretKeySpec(properties.getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                String expected = Base64.getEncoder().encodeToString(oracle.doFinal(canonical.getBytes(StandardCharsets.UTF_8)));
                assertEquals(expected, ReflectionTestUtils.invokeMethod(internalClient, "signature", origin, target, path, deadline, timestamp, nonce, body));
                assertSame(initialized, ReflectionTestUtils.getField(internalClient, "mac"));
            }

            internalClient.init();
            assertSame(initialized, ReflectionTestUtils.getField(internalClient, "mac"));
            assertSame(client, ReflectionTestUtils.getField(internalClient, "client"));
            internalClient.stop();
            internalClient.stop();
            internalClient.init();
            assertSame(initialized, ReflectionTestUtils.getField(internalClient, "mac"));
            assertSame(client, ReflectionTestUtils.getField(internalClient, "client"));
            assertTrue(client.dispatcher().executorService().isShutdown());
        } finally {
            internalClient.stop();
        }
    }

    @Test(timeout = 20000)
    public void peerHmacRemainsIndependentAcrossConcurrentSigningAndVerification() throws Exception {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setEnabled(true);
        properties.setSecret("并发签名🔐");
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.isLeaseValid()).thenReturn(true);
        when(cluster.getInstanceId()).thenReturn("console-b");
        when(cluster.isAlive("console-a")).thenReturn(true);
        ConsoleInternalClient internalClient = new ConsoleInternalClient();
        ReflectionTestUtils.setField(internalClient, "properties", properties);
        ReflectionTestUtils.setField(internalClient, "cluster", cluster);
        ExecutorService executor = Executors.newFixedThreadPool(8);
        CountDownLatch start = new CountDownLatch(1);
        try {
            internalClient.init();
            List<Future<?>> requests = new ArrayList<>();
            for (int worker = 0; worker < 8; worker++) {
                int workerId = worker;
                requests.add(executor.submit(() -> {
                    start.await();
                    for (int round = 0; round < 32; round++) {
                        String origin = "console-a";
                        String target = "console-b";
                        String path = ConsoleInternalPaths.DISPATCH;
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        String deadline = String.valueOf(System.currentTimeMillis() + 30000);
                        String nonce = "concurrent-" + workerId + "-" + round;
                        String body = "{\"message\":\"并发中文🔐\",\"worker\":" + workerId + ",\"round\":" + round + "}";
                        String canonical = origin + "\n" + target + "\nPOST\n" + path + "\n" + deadline + "\n" + timestamp + "\n" + nonce + "\n" + body;
                        Mac oracle = Mac.getInstance("HmacSHA256");
                        oracle.init(new SecretKeySpec(properties.getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                        String expected = Base64.getEncoder().encodeToString(oracle.doFinal(canonical.getBytes(StandardCharsets.UTF_8)));
                        String signature = ReflectionTestUtils.invokeMethod(internalClient, "signature", origin, target, path, deadline, timestamp, nonce, body);
                        assertEquals(expected, signature);
                        MockHttpServletRequest request = new MockHttpServletRequest("POST", path);
                        request.addHeader(ConsoleInternalClient.HEADER_ORIGIN, origin);
                        request.addHeader(ConsoleInternalClient.HEADER_TARGET, target);
                        request.addHeader(ConsoleInternalClient.HEADER_TIME, timestamp);
                        request.addHeader(ConsoleInternalClient.HEADER_DEADLINE, deadline);
                        request.addHeader(ConsoleInternalClient.HEADER_NONCE, nonce);
                        request.addHeader(ConsoleInternalClient.HEADER_SIGNATURE, expected);
                        assertEquals(origin, internalClient.verify(request, body));
                        try {
                            internalClient.verify(request, body);
                            fail("a verified nonce must not be accepted twice");
                        } catch (IllegalArgumentException expectedReplay) {
                            assertEquals("cluster request replay rejected", expectedReplay.getCause().getMessage());
                        }
                    }
                    return null;
                }));
            }

            start.countDown();
            for (Future<?> request : requests) {
                request.get(10, TimeUnit.SECONDS);
            }

            assertEquals(256, ((Map<?, ?>) ReflectionTestUtils.getField(internalClient, "nonces")).size());
        } finally {
            start.countDown();
            executor.shutdownNow();
            internalClient.stop();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    @Test
    public void enabledPeerRejectsMissingOrBlankSecretsBeforeAllocatingResources() throws Exception {
        for (String secret : Arrays.asList(null, "", " ", "\t\r\n")) {
            ConsoleClusterProperties properties = new ConsoleClusterProperties();
            properties.setEnabled(true);
            properties.setSecret(secret);
            ConsoleInternalClient internalClient = new ConsoleInternalClient();
            ReflectionTestUtils.setField(internalClient, "properties", properties);
            ReflectionTestUtils.setField(internalClient, "cluster", mock(ConsoleClusterService.class));
            try {
                try {
                    internalClient.init();
                    fail("enabled cluster must reject a missing or blank secret during initialization");
                } catch (IllegalArgumentException expected) {
                    assertTrue(expected.getMessage().toLowerCase(Locale.ROOT).contains("secret"));
                }

                assertNull(ReflectionTestUtils.getField(internalClient, "mac"));
                assertNull(ReflectionTestUtils.getField(internalClient, "client"));
                internalClient.stop();
                internalClient.stop();
                properties.setSecret("valid-after-stop");
                internalClient.init();
                assertNull(ReflectionTestUtils.getField(internalClient, "mac"));
                assertNull(ReflectionTestUtils.getField(internalClient, "client"));
            } finally {
                internalClient.stop();
            }
        }
    }

    @Test
    public void websocketInitializesItsNotificationPoolOnceAndDoesNotReviveAfterStop() throws Exception {
        WsChannel channel = new WsChannel();
        try (MockedStatic<DmGlobalEventBus> events = mockStatic(DmGlobalEventBus.class)) {
            try {
                assertNull(ReflectionTestUtils.getField(channel, "notifications"));
                channel.init();
                ThreadPoolExecutor notifications = (ThreadPoolExecutor) ReflectionTestUtils.getField(channel, "notifications");
                assertEquals(2, notifications.getCorePoolSize());
                assertEquals(8, notifications.getMaximumPoolSize());
                assertEquals(30, notifications.getKeepAliveTime(TimeUnit.SECONDS));
                assertEquals(256, notifications.getQueue().remainingCapacity());
                assertTrue(notifications.getRejectedExecutionHandler() instanceof ThreadPoolExecutor.AbortPolicy);
                Thread worker = notifications.getThreadFactory().newThread(() -> {
                });

                assertTrue(worker.isDaemon());
                assertEquals("console-user-notification", worker.getName());
                channel.init();
                assertSame(notifications, ReflectionTestUtils.getField(channel, "notifications"));
                channel.stop();
                channel.stop();
                channel.init();
                assertSame(notifications, ReflectionTestUtils.getField(channel, "notifications"));
                assertTrue(notifications.isShutdown());
                assertFalse(((AtomicBoolean) ReflectionTestUtils.getField(channel, "inited")).get());
                events.verify(() -> DmGlobalEventBus.addQueryResultEventListen(any()), times(1));
            } finally {
                channel.stop();
            }
        }
    }

    @Test
    public void websocketStoppedBeforeInitDoesNotCreateResourcesOrListeners() throws Exception {
        WsChannel channel = new WsChannel();
        try (MockedStatic<DmGlobalEventBus> events = mockStatic(DmGlobalEventBus.class)) {
            try {
                channel.stop();
                channel.stop();
                channel.init();
                assertNull(ReflectionTestUtils.getField(channel, "notifications"));
                assertFalse(((AtomicBoolean) ReflectionTestUtils.getField(channel, "inited")).get());
                events.verifyNoInteractions();
            } finally {
                channel.stop();
            }
        }
    }

    @Test
    public void websocketReleasesItsPoolWhenListenerInitializationFails() throws Exception {
        WsChannel channel = new WsChannel();
        IllegalStateException initializationFailure = new IllegalStateException("event listener registration failed");
        try (MockedStatic<DmGlobalEventBus> events = mockStatic(DmGlobalEventBus.class)) {
            events.when(() -> DmGlobalEventBus.addQueryResultEventListen(any())).thenThrow(initializationFailure);
            try {
                try {
                    channel.init();
                    fail("listener registration must fail");
                } catch (IllegalStateException expected) {
                    assertSame(initializationFailure, expected);
                }

                ThreadPoolExecutor notifications = (ThreadPoolExecutor) ReflectionTestUtils.getField(channel, "notifications");
                assertTrue(notifications.isShutdown());
                assertFalse(((AtomicBoolean) ReflectionTestUtils.getField(channel, "inited")).get());
                channel.stop();
                channel.stop();
                channel.init();
                assertSame(notifications, ReflectionTestUtils.getField(channel, "notifications"));
                assertTrue(notifications.isShutdown());
                assertFalse(((AtomicBoolean) ReflectionTestUtils.getField(channel, "inited")).get());
                events.verify(() -> DmGlobalEventBus.addQueryResultEventListen(any()), times(1));
            } finally {
                channel.stop();
            }
        }
    }

    @Test
    public void consoleInventoryReportsExpiredInstancesAsOffline() throws Exception {
        DmSysConsoleDO online = new DmSysConsoleDO();
        online.setConsoleInstanceId("control-a");
        online.setAdvertiseUrl("http://console-a:8222");
        online.setState("READY");
        online.setLeaseUntil(new Date(Long.MAX_VALUE));

        DmSysConsoleDO expired = new DmSysConsoleDO();
        expired.setConsoleInstanceId("control-b");
        expired.setState("OFFLINE");
        expired.setLeaseUntil(new Date(0));

        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        when(mapper.listConsolesWithStatus()).thenReturn(List.of(online, expired));
        ConsoleClusterService service = new ConsoleClusterService();
        ReflectionTestUtils.setField(service, "mapper", mapper);
        ClusterController controller = new ClusterController();
        ReflectionTestUtils.setField(controller, "consoleClusterService", service);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

        mvc.perform(post("/api/entry/cluster/listConsoleInstances"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].consoleInstanceId").value("control-a"))
            .andExpect(jsonPath("$.data[0].advertiseUrl").value("http://console-a:8222"))
            .andExpect(jsonPath("$.data[0].status").value("READY"))
            .andExpect(jsonPath("$.data[1].consoleInstanceId").value("control-b"))
            .andExpect(jsonPath("$.data[1].status").value("OFFLINE"))
            .andExpect(jsonPath("$.data[0].instanceId").doesNotExist());
    }

    @Test
    public void duplicateConsoleIdIsRejectedUntilTheOldLeaseExpires() {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setInstanceId("console-a");
        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        doThrow(new DuplicateKeyException("duplicate ID")).when(mapper).insertConsole(any());
        when(mapper.replaceExpiredConsole(any())).thenReturn(0, 1);
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBean(WsChannel.class)).thenReturn(mock(WsChannel.class));
        when(context.getBean(RSocketServerSender.class)).thenReturn(mock(RSocketServerSender.class));
        ConsoleClusterService rejected = new ConsoleClusterService();
        ConsoleClusterService restarted = new ConsoleClusterService();
        for (ConsoleClusterService cluster : List.of(rejected, restarted)) {
            ReflectionTestUtils.setField(cluster, "properties", properties);
            ReflectionTestUtils.setField(cluster, "mapper", mapper);
            ReflectionTestUtils.setField(cluster, "context", context);
        }

        try {
            try {
                rejected.init();
                fail("a live Console already owns the configured ID");
            } catch (IllegalStateException expected) {
                assertEquals("Console instance ID is already in use: console-a", expected.getMessage());
            }

            assertFalse(rejected.isLeaseValid());
            restarted.init();
            assertTrue(restarted.isLeaseValid());
            assertEquals(rejected.getConsoleInstanceId(), restarted.getConsoleInstanceId());
            assertNotEquals(rejected.getInstanceId(), restarted.getInstanceId());
        } finally {
            rejected.stop();
            restarted.stop();
        }
    }

    @Test(timeout = 5000)
    public void springInjectsClusterBeforeUnifiedInitializationAndClosesItsLeaseScheduler() throws Exception {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setInstanceId("configured-console");
        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        WsChannel channels = mock(WsChannel.class);
        RSocketServerSender sender = mock(RSocketServerSender.class);
        when(mapper.updateStateAndLeaseUntilExcludingState(anyString(), eq(ConsoleLeaseState.READY.name()), eq(ConsoleLeaseState.DRAINING.name()))).thenReturn(1);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getBeanFactory().registerSingleton("properties", properties);
        context.getBeanFactory().registerSingleton("mapper", mapper);
        context.getBeanFactory().registerSingleton("channels", channels);
        context.getBeanFactory().registerSingleton("sender", sender);
        context.registerBean(ConsoleClusterService.class);
        try {
            verifyNoInteractions(mapper);
            context.refresh();

            ConsoleClusterService cluster = context.getBean(ConsoleClusterService.class);
            assertSame(properties, ReflectionTestUtils.getField(cluster, "properties"));
            assertSame(mapper, ReflectionTestUtils.getField(cluster, "mapper"));
            assertSame(context, ReflectionTestUtils.getField(cluster, "context"));
            assertNull(ReflectionTestUtils.getField(cluster, "renewal"));
            assertFalse(cluster.isLeaseValid());
            verifyNoInteractions(mapper);

            UnifiedPostConstructUtils.doPostConstruct(context, type -> type == ConsoleClusterService.class);

            ScheduledExecutorService renewal = (ScheduledExecutorService) ReflectionTestUtils.getField(cluster, "renewal");
            assertNotNull(renewal);
            assertFalse(renewal.isShutdown());
            assertTrue(cluster.isLeaseValid());
            assertFalse(cluster.isAcceptingWork());
            ArgumentCaptor<DmSysConsoleDO> member = ArgumentCaptor.forClass(DmSysConsoleDO.class);
            verify(mapper).insertConsole(member.capture());
            assertEquals(cluster.getInstanceId(), member.getValue().getInstanceId());
            assertEquals("configured-console", member.getValue().getConsoleInstanceId());
            assertEquals(ConsoleLeaseState.STARTING.name(), member.getValue().getState());
            ReflectionTestUtils.invokeMethod(cluster, "init");
            assertSame(renewal, ReflectionTestUtils.getField(cluster, "renewal"));
            cluster.markReady();
            assertTrue(cluster.isAcceptingWork());

            UnifiedPostConstructUtils.doDestroyConstruct(context, type -> type == ConsoleClusterService.class);
            assertTrue(renewal.isShutdown());
            assertFalse(cluster.isAcceptingWork());
            verify(channels).stop();
            verify(sender).shutdown();
            cluster.stop();
            ReflectionTestUtils.invokeMethod(cluster, "init");
            assertSame(renewal, ReflectionTestUtils.getField(cluster, "renewal"));
            assertTrue(renewal.isShutdown());
            assertFalse(cluster.isLeaseValid());
            verify(mapper, times(1)).insertConsole(any());
            context.close();
            assertTrue(renewal.isShutdown());
            verify(channels, times(1)).stop();
            verify(sender, times(1)).shutdown();
        } finally {
            if (context.isActive()) {
                UnifiedPostConstructUtils.doDestroyConstruct(context, type -> type == ConsoleClusterService.class);
            }

            context.close();
        }
    }

    @Test
    public void unifiedLifecycleInitializesResourcesBeforeTheLeaseAndDrainsTheLeaseFirst() throws Exception {
        ConsoleInternalClient internalClient = mock(ConsoleInternalClient.class);
        ConsoleInternalController controller = mock(ConsoleInternalController.class);
        WsChannel channels = mock(WsChannel.class);
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        Map<String, UnifiedPostConstruct> beans = new LinkedHashMap<>();
        beans.put("cluster", cluster);
        beans.put("channels", channels);
        beans.put("internalClient", internalClient);
        beans.put("controller", controller);
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBeansOfType(UnifiedPostConstruct.class)).thenReturn(beans);
        doReturn(ConsoleClusterService.class).when(context).getType("cluster");
        doReturn(WsChannel.class).when(context).getType("channels");
        doReturn(ConsoleInternalClient.class).when(context).getType("internalClient");
        doReturn(ConsoleInternalController.class).when(context).getType("controller");

        UnifiedPostConstructUtils.doPostConstruct(context);
        UnifiedPostConstructUtils.doDestroyConstruct(context);

        for (UnifiedPostConstruct resource : List.of(internalClient, controller, channels)) {
            InOrder lifecycle = inOrder(resource, cluster);
            lifecycle.verify(resource).init();
            lifecycle.verify(cluster).init();
            lifecycle.verify(cluster).stop();
            lifecycle.verify(resource).stop();
        }
    }

    @Test
    public void unifiedShutdownContinuesClosingResourcesWhenDrainingTheLeaseFails() {
        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        WsChannel channels = mock(WsChannel.class);
        RSocketServerSender sender = mock(RSocketServerSender.class);
        ConsoleInternalClient internalClient = mock(ConsoleInternalClient.class);
        ConsoleInternalController controller = mock(ConsoleInternalController.class);
        ScheduledExecutorService renewal = mock(ScheduledExecutorService.class);
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBean(WsChannel.class)).thenReturn(channels);
        when(context.getBean(RSocketServerSender.class)).thenReturn(sender);

        ConsoleClusterService cluster = new ConsoleClusterService();
        ReflectionTestUtils.setField(cluster, "mapper", mapper);
        ReflectionTestUtils.setField(cluster, "context", context);
        ReflectionTestUtils.setField(cluster, "renewal", renewal);
        when(mapper.updateStateAndLeaseUntilToNow(cluster.getInstanceId(), ConsoleLeaseState.DRAINING.name())).thenThrow(new IllegalStateException("database unavailable"));

        Map<String, UnifiedPostConstruct> beans = new LinkedHashMap<>();
        beans.put("cluster", cluster);
        beans.put("internalClient", internalClient);
        beans.put("controller", controller);
        when(context.getBeansOfType(UnifiedPostConstruct.class)).thenReturn(beans);
        doReturn(ConsoleClusterService.class).when(context).getType("cluster");
        doReturn(ConsoleInternalClient.class).when(context).getType("internalClient");
        doReturn(ConsoleInternalController.class).when(context).getType("controller");

        UnifiedPostConstructUtils.doDestroyConstruct(context);

        assertFalse(cluster.isLeaseValid());
        assertEquals(ConsoleLeaseState.DRAINING, ReflectionTestUtils.getField(cluster, "state"));
        verify(channels).stop();
        verify(sender).shutdown();
        InOrder shutdown = inOrder(renewal, mapper, controller, internalClient);
        shutdown.verify(renewal).shutdownNow();
        shutdown.verify(mapper).updateStateAndLeaseUntilToNow(cluster.getInstanceId(), ConsoleLeaseState.DRAINING.name());
        shutdown.verify(controller).stop();
        shutdown.verify(internalClient).stop();
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void rsocketReadyFailureClosesTheStartedServerAndPreservesTheOriginalFailure() {
        for (boolean cleanupFails : List.of(false, true)) {
            ConsoleRSocketServer server = mock(ConsoleRSocketServer.class);
            ConsoleClusterService cluster = mock(ConsoleClusterService.class);
            ApplicationContext context = mock(ApplicationContext.class);
            when(context.getClassLoader()).thenReturn(getClass().getClassLoader());
            IllegalStateException readyFailure = new IllegalStateException("console lease is not valid");
            IllegalStateException cleanupFailure = new IllegalStateException("server cleanup failed");
            doThrow(readyFailure).when(cluster).markReady();
            if (cleanupFails) {
                doThrow(cleanupFailure).when(server).stop();
            }

            RSocketServerServiceImpl service = new RSocketServerServiceImpl();
            ReflectionTestUtils.setField(service, "consoleRSocketServer", server);
            ReflectionTestUtils.setField(service, "cluster", cluster);
            ReflectionTestUtils.setField(service, "applicationContext", context);
            try (MockedStatic<RSocketApiManager> apis = mockStatic(RSocketApiManager.class)) {
                try {
                    service.init();
                    fail("READY publication must fail");
                } catch (IllegalStateException expected) {
                    assertSame(readyFailure, expected);
                    assertEquals(cleanupFails ? 1 : 0, expected.getSuppressed().length);
                    if (cleanupFails) {
                        assertSame(cleanupFailure, expected.getSuppressed()[0]);
                    }
                }

                apis.verify(() -> RSocketApiManager.scanAllApiAndRegister(eq(getClass().getClassLoader()), eq("com.clougence.clouddm"), any()));
            }

            InOrder startup = inOrder(server, cluster);
            startup.verify(server).start();
            startup.verify(cluster).markReady();
            startup.verify(server).stop();
        }
    }

    @Test
    public void clusterCanStopAfterConfigurationValidationFailsWithoutCreatingAScheduler() {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setEnabled(true);
        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBean(WsChannel.class)).thenReturn(mock(WsChannel.class));
        when(context.getBean(RSocketServerSender.class)).thenReturn(mock(RSocketServerSender.class));
        ConsoleClusterService cluster = new ConsoleClusterService();
        ReflectionTestUtils.setField(cluster, "properties", properties);
        ReflectionTestUtils.setField(cluster, "mapper", mapper);
        ReflectionTestUtils.setField(cluster, "context", context);
        try {
            try {
                ReflectionTestUtils.invokeMethod(cluster, "init");
                fail("cluster configuration must be validated before starting resources");
            } catch (IllegalArgumentException expected) {
                assertNull(ReflectionTestUtils.getField(cluster, "renewal"));
            }

            cluster.stop();
            cluster.stop();
            ReflectionTestUtils.invokeMethod(cluster, "init");
            assertNull(ReflectionTestUtils.getField(cluster, "renewal"));
            verify(mapper, never()).insertConsole(any());
        } finally {
            cluster.stop();
        }
    }

    @Test(timeout = 5000)
    public void singleConsoleRecoversItsLeaseWithoutRevivingAfterStop() {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBean(WsChannel.class)).thenReturn(mock(WsChannel.class));
        when(context.getBean(RSocketServerSender.class)).thenReturn(mock(RSocketServerSender.class));
        ConsoleClusterService cluster = new ConsoleClusterService();
        ReflectionTestUtils.setField(cluster, "properties", properties);
        ReflectionTestUtils.setField(cluster, "mapper", mapper);
        ReflectionTestUtils.setField(cluster, "context", context);
        ReflectionTestUtils.setField(cluster, "state", ConsoleLeaseState.READY);
        when(mapper.updateStateAndLeaseUntilExcludingState(cluster.getInstanceId(), ConsoleLeaseState.READY.name(), ConsoleLeaseState.DRAINING.name()))
            .thenThrow(new IllegalStateException("database unavailable"))
            .thenReturn(1);

        try {
            ReflectionTestUtils.invokeMethod(cluster, "renew");
            assertFalse(cluster.isLeaseValid());
            assertFalse(cluster.isAcceptingWork());
            assertTrue(cluster.isAlive(cluster.getInstanceId()));

            ReflectionTestUtils.invokeMethod(cluster, "renew");
            assertTrue(cluster.isLeaseValid());
            assertTrue(cluster.isAcceptingWork());

            cluster.stop();
            ReflectionTestUtils.invokeMethod(cluster, "renew");
            assertFalse(cluster.isLeaseValid());
            assertFalse(cluster.isAcceptingWork());
            assertFalse(cluster.isAlive(cluster.getInstanceId()));
            verify(mapper, times(2)).updateStateAndLeaseUntilExcludingState(cluster.getInstanceId(), ConsoleLeaseState.READY.name(), ConsoleLeaseState.DRAINING.name());
            verify(mapper, never()).updateStateAndLeaseUntil(anyString(), anyString());
        } finally {
            cluster.stop();
        }
    }

    @Test(timeout = 5000)
    public void clusteredConsoleNeverRenewsAnExpiredLease() {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setEnabled(true);
        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        ApplicationContext context = mock(ApplicationContext.class);
        WsChannel channels = mock(WsChannel.class);
        RSocketServerSender sender = mock(RSocketServerSender.class);
        when(context.getBean(WsChannel.class)).thenReturn(channels);
        when(context.getBean(RSocketServerSender.class)).thenReturn(sender);
        ConsoleClusterService cluster = new ConsoleClusterService();
        ReflectionTestUtils.setField(cluster, "properties", properties);
        ReflectionTestUtils.setField(cluster, "mapper", mapper);
        ReflectionTestUtils.setField(cluster, "context", context);
        ReflectionTestUtils.setField(cluster, "state", ConsoleLeaseState.READY);

        try {
            ReflectionTestUtils.invokeMethod(cluster, "renew");
            assertFalse(cluster.isLeaseValid());
            assertFalse(cluster.isAcceptingWork());
            verify(channels).stop();
            verify(sender).shutdown();
            verify(mapper, never()).updateStateAndLeaseUntil(anyString(), anyString());
            verify(mapper, never()).updateStateAndLeaseUntilExcludingState(anyString(), anyString(), anyString());
        } finally {
            cluster.stop();
        }
    }

    @Test
    public void schedulerKeepsItsLiveOwnerAndDoesNotPreemptDrainingWork() {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setEnabled(true);
        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        ConsoleClusterService follower = new ConsoleClusterService();
        ReflectionTestUtils.setField(follower, "properties", properties);
        ReflectionTestUtils.setField(follower, "mapper", mapper);
        ReflectionTestUtils.setField(follower, "context", mock(ApplicationContext.class));
        ReflectionTestUtils.setField(follower, "state", ConsoleLeaseState.READY);
        ReflectionTestUtils.setField(follower, "leaseDeadline", System.nanoTime() + TimeUnit.SECONDS.toNanos(30));
        DmSysConsoleDO leader = new DmSysConsoleDO();
        leader.setInstanceId("existing-leader");
        leader.setState(ConsoleLeaseState.DRAINING.name());
        when(mapper.querySchedulerOwnerInstanceId()).thenReturn(leader.getInstanceId());
        when(mapper.queryByInstanceIdAndLeaseUntilAfterNow(leader.getInstanceId())).thenReturn(leader);

        assertSame(leader, follower.schedulerLeader());
        assertFalse(follower.isSchedulerLeader());
        verify(mapper, never()).updateSchedulerOwnerInstanceId(any(), any());
    }

    @Test
    public void onlyOneConsoleClaimsSchedulingAfterThePreviousMemberExpires() {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setEnabled(true);
        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        ConsoleClusterService first = new ConsoleClusterService();
        ReflectionTestUtils.setField(first, "properties", properties);
        ReflectionTestUtils.setField(first, "mapper", mapper);
        ReflectionTestUtils.setField(first, "context", mock(ApplicationContext.class));
        ConsoleClusterService second = new ConsoleClusterService();
        ReflectionTestUtils.setField(second, "properties", properties);
        ReflectionTestUtils.setField(second, "mapper", mapper);
        ReflectionTestUtils.setField(second, "context", mock(ApplicationContext.class));
        ReflectionTestUtils.setField(first, "state", ConsoleLeaseState.READY);
        ReflectionTestUtils.setField(second, "state", ConsoleLeaseState.READY);
        ReflectionTestUtils.setField(first, "leaseDeadline", System.nanoTime() + TimeUnit.SECONDS.toNanos(30));
        ReflectionTestUtils.setField(second, "leaseDeadline", System.nanoTime() + TimeUnit.SECONDS.toNanos(30));
        AtomicReference<String> owner = new AtomicReference<>("expired-leader");
        when(mapper.querySchedulerOwnerInstanceId()).thenAnswer(invocation -> owner.get());
        when(mapper.queryByInstanceIdAndLeaseUntilAfterNow(anyString())).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);
            if ("expired-leader".equals(id)) {
                return null;
            }

            DmSysConsoleDO member = new DmSysConsoleDO();
            member.setInstanceId(id);
            member.setState(ConsoleLeaseState.READY.name());
            return member;
        });

        when(mapper.updateSchedulerOwnerInstanceId(any(), anyString())).thenAnswer(invocation -> {
            if (owner.compareAndSet(invocation.getArgument(0), invocation.getArgument(1))) {
                return 1;
            }
            return 0;
        });

        CompletableFuture<Boolean> firstClaim = CompletableFuture.supplyAsync(first::isSchedulerLeader);
        CompletableFuture<Boolean> secondClaim = CompletableFuture.supplyAsync(second::isSchedulerLeader);
        assertNotEquals(firstClaim.join(), secondClaim.join());
        assertEquals(owner.get().equals(first.getInstanceId()), first.isSchedulerLeader());
        assertEquals(owner.get().equals(second.getInstanceId()), second.isSchedulerLeader());
    }

    @Test
    public void expiredConsoleCannotStartSchedulingOrClaimLeadership() {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setEnabled(true);
        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        ConsoleClusterService cluster = new ConsoleClusterService();
        ReflectionTestUtils.setField(cluster, "properties", properties);
        ReflectionTestUtils.setField(cluster, "mapper", mapper);
        ReflectionTestUtils.setField(cluster, "context", mock(ApplicationContext.class));
        ReflectionTestUtils.setField(cluster, "state", ConsoleLeaseState.READY);

        assertFalse(cluster.isSchedulerLeader());
        try {
            cluster.requireSchedulerLeader();
            fail("an expired Console must not start scheduled work");
        } catch (IllegalStateException expected) {
            assertEquals("console is not the scheduler leader", expected.getMessage());
        }

        verify(mapper, never()).updateSchedulerOwnerInstanceId(any(), any());
    }

    @Test
    public void stoppedClusteredConsoleImmediatelyRejectsForwardedWork() {
        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setEnabled(true);
        DmSysConsoleMapper mapper = mock(DmSysConsoleMapper.class);
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBean(WsChannel.class)).thenReturn(mock(WsChannel.class));
        when(context.getBean(RSocketServerSender.class)).thenReturn(mock(RSocketServerSender.class));
        ConsoleClusterService cluster = new ConsoleClusterService();
        ReflectionTestUtils.setField(cluster, "properties", properties);
        ReflectionTestUtils.setField(cluster, "mapper", mapper);
        ReflectionTestUtils.setField(cluster, "context", context);
        ReflectionTestUtils.setField(cluster, "state", ConsoleLeaseState.READY);
        ReflectionTestUtils.setField(cluster, "leaseDeadline", System.nanoTime() + TimeUnit.SECONDS.toNanos(30));
        assertTrue(cluster.isLeaseValid());
        assertNull(ReflectionTestUtils.getField(cluster, "renewal"));

        cluster.stop();

        assertFalse(cluster.isLeaseValid());
        assertFalse(cluster.isAcceptingWork());
        verify(mapper).updateStateAndLeaseUntilToNow(cluster.getInstanceId(), ConsoleLeaseState.DRAINING.name());
        cluster.stop();
        ReflectionTestUtils.invokeMethod(cluster, "init");
        assertNull(ReflectionTestUtils.getField(cluster, "renewal"));
        verify(mapper, never()).insertConsole(any());
        verify(mapper, times(2)).updateStateAndLeaseUntilToNow(cluster.getInstanceId(), ConsoleLeaseState.DRAINING.name());
    }

    private void assertRoutingRetryContract(boolean explicitlyNotDispatched) throws Exception {
        SystemDal systemDal = mock(SystemDal.class);
        DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
        when(systemDal.workerMapper()).thenReturn(workers);

        DmSysWorkerDO first = new DmSysWorkerDO();
        first.setWorkerSeqNumber("test-worker");
        first.setOwnerInstanceId("console-b");
        first.setConnectionGeneration(7);

        DmSysWorkerDO second = new DmSysWorkerDO();
        second.setWorkerSeqNumber("test-worker");
        second.setOwnerInstanceId("console-c");
        second.setConnectionGeneration(8);
        when(workers.queryConnectedByWsn("test-worker")).thenReturn(first, second);

        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.getInstanceId()).thenReturn("console-a");
        when(cluster.isLeaseValid()).thenReturn(true);
        DmSysConsoleDO memberB = new DmSysConsoleDO();
        memberB.setInstanceId("console-b");
        memberB.setAdvertiseUrl("http://console-b:8222");
        DmSysConsoleDO memberC = new DmSysConsoleDO();
        memberC.setInstanceId("console-c");
        memberC.setAdvertiseUrl("http://console-c:8222");
        when(cluster.findAlive("console-b")).thenReturn(memberB);
        when(cluster.findAlive("console-c")).thenReturn(memberC);

        ConsoleClusterProperties properties = new ConsoleClusterProperties();
        properties.setEnabled(true);
        ConsoleInternalClient internalClient = mock(ConsoleInternalClient.class);
        List<String> transmitted = new ArrayList<>();
        when(internalClient.post(any(), eq(ConsoleInternalPaths.FORWARD_WORKER_RPC), any(), anyLong())).thenAnswer(invocation -> {
            assertEquals(RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME, MDC.get("module"));
            assertEquals("caller-trace", MDC.get("trace"));
            transmitted.add(JsonUtils.toJson(invocation.getArgument(2)));
            if (!explicitlyNotDispatched) {
                throw new SocketTimeoutException("response lost");
            }

            if (transmitted.size() == 1) {
                return JsonUtils.toJson(RSocketRespUtil.buildError(RSocketRespCode.OWNER_CHANGED_NOT_DISPATCHED.getCode(), "owner changed before dispatch"));
            }

            return JsonUtils.toJson(RSocketRespUtil.buildSuccess("pong"));
        });

        RSocketSendDTO send = new RSocketSendDTO();
        send.setWorkerSeqNumber("test-worker");
        send.setUid("test-user");
        DmServerSender sender = new DmServerSender(mock(RSocketRequestManager.class),
            systemDal,
            mock(DmServerSideRegistry.class),
            RSocketSerializationImpl.DEFAULT,
            cluster,
            properties,
            internalClient);
        RSocketRespDTO<?> result;
        Map<String, String> previous = MDC.getCopyOfContextMap();
        try {
            MDC.setContextMap(Map.of("module", "caller-module", "trace", "caller-trace"));
            result = sender.requestNonBlock("test.worker.ping", "test-worker", new Object[] { send, 123L });
            assertEquals("caller-module", MDC.get("module"));
            assertEquals("caller-trace", MDC.get("trace"));
        } finally {
            if (previous == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(previous);
            }
        }

        ConsoleForwardRequest firstRequest = JsonUtils.toObj(transmitted.get(0), ConsoleForwardRequest.class);
        assertEquals("console-a", firstRequest.getOriginConsoleId());
        assertEquals("console-b", firstRequest.getTargetConsoleId());
        assertEquals(1, firstRequest.getForwardCount());

        if (explicitlyNotDispatched) {
            assertEquals(RSocketRespCode.OWNER_CHANGED_NOT_DISPATCHED.getCode(), result.getCode());
        } else {
            assertEquals(RSocketRespCode.EXECUTION_UNKNOWN.getCode(), result.getCode());
        }

        assertEquals(1, transmitted.size());
        verify(workers).queryConnectedByWsn("test-worker");
        verify(cluster, never()).findAlive("console-c");
    }

    @Test
    public void retiredSocketCannotUpdateTheNewOwnerAndIdentityStaysOffTheWire() {
        SystemDal systemDal = mock(SystemDal.class);
        DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
        when(systemDal.workerMapper()).thenReturn(workers);
        ExecutionDal executionDal = mock(ExecutionDal.class);
        DmExecSessionMapper sessions = mock(DmExecSessionMapper.class);
        when(executionDal.sessionMapper()).thenReturn(sessions);
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.getInstanceId()).thenReturn("console-a");
        when(cluster.isAcceptingWork()).thenReturn(true);
        when(cluster.isLeaseValid()).thenReturn(true);
        DmServerSideRegistry registry = new DmServerSideRegistry(mock(AuthDal.class), systemDal, executionDal, List.of(), cluster, throwable -> {
        });

        DmSysWorkerDO worker = new DmSysWorkerDO();
        worker.setWorkerSeqNumber("worker");
        worker.setConnectionGeneration(7);
        worker.setConnStatus(WorkerConnStatus.DISCONNECTED);
        when(workers.lockByWsn("worker")).thenReturn(worker);
        when(workers.getByWsn("worker")).thenReturn(worker);

        RSocketRequester retired = mock(RSocketRequester.class);
        when(retired.rsocket()).thenReturn(mock(RSocket.class));
        RSocketRegisterInfo first = new RSocketRegisterInfo();
        first.setWorkerSeqNumber("worker");
        first.setRequester(retired);
        registry.register(first);
        RSocketRequester current = mock(RSocketRequester.class);
        when(current.rsocket()).thenReturn(mock(RSocket.class));
        RSocketRegisterInfo second = new RSocketRegisterInfo();
        second.setWorkerSeqNumber("worker");
        second.setRequester(current);
        registry.register(second);
        registry.unRegister(first);
        assertSame(current, registry.getRSocketRequester("worker"));
        verify(workers).updateConnectionByWsnAndOwner(argThat(update -> update.getConnectionGeneration() == 8 && "console-a".equals(update.getOwnerInstanceId())));
        verify(sessions, times(2)).updateActiveBatchIdByWsn("worker", DmExecSessionDO.INTERRUPTED_BATCH_ID);

        WorkerIdentity identity = new WorkerIdentity();
        identity.setWorkerSeqNumber("worker");
        identity.setAccessKey("ak");
        RSocketRequestWrapperDTO request = new RSocketRequestWrapperDTO();
        request.setWorkerIdentity(identity);
        try {
            registry.checkIncoming(request, retired);
            fail("an obsolete socket must not supply a current connection identity");
        } catch (IllegalStateException expected) {
            assertEquals("stale worker connection", expected.getMessage());
        }

        registry.checkIncoming(request, current);
        assertEquals("console-a", identity.getOwnerConsoleId());
        assertEquals(9, identity.getConnectionGeneration());
        String json = JsonUtils.toJson(identity);
        assertFalse(json.contains("ownerConsoleId"));
        assertFalse(json.contains("connectionGeneration"));
        assertEquals("worker", JsonUtils.toObj(json, WorkerIdentity.class).getWorkerSeqNumber());
    }

    @Test
    public void registrationFinishingAfterConsoleStopClosesAndRemovesTheLateSocket() {
        SystemDal systemDal = mock(SystemDal.class);
        DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
        when(systemDal.workerMapper()).thenReturn(workers);
        ExecutionDal executionDal = mock(ExecutionDal.class);
        when(executionDal.sessionMapper()).thenReturn(mock(DmExecSessionMapper.class));
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        AtomicBoolean accepting = new AtomicBoolean(true);
        when(cluster.isAcceptingWork()).thenAnswer(invocation -> accepting.get());
        when(cluster.getInstanceId()).thenReturn("console-a");
        DmWorkerRegisterNotify callback = mock(DmWorkerRegisterNotify.class);
        doAnswer(invocation -> {
            accepting.set(false);
            return null;
        }).when(callback).notifyRegister("worker");
        DmServerSideRegistry registry = new DmServerSideRegistry(mock(AuthDal.class), systemDal, executionDal, List.of(callback), cluster, mock(RSocketExceptionManager.class));
        DmSysWorkerDO worker = new DmSysWorkerDO();
        worker.setWorkerSeqNumber("worker");
        worker.setConnectionGeneration(7);
        worker.setConnStatus(WorkerConnStatus.DISCONNECTED);
        when(workers.lockByWsn("worker")).thenReturn(worker);
        RSocket socket = mock(RSocket.class);
        RSocketRequester requester = mock(RSocketRequester.class);
        when(requester.rsocket()).thenReturn(socket);
        RSocketRegisterInfo info = new RSocketRegisterInfo();
        info.setWorkerSeqNumber("worker");
        info.setRequester(requester);
        try {
            registry.register(info);
            fail("a registration finishing after stop must be rejected");
        } catch (IllegalStateException expected) {
            assertEquals("console stopped during worker registration", expected.getMessage());
        }

        assertNull(registry.connection("worker"));
        assertNull(registry.getRSocketRequester("worker"));
        assertTrue(registry.getRequesterMap().isEmpty());
        verify(socket).dispose();
        verify(workers).updateConnectionByWsnAndOwner(argThat(update -> "worker".equals(update.getWorkerSeqNumber()) && "console-a".equals(update.getOwnerInstanceId())
                                                                        && update.getConnectionGeneration() == 8 && update.getConnStatus() == WorkerConnStatus.DISCONNECTED));
    }

    @Test
    public void expiredConsoleWorkersAreRetiredWithTheirOriginalOwnershipOnly() {
        SystemDal systemDal = mock(SystemDal.class);
        DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
        when(systemDal.workerMapper()).thenReturn(workers);
        ConsoleClusterService cluster = mock(ConsoleClusterService.class);
        when(cluster.getInstanceId()).thenReturn("console-a");
        when(cluster.isLeaseValid()).thenReturn(true);
        when(cluster.isAlive("console-live")).thenReturn(true);
        DmSysWorkerDO expired = new DmSysWorkerDO();
        expired.setWorkerSeqNumber("expired");
        expired.setOwnerInstanceId("console-dead");
        expired.setConnectionGeneration(4);
        DmSysWorkerDO live = new DmSysWorkerDO();
        live.setWorkerSeqNumber("live");
        live.setOwnerInstanceId("console-live");
        live.setConnectionGeneration(7);
        DmSysWorkerDO legacy = new DmSysWorkerDO();
        legacy.setWorkerSeqNumber("legacy");
        DmSysWorkerDO local = new DmSysWorkerDO();
        local.setWorkerSeqNumber("local");
        local.setOwnerInstanceId("console-a");
        local.setConnectionGeneration(9);
        local.setLastHeartbeatReportMs(System.currentTimeMillis());
        when(workers.queryByConnStatus(WorkerConnStatus.CONNECTED)).thenReturn(List.of(expired, live, legacy, local));
        WorkerStatusRService status = mock(WorkerStatusRService.class);
        DmServerSideRegistry registry = mock(DmServerSideRegistry.class);
        WorkerServiceImpl service = new WorkerServiceImpl();
        ReflectionTestUtils.setField(service, "systemDal", systemDal);
        ReflectionTestUtils.setField(service, "cluster", cluster);
        ReflectionTestUtils.setField(service, "registry", registry);
        ReflectionTestUtils.setField(service, "statusRService", status);

        ReflectionTestUtils.invokeMethod(service, "checkWorkerStatus");
        verify(workers).updateConnectionByWsnAndOwner(argThat(update -> "expired".equals(update.getWorkerSeqNumber()) && "console-dead".equals(update.getOwnerInstanceId())
                                                                        && update.getConnectionGeneration() == 4 && update.getConnStatus() == WorkerConnStatus.DISCONNECTED
                                                                        && update.getLastHeartbeatReportMs() == null));
        verify(workers).updateConnectionByWsnAndOwner(argThat(update -> "legacy".equals(update.getWorkerSeqNumber()) && update.getOwnerInstanceId() == null
                                                                        && update.getConnectionGeneration() == 0 && update.getConnStatus() == WorkerConnStatus.DISCONNECTED));
        verify(workers, never()).updateConnectionByWsnAndOwner(argThat(update -> "live".equals(update.getWorkerSeqNumber())));
        verify(workers, never()).updateConnectionByWsn(any());
        verify(status).ping(argThat(send -> "local".equals(send.getWorkerSeqNumber())), anyLong());
        verifyNoMoreInteractions(status);
        verifyNoInteractions(registry);

        clearInvocations(workers, status);
        when(cluster.isLeaseValid()).thenReturn(false);
        ReflectionTestUtils.invokeMethod(service, "checkWorkerStatus");
        verifyNoInteractions(workers, status);
    }

    @Test
    public void interruptedSessionsRejectProbeAndControlWithoutWorkerRpc() {
        QueryServiceImpl queries = new QueryServiceImpl();
        ExecutionDal executionDal = mock(ExecutionDal.class);
        DmExecSessionMapper sessions = mock(DmExecSessionMapper.class);
        when(executionDal.sessionMapper()).thenReturn(sessions);
        ReflectionTestUtils.setField(queries, "execDal", executionDal);
        ExecuteRService rpc = mock(ExecuteRService.class);
        ReflectionTestUtils.setField(queries, "sessionRService", rpc);
        DmExecSessionDO interrupted = new DmExecSessionDO();
        interrupted.setUid("uid");
        interrupted.setSessionId("sid");
        interrupted.setWsn("worker");
        interrupted.setActiveBatchId(DmExecSessionDO.INTERRUPTED_BATCH_ID);
        when(sessions.queryBySessionId("uid", "sid")).thenReturn(interrupted);
        for (Runnable operation : List.<Runnable> of(() -> queries.findActiveSession("uid", "sid"), () -> queries.isExecuting("uid", "sid"), () -> queries
            .commitSession("uid", "sid"), () -> queries.rollbackSession("uid", "sid"))) {
            try {
                operation.run();
                fail("a reconnected Worker cannot resume its previous SQL session");
            } catch (ErrorMessageException expected) {
                assertEquals(DmErrorCode.QUERY_SESSION_INTERRUPTED.code(), expected.getErrorCode());
            }
        }

        verifyNoInteractions(rpc);
        verify(sessions, never()).updateActiveBatchIfBatchNull(anyString(), anyString(), anyString());
    }

    @Test
    public void preDispatchFailuresEndOnlyThisCallsAuditsAndPreserveBusinessCodes() {
        for (String failure : List.of("claim", "request", "worker")) {
            DmExecSessionMapper sessions = mock(DmExecSessionMapper.class);
            DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
            ExecuteRService rpc = mock(ExecuteRService.class);
            AuditService audits = mock(AuditService.class);
            QueryServiceImpl queries = queryServiceForAuditBoundary(sessions, workers, rpc, audits);
            QueryRequest first = new QueryRequest();
            first.setQueryId("this-call-first");
            QueryRequest second = new QueryRequest();
            second.setQueryId("this-call-second");
            List<QueryRequest> requests = List.of(first, second);
            String expectedCode = ConsoleErrorCode.UNKNOWN.getCode();
            if ("claim".equals(failure)) {
                DmExecSessionDO claimed = sessions.queryBySessionId("uid", "sid");
                claimed.setActiveBatchId("same-batch");
                when(sessions.updateActiveBatchIfBatchNull("uid", "sid", "same-batch")).thenReturn(0);
                expectedCode = DmErrorCode.QUERY_SESSION_INTERRUPTED.code();
            } else if ("worker".equals(failure)) {
                when(workers.queryConnectedByWsn("worker")).thenReturn(null);
            }

            try (MockedStatic<DmDsUtils> configs = mockStatic(DmDsUtils.class)) {
                if ("request".equals(failure)) {
                    configs.when(() -> DmDsUtils.fillRequestConfig(requests, 1L)).thenThrow(new ErrorMessageException("Invalid request configuration"));
                }

                try {
                    queries.asyncExecuteQuery(sessions.queryBySessionId("uid", "sid"), "same-batch", requests);
                    fail("a failed pre-dispatch check must reject this call");
                } catch (ErrorMessageException expected) {
                    assertEquals(expectedCode, expected.getErrorCode());
                }
            }

            ArgumentCaptor<SqlExecNotifyDTO> ended = ArgumentCaptor.forClass(SqlExecNotifyDTO.class);
            verify(audits, times(2)).recordAudit(ended.capture(), isNull());
            assertEquals(List.of("this-call-first", "this-call-second"), ended.getAllValues().stream().map(SqlExecNotifyDTO::getQueryId).toList());
            for (SqlExecNotifyDTO audit : ended.getAllValues()) {
                assertEquals(SQL_END, audit.getType());
                assertEquals(SqlStatus.ERROR, audit.getStatus());
                assertEquals("sid", audit.getSessionId());
                assertNotNull(audit.getTime());
            }

            verifyNoMoreInteractions(audits);
            verifyNoInteractions(rpc);
            verify(sessions, never()).clearActiveBatchByBatch(anyString(), anyString(), anyString());
        }
    }

    @Test
    public void unknownDispatchOutcomesKeepAuditsPendingAndDoNotReleaseTheClaim() {
        for (boolean throwsException : new boolean[] { true, false }) {
            DmExecSessionMapper sessions = mock(DmExecSessionMapper.class);
            DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
            ExecuteRService rpc = mock(ExecuteRService.class);
            AuditService audits = mock(AuditService.class);
            QueryServiceImpl queries = queryServiceForAuditBoundary(sessions, workers, rpc, audits);
            QueryRequest request = new QueryRequest();
            request.setQueryId("unknown-call");
            List<QueryRequest> requests = List.of(request);
            ErrorMessageException unknown = new ErrorMessageException(RSocketRespCode.EXECUTION_UNKNOWN.getCode(), "Worker response was lost");
            if (throwsException) {
                when(rpc.asyncExecuteQuery(any(), eq("sid"), eq("same-batch"), same(requests))).thenThrow(unknown);
            } else {
                AsyncWaitResult failed = new AsyncWaitResult();
                failed.setSuccess(false);
                failed.setMessage("Failure after the Worker queued the batch");
                when(rpc.asyncExecuteQuery(any(), eq("sid"), eq("same-batch"), same(requests))).thenReturn(failed);
            }

            try (MockedStatic<DmDsUtils> configs = mockStatic(DmDsUtils.class)) {
                try {
                    queries.asyncExecuteQuery(sessions.queryBySessionId("uid", "sid"), "same-batch", requests);
                    fail("an unavailable dispatch outcome must propagate");
                } catch (ErrorMessageException expected) {
                    if (throwsException) {
                        assertSame(unknown, expected);
                        assertEquals(RSocketRespCode.EXECUTION_UNKNOWN.getCode(), expected.getErrorCode());
                    } else {
                        assertEquals("Failure after the Worker queued the batch", expected.getMessage());
                    }
                }
            }

            verify(sessions).updateActiveBatchIfBatchNull("uid", "sid", "same-batch");
            verify(rpc).asyncExecuteQuery(any(), eq("sid"), eq("same-batch"), same(requests));
            verifyNoInteractions(audits);
            verify(sessions, never()).clearActiveBatchByBatch(anyString(), anyString(), anyString());
        }
    }

    @Test
    public void auditCompletionFailuresDoNotReplaceTheOriginalPreDispatchFailure() {
        DmExecSessionMapper sessions = mock(DmExecSessionMapper.class);
        DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
        ExecuteRService rpc = mock(ExecuteRService.class);
        AuditService audits = mock(AuditService.class);
        QueryServiceImpl queries = queryServiceForAuditBoundary(sessions, workers, rpc, audits);
        ErrorMessageException original = new ErrorMessageException(DmErrorCode.QUERY_SESSION_INTERRUPTED.code(), "Original claim failure");
        when(sessions.updateActiveBatchIfBatchNull("uid", "sid", "same-batch")).thenThrow(original);
        doThrow(new IllegalStateException("audit storage unavailable")).when(audits).recordAudit(any(), isNull());
        QueryRequest first = new QueryRequest();
        first.setQueryId("first-audit-fails");
        QueryRequest second = new QueryRequest();
        second.setQueryId("second-audit-still-attempted");
        try (MockedStatic<DmDsUtils> configs = mockStatic(DmDsUtils.class)) {
            try {
                queries.asyncExecuteQuery(sessions.queryBySessionId("uid", "sid"), "same-batch", List.of(first, second));
                fail("the original claim failure must propagate");
            } catch (ErrorMessageException expected) {
                assertSame(original, expected);
                assertEquals(DmErrorCode.QUERY_SESSION_INTERRUPTED.code(), expected.getErrorCode());
            }
        }

        verify(audits).recordAudit(argThat(audit -> "first-audit-fails".equals(audit.getQueryId())), isNull());
        verify(audits).recordAudit(argThat(audit -> "second-audit-still-attempted".equals(audit.getQueryId())), isNull());
        verifyNoMoreInteractions(audits);
        verifyNoInteractions(rpc);
        verify(sessions, never()).clearActiveBatchByBatch(anyString(), anyString(), anyString());
    }

    private QueryServiceImpl queryServiceForAuditBoundary(DmExecSessionMapper sessions, DmSysWorkerMapper workers, ExecuteRService rpc, AuditService audits) {
        ExecutionDal executionDal = mock(ExecutionDal.class);
        when(executionDal.sessionMapper()).thenReturn(sessions);
        SystemDal systemDal = mock(SystemDal.class);
        when(systemDal.workerMapper()).thenReturn(workers);
        DmExecSessionDO session = new DmExecSessionDO();
        session.setUid("uid");
        session.setSessionId("sid");
        session.setWsn("worker");
        session.setDatasourceId(1L);
        when(sessions.queryBySessionId("uid", "sid")).thenReturn(session);
        when(sessions.updateActiveBatchIfBatchNull("uid", "sid", "same-batch")).thenReturn(1);
        DmSysWorkerDO worker = new DmSysWorkerDO();
        worker.setWorkerSeqNumber("worker");
        worker.setClusterId(1L);
        worker.setUid("uid");
        when(workers.queryConnectedByWsn("worker")).thenReturn(worker);
        QueryServiceImpl queries = new QueryServiceImpl();
        ReflectionTestUtils.setField(queries, "execDal", executionDal);
        ReflectionTestUtils.setField(queries, "systemDal", systemDal);
        ReflectionTestUtils.setField(queries, "sessionRService", rpc);
        ReflectionTestUtils.setField(queries, "auditService", audits);
        return queries;
    }

    @Test
    public void synchronousPollingWaitsForWorkerThenRejectsAnInvalidatedBatchBeforeConsumingResults() {
        DmExecSessionMapper sessions = mock(DmExecSessionMapper.class);
        DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
        ExecuteRService rpc = mock(ExecuteRService.class);
        QueryServiceImpl queries = queryServiceForAuditBoundary(sessions, workers, rpc, mock(AuditService.class));
        DmExecSessionDO session = sessions.queryBySessionId("uid", "sid");
        when(sessions.updateActiveBatchIfBatchNull(eq("uid"), eq("sid"), anyString())).thenAnswer(invocation -> {
            session.setActiveBatchId(invocation.getArgument(2));
            return 1;
        });

        AsyncWaitResult accepted = new AsyncWaitResult();
        accepted.setSuccess(true);
        when(rpc.asyncExecuteQuery(any(), eq("sid"), anyString(), anyList())).thenReturn(accepted);
        when(rpc.isExecuting(any(), eq("sid"))).thenAnswer(invocation -> {
            session.setActiveBatchId(DmExecSessionDO.INTERRUPTED_BATCH_ID);
            return true;
        }).thenReturn(false);
        clearInvocations(sessions, workers);

        try (MockedStatic<DmDsUtils> configs = mockStatic(DmDsUtils.class)) {
            try {
                queries.syncExecuteQuery("uid", "sid", new QueryRequest());
                fail("the final result-pop boundary must reject the invalidated batch after the Worker finishes");
            } catch (ErrorMessageException expected) {
                assertEquals(DmErrorCode.QUERY_SESSION_INTERRUPTED.code(), expected.getErrorCode());
            }
        }

        ArgumentCaptor<RSocketSendDTO> originalRoute = ArgumentCaptor.forClass(RSocketSendDTO.class);
        verify(rpc).asyncExecuteQuery(originalRoute.capture(), eq("sid"), anyString(), anyList());
        verify(rpc, times(2)).isExecuting(same(originalRoute.getValue()), eq("sid"));
        verify(sessions, times(2)).queryBySessionId("uid", "sid");
        verify(workers).queryConnectedByWsn("worker");
        verify(rpc, never()).lastResultList(any(), anyString());
        verify(sessions, never()).clearActiveBatchByBatch(anyString(), anyString(), anyString());
    }

    @Test
    public void activeBatchDoesNotReportExecutionWhenTheWorkerIsIdle() {
        DmExecSessionMapper sessions = mock(DmExecSessionMapper.class);
        DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
        ExecuteRService rpc = mock(ExecuteRService.class);
        QueryServiceImpl queries = queryServiceForAuditBoundary(sessions, workers, rpc, mock(AuditService.class));
        DmExecSessionDO session = sessions.queryBySessionId("uid", "sid");
        session.setActiveBatchId("still-collecting-results");
        when(rpc.isExecuting(any(), eq("sid"))).thenReturn(false);

        assertFalse(queries.isExecuting("uid", "sid"));
        assertEquals("still-collecting-results", session.getActiveBatchId());
        verify(rpc).isExecuting(any(), eq("sid"));
        verify(rpc, never()).lastResultList(any(), anyString());
        verify(sessions, never()).clearActiveBatchByBatch(anyString(), anyString(), anyString());
    }

    @Test
    public void onlyTheClaimedBatchCanConsumeAndReleaseResults() {
        DmExecSessionMapper sessions = mock(DmExecSessionMapper.class);
        DmSysWorkerMapper workers = mock(DmSysWorkerMapper.class);
        ExecuteRService rpc = mock(ExecuteRService.class);
        QueryServiceImpl queries = queryServiceForAuditBoundary(sessions, workers, rpc, mock(AuditService.class));
        DmExecSessionDO session = sessions.queryBySessionId("uid", "sid");
        session.setActiveBatchId("server-generated-batch");
        session.setConfig(JsonUtils.toJson(new SessionContextDTO()));
        try {
            queries.fetchQueryResult("uid", "sid", "another-batch");
            fail("another request must not consume the claimed session queue");
        } catch (ErrorMessageException expected) {
            assertEquals(DmErrorCode.QUERY_SESSION_INTERRUPTED.code(), expected.getErrorCode());
        }

        verifyNoInteractions(rpc);
        ResultList finished = new ResultList();
        finished.setResultList(List.of());
        StatusDTO status = new StatusDTO();
        status.setExecuting(false);
        status.setWaitQuerySize(0);
        finished.setStatus(status);
        when(rpc.lastResultList(any(), eq("sid"))).thenReturn(finished);

        assertSame(finished, queries.fetchQueryResult("uid", "sid", "server-generated-batch"));

        verify(rpc).lastResultList(any(), eq("sid"));
        verify(sessions).clearActiveBatchByBatch("uid", "sid", "server-generated-batch");
        verify(sessions, never()).clearActiveBatchByBatch("uid", "sid", "another-batch");
    }

    @Test
    public void approvalSnapshotsRetryFailedStartsAndObserveChangesFromOtherConsoles() throws Exception {
        String owner = "approval-owner";
        DmSysUserConfDO savedConfig = new DmSysUserConfDO();
        savedConfig.setConfigName(RootUserConfig.Fields.feishuApprovalAppSecret);
        savedConfig.setConfigValue("saved-v2");
        DmSysUserConfMapper configs = mock(DmSysUserConfMapper.class);
        when(configs.listByUid(owner)).thenReturn(List.of(savedConfig));
        SystemDal systemDal = mock(SystemDal.class);
        when(systemDal.userConfMapper()).thenReturn(configs);

        UserConfigKvDef definition = new UserConfigKvDef();
        definition.setConfigName(savedConfig.getConfigName());
        definition.setUserConfigTagType(UserConfigTagType.FEISHU);
        UserConfigServiceImpl definitions = mock(UserConfigServiceImpl.class);
        when(definitions.fetchUserDefinedDefaultConfig(owner)).thenReturn(List.of(definition));
        ApprovalProviderServiceImpl providerService = mock(ApprovalProviderServiceImpl.class);
        when(providerService.isApprovalConfigured(owner, ApprovalProvider.Feishu)).thenReturn(true);
        ApprovalStarter starter = new ApprovalStarter();
        ReflectionTestUtils.setField(starter, "approvalService", providerService);

        ProviderConfigRefresh refresh = new ProviderConfigRefresh();
        ReflectionTestUtils.setField(refresh, "systemDal", systemDal);
        ReflectionTestUtils.setField(refresh, "userConfigService", definitions);
        ReflectionTestUtils.setField(refresh, "approvalStarter", starter);
        AtomicReference<String> clientConfig = new AtomicReference<>("stale-v1");
        AtomicBoolean rejectStart = new AtomicBoolean(true);
        ApprovalProviderSpi provider = mock(ApprovalProviderSpi.class);
        doAnswer(invocation -> {
            clientConfig.set(null);
            return null;
        }).when(provider).stop(eq(owner), any(LifeSpiRequest.class));
        doAnswer(invocation -> {
            if (rejectStart.get()) {
                throw new IllegalStateException("approval client initialization failed");
            }

            clientConfig.compareAndSet(null, savedConfig.getConfigValue());
            return null;
        }).when(provider).start(eq(owner), any(LifeSpiRequest.class));

        try (MockedStatic<PluginManager> plugins = mockStatic(PluginManager.class)) {
            plugins.when(() -> PluginManager.findSpi(ApprovalProviderSpi.class, ApprovalProvider.Feishu.name())).thenReturn(provider);

            try {
                ReflectionTestUtils.invokeMethod(refresh, "refreshApproval", owner);
                fail("failed approval initialization must remain retryable");
            } catch (IllegalStateException expected) {
                assertEquals("provider configuration is not ready", expected.getMessage());
            }

            Map<?, ?> snapshots = (Map<?, ?>) ReflectionTestUtils.getField(refresh, "approvalSnapshots");
            assertFalse(snapshots.containsKey(owner));
            rejectStart.set(false);
            ReflectionTestUtils.invokeMethod(refresh, "refreshApproval", owner);
            assertEquals("saved-v2", clientConfig.get());
            verify(provider, times(2)).start(eq(owner), any(LifeSpiRequest.class));

            savedConfig.setConfigValue("changed-by-another-console-v3");
            ReflectionTestUtils.invokeMethod(refresh, "refreshApproval", owner);
            assertEquals("changed-by-another-console-v3", clientConfig.get());
            ReflectionTestUtils.invokeMethod(refresh, "refreshApproval", owner);
            verify(provider, times(3)).start(eq(owner), any(LifeSpiRequest.class));
        }
    }

}

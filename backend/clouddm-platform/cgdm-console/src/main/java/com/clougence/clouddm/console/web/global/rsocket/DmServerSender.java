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
package com.clougence.clouddm.console.web.global.rsocket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContextHolder;

import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.comm.RSocketSerialization;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.server.RSocketServerSender;
import com.clougence.clouddm.comm.constants.rsocket.RSocketLogNames;
import com.clougence.clouddm.comm.constants.rsocket.RSocketRouteNames;
import com.clougence.clouddm.comm.model.*;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.comm.model.rsocket.AsyncRequestFuture;
import com.clougence.clouddm.comm.model.rsocket.RSocketRegisterInfo;
import com.clougence.clouddm.comm.util.RSocketRespUtil;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.component.cluster.ConsoleInternalClient;
import com.clougence.clouddm.console.web.component.cluster.model.ConsoleForwardRequest;
import com.clougence.clouddm.console.web.component.config.ConsoleClusterProperties;
import com.clougence.clouddm.console.web.constants.ConsoleInternalPaths;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.DmSysConsoleDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;

@Slf4j
public class DmServerSender implements RSocketServerSender {
    private final RSocketRequestManager    requestManager;
    private final SystemDal                systemDal;
    private final DmServerSideRegistry     registry;
    private final RSocketSerialization     serialization;
    private final ConsoleClusterService    cluster;
    private final ConsoleClusterProperties properties;
    private final ConsoleInternalClient    internalClient;

    public DmServerSender(RSocketRequestManager requestManager, SystemDal systemDal, DmServerSideRegistry registry, RSocketSerialization serialization,
                          ConsoleClusterService cluster, ConsoleClusterProperties properties, ConsoleInternalClient internalClient){
        this.requestManager = requestManager;
        this.systemDal = systemDal;
        this.registry = registry;
        this.serialization = serialization;
        this.cluster = cluster;
        this.properties = properties;
        this.internalClient = internalClient;
    }

    @Override
    public RSocketRespDTO<?> requestNonBlock(Long clusterId, String route, Object[] params) {
        if (clusterId == null || clusterId <= 0) {
            throw new IllegalArgumentException("cluster id is required");
        }

        Set<String> readyConsoleIds = cluster.readyMembers().stream().map(DmSysConsoleDO::getInstanceId).collect(Collectors.toSet());
        List<DmSysWorkerDO> workers = systemDal.workerMapper()
            .queryConnectedByClusterId(clusterId)
            .stream()
            .filter(worker -> readyConsoleIds.contains(worker.getOwnerInstanceId()))
            .toList();
        if (workers.isEmpty()) {
            throw new ErrorMessageException(DmErrorCode.CLUSTER_HAVE_NO_WORKS_ERROR.code(), MessageUtils.getClusterHaveNoWorksErrorMessage(clusterId));
        }

        DmSysWorkerDO worker = null;
        for (DmSysWorkerDO candidate : workers) {
            if (cluster.getInstanceId().equals(candidate.getOwnerInstanceId()) && registry.getRSocketRequester(candidate.getWorkerSeqNumber()) != null) {
                worker = candidate;
                break;
            }
        }

        if (worker == null) {
            worker = workers.get(ThreadLocalRandom.current().nextInt(workers.size()));
        }
        return route(route, worker, encode(params));
    }

    @Override
    public RSocketRespDTO<?> requestNonBlock(String route, String wsn, Object[] params) {
        DmSysWorkerDO worker = systemDal.workerMapper().queryConnectedByWsn(wsn);
        if (worker == null) {
            return RSocketRespUtil.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), wsn));
        }
        return route(route, worker, encode(params));
    }

    @Override
    public RSocketRespDTO<?> requestNonBlockWithJsonParams(String route, String wsn, String[] params) {
        DmSysWorkerDO worker = systemDal.workerMapper().queryConnectedByWsn(wsn);
        if (worker == null) {
            return RSocketRespUtil.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), wsn));
        }
        return route(route, worker, Arrays.asList(params));
    }

    private List<String> encode(Object[] params) {
        List<String> result = new ArrayList<>();
        for (Object param : params) {
            String provider = null;
            if (param != null) {
                Serialization annotation = param.getClass().getAnnotation(Serialization.class);
                if (annotation != null) {
                    provider = annotation.provider();
                }
            }

            result.add(JsonUtils.toJson(new RSocketParam(provider, serialization.encode(provider, param))));
        }
        return result;
    }

    private RSocketRespDTO<?> route(String route, DmSysWorkerDO worker, List<String> params) {
        ConsoleForwardRequest request = new ConsoleForwardRequest();
        request.setOriginConsoleId(cluster.getInstanceId());
        request.setTargetConsoleId(worker.getOwnerInstanceId());
        request.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        request.setConnectionGeneration(worker.getConnectionGeneration());
        request.setRoute(route);
        request.setParams(params);
        request.setDeadline(System.currentTimeMillis() + 60000);
        request.setLocale(LocaleContextHolder.getLocale().toLanguageTag());
        if (cluster.getInstanceId().equals(worker.getOwnerInstanceId())) {
            request.setForwardCount(0);
            return sendLocal(request);
        }

        request.setForwardCount(1);
        if (!properties.isEnabled()) {
            return RSocketRespUtil.buildError("remote console routing is disabled");
        }

        DmSysConsoleDO owner = cluster.findAlive(worker.getOwnerInstanceId());
        if (owner == null) {
            return RSocketRespUtil.buildError("worker owner is unavailable");
        }
        return forwardToOwner(route, owner, request);
    }

    private RSocketRespDTO<?> forwardToOwner(String route, DmSysConsoleDO owner, ConsoleForwardRequest request) {
        long started = System.nanoTime();
        String previousModule = MDC.get("module");
        try {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);
            RSocketRespDTO<?> response = JsonUtils.toObj(internalClient.post(owner, ConsoleInternalPaths.FORWARD_WORKER_RPC, request, request.getDeadline()), RSocketRespDTO.class);
            log.info("[console->sidecar] forwarded request, route={}, request_id={}, elapse={} ms, wsn={}, owner={}, generation={}, code={}", route, response
                .getRequestId(), TimeUnit.NANOSECONDS
                    .toMillis(System.nanoTime() - started), request.getWorkerSeqNumber(), request.getTargetConsoleId(), request.getConnectionGeneration(), response.getCode());
            return response;
        } catch (Exception e) {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
            log.error("Worker RPC outcome unknown: wsn={}, route={}, owner={}, generation={}", request.getWorkerSeqNumber(), route, request.getTargetConsoleId(), request
                .getConnectionGeneration(), e);
            return RSocketRespUtil.buildError(RSocketRespCode.EXECUTION_UNKNOWN.getCode(), DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_EXECUTION_UNKNOWN.name()));
        } finally {
            if (previousModule == null) {
                MDC.remove("module");
            } else {
                MDC.put("module", previousModule);
            }
        }
    }

    public RSocketRespDTO<?> sendLocal(ConsoleForwardRequest request) {
        long started = System.nanoTime();
        if (request.getDeadline() <= System.currentTimeMillis()) {
            log.warn("Worker RPC deadline expired before dispatch: route={}, wsn={}, owner={}", request.getRoute(), request.getWorkerSeqNumber(), request.getTargetConsoleId());
            return RSocketRespUtil.buildError("worker request deadline exceeded before dispatch");
        }

        RSocketRegisterInfo connection = registry.connection(request.getWorkerSeqNumber());
        DmSysWorkerDO worker = systemDal.workerMapper().queryConnectedByWsn(request.getWorkerSeqNumber());
        if (cannotDispatchToCurrentWorker(request, worker, connection)) {
            log.warn("Worker RPC not dispatched: route={}, wsn={}, owner={}, generation={}", request.getRoute(), request.getWorkerSeqNumber(), request.getTargetConsoleId(), request
                .getConnectionGeneration());
            return RSocketRespUtil.buildError(RSocketRespCode.OWNER_CHANGED_NOT_DISPATCHED.getCode(), "worker owner changed before dispatch");
        }

        return dispatchLocalRequest(request, connection, started);
    }

    private RSocketRespDTO<?> dispatchLocalRequest(ConsoleForwardRequest request, RSocketRegisterInfo connection, long started) {
        AsyncRequestFuture future = requestManager.registerRsocketRequest(request.getRoute());
        RSocketRequestWrapperDTO wrapper = new RSocketRequestWrapperDTO();
        wrapper.setRequestId(future.getRequestId());
        wrapper.setRSocketDirectionType(RSocketDirectionType.SERVER_TO_CLIENT);
        wrapper.setWorkerIdentity(getIdentity());
        wrapper.setApiFullMethodName(request.getRoute());
        wrapper.setParamJsonValues(request.getParams());
        String unknownMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_EXECUTION_UNKNOWN.name());
        Runnable disconnected = () -> {
            RSocketRespDTO<String> failure = RSocketRespUtil.buildError(RSocketRespCode.EXECUTION_UNKNOWN.getCode(), unknownMessage);
            failure.setRequestId(future.getRequestId());
            requestManager.fillAsyncResult(failure);
        };
        Disposable closeSignal = connection.getRequester().rsocket().onClose().subscribe(ignored -> {
        }, error -> disconnected.run(), disconnected);
        String previousModule = MDC.get("module");
        try {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);
            connection.getRequester().route(RSocketRouteNames.MAIN_REQUEST_DISPATCHER).data(wrapper).retrieveMono(RSocketRespDTO.class).subscribe(accepted -> {
            }, error -> disconnected.run());
            return awaitLocalResponse(request, future, started);
        } catch (Exception e) {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
            log.error("Worker RPC response unavailable: route={}, request_id={}, wsn={}, owner={}, generation={}", request.getRoute(), future.getRequestId(), request
                .getWorkerSeqNumber(), request.getTargetConsoleId(), request.getConnectionGeneration(), e);
            return RSocketRespUtil.buildError(RSocketRespCode.EXECUTION_UNKNOWN.getCode(), DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_EXECUTION_UNKNOWN.name()));
        } finally {
            try {
                closeSignal.dispose();
                requestManager.removeTimeoutFutureResult(future);
            } finally {
                if (previousModule == null) {
                    MDC.remove("module");
                } else {
                    MDC.put("module", previousModule);
                }
            }
        }
    }

    private RSocketRespDTO<?> awaitLocalResponse(ConsoleForwardRequest request, AsyncRequestFuture future, long started) throws Exception {
        long remaining = request.getDeadline() - System.currentTimeMillis();
        if (remaining <= 0) {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
            log.error("Worker RPC deadline exceeded after dispatch: route={}, request_id={}, wsn={}, owner={}", request.getRoute(), future.getRequestId(), request
                .getWorkerSeqNumber(), request.getTargetConsoleId());
            return RSocketRespUtil.buildError(RSocketRespCode.EXECUTION_UNKNOWN.getCode(), DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_EXECUTION_UNKNOWN.name()));
        }

        RSocketRespDTO<?> response = future.getSettableFuture().get(remaining, TimeUnit.MILLISECONDS);
        if (RSocketRespCode.EXECUTION_UNKNOWN.getCode().equals(response.getCode())) {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
            log.error("Worker RPC outcome unknown: route={}, request_id={}, wsn={}, owner={}, generation={}", request.getRoute(), future.getRequestId(), request
                .getWorkerSeqNumber(), request.getTargetConsoleId(), request.getConnectionGeneration());
        } else if (!RSocketRespCode.OWNER_CHANGED_NOT_DISPATCHED.getCode().equals(response.getCode())) {
            log.info("[console->sidecar] send request to sidecar, route={}, request_id={}, elapse={} ms, wsn={}, owner={}, generation={}, code={}", request.getRoute(), future
                .getRequestId(), TimeUnit.NANOSECONDS
                    .toMillis(System.nanoTime() - started), request.getWorkerSeqNumber(), request.getTargetConsoleId(), request.getConnectionGeneration(), response.getCode());
        }
        return response;
    }

    private boolean cannotDispatchToCurrentWorker(ConsoleForwardRequest request, DmSysWorkerDO worker, RSocketRegisterInfo connection) {
        if (!cluster.isLeaseValid() || worker == null || connection == null) {
            return true;
        }

        if (!cluster.getInstanceId().equals(request.getTargetConsoleId()) || !cluster.getInstanceId().equals(worker.getOwnerInstanceId())) {
            return true;
        }

        if (worker.getConnectionGeneration() != request.getConnectionGeneration() || connection.getConnectionGeneration() != request.getConnectionGeneration()) {
            return true;
        }

        if (connection.getRequester().rsocket().isDisposed()) {
            return true;
        }
        return false;
    }

    @Override
    public WorkerIdentity getIdentity() {
        WorkerIdentity identity = new WorkerIdentity();
        identity.setOwnerConsoleId(cluster.getInstanceId());
        return identity;
    }

    @Override
    public void shutdown() {
        for (String wsn : List.copyOf(registry.getRequesterMap().keySet())) {
            RSocketRegisterInfo connection = registry.connection(wsn);
            if (connection != null) {
                try {
                    registry.unRegister(connection);
                } catch (Exception e) {
                    log.warn("Unregister worker connection failed: wsn={}", wsn, e);
                } finally {
                    try {
                        connection.getRequester().rsocket().dispose();
                    } catch (Exception e) {
                        log.warn("Close worker connection failed: wsn={}", wsn, e);
                    }
                }
            }
        }
    }
}

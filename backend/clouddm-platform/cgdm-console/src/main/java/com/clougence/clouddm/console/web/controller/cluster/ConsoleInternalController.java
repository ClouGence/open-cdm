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
package com.clougence.clouddm.console.web.controller.cluster;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.comm.component.server.RSocketServerSender;
import com.clougence.clouddm.comm.model.RSocketRespDTO;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.component.cluster.ConsoleInternalClient;
import com.clougence.clouddm.console.web.component.cluster.model.ConsoleForwardRequest;
import com.clougence.clouddm.console.web.component.cluster.model.ConsoleInternalAction;
import com.clougence.clouddm.console.web.component.cluster.model.ConsoleNotification;
import com.clougence.clouddm.console.web.component.execute.AsyncTaskService;
import com.clougence.clouddm.console.web.component.execute.model.ConsoleTaskControlAction;
import com.clougence.clouddm.console.web.component.execute.model.ConsoleTaskControlRequest;
import com.clougence.clouddm.console.web.constants.ConsoleInternalPaths;
import com.clougence.clouddm.console.web.global.rsocket.DmServerSender;
import com.clougence.clouddm.console.web.global.ws.WsChannel;
import com.clougence.clouddm.console.web.global.ws.WsType;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAsyncTaskDO;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ConsoleInternalController implements UnifiedPostConstruct {
    @Resource
    private ConsoleInternalClient internalClient;
    @Resource
    private ConsoleClusterService cluster;
    @Resource
    private RSocketServerSender   sender;
    @Resource
    private WsChannel             channels;
    @Resource
    private ExecutionDal          executionDal;
    @Resource
    private AsyncTaskService      asyncTasks;
    private ThreadPoolExecutor    calls;
    private boolean               stopped;

    @Override
    public synchronized void init() {
        if (this.stopped || this.calls != null) {
            return;
        }

        this.calls = new ThreadPoolExecutor(4, 16, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(64), r -> {
            Thread thread = new Thread(r, "console-peer-rpc");
            thread.setDaemon(true);
            return thread;
        }, new ThreadPoolExecutor.AbortPolicy());
    }

    @RequestMapping(value = ConsoleInternalPaths.FORWARD_WORKER_RPC, method = RequestMethod.POST)
    public CompletableFuture<RSocketRespDTO<?>> forward(@RequestBody String body, HttpServletRequest servletRequest) {
        String origin = authenticate(servletRequest, body);
        ConsoleForwardRequest request = JsonUtils.toObj(body, ConsoleForwardRequest.class);
        if (hasInvalidForwardRequest(request, origin, servletRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid forward request");
        }
        return submit(request.getLocale(), request.getDeadline(), () -> ((DmServerSender) sender).sendLocal(request));
    }

    private boolean hasInvalidForwardRequest(ConsoleForwardRequest request, String origin, HttpServletRequest servletRequest) {
        if (request.getForwardCount() != 1 || !origin.equals(request.getOriginConsoleId())) {
            return true;
        }

        if (!cluster.getInstanceId().equals(request.getTargetConsoleId())) {
            return true;
        }

        if (!String.valueOf(request.getDeadline()).equals(servletRequest.getHeader(ConsoleInternalClient.HEADER_DEADLINE))) {
            return true;
        }
        return request.getParams() == null || request.getRoute() == null;
    }

    @RequestMapping(value = ConsoleInternalPaths.DISPATCH, method = RequestMethod.POST)
    public CompletableFuture<String> dispatch(@RequestBody String body, HttpServletRequest servletRequest) {
        authenticate(servletRequest, body);
        ObjectMapper mapper = JsonUtils.defaultObjectMapper();
        try {
            JsonNode request = mapper.readTree(body);
            ConsoleInternalAction action = ConsoleInternalAction.valueOf(request.path("action").asText());
            JsonNode payload = request.path("payload");
            if (!payload.isObject()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid cluster dispatch payload");
            }

            switch (action) {
                case CONTROL_TASK: {
                    ConsoleTaskControlRequest task = mapper.convertValue(payload, ConsoleTaskControlRequest.class);
                    return controlTask(task, servletRequest);
                }
                case DELIVER_USER_EVENT: {
                    ConsoleNotification notification = mapper.convertValue(payload, ConsoleNotification.class);
                    deliverUserEvent(notification);
                    return CompletableFuture.completedFuture(null);
                }
                default: {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid cluster dispatch action");
                }
            }
        } catch (JsonProcessingException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid cluster dispatch request");
        }
    }

    private CompletableFuture<String> controlTask(ConsoleTaskControlRequest request, HttpServletRequest servletRequest) {
        String action = request.getAction();
        if (!ConsoleTaskControlAction.PAUSE_ASYNC.name().equals(action) && !ConsoleTaskControlAction.CANCEL_ASYNC.name().equals(action)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid task control action");
        }

        long deadline = Long.parseLong(servletRequest.getHeader(ConsoleInternalClient.HEADER_DEADLINE));
        return submit(request.getLocale(), deadline, () -> {
            DmExecAsyncTaskDO task = this.executionDal.asyncTaskMapper().queryById(request.getTaskId());
            if (task == null || !this.cluster.getConsoleInstanceId().equals(task.getConsoleInstanceId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "task owner changed");
            }

            if (ConsoleTaskControlAction.PAUSE_ASYNC.name().equals(action)) {
                this.asyncTasks.pauseTask(request.getTaskId(), request.getReasons());
            } else {
                this.asyncTasks.cancelTask(request.getTaskId(), request.getReasons());
            }
            return "null";
        });
    }

    private <T> CompletableFuture<T> submit(String locale, long deadline, Supplier<T> action) {
        Map<String, String> callerMdc = MDC.getCopyOfContextMap();
        return CompletableFuture.supplyAsync(() -> executePeerRequest(locale, deadline, action, callerMdc), calls);
    }

    private <T> T executePeerRequest(String locale, long deadline, Supplier<T> action, Map<String, String> callerMdc) {
        Map<String, String> previousMdc = MDC.getCopyOfContextMap();
        LocaleContext previous = LocaleContextHolder.getLocaleContext();
        try {
            if (callerMdc == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(callerMdc);
            }

            LocaleContextHolder.setLocale(Locale.forLanguageTag(locale));
            if (System.currentTimeMillis() >= deadline) {
                throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "cluster request deadline exceeded");
            }
            return action.get();
        } finally {
            LocaleContextHolder.setLocaleContext(previous);
            if (previousMdc == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(previousMdc);
            }
        }
    }

    private void deliverUserEvent(ConsoleNotification notification) {
        WsType type = notification.getType();
        if (type != WsType.WS_RES_ASYNC_EVENT && type != WsType.WS_RES_EXPORT_EVENT && type != WsType.WS_RES_DRIVER_DOWNLOAD_EVENT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid notification type");
        }

        channels.broadcastLocal(type, notification.getUid(), notification.getData());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Void> rejected(ResponseStatusException error) {
        return ResponseEntity.status(error.getStatusCode()).build();
    }

    private String authenticate(HttpServletRequest request, String body) {
        try {
            return internalClient.verify(request, body);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invalid cluster authentication");
        }
    }

    @PreDestroy
    @Override
    public synchronized void stop() {
        this.stopped = true;
        if (this.calls != null) {
            this.calls.shutdownNow();
        }
    }
}

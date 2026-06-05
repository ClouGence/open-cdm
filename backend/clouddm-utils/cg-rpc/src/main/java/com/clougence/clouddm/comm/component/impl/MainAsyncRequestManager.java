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
package com.clougence.clouddm.comm.component.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;

import com.clougence.clouddm.comm.component.RSocketExceptionManager;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.client.ClientSideRegistry;
import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.constants.rsocket.RSocketLogNames;
import com.clougence.clouddm.comm.constants.rsocket.RSocketRouteNames;
import com.clougence.clouddm.comm.model.RSocketDirectionType;
import com.clougence.clouddm.comm.model.RSocketRespDTO;
import com.clougence.clouddm.comm.model.SendBackDataDTO;
import com.clougence.clouddm.comm.model.rsocket.AsyncRequestFuture;
import com.clougence.clouddm.comm.util.DeflateCompressorUtil;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.future.BasicFuture;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author wanshao create time is 2021/1/9
 **/
@Slf4j
public class MainAsyncRequestManager implements RSocketRequestManager {

    /** key is requestId. All registered request's related future object is stored here */
    private final Map<String, BasicFuture<RSocketRespDTO<?>>> settableFutureMap = new ConcurrentHashMap<>();
    private final RSocketExceptionManager                     rSocketExceptionManager;
    private final ApplicationContext                          applicationContext;
    private final ObjectMapper                                objectMapper      = new ObjectMapper();

    public MainAsyncRequestManager(RSocketExceptionManager rSocketExceptionManager, ApplicationContext applicationContext){
        this.rSocketExceptionManager = rSocketExceptionManager;
        this.applicationContext = applicationContext;
    }

    @Override
    public AsyncRequestFuture registerRsocketRequest(String methodName) {
        String requestId = generateRequestId();
        BasicFuture<RSocketRespDTO<?>> futureTask = new BasicFuture<>();
        settableFutureMap.put(requestId, futureTask);
        AsyncRequestFuture future = new AsyncRequestFuture();
        future.setSettableFuture(futureTask);
        future.setRequestId(requestId);
        future.setRouteName(methodName);
        return future;
    }

    private synchronized String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void fillAsyncResult(RSocketRespDTO<?> responseData) {
        try {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);
            if (responseData.getRequestId() == null) {
                log.error("request id is empty,rsocket response will be ignored.response msg:" + responseData.getMsg());
                return;
            }
            BasicFuture<RSocketRespDTO<?>> futureTask = settableFutureMap.get(responseData.getRequestId());
            // if receive delayed, the key has been removed
            if (futureTask == null) {
                log.error("request:" + responseData.getRequestId() + " 's future task is empty.rsocket response will be ignored.");
                return;
            }
            futureTask.completed(responseData);
            log.debug("receive async result. request id:" + responseData.getRequestId());
            settableFutureMap.remove(responseData.getRequestId());
        } catch (Exception e) {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
            String errMsg = "Fill async result failed with exception. Request Id is " + responseData.getRequestId() + ", api methodName is " + responseData.getApiFullMethodName()
                            + ". Root cause is " + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
            throw e;
        } finally {
            MDC.remove("module");
        }
    }

    @SneakyThrows
    @Override
    public void removeTimeoutFutureResult(AsyncRequestFuture asyncRequestFuture) {
        settableFutureMap.remove(asyncRequestFuture.getRequestId());
    }

    @Override
    @SneakyThrows
    public void sendAsyncResultBack(RSocketRespDTO rSocketRespDTO) {
        try {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_LOG_NAME);
            String workerSeqNumber = rSocketRespDTO.getWorkerIdentity().getWorkerSeqNumber();
            RSocketRequester requester;
            if (RSocketDirectionType.SERVER_TO_CLIENT == rSocketRespDTO.getRSocketDirectionType()) {
                requester = applicationContext.getBean(ServerSideRegistry.class).getRSocketRequester(rSocketRespDTO.getWorkerIdentity().getWorkerSeqNumber());
            } else if (RSocketDirectionType.CLIENT_TO_SERVER == rSocketRespDTO.getRSocketDirectionType()) {
                ClientSideRegistry clientSideRegistry = applicationContext.getBean(ClientSideRegistry.class);
                requester = clientSideRegistry.getRSocketRequester();
            } else {
                throw new IllegalArgumentException("There is no direction info in request: " + objectMapper.writeValueAsString(rSocketRespDTO));
            }
            if (requester != null) {
                // long size = ObjectSizeCalculator.getObjectSize(data);
                // if (size > 1024 * 1024 && !flushed.get()) {
                // FileUtils.write(new File(System.getProperty("user.home") + "/big_data.txt"), new ObjectMapper().writeValueAsString(data), Charsets.UTF_8, false);
                // flushed.set(true);
                // }
                SendBackDataDTO sendBackDataDTO = new SendBackDataDTO();
                if ("true".equals(System.getProperty("debug"))) {
                    sendBackDataDTO.setRSocketRespDTO(rSocketRespDTO);
                } else {
                    sendBackDataDTO.setCompressedRespStr(DeflateCompressorUtil.compress(rSocketRespDTO));
                }
                sendBackDataDTO.setRSocketDirectionType(rSocketRespDTO.getRSocketDirectionType());
                // mac os not print
                // if (!System.getProperty("user.home").startsWith("/Users/")) {
                // log.info("Send data size is " + ObjectSizeCalculator.getObjectSize(sendBackDataDTO) + ", method name is " + rSocketRespDTO.getApiMethodName());
                // }
                requester.route(RSocketRouteNames.MAIN_RESPONSE_RECEIVER).data(sendBackDataDTO).send().subscribe();
                log.debug("Send back async response success to " + RSocketRouteNames.MAIN_RESPONSE_RECEIVER + ". Response's related request's invoke api method is "
                          + rSocketRespDTO.getApiFullMethodName() + ",request id:" + rSocketRespDTO.getRequestId());
            } else {
                log.warn("Send back async response to " + workerSeqNumber + " FAILED. Requester from map is EMPTY !!!");
            }
        } catch (Exception exception) {
            MDC.put("module", RSocketLogNames.RSOCKET_SEND_RECV_ERROR_LOG_NAME);
            log.error("send back async result to " + rSocketRespDTO.getWorkerIdentity().getWorkerSeqNumber() + " FAILED. RequestId is " + rSocketRespDTO.getRequestId()
                      + ". Root cause is " + ExceptionUtils.getRootCauseMessage(exception), exception);
        } finally {
            MDC.remove("module");
        }
    }

    @Override
    @SneakyThrows
    @MessageMapping(RSocketRouteNames.MAIN_RESPONSE_RECEIVER)
    public Mono<Void> responseReceiver(SendBackDataDTO sendBackDataDTO) {
        RSocketRespDTO<?> responseData;
        try {
            if ("true".equals(System.getProperty("debug"))) {
                responseData = sendBackDataDTO.getRSocketRespDTO();
            } else {
                responseData = (RSocketRespDTO<?>) DeflateCompressorUtil.decompress(sendBackDataDTO.getCompressedRespStr(), RSocketRespDTO.class);
            }
            if (responseData == null) {
                throw new IllegalStateException("Send back data have problem. Compressed resp str: " + sendBackDataDTO.getCompressedRespStr());
            }
            fillAsyncResult(responseData);
        } catch (Exception e) {
            String errMsg = "Save response data for send back DTO " + objectMapper.writeValueAsString(sendBackDataDTO) + " failed with exception. Root cause is "
                            + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
            rSocketExceptionManager.saveExcOrAlert(e);
        }
        return Mono.empty();
    }
}

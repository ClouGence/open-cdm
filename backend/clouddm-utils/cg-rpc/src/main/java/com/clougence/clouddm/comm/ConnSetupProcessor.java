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
package com.clougence.clouddm.comm;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;

import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.constants.rsocket.ServerRouteNames;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.clouddm.comm.model.rsocket.RSocketRegisterInfo;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import io.rsocket.exceptions.ConnectionErrorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Slf4j
public class ConnSetupProcessor {

    private final ServerSideRegistry serverSideRegistry;
    private final ObjectReader       authReader;

    public ConnSetupProcessor(ServerSideRegistry serverSideRegistry){
        this.serverSideRegistry = serverSideRegistry;
        this.authReader = new ObjectMapper().readerFor(ConnAuthDTO.class);
    }

    @ConnectMapping(ServerRouteNames.CONN_SETUP)
    Mono<Void> channelConnect(RSocketRequester requester, @Payload String authInfoJson) {
        return Mono.defer(() -> registerWorkerConnection(requester, authInfoJson)).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Void> registerWorkerConnection(RSocketRequester requester, String authInfoJson) {
        RSocketRegisterInfo registerInfo = new RSocketRegisterInfo();
        try {
            ConnAuthDTO authInfo = authReader.readValue(authInfoJson);
            checkParams(authInfo);
            registerInfo.setRequester(requester);
            registerInfo.setWorkerSeqNumber(authInfo.getWsn());
            registerInfo.setWorkerIp(authInfo.getIp());
            serverSideRegistry.register(registerInfo);
            requester.rsocket()
                .onClose()
                .publishOn(Schedulers.boundedElastic())
                .doFinally(signal -> serverSideRegistry.unRegister(registerInfo))
                .subscribe(null, error -> log.debug("Worker connection closed: {}", authInfo.getWsn()));

            return Mono.empty();
        } catch (Exception e) {
            log.warn("Worker registration failed: wsn={}", registerInfo.getWorkerSeqNumber(), e);
            releaseFailedConnection(registerInfo, requester);
            return Mono.error(new ConnectionErrorException("worker connection temporarily unavailable"));
        }
    }

    private void releaseFailedConnection(RSocketRegisterInfo info, RSocketRequester requester) {
        try {
            serverSideRegistry.unRegister(info);
        } catch (Exception e) {
            log.warn("Release failed worker connection failed: wsn={}", info.getWorkerSeqNumber(), e);
        } finally {
            requester.rsocket().dispose();
        }
    }

    protected void checkParams(ConnAuthDTO authInfo) {
        if (StringUtils.isBlank(authInfo.getAk())) {
            throw new IllegalArgumentException("user ak can not be empty.remote ip:" + authInfo.getIp());
        }

        if (StringUtils.isBlank(authInfo.getSk())) {
            throw new IllegalArgumentException("user sk can not be empty.remote ip:" + authInfo.getIp());
        }

        if (StringUtils.isBlank(authInfo.getWsn())) {
            throw new IllegalArgumentException("worker wsn can not be empty.remote ip:" + authInfo.getIp());
        }

        if (StringUtils.isBlank(authInfo.getIp())) {
            throw new IllegalArgumentException("worker ip can not be empty.worker wsn:" + authInfo.getWsn());
        }
    }
}

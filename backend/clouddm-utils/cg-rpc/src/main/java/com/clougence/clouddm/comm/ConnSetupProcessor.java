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

import com.clougence.clouddm.comm.component.RSocketExceptionManager;
import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.constants.rsocket.ServerRouteNames;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.clouddm.comm.model.rsocket.RSocketRegisterInfo;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.rsocket.exceptions.RejectedSetupException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Slf4j
public class ConnSetupProcessor {

    private ServerSideRegistry      serverSideRegistry;

    private RSocketExceptionManager rSocketExceptionManager;

    public ConnSetupProcessor(ServerSideRegistry serverSideRegistry, RSocketExceptionManager rSocketExceptionManager){
        this.serverSideRegistry = serverSideRegistry;
        this.rSocketExceptionManager = rSocketExceptionManager;
    }

    @ConnectMapping(ServerRouteNames.CONN_SETUP)
    Mono<Void> channelConnect(RSocketRequester requester, @Payload String authInfoJson) {
        try {
            // prepare register info
            ConnAuthDTO authInfo = new ObjectMapper().readValue(authInfoJson, ConnAuthDTO.class);
            checkParams(authInfo);

            final String clientIp = authInfo.getIp();
            final String wsn = authInfo.getWsn();

            RSocketRegisterInfo registerInfo = new RSocketRegisterInfo();
            registerInfo.setRequester(requester);
            registerInfo.setWorkerSeqNumber(wsn);
            registerInfo.setWorkerIp(clientIp);

            serverSideRegistry.register(registerInfo);
            requester.rsocket().onClose().doFirst(() -> {
                try {
                    log.warn("User worker CONNECTED,ip:" + clientIp + ",wsn:" + wsn);
                } catch (Exception e) {
                    log.error("worker (" + wsn + ") register error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
                    // if register with exception, reject connection
                    requester.rsocket().dispose();
                }
            }).doOnError(error -> {
                // Warn when channels are closed by clients
                log.warn("Process setup failed with exception. Worker wsn:" + wsn + ". Root cause is " + ExceptionUtils.getRootCauseMessage(error));
            }).doFinally(consumer -> {
                // Remove disconnected clients from the client list
                serverSideRegistry.unRegister(wsn);
                log.warn("user worker DISCONNECTED,ip:" + clientIp + ",wsn:" + wsn);
            }).subscribe();
        } catch (Exception e) {
            String errMsg = "channel connect error. Received auth info is " + authInfoJson + ". Root cause is:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
            rSocketExceptionManager.saveExcOrAlert(e);
            return Mono.error(new RejectedSetupException("connection is not authenticated"));
        }
        return Mono.empty();
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

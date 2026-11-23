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

import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.model.RSocketAuthException;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import io.rsocket.SocketAcceptor;
import io.rsocket.exceptions.ConnectionErrorException;
import io.rsocket.exceptions.RejectedSetupException;
import io.rsocket.plugins.SocketAcceptorInterceptor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author bucketli 2021/1/18 12:31
 */
@Slf4j
public class DmConsoleSAInterceptor implements SocketAcceptorInterceptor {

    private final ServerSideRegistry serverSideRegistry;
    private final ObjectReader       authReader;

    public DmConsoleSAInterceptor(ServerSideRegistry serverSideRegistry){
        this.serverSideRegistry = serverSideRegistry;
        this.authReader = new ObjectMapper().readerFor(ConnAuthDTO.class);
    }

    @Override
    public SocketAcceptor apply(SocketAcceptor socketAcceptor) {
        return (setup, sendingSocket) -> {
            String authData = setup.getDataUtf8();
            return Mono.defer(() -> {
                try {
                    ConnAuthDTO authInfo = authReader.readValue(authData);
                    checkParams(authInfo);
                    serverSideRegistry.checkAuth(authInfo);
                    return socketAcceptor.accept(setup, sendingSocket);
                } catch (RSocketAuthException | IllegalArgumentException e) {
                    log.warn("Worker connection authentication rejected", e);
                    return Mono.error(new RejectedSetupException("worker credentials are invalid"));
                } catch (Exception e) {
                    log.warn("Worker connection authentication temporarily unavailable", e);
                    return Mono.error(new ConnectionErrorException("console is temporarily unavailable"));
                }
            }).subscribeOn(Schedulers.boundedElastic());
        };
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

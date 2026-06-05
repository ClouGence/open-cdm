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
 */package com.clougence.clouddm.worker.component.rsocket;

import jakarta.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import com.clougence.clouddm.comm.WorkerRSocketClient;
import com.clougence.clouddm.comm.component.RSocketRequestDispatcher;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.client.ClientSideRegistry;
import com.clougence.clouddm.comm.component.client.RSocketClientAuthManager;
import com.clougence.clouddm.comm.component.client.RSocketClientSender;
import com.clougence.clouddm.comm.component.impl.MainAsyncRequestManager;
import com.clougence.clouddm.comm.component.impl.MainRequestDispatcher;

/**
 * @author wanshao create time is 2021/1/14
 **/
@Configuration
public class WorkerRSocketConfig {

    @Resource
    private ApplicationContext applicationContext;

    // server client share part
    @Bean
    public RSocketMessageHandler sidecarRSocketMessageHandler() {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRouteMatcher(new PathPatternRouteMatcher());
        handler.setRSocketStrategies(sidecarRSocketStrategies());
        return handler;
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public RSocketRequester.Builder sidecarRSocketRequesterBuilder() {
        return RSocketRequester.builder().rsocketStrategies(sidecarRSocketStrategies());
    }

    @Bean
    @SuppressWarnings("removal")
    public RSocketStrategies sidecarRSocketStrategies() {
        return RSocketStrategies.builder()
            .encoders(encoders -> encoders.add(new Jackson2JsonEncoder()))
            .decoders(decoders -> decoders.add(new Jackson2JsonDecoder()))
            .routeMatcher(new PathPatternRouteMatcher())
            .build();
    }

    @Bean
    public WorkerRSocketClient sidecarWorkerRSocketClient() {
        return new WorkerRSocketClient(new DmClientStopListener(),
            sidecarRSocketStrategies(),
            sidecarRSocketRequestManager(),
            sidecarRSocketRequestDispatcher(),
            sidecarRSocketClientAuthManager(),
            sidecarRSocketRequesterBuilder());
    }

    @Bean
    public RSocketRequestManager sidecarRSocketRequestManager() {
        return new MainAsyncRequestManager(new DmClientExceptionManager(), applicationContext);
    }

    @Bean
    public RSocketRequestDispatcher sidecarRSocketRequestDispatcher() {
        return new MainRequestDispatcher(sidecarRSocketRequestManager(), new DmClientExceptionManager(), RSocketSerializationImpl.DEFAULT);
    }

    @Bean
    public RSocketClientSender sidecarRSocketClientSender() {
        return new DmClientSender(sidecarWorkerRSocketClient(), sidecarRSocketRequestManager());
    }

    @Bean
    public ClientSideRegistry sidecarClientSideRegistry() {
        return new DmClientSideRegistry(sidecarWorkerRSocketClient());
    }

    @Bean
    public RSocketClientAuthManager sidecarRSocketClientAuthManager() {
        return new DmClientAuthManager();
    }
}

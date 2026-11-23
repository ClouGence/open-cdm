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

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import com.clougence.clouddm.comm.ConnSetupProcessor;
import com.clougence.clouddm.comm.ConsoleRSocketServer;
import com.clougence.clouddm.comm.component.RSocketRequestDispatcher;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.impl.MainAsyncRequestManager;
import com.clougence.clouddm.comm.component.impl.MainRequestDispatcher;
import com.clougence.clouddm.comm.component.server.RSocketServerSender;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.component.cluster.ConsoleInternalClient;
import com.clougence.clouddm.console.web.component.config.ConsoleClusterProperties;
import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.clouddm.console.web.global.notify.DmWorkerRegisterNotify;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;

import io.rsocket.plugins.SocketAcceptorInterceptor;
import jakarta.annotation.Resource;

/**
 * @author wanshao create time is 2021/1/13
 **/
@Configuration
public class ConsoleRSocketConfig {

    @Resource
    private SystemDal                    systemDal;
    @Resource
    private AuthDal                      authDal;
    @Resource
    private ExecutionDal                 executionDal;
    @Resource
    private ApplicationContext           appCtx;
    @Resource
    private ConsoleConfig                config;
    @Resource
    private List<DmWorkerRegisterNotify> notifyServices;
    @Resource
    private ConsoleClusterService        cluster;
    @Resource
    private ConsoleClusterProperties     clusterProperties;
    @Resource
    private ConsoleInternalClient        internalClient;

    // server client share part
    @Bean
    public RSocketMessageHandler consoleRSocketMessageHandler() {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRouteMatcher(new PathPatternRouteMatcher());
        handler.setRSocketStrategies(consoleRSocketStrategies());
        return handler;
    }

    @Bean
    @SuppressWarnings("removal")
    public RSocketStrategies consoleRSocketStrategies() {
        return RSocketStrategies.builder()
            .encoders(encoders -> encoders.add(new Jackson2JsonEncoder()))
            .decoders(decoders -> decoders.add(new Jackson2JsonDecoder()))
            .routeMatcher(new PathPatternRouteMatcher())
            .build();
    }

    @Bean
    public ConsoleRSocketServer consoleRSocketServer() {
        return new ConsoleRSocketServer(consoleRSocketStrategies(),
            consoleRSocketRequestDispatcher(),
            consoleConnSetupProcessor(),
            consoleRSocketRequestManager(),
            new DmServerStopListener(),
            consoleSocketAcceptorInterceptor(),
            config.getRsocketConsolePort());
    }

    @Bean
    public ConnSetupProcessor consoleConnSetupProcessor() {
        return new ConnSetupProcessor(consoleServerSideRegistry());
    }

    @Bean
    public RSocketRequestManager consoleRSocketRequestManager() {
        return new MainAsyncRequestManager(new DmConsoleExceptionManager(), appCtx);
    }

    @Bean
    public RSocketRequestDispatcher consoleRSocketRequestDispatcher() {
        return new MainRequestDispatcher(consoleRSocketRequestManager(),
            new DmConsoleExceptionManager(),
            RSocketSerializationImpl.DEFAULT,
            consoleServerSideRegistry()::checkIncoming);
    }

    public SocketAcceptorInterceptor consoleSocketAcceptorInterceptor() {
        return new DmConsoleSAInterceptor(consoleServerSideRegistry());
    }

    @Bean
    public RSocketServerSender consoleRSocketServerSender() {
        return new DmServerSender(consoleRSocketRequestManager(),
            this.systemDal,
            consoleServerSideRegistry(),
            RSocketSerializationImpl.DEFAULT,
            cluster,
            clusterProperties,
            internalClient);
    }

    @Bean
    public DmServerSideRegistry consoleServerSideRegistry() {
        return new DmServerSideRegistry(authDal, systemDal, executionDal, notifyServices, cluster, new DmConsoleExceptionManager());
    }
}

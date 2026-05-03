package com.clougence.clouddm.console.web.global.rsocket;

import java.util.List;

import javax.annotation.Resource;

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
import com.clougence.clouddm.comm.component.server.RSocketConnManager;
import com.clougence.clouddm.comm.component.server.RSocketServerSender;
import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerStatusMapper;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.notify.DmWorkerRegisterNotify;
import com.clougence.rdp.component.jwtsession.RdpJwtService;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.service.RdpUserService;

import io.rsocket.plugins.SocketAcceptorInterceptor;

/**
 * @author wanshao create time is 2021/1/13
 **/
@Configuration
public class ConsoleRSocketConfig {

    @Resource
    private DmWorkerStatusMapper         dmWorkerStatusMapper;

    @Resource
    private ApplicationContext           appCtx;

    @Resource
    private RdpUserMapper                dmUserMapper;

    @Resource
    private DmWorkerMapper               workerMapper;

    @Resource
    private RdpJwtService                jwtService;

    @Resource
    private RdpUserService               userService;

    @Resource
    private DmConsoleConfig              consoleConfig;

    @Resource
    private List<DmWorkerRegisterNotify> notifyServices;

    // server client share part
    @Bean
    public RSocketMessageHandler consoleRSocketMessageHandler() {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRouteMatcher(new PathPatternRouteMatcher());
        handler.setRSocketStrategies(consoleRSocketStrategies());
        return handler;
    }

    @Bean
    public RSocketStrategies consoleRSocketStrategies() {
        return RSocketStrategies.builder()
            .encoders(encoders -> encoders.add(new Jackson2JsonEncoder()))
            .decoders(decoders -> decoders.add(new Jackson2JsonDecoder()))
            .routeMatcher(new PathPatternRouteMatcher())
            .build();
    }

    @Bean
    public ConsoleRSocketServer consoleRSocketServer() {
        return new ConsoleRSocketServer(consoleRSocketConnManager(),
            consoleRSocketStrategies(),
            consoleRSocketRequestDispatcher(),
            consoleConnSetupProcessor(),
            consoleRSocketRequestManager(),
            new DmServerStopListener(),
            consoleSocketAcceptorInterceptor(),
            consoleConfig.getRsocketConsolePort());
    }

    @Bean
    public ConnSetupProcessor consoleConnSetupProcessor() {
        return new ConnSetupProcessor(consoleServerSideRegistry(), new DmConsoleExceptionManager());
    }

    @Bean
    public RSocketRequestManager consoleRSocketRequestManager() {
        return new MainAsyncRequestManager(new DmConsoleExceptionManager(), appCtx);
    }

    @Bean
    public RSocketRequestDispatcher consoleRSocketRequestDispatcher() {
        return new MainRequestDispatcher(consoleRSocketRequestManager(), new DmConsoleExceptionManager(), RSocketSerializationImpl.DEFAULT);
    }

    @Bean
    public RSocketConnManager consoleRSocketConnManager() {
        return new DmConsoleConnManager(dmWorkerStatusMapper);
    }

    public SocketAcceptorInterceptor consoleSocketAcceptorInterceptor() {
        return new DmConsoleSAInterceptor(consoleServerSideRegistry());
    }

    @Bean
    public RSocketServerSender consoleRSocketServerSender() {
        return new DmServerSender(consoleRSocketRequestManager(),
            this.workerMapper,
            this.dmWorkerStatusMapper,
            consoleServerSideRegistry(),
            jwtService,
            userService,
            RSocketSerializationImpl.DEFAULT);
    }

    @Bean
    public ServerSideRegistry consoleServerSideRegistry() {
        return new DmServerSideRegistry(dmWorkerStatusMapper, dmUserMapper, workerMapper, notifyServices, new DmConsoleExceptionManager());
    }
}

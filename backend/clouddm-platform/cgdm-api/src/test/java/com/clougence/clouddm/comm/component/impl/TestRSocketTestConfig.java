//package com.clougence.clouddm.comm.component.impl;
//import jakarta.annotation.Resource;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Scope;
//import org.springframework.http.codec.json.Jackson2JsonDecoder;
//import org.springframework.http.codec.json.Jackson2JsonEncoder;
//import org.springframework.messaging.rsocket.RSocketRequester;
//import org.springframework.messaging.rsocket.RSocketStrategies;
//import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
//import org.springframework.web.util.pattern.PathPatternRouteMatcher;
//import com.clougence.clouddm.comm.ConnSetupProcessor;
//import com.clougence.clouddm.comm.ConsoleRSocketServer;
//import com.clougence.clouddm.comm.WorkerRSocketClient;
//import com.clougence.clouddm.comm.component.RSocketExceptionManager;
//import com.clougence.clouddm.comm.component.RSocketRequestDispatcher;
//import com.clougence.clouddm.comm.component.RSocketRequestManager;
//import com.clougence.clouddm.comm.component.RSocketStopListener;
//import com.clougence.clouddm.comm.component.client.ClientSideRegistry;
//import com.clougence.clouddm.comm.component.client.RSocketClientAuthManager;
//import com.clougence.clouddm.comm.component.client.RSocketClientSender;
//import com.clougence.clouddm.comm.component.server.RSocketConnManager;
//import com.clougence.clouddm.comm.component.server.RSocketServerSender;
//import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
//import io.rsocket.plugins.SocketAcceptorInterceptor;
//import com.clougence.clouddm.comm.component.impl.client.*;
//import com.clougence.clouddm.comm.component.impl.server.*;
//import com.clougence.clouddm.base.service.plugin.TestRSocketClientAuthManager;
///**
// * @author wanshao create time is 2021/1/8
// **/
//@Configuration
//public class TestRSocketTestConfig {
//
//    @Resource
//    private ApplicationContext applicationContext;
//
//    // server client share part
//    @Bean
//    public RSocketMessageHandler rsocketMessageHandler() {
//        RSocketMessageHandler handler = new RSocketMessageHandler();
//        handler.setRouteMatcher(new PathPatternRouteMatcher());
//        handler.setRSocketStrategies(rsocketStrategies());
//        return handler;
//    }
//
//    @Bean
//    public RSocketStrategies rsocketStrategies() {
//        return RSocketStrategies.builder()
//            .encoders(encoders -> encoders.add(new Jackson2JsonEncoder()))
//            .decoders(decoders -> decoders.add(new Jackson2JsonDecoder()))
//            .routeMatcher(new PathPatternRouteMatcher())
//            .build();
//    }
//
//    @Bean
//    public WorkerRSocketClient workerRSocketClient() {
//        return new WorkerRSocketClient(rSocketStopListener(),
//            rsocketStrategies(),
//            rSocketRequestManager(),
//            rSocketRequestDispatcher(),
//            rSocketClientAuthManager(),
//            rSocketRequesterBuilder());
//    }
//
//    @Bean
//    @Scope("prototype")
//    @ConditionalOnMissingBean
//    public RSocketRequester.Builder rSocketRequesterBuilder() {
//        return RSocketRequester.builder().rsocketStrategies(rsocketStrategies());
//    }
//
//    @Bean
//    public ConnSetupProcessor connSetupProcessor() {
//        return new ConnSetupProcessor(serverSideRegistry(), rSocketExceptionManager());
//    }
//
//    @Bean
//    public RSocketRequestDispatcher rSocketRequestDispatcher() {
//        return new MainRequestDispatcher(rSocketRequestManager(), rSocketExceptionManager());
//    }
//
//    @Bean
//    public ConsoleRSocketServer consoleRSocketServer() {
//        return new ConsoleRSocketServer(rSocketConnManager(),
//            rsocketStrategies(),
//            rSocketRequestDispatcher(),
//            connSetupProcessor(),
//            rSocketRequestManager(),
//            rSocketStopListener(),
//            socketAcceptorInterceptor(),
//            8008);
//    }
//
//    @Bean
//    public RSocketStopListener rSocketStopListener() {
//        return new TestRSocketStopListener();
//    }
//
//    @Bean
//    public RSocketExceptionManager rSocketExceptionManager() {
//        return new TestRSocketExceptionManager();
//    }
//
//    @Bean
//    public ServerSideRegistry serverSideRegistry() {
//        return new TestServerSideRegistry(testWorkerStatusMapper());
//    }
//
//    @Bean
//    public TestWorkerStatusMapper testWorkerStatusMapper() {
//        return new TestWorkerStatusMapper();
//    }
//
//    @Bean
//    public RSocketConnManager rSocketConnManager() {
//        return new TestRSocketConnManager();
//    }
//
//    @Bean
//    public RSocketRequestManager rSocketRequestManager() {
//        return new MainAsyncRequestManager(rSocketExceptionManager(), applicationContext);
//    }
//
//    @Bean
//    public SocketAcceptorInterceptor socketAcceptorInterceptor() {
//        return new TestSocketAcceptorInterceptor();
//    }
//
//    @Bean
//    public RSocketClientSender rSocketClientSender() {
//        return new TestRSocketClientSender(workerRSocketClient(), rSocketRequestManager());
//    }
//
//    @Bean
//    public ClientSideRegistry clientSideRegistry() {
//        return new TestClientSideRegistry(workerRSocketClient());
//    }
//
//    @Bean
//    public RSocketServerSender rSocketServerSender() {
//        return new TestRSocketServerSender(rSocketRequestManager(), testWorkerStatusMapper(), serverSideRegistry());
//    }
//
//    @Bean
//    public RSocketClientAuthManager rSocketClientAuthManager() {
//        return new TestRSocketClientAuthManager();
//    }
//
//}

package com.clougence.clouddm.worker.component.rsocket;

import javax.annotation.Resource;

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

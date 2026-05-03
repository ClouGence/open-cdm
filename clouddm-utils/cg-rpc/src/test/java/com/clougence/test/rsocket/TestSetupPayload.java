//package com.clougence.test.rsocket;
//
//import static com.clougence.clouddm.comm.constants.rsocket.RSocketConnConstants.MTU_BYTE_SIZE;
//
//import java.time.Duration;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.codec.json.Jackson2JsonDecoder;
//import org.springframework.http.codec.json.Jackson2JsonEncoder;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.rsocket.RSocketRequester;
//import org.springframework.messaging.rsocket.RSocketStrategies;
//import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
//import org.springframework.util.MimeTypeUtils;
//import org.springframework.web.util.pattern.PathPatternRouteMatcher;
//
//import com.clougence.clouddm.comm.constants.rsocket.RSocketConnConstants;
//import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
//import com.clougence.utils.HostUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import io.netty.channel.ChannelOption;
//import io.netty.channel.epoll.EpollChannelOption;
//import io.rsocket.SocketAcceptor;
//import io.rsocket.metadata.WellKnownMimeType;
//import io.rsocket.transport.netty.client.TcpClientTransport;
//import io.rsocket.transport.netty.server.TcpServerTransport;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Mono;
//import reactor.netty.tcp.TcpClient;
//import reactor.netty.tcp.TcpServer;
//import reactor.test.StepVerifier;
//import reactor.util.retry.Retry;
//
///**
// * @author wanshao create time is 2021/3/2
// **/
//@SpringBootTest
//@Slf4j
//public class TestSetupPayload {
//
//    private static Mono<RSocketRequester> requesterMono;
//
//    private final static int              SERVER_PORT                    = 5678;
//
//    private final static int              TCP_CONN_TIMEOUT               = 3000;
//    private final static int              RSOCKET_RETRY_INTERVAL_SECONDS = 5;
//    private final static int              KEEP_ALIVE_INTERVAL_SEC        = 5;
//    private final static int              KEEP_ALIVE_MAX_TIME_SEC        = 30;
//
//    @SneakyThrows
//    @BeforeAll
//    public static void setupOnce() {
//        RSocketStrategies rSocketStrategies = RSocketStrategies.builder()
//            .encoders(encoders -> encoders.add(new Jackson2JsonEncoder()))
//            .decoders(decoders -> decoders.add(new Jackson2JsonDecoder()))
//            .routeMatcher(new PathPatternRouteMatcher())
//            .build();
//
//        RSocketRequester.Builder rsocketRequesterBuilder = RSocketRequester.builder().rsocketStrategies(rSocketStrategies);
//        ConnAuthDTO connAuthDTO = new ConnAuthDTO();
//        connAuthDTO.setAk("myak");
//        connAuthDTO.setSk("mysk");
//        connAuthDTO.setIp(HostUtil.getHostIp());
//        connAuthDTO.setWsn("wsn");
//        connAuthDTO.setSendAuthTimeMs(System.currentTimeMillis());
//
//        SocketAcceptor responder = RSocketMessageHandler.responder(rSocketStrategies, new TestSetupPayload());
//
//        // init server
//        io.rsocket.core.RSocketServer.create()
//            .interceptors(registry -> registry.forSocketAcceptor(new TestSocketAcceptor()))
//            .acceptor(responder)
//            .fragment(RSocketConnConstants.MTU_BYTE_SIZE)
//            .bind(TcpServerTransport.create(TcpServer.create()
//                .option(EpollChannelOption.SO_KEEPALIVE, true)
//                .option(EpollChannelOption.TCP_NODELAY, true)
//                .port(SERVER_PORT)
//                .doOnConnection(t -> log.info("NEW WORKER CONNECTED...."))))
//            .subscribe();
//
//        // init client
//
//        requesterMono = rsocketRequesterBuilder.setupRoute("conn-setup")
//            .setupData(new ObjectMapper().writeValueAsString(connAuthDTO))
//            .dataMimeType(MimeTypeUtils.parseMimeType(WellKnownMimeType.APPLICATION_JSON.getString()))
//            .rsocketConnector(connector -> connector.acceptor(responder)
//                .fragment(MTU_BYTE_SIZE)
//                .keepAlive(Duration.ofSeconds(KEEP_ALIVE_INTERVAL_SEC), Duration.ofSeconds(KEEP_ALIVE_MAX_TIME_SEC))
//                .reconnect(Retry.fixedDelay(Integer.MAX_VALUE, Duration.ofSeconds(RSOCKET_RETRY_INTERVAL_SECONDS))
//                    .doAfterRetry(retrySignal -> log.warn("RSocket client is reconnecting to get the newest connection...."))))
//            .connect(TcpClientTransport.create(TcpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TCP_CONN_TIMEOUT)
//                .option(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.SO_KEEPALIVE, true)
//                .host("localhost")
//                .port(SERVER_PORT)));
//
//    }
//
//    @Test
//    public void testRequestGetsResponse() {
//        // Send a request message
//        Mono<String> result = requesterMono.block().route("default-handler").data("hello").retrieveMono(String.class);
//
//        // Verify that the response message contains the expected data
//        StepVerifier.create(result).expectNext("success").verifyComplete();
//    }
//
//    @MessageMapping("default-handler")
//    Mono<String> requestResponse(final String msg) {
//        log.info("Received request-response request: {}", msg);
//        return Mono.just("success");
//    }
//
//}

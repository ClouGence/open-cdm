package com.clougence.clouddm.comm;

import static com.clougence.clouddm.comm.constants.rsocket.RSocketConnConstants.MTU_BYTE_SIZE;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.SSLParameters;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.MimeTypeUtils;

import com.clougence.clouddm.comm.component.RSocketRequestDispatcher;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.RSocketStopListener;
import com.clougence.clouddm.comm.component.client.RSocketClientAuthManager;
import com.clougence.clouddm.comm.constants.rsocket.ServerRouteNames;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.utils.ExceptionUtils;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.rsocket.SocketAcceptor;
import io.rsocket.exceptions.RejectedSetupException;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import reactor.netty.tcp.SslProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;
import reactor.util.retry.Retry;

/**
 * <pre>
 *     resume code example can reference rsocket-java source code ResumeIntegrationTest.newClientRSocket
 * </pre>
 *
 * @author wanshao create time is 2020/6/15
 **/
@Slf4j
public class WorkerRSocketClient {

    // ------- constants -----------
    private final int                TCP_CONN_TIMEOUT               = 5000;
    private final int                RSOCKET_RETRY_INTERVAL_SECONDS = 5;
    private final int                MONO_BLOCKING_SECONDS          = 4;
    private final AtomicInteger      failedTime                     = new AtomicInteger(0);
    private final int                KEEP_ALIVE_INTERVAL_SEC        = 5;
    private final int                KEEP_ALIVE_MAX_TIME_SEC        = 30;
    public final int                 MAX_RECREATE_REQUESTER_TIME    = 5;
    private final AtomicBoolean      inited                         = new AtomicBoolean(false);
    private Mono<RSocketRequester>   requesterMono;
    private RSocketStopListener      rSocketStopListener;
    private RSocketStrategies        rSocketStrategies;
    private RSocketRequestManager    rSocketRequestManager;
    private RSocketRequestDispatcher rSocketRequestDispatcher;
    private RSocketClientAuthManager clientAuthManager;
    private RSocketRequester.Builder rsocketRequesterBuilder;
    private AtomicBoolean            duringRetry                    = new AtomicBoolean(false);

    public WorkerRSocketClient(RSocketStopListener rSocketStopListener, RSocketStrategies rSocketStrategies, RSocketRequestManager rSocketRequestManager,
                               RSocketRequestDispatcher rSocketRequestDispatcher, RSocketClientAuthManager clientAuthManager, RSocketRequester.Builder rsocketRequesterBuilder){
        this.rSocketStopListener = rSocketStopListener;
        this.rSocketStrategies = rSocketStrategies;
        this.rSocketRequestManager = rSocketRequestManager;
        this.rSocketRequestDispatcher = rSocketRequestDispatcher;
        this.clientAuthManager = clientAuthManager;
        this.rsocketRequesterBuilder = rsocketRequesterBuilder;
    }

    private ConnAuthDTO connAuthDTO;

    public RSocketRequester getWorkingRequester() {
        if (duringRetry.get()) {
            throw new RuntimeException("Client is retry to connect to server, all rsocket request will be rejected to avoid concurrency issue....");
        }
        RSocketRequester requester = requesterMono.block(Duration.ofSeconds(MONO_BLOCKING_SECONDS));
        if (requester == null || requester.rsocket().isDisposed()) {
            throw new RuntimeException("try to get rsocket requester ,but it is ABNORMAL....");
        }
        return requester;
    }

    public void start() {
        if (inited.compareAndSet(false, true)) {
            try {
                log.info("start to init rsocket client...");
                connAuthDTO = clientAuthManager.acquireClientAuthInfo();
                initialConnect(connAuthDTO);
                log.info("rsocket sidecar client is started at port " + connAuthDTO.getConsolePort() + "....");
            } catch (Exception e) {
                String msg = "failed to init rsocket client.msg:" + ExceptionUtils.getRootCauseMessage(e);
                log.error(msg, e);
                throw new RuntimeException(msg, e);
            }
        }
    }

    protected void initialConnect(ConnAuthDTO connAuthDTO) {
        SocketAcceptor responder = RSocketMessageHandler.responder(rSocketStrategies, rSocketRequestDispatcher, rSocketRequestManager);
        SslContext sslContext = genSslContext();
        prepareRequestMono(responder, sslContext, connAuthDTO);
        RSocketRequester requester = this.getWorkingRequester();
        requester.rsocket().onClose().doOnError(error -> {
            String errMsg = "[RSOCKET] connect ERROR.msg: " + ExceptionUtils.getRootCauseMessage(error);
            log.error(errMsg, error);
            failedTime.incrementAndGet();
            exitOrNot(failedTime.get(), error);
        }).doFinally(consumer -> log.info("[RSOCKET] worker client DISCONNECTED to console")).subscribe();
    }

    @SneakyThrows
    protected void prepareRequestMono(SocketAcceptor responder, SslContext sslContext, ConnAuthDTO connAuthDTO) {
        String consoleHost = clientAuthManager.acquireServerDomain();
        int consolePort = Integer.parseInt(connAuthDTO.getConsolePort());

        SslProvider sslProvider = SslProvider.builder()
            .sslContext(sslContext)
            .handlerConfigurator(handler -> {
                SSLParameters sslParameters = handler.engine().getSSLParameters();
                sslParameters.setEndpointIdentificationAlgorithm(null);
                handler.engine().setSSLParameters(sslParameters);
            })
            .build();
        requesterMono = rsocketRequesterBuilder.setupRoute(ServerRouteNames.CONN_SETUP)
            .setupData(connAuthDTO)
            .dataMimeType(MimeTypeUtils.parseMimeType(WellKnownMimeType.APPLICATION_JSON.getString()))
            .rsocketConnector(connector -> connector.acceptor(responder)
                .fragment(MTU_BYTE_SIZE)
                .keepAlive(Duration.ofSeconds(KEEP_ALIVE_INTERVAL_SEC), Duration.ofSeconds(KEEP_ALIVE_MAX_TIME_SEC))
                .reconnect(Retry.fixedDelay(Integer.MAX_VALUE, Duration.ofSeconds(RSOCKET_RETRY_INTERVAL_SECONDS)).doBeforeRetry(retrySignal -> {
                    log.warn("[BEGIN] RSocket client is reconnecting to get the newest connection....");
                    duringRetry.set(true);
                }).doAfterRetry(retrySignal -> {
                    log.warn("[END] RSocket client is reconnecting to get the newest connection....");
                    duringRetry.set(false);
                })))
            .connect(TcpClientTransport.create(TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TCP_CONN_TIMEOUT)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .host(consoleHost)
                .port(consolePort)
                .secure(sslProvider)));
    }

    protected void exitOrNot(int currentFailed, Throwable e) {
        if (e instanceof RejectedSetupException) {
            log.error("connect fail with RejectedSetupException,try to exist.");
            Thread t = new Thread(() -> {
                rSocketStopListener.doAfterStop();
            });
            t.start();
        } else {
            log.error("connect fail " + currentFailed + " times, current exception:" + e.getClass().getSimpleName() + ", continue to retry.");
        }
    }

    public void stop() {
        if (inited.compareAndSet(true, false)) {
            try {
                log.warn("begin to stop sidecar's rsocket....");
                if (this.requesterMono != null) {
                    RSocketRequester requester = requesterMono.block(Duration.ofSeconds(1));
                    if (requester != null && !requester.rsocket().isDisposed()) {
                        requester.rsocket().dispose();
                    }
                }
            } catch (Exception e) {
                String msg = "stop sidecar rsocket fail,but ignore.msg:" + ExceptionUtils.getRootCauseMessage(e);
                log.error(msg, e);
            }
        }
    }

    @SneakyThrows
    protected SslContext genSslContext() {
        InputStream trustCertCollectionFile = getFileFromClassPath("ssl/server.crt", "server.crt");
        return getSslContextBuilder(trustCertCollectionFile, null, null).build();
    }

    public InputStream getFileFromClassPath(String resourceFileName, String newFileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(resourceFileName);
    }

    @SneakyThrows
    private SslContextBuilder getSslContextBuilder(InputStream trustCertFile, File certChainFile, File privateKeyFile) {
        if (trustCertFile == null) {
            throw new IllegalArgumentException("server.crt file can not empty.");
        }
        SslContextBuilder builder = SslContextBuilder.forClient();
        builder.endpointIdentificationAlgorithm(null);
        builder.trustManager(trustCertFile);
        if (certChainFile != null && privateKeyFile != null) {
            builder.keyManager(certChainFile, privateKeyFile);
        }
        return builder;
    }

    public String getAccessKey() { return connAuthDTO.getAk(); }

    public String getWorkerSequenceNumber() { return connAuthDTO.getWsn(); }
}

package com.clougence.clouddm.comm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;

import com.clougence.clouddm.comm.component.RSocketLifeManager;
import com.clougence.clouddm.comm.component.RSocketRequestDispatcher;
import com.clougence.clouddm.comm.component.RSocketRequestManager;
import com.clougence.clouddm.comm.component.RSocketStopListener;
import com.clougence.clouddm.comm.component.server.RSocketConnManager;
import com.clougence.clouddm.comm.constants.rsocket.RSocketConnConstants;
import com.clougence.utils.ExceptionUtils;

import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.rsocket.plugins.SocketAcceptorInterceptor;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.tcp.TcpServer;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Slf4j
public class ConsoleRSocketServer implements RSocketLifeManager {

    private final int                 SERVER_PORT;

    private RSocketConnManager        rSocketConnManager;

    private RSocketStrategies         rSocketStrategies;

    private RSocketRequestDispatcher  rSocketRequestDispatcher;

    private ConnSetupProcessor        connSetupProcessor;

    private RSocketRequestManager     rSocketRequestManager;

    private RSocketStopListener       rSocketStopListener;

    private SocketAcceptorInterceptor socketAcceptorInterceptor;

    public ConsoleRSocketServer(RSocketConnManager rSocketConnManager, RSocketStrategies rSocketStrategies, RSocketRequestDispatcher rSocketRequestDispatcher,
                                ConnSetupProcessor connSetupProcessor, RSocketRequestManager rSocketRequestManager, RSocketStopListener rSocketStopListener,
                                SocketAcceptorInterceptor socketAcceptorInterceptor, int serverPort){
        this.rSocketConnManager = rSocketConnManager;
        this.rSocketStrategies = rSocketStrategies;
        this.rSocketRequestDispatcher = rSocketRequestDispatcher;
        this.connSetupProcessor = connSetupProcessor;
        this.rSocketRequestManager = rSocketRequestManager;
        this.rSocketStopListener = rSocketStopListener;
        this.socketAcceptorInterceptor = socketAcceptorInterceptor;
        this.SERVER_PORT = serverPort;
    }

    @Override
    @SneakyThrows
    public void start() {
        log.info("Console rsocket server begin to start on port " + SERVER_PORT + "......");
        rSocketConnManager.resetWorkerStatusInDB();
        startSslServer();
    }

    @Override
    public void stop() {
        log.info("Console rsocket server vegin to stop on port " + SERVER_PORT + "......");
        rSocketStopListener.doAfterStop();
    }

    protected void startSslServer() {
        SslContext sslContext = genSslContext();
        log.info("begin to start RSocket server...");
        io.rsocket.core.RSocketServer.create()
            .interceptors(registry -> registry.forSocketAcceptor(socketAcceptorInterceptor))
            .acceptor(RSocketMessageHandler.responder(rSocketStrategies, connSetupProcessor, rSocketRequestDispatcher, rSocketRequestManager))
            .fragment(RSocketConnConstants.MTU_BYTE_SIZE)
            .bind(TcpServerTransport.create(TcpServer.create()
                .option(EpollChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_NODELAY, true)
                .secure(ssl -> ssl.sslContext(sslContext))
                .port(SERVER_PORT)
                .doOnConnection(t -> log.info("NEW WORKER CONNECTED...."))))
            .subscribe();
        log.info("RSocket server is LISTENING at port " + SERVER_PORT);
    }

    @SneakyThrows
    protected SslContext genSslContext() {
        InputStream certChainFile = getFileFromClassPath("ssl/server.crt", "server.crt");
        InputStream privateKeyFile = getFileFromClassPath("ssl/server.pem", "server.pem");

        SslContextBuilder sslClientContextBuilder = SslContextBuilder.forServer(certChainFile, privateKeyFile);
        return sslClientContextBuilder.build();
    }

    public InputStream getFileFromClassPath(String resourceFileName, String newFileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // File newFile = new File(newFileName);
        // copyInputStreamToFile(inputStream, newFile);
        return classLoader.getResourceAsStream(resourceFileName);
    }

    private void copyInputStreamToFile(InputStream inputStream, File file) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (Exception e) {
            String msg = "read from file error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}

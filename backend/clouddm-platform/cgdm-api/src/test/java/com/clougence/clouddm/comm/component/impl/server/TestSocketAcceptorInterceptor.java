package com.clougence.clouddm.comm.component.impl.server;

import io.rsocket.SocketAcceptor;
import io.rsocket.plugins.SocketAcceptorInterceptor;

/**
 * @author bucketli 2021/1/18 13:17
 */
public class TestSocketAcceptorInterceptor implements SocketAcceptorInterceptor {

    @Override
    public SocketAcceptor apply(SocketAcceptor socketAcceptor) {
        return (setup, sendingSocket) -> socketAcceptor.accept(setup, sendingSocket);
    }
}

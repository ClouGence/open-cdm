package com.clougence.clouddm.console.web.global.rsocket;

import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.model.RSocketAuthException;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.rsocket.SocketAcceptor;
import io.rsocket.plugins.SocketAcceptorInterceptor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author bucketli 2021/1/18 12:31
 */
@Slf4j
public class DmConsoleSAInterceptor implements SocketAcceptorInterceptor {

    private final int          JUDGE_RESUME_CONN_THRESHOLD_MS = 3000;

    private final ServerSideRegistry serverSideRegistry;

    public DmConsoleSAInterceptor(ServerSideRegistry serverSideRegistry){
        this.serverSideRegistry = serverSideRegistry;
    }

    @Override
    public SocketAcceptor apply(SocketAcceptor socketAcceptor) {
        return (setup, sendingSocket) -> {
            try {
                ConnAuthDTO authInfo = new ObjectMapper().readValue(setup.getDataUtf8(), ConnAuthDTO.class);
                checkParams(authInfo);

                if (System.currentTimeMillis() - authInfo.getSendAuthTimeMs() > JUDGE_RESUME_CONN_THRESHOLD_MS) {
                    log.info("receive an OLD RESUME connection. Resume connection can come in parallel, repeated connection will be rejected.");
                } else {
                    log.info("receive an NEW connection and try to init.");
                }

                serverSideRegistry.checkAuth(authInfo);
                return socketAcceptor.accept(setup, sendingSocket);
            } catch (Exception e) {
                String errMsg = "try to accept a connection error.msg:" + ExceptionUtils.getRootCauseMessage(e);
                log.warn(errMsg, e);
                return Mono.error(new RSocketAuthException(errMsg));
            }
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

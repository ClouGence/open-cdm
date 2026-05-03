package com.clougence.clouddm.comm.component.server;

import com.clougence.clouddm.comm.model.RSocketRespDTO;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;

/**
 * @author wanshao create time is 2021/1/9
 **/
public interface RSocketServerSender {

    long CONSOLE_R_REQUEST_TIMEOUT_MS = 10000;

    RSocketRespDTO<?> requestNonBlock(Long clusterId, String apiFullMethodName, Object[] param);

    RSocketRespDTO<?> requestNonBlock(String apiFullMethodName, String specifiedWsn, Object[] param);

    RSocketRespDTO<?> requestNonBlockWithJsonParams(String apiFullMethodName, String specifiedWsn, String[] paramJson);

    WorkerIdentity getIdentity();

    void shutdown();
}

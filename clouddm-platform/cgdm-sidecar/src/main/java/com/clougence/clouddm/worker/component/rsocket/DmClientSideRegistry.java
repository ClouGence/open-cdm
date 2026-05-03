package com.clougence.clouddm.worker.component.rsocket;

import org.springframework.messaging.rsocket.RSocketRequester;

import com.clougence.clouddm.comm.WorkerRSocketClient;
import com.clougence.clouddm.comm.component.client.ClientSideRegistry;

/**
 * @author wanshao create time is 2021/1/14
 **/
public class DmClientSideRegistry implements ClientSideRegistry {

    private WorkerRSocketClient workerRSocketClient;

    public DmClientSideRegistry(WorkerRSocketClient workerRSocketClient){
        this.workerRSocketClient = workerRSocketClient;
    }

    @Override
    public RSocketRequester getRSocketRequester() { return workerRSocketClient.getWorkingRequester(); }

}

package com.clougence.clouddm.comm.component.impl.client;

import javax.annotation.Resource;

import org.springframework.messaging.rsocket.RSocketRequester;
import com.clougence.clouddm.comm.WorkerRSocketClient;
import com.clougence.clouddm.comm.component.client.ClientSideRegistry;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/13
 **/
@Slf4j
public class TestClientSideRegistry implements ClientSideRegistry {

    @Resource
    private WorkerRSocketClient workerRSocketClient;

    public TestClientSideRegistry(WorkerRSocketClient workerRSocketClient){
        this.workerRSocketClient = workerRSocketClient;
    }

    @Override
    public RSocketRequester getRSocketRequester() { return workerRSocketClient.getWorkingRequester(); }
}

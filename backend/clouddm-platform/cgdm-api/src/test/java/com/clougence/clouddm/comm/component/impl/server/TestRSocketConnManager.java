package com.clougence.clouddm.comm.component.impl.server;

import com.clougence.clouddm.comm.component.server.RSocketConnManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Slf4j
public class TestRSocketConnManager implements RSocketConnManager {

    @Override
    public void resetWorkerStatusInDB() {
        log.warn("Using the default rsocket conn manager is not recommended...");
    }
}

package com.clougence.clouddm.worker.component.rsocket;

import com.clougence.clouddm.comm.component.RSocketStopListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/14
 **/
@Slf4j
public class DmClientStopListener implements RSocketStopListener {

    @Override
    public void doAfterStop() {
        log.info("rsocket client is STOPPING...");
        System.exit(-1);
    }
}

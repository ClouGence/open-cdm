package com.clougence.clouddm.console.web.global.rsocket;

import com.clougence.clouddm.comm.component.RSocketStopListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/14
 **/
@Slf4j
public class DmServerStopListener implements RSocketStopListener {

    @Override
    public void doAfterStop() {
        log.info("Do something after server is stop.....");
    }
}

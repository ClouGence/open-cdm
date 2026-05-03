package com.clougence.clouddm.console.web.global.rsocket;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.comm.component.RSocketStopListener;

/**
 * @author wanshao
 * create time is  2021/1/13
 **/
@Component
public class DmRSocketStopListener implements RSocketStopListener {

    @Override
    public void doAfterStop() {
    }
}

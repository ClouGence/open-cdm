package com.clougence.clouddm.worker.component.rsocket;

import com.clougence.clouddm.comm.component.RSocketExceptionManager;

/**
 * @author wanshao create time is 2021/1/14
 **/
public class DmClientExceptionManager implements RSocketExceptionManager {

    @Override
    public void saveExcOrAlert(Throwable throwable) {
        // todo later can report exception to console and save to sentry
    }
}

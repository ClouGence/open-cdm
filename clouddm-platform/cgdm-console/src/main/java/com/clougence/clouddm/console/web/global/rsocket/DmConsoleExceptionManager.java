package com.clougence.clouddm.console.web.global.rsocket;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.comm.component.RSocketExceptionManager;

import io.sentry.Sentry;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Component
public class DmConsoleExceptionManager implements RSocketExceptionManager {

    @Override
    public void saveExcOrAlert(Throwable throwable) {
        Sentry.captureException(throwable);
    }
}

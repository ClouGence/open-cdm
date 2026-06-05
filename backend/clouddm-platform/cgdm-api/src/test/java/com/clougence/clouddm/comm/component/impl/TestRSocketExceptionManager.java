package com.clougence.clouddm.comm.component.impl;

import com.clougence.clouddm.comm.component.RSocketExceptionManager;
import com.clougence.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Slf4j
public class TestRSocketExceptionManager implements RSocketExceptionManager {

    @Override
    public void saveExcOrAlert(Throwable throwable) {
        log.warn("Using the default rsocket exception manager is not recommended. Root cause is " + ExceptionUtils.getRootCauseMessage(throwable), throwable);

    }
}

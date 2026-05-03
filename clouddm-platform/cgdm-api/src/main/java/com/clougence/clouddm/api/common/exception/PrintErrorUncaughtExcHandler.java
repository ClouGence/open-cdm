package com.clougence.clouddm.api.common.exception;

import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/2/11 12:40
 */
@Slf4j
public class PrintErrorUncaughtExcHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Thread " + t.getName() + " got a fatal exception,system start to exit ,msg:" + ExceptionUtils.getRootCauseMessage(e), e);
    }
}
